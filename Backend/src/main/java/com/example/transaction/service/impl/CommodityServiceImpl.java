package com.example.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.*;
import com.example.transaction.dto.CommoditySearch;
import com.example.transaction.dto.Condition;
import com.example.transaction.dto.commodity.CommodityInfo;
import com.example.transaction.dto.commodity.DetailedCommodityInfo;
import com.example.transaction.dto.commodity.MyCommodityCondition;
import com.example.transaction.dto.commodity.Pagination;
import com.example.transaction.dto.notice.NoticeInfo;
import com.example.transaction.pojo.*;
import com.example.transaction.service.CommentService;
import com.example.transaction.service.CommodityService;
import com.example.transaction.service.NoticeService;
import com.example.transaction.util.FileUtil;
import com.example.transaction.util.MyPage;
import com.example.transaction.util.code.Address;
import com.example.transaction.util.code.Nums;
import com.example.transaction.util.code.ResourcePath;
import com.example.transaction.util.responseFromServer;
import io.netty.util.internal.StringUtil;
import org.apache.ibatis.annotations.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/26 15:46
 * @Content:
 */

@Service("CommodityService")
public class CommodityServiceImpl implements CommodityService {
    private final CommodityDAO commodityDAO;
    private final TypeDAO typeDAO;
    private final CommodityImageDAO commodityImageDAO;
    private final NoticeDAO noticeDAO;
    private int count = 0;

    @Autowired
    NoticeService noticeService;
    @Autowired
    CommentService commentService;
    @Autowired
    ReservationDAO reservationDAO;

    @Autowired
    CommodityServiceImpl(CommodityDAO commodityDAO, TypeDAO typeDAO, CommodityImageDAO commodityImageDAO, NoticeDAO noticeDAO) {
        this.commodityDAO = commodityDAO;
        this.typeDAO = typeDAO;
        this.commodityImageDAO = commodityImageDAO;
        this.noticeDAO = noticeDAO;
    }

    @Override
    @Transactional
    public responseFromServer search(Condition condition) {
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        /*处理分页条件*/
        Page<Commodity> page;
        if (condition.getPageIndex() == null || condition.getPageIndex() <= 0) {
            page = new Page<>(1, Nums.pageSize);
        } else {
            page = new Page<>(condition.getPageIndex(), Nums.pageSize);
        }
        /*处理时间条件*/
        Timestamp timestamp;
        if (condition.getEndTime() == null) {
            timestamp = new Timestamp(System.currentTimeMillis());
        } else {
            timestamp = new Timestamp(condition.getEndTime().getTime());
        }
//todo        queryWrapper.eq("n.id", "c.notice_id");
        queryWrapper.le("n.create_time", timestamp);

        /*处理搜索串*/
        String searchStr = condition.getKeyword();
        if (searchStr != null) {
            queryWrapper.like("c.name", searchStr);
        }

        /*价格区间*/
        if (condition.getLowPrice() != null) {
            queryWrapper.ge("c.expected_price", condition.getLowPrice());
        }
        if (condition.getHighPrice() != null) {
            queryWrapper.le("c.expected_price", condition.getHighPrice());
        }

        /*处理类型*/
        if (condition.getType() != null) {
//todo            queryWrapper.eq("c.id", "t.commodity_id");
            if (!StringUtil.isNullOrEmpty(condition.getType()) && !condition.getType().equals("全部")) {
                queryWrapper.eq("c.type", condition.getType());
            }
        }


        /*处理地址*/
        if (condition.getUserAddress() != null && !condition.getUserAddress().equals("全校")) {
            int addressCode;
            String address = condition.getUserAddress();
            try {
                if (address != "") {
                    addressCode = Address.valueOf(address).getCode();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return responseFromServer.error();
            }
            /*这里没有用到地址码,枚举只是用了检测该地址是否有效*/
            queryWrapper.eq("n.address", address);
        }

        if (condition.getOutdated() != null && condition.getOutdated() > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE,condition.getOutdated());
            timestamp = new Timestamp(calendar.getTimeInMillis());
            queryWrapper.le("n.end_time", timestamp);
        }
        try {
            IPage<Commodity> resultPage;
            queryWrapper.ge("n.end_time", new Timestamp(System.currentTimeMillis()));
            if (condition.getSort() != null && condition.sort >= 0) {
                switch (condition.getSort()) {
                    /*失效时间*/
                    case 2:
                        timestamp = new Timestamp((new Date()).getTime() + 1000 * 60 * 60 * 24 * Nums.recentDays);
                        queryWrapper.le("n.end_time", timestamp);
                        queryWrapper.ge("n.end_time", System.currentTimeMillis());
                        resultPage = commodityDAO.searchEndTimeDESC(page, queryWrapper);
                        break;
                    /*便宜好物*/
                    case 3:
                        queryWrapper.le("c.expected_price", 10.0);
                        resultPage = commodityDAO.search(page, queryWrapper);
                        break;
                    /*价格从低到高*/
                    case 5:
                        resultPage = commodityDAO.searchPriceASC(page, queryWrapper);
                        break;
                    /*失效时间*/
                    case 6:

                        resultPage = commodityDAO.searchEndTimeDESC(page, queryWrapper);
                        break;
                    /*发布者信誉值(成功率)*/
                    case 7:
                        resultPage = commodityDAO.searchEstimateASC(page, queryWrapper);
                        break;

                    /*最新*/
                    case 0:
                        /*附近,地址在参数中*/
                    case 1:
                        /*最新*/
                    case 4:
                    default:
                        resultPage = commodityDAO.search(page, queryWrapper);
                }
            } else {
                resultPage = commodityDAO.search(page, queryWrapper);
            }


            if (resultPage == null) {
                throw new Exception();
            }
            /**将commodity转换为commodityInfo*/
            MyPage myPage = new MyPage<Commodity>(resultPage);
            List<CommodityInfo> commodityInfoList = new ArrayList<>();
            for (Commodity commodity : resultPage.getRecords()) {
                /**查询对应的notice信息*/
                QueryWrapper queryWrapper1 = new QueryWrapper();
                queryWrapper1.eq("id", commodity.getNoticeId());
                responseFromServer noticeResposne = noticeService.getNoticeInfoPage(queryWrapper1, 1);
                if (noticeResposne.isFailure()) {
                    throw new Exception();
                }
                MyPage noticePage = (MyPage) noticeResposne.getData();
                if (noticePage.getPageList().size() != 1) {
                    throw new Exception();
                }
                NoticeInfo noticeInfo = (NoticeInfo) noticePage.getPageList().get(0);
                CommodityInfo commodityInfo = new CommodityInfo(commodity, noticeInfo);
                /*查询是否已有人预约*/
                Integer count = reservationDAO.getCountByCommodityId(commodity.getId());
                if(count!=null && count >=0){
                    commodityInfo.setState(count.intValue()+"人预约");
                }else{
                    commodityInfo.setState("发布中");
                }
                commodityInfoList.add(commodityInfo);
            }
            myPage.setPageList(commodityInfoList);

            /*将查询的截止时间返回*/
            myPage.setEndTime(timestamp);
            return responseFromServer.success(myPage);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return responseFromServer.error();
        }


    }

    private responseFromServer getMyBoughtCommodities(Integer accountId,Pagination pagination){
        /**
         * 返回分页
         */
        /**
         * ZZH
         * TODO : 获取我购买的商品
         */
        return null;
    }

    private responseFromServer getMyPublishedCommodities(Integer accountId,Pagination pagination){
        /**
         * 返回分页
         */
        return getOthersCommodity(pagination,accountId);
    }


    @Override
    public responseFromServer getMyCommodities(MyCommodityCondition condition){
        Pagination pagination = new Pagination();
        pagination.setPageIndex(condition.getPageIndex());
        pagination.setEndTime(condition.getEndTime());
        switch(condition.getType()){
            case 0:
                /**
                 * 我发布的
                 */
                break;
            case 1:
                /**
                 * 我买入的
                 */
                break;
            case 3:
                /**
                 * 全部
                 */
                break;
            default:
                return responseFromServer.error();
        }
        return null;
    }


    /**
     * @Description: 获取别人的商品信息
     * @Date: 2020/5/24 21:46
     */
    @Override
    public responseFromServer getOthersCommodity(Pagination pagination, Integer accoutnId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        Page<Commodity> page;
        if (pagination.getPageIndex() == null || pagination.getPageIndex() <= 0) {
            page = new Page<>(1, Nums.pageSize);
        } else {
            page = new Page<>(pagination.getPageIndex(), Nums.pageSize);
        }
        queryWrapper.le("n.end_time", pagination.getEndTime());
        queryWrapper.eq("n.account_id", accoutnId);
        IPage<Commodity> resultPage = commodityDAO.search(page, queryWrapper);
        MyPage myPage = new MyPage<Commodity>(resultPage);
        List<CommodityInfo> commodityInfoList = new ArrayList<>();
        for (Commodity commodity : resultPage.getRecords()) {
            commodityInfoList.add(new CommodityInfo(commodity, null));
        }
        myPage.setPageList(commodityInfoList);
        return responseFromServer.success(myPage);
    }


    /**
     * 根据id获取商品信息
     *
     * @param id 商品id
     * @return 执行结果
     */
    @Override
    public responseFromServer getById(Integer id) {
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        //id查找是唯一的
        List<Commodity> commodities = commodityDAO.selectWithCondition(queryWrapper);
        if (commodities == null || commodities.size() != 1) {
            return responseFromServer.error();
        }
        return responseFromServer.success(commodities.get(0));
    }

    @Override
    public responseFromServer getSimpleCommodity(Integer id) {
        Commodity commodity = commodityDAO.getSimpleCommodityById(id);
        if (commodity == null) {
            return responseFromServer.error();
        }
        return responseFromServer.success(commodity);
    }

    /**
     * 用于返回商品详情界面需要的商品信息
     *
     * @param id
     * @return
     */
    @Override
    public responseFromServer getDetailedCommodityInfo(Integer id) {
        responseFromServer response = getDetailedCommodity(id);
        if (response.isFailure()) {
            return response;
        }
        DetailedCommodityInfo commodityInfo = new DetailedCommodityInfo((Commodity) response.getData());
        commodityInfo.getCommodity().setCommodityImages(null);
        response = commentService.getCommentByCommodityId(1, 1);
        if (response.isFailure()) {
            return response;
        }
        commodityInfo.setCommentsFromCommentList(
                ((MyPage<Comment>) response.getData()).getPageList()
        );
        return responseFromServer.success(commodityInfo);
    }

    @Override
    public responseFromServer getDetailedCommodity(Integer id) {
        responseFromServer response = getById(id);
        Commodity commodity;
        if (response.isFailure()) {
            return responseFromServer.error();
        } else {
            commodity = (Commodity) response.getData();
            commodity.setImagesList();
        }

        return responseFromServer.success(commodity);
    }

    /**
     * 商品名称模糊检索，崭新程度排序
     *
     * @param pageIndex 当前页数
     * @param name      商品名
     *                  count为偶数:顺序（从小到大）；奇数:倒序
     * @return Commodity数组
     */
    @Override
    public responseFromServer getByNameSortedByNewness(Integer pageIndex, String name) {
        Page<Commodity> page = new Page<>((pageIndex - 1) * Nums.pageSize, Nums.pageSize);
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis()); //当前时间
        IPage<Commodity> iPage = commodityDAO.sortByNewness(page, name);
        MyPage<Commodity> myPage = new MyPage<>(iPage);
        return responseFromServer.success(myPage);
    }

    /**
     * 根据类型分类
     *
     * @param typeId 标签
     * @return Commodity数组
     */
    @Override
    public responseFromServer getByTypeId(Integer pageIndex, Integer typeId) {
        Page<Commodity> page = new Page<>((pageIndex - 1) * Nums.pageSize, Nums.pageSize);
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis()); //当前时间
        IPage<Commodity> iPage = commodityDAO.sortByType(page, typeId);
        MyPage<Commodity> myPage = new MyPage<>(iPage);
        return responseFromServer.success(myPage);
    }

    /**
     * 根据价格区间筛选物品
     *
     * @param low  最低价
     * @param high 最高价
     * @return Commodity数组
     */
    @Override
    public responseFromServer getBetweenPrice(Integer pageIndex, String name, Integer low, Integer high) {
        Page<Commodity> page = new Page<>((pageIndex - 1) * Nums.pageSize, Nums.pageSize);
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis()); //当前时间
        IPage<Commodity> iPage = commodityDAO.betweenPrice(page, name, low, high);
        MyPage<Commodity> myPage = new MyPage<>(iPage);
        return responseFromServer.success(myPage);
    }

    /**
     * 根据所有者信誉排序
     *
     * @param name 商品名
     * @return Commodity数组
     */
    @Override
    public responseFromServer sortByCredit(Integer pageIndex, String name) {
        Page<Commodity> page = new Page<>((pageIndex - 1) * Nums.pageSize, Nums.pageSize);
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis()); //当前时间
        IPage<Commodity> iPage = commodityDAO.sortByCredit(page, name);
        MyPage<Commodity> myPage = new MyPage<>(iPage);
        return responseFromServer.success(myPage);
    }

    /**
     * 插入商品
     *
     * @param commodity 商品
     * @return 执行结果
     */
    @Override
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Transactional
    public responseFromServer insertCommodity(Commodity commodity) {
        if (commodityDAO.insert(commodity) != 1 || !insertCommodityInfo(commodity)) {
            /*回滚事务*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }

    /**
     * 更新商品
     *
     * @param commodity 商品
     * @return 执行结果
     */
    @Override
    @Transactional
    public responseFromServer updateCommodity(Commodity commodity) {
/*        if(isIdentityError(commodity, session))  //身份检查
            return responseFromServer.illegal();*/
        if (commodity.getId() == null) {
            return responseFromServer.error();
        }
        /**
         * 将空串和-1的非法值设置为null
         */
        commodity.clear();
        if (commodityDAO.updateById(commodity) != 1 || !updateCommodityInfo(commodity)) {
            /*回滚事务*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        } else {
            return responseFromServer.success();
        }
    }

    /**
     * 删除商品
     *
     * @param commodity 商品名
     * @return 执行结果
     */
    @Override
    @Transactional
    public responseFromServer deleteCommodity(Commodity commodity) {
        /*身份检查放到controller层，避免批量删除时重复多次验证信息*/
/*
        if(isIdentityError(commodity, session))  //身份检查
            return responseFromServer.illegal();
*/
        deleteCommodityInfo(commodity);
        if (commodityDAO.deleteById(commodity.getId()) != 1) {
            /*回滚事务*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        } else {
            return responseFromServer.success();
        }
    }

    /**
     * 添加商品类型、图片等信息
     *
     * @param commodity 商品
     * @return 执行结果
     */
    @Transactional
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public boolean insertCommodityInfo(Commodity commodity) {
        for (String url : commodity.getImages()) {
            if (validateCommodityImageUrl(commodity.getId(), url).isFailure()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }

        /*已经取消了多类型标签的功能*/
//        if (commodity.getTypes() == null || commodity.getTypes().size() == 0) {
//            return true;
//        }
//
//        List<Type> typeList = commodity.getTypes();
//        for (Type type : typeList) {
//            type.setCommodityId(commodity.getId());
//            if (typeDAO.insert(type) != 1) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                return false;
//            }
//        }
        return true;
    }

    /**
     * 更新商品信息
     *
     * @param commodity 商品
     * @return 执行结果
     */
    @Transactional
    public boolean updateCommodityInfo(Commodity commodity) {
        for (String url : commodity.getImages()) {
            if (validateCommodityImageUrl(commodity.getId(), url).isFailure()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
        /*已经取消了多类型标签的功能*/
//        if (commodity.getTypes() == null || commodity.getTypes().size() == 0) {
//            return true;
//        }
//
//        List<Type> typeList = commodity.getTypes();
//        for (Type type : typeList) {
//            type.setCommodityId(commodity.getId());
//            if (typeDAO.updateById(type) != 1) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                return false;
//            }
//        }
        return true;
    }

    /**
     * 查询某一notice下所有商品
     *
     * @param notice 通告
     * @return 执行结果
     */
    @Override
    public responseFromServer selectAllByNotice(Notice notice) {
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("notice_id", notice.getId());
        List<Commodity> commodities = commodityDAO.selectWithCondition(queryWrapper);
        return responseFromServer.success(commodities);
    }

    /**
     * 删除某一notice下所有商品
     *
     * @param notice 通告
     * @return 执行结果
     */
    @Override
    @Transactional
    public responseFromServer deleteAllByNotice(Notice notice) {
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("notice_id", notice.getId());
        List<Commodity> commodities = commodityDAO.selectWithCondition(queryWrapper);
        for (Commodity commodity : commodities) {
            if (!deleteCommodity(commodity).isSuccess()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return responseFromServer.error();
            }
        }
        return responseFromServer.success();
    }

    /**
     * 删除图片、类型等信息,由于外键方式为CASCADE，因此只需删除本地缓存文件资源
     *
     * @param commodity 商品
     */
    public void deleteCommodityInfo(Commodity commodity) {
        List<CommodityImage> commodityImageList = commodity.getCommodityImages();
        for (CommodityImage commodityImage : commodityImageList) {
            String path = commodityImage.getImageUrl();
            File file = new File(ResourcePath.imagePath + path);
            file.delete();
        }
    }

    /**
     * 返回图片路径
     *
     * @param files 文件数组
     * @return 执行结果
     */
    @Override
    public responseFromServer imageUrl(MultipartFile[] files) {
        QueryWrapper<CommodityImage> queryWrapper = new QueryWrapper<>();
        List<CommodityImage> commodityImages = commodityImageDAO.selectList(queryWrapper);
        List<String> paths = new ArrayList<>(); //存储附件路径
        for (MultipartFile file : files) {
            try {
                byte[] bytes = file.getBytes();
                String base = "E:/CampusTransactionImages/images/";
//                String base = System.getProperty("user.dir") + "\\images\\";
                Path path = Paths.get(base + commodityImages.size() + 1);
                //如果没有files文件夹，则创建
                if (!Files.isWritable(path)) {
                    Files.createDirectories(Paths.get(System.getProperty("user.dir")));
                }
                //文件写入指定路径
                Files.write(path, bytes);
                paths.add(base + commodityImages.size() + 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseFromServer.success(paths);
    }

    /**
     * 用户身份检查
     *
     * @param commodity 商品
     * @param session   HttpSession
     * @return 结果
     */
    public boolean isIdentityError(Commodity commodity, HttpSession session) {
//        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("id", commodity.getId());
//        Account account = commodityDAO.selectWithCondition(queryWrapper).get(0).getNotice().getUser();
//        return !AccountVerify.verify(account, session);
        return false;
    }

    /**
     * 更新商品图片的商品id
     *
     * @param commodityId
     * @param url
     * @return
     */
    @Override
    @Transactional
    public responseFromServer validateCommodityImageUrl(Integer commodityId, String url) {
        CommodityImage image = new CommodityImage();
        image.setCommodityId(commodityId);
        image.setImageUrl(url);
//        File file = new File("E:/CampusTra nsaction/temp/"+url);
//        file.renameTo(new File("E:/CampusTransaction/images/"+url));
        try {
            /*将图片从temp文件夹移动到images文件夹下*/
            Files.move(Paths.get("E:/CampusTransactionResources/temp/" + url), Paths.get("E:/CampusTransactionResources/images/" + url), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return responseFromServer.error();
        }

        if (1 != commodityImageDAO.updateByUrl(url, commodityId)) {
            return responseFromServer.error();
        }

        return responseFromServer.success();
    }

    /**
     * 更新多个商品图片的商品id
     *
     * @param commodityId
     * @param urls
     * @return
     */
    @Override
    @Transactional
    public responseFromServer validateCommodityImageUrls(Integer commodityId, List<String> urls) {
        if (urls != null && urls.size() != 0 && commodityId != null) {
            for (String url : urls) {
                if (validateCommodityImageUrl(commodityId, url).isFailure()) {
                    return responseFromServer.error();
                }
            }
        }
        return responseFromServer.success();
    }

    /**
     * 上传多个商品图片
     *
     * @param files
     * @param commodityId
     * @return
     */
    @Override
    @Transactional
    public responseFromServer uploadCommodityImages(MultipartFile[] files, Integer commodityId, Boolean updateToCommodity) {
        /*如果是上传到已经创建的商品,直接存到images文件夹下*/
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            /*获得文件名*/
            responseFromServer response = FileUtil.checkImageFile(file, updateToCommodity, false);
            if (response.isFailure()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return responseFromServer.error();
            }
            String filename = (String) response.getData();

            if (updateToCommodity) {
                /*上传到已经创建的商品中*/
                if (commodityImageDAO.insert((new CommodityImage(filename, commodityId))) != 1) {
                    /*插入数据库错误*/
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return responseFromServer.error();
                }
            }
            /*保存文件*/
            if (FileUtil.saveFile(file, updateToCommodity, filename, false).isFailure()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return responseFromServer.error(0, "保存文件异常");
            }
        }
        return responseFromServer.success(fileNames);
    }
}
