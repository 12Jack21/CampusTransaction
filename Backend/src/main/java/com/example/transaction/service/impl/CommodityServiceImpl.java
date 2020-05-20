package com.example.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.CommodityDAO;
import com.example.transaction.dao.CommodityImageDAO;
import com.example.transaction.dao.NoticeDAO;
import com.example.transaction.dao.TypeDAO;
import com.example.transaction.dto.Condition;
import com.example.transaction.pojo.*;
import com.example.transaction.service.CommodityService;
import com.example.transaction.util.FileUtil;
import com.example.transaction.util.MyPage;
import com.example.transaction.util.code.Address;
import com.example.transaction.util.code.Nums;
import com.example.transaction.util.code.ResourcePath;
import com.example.transaction.util.responseFromServer;
import org.apache.ibatis.annotations.Options;
import org.jetbrains.annotations.TestOnly;
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
import java.util.List;
import java.util.UUID;

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

        /*处理排序规则*/

        if (condition.getSortType() != null && condition.sortType >= 0) {
            switch (condition.getSortType()) {
                /*最新*/
                case 0:
                    queryWrapper.orderByDesc("c.create_time");
                    break;

                /*附近,地址在参数中*/
                case 1:
                    break;

                /*失效时间*/
                case 2:
                    condition.recent = true;
                    condition.setOutdated(2);
                    break;

                /*便宜好物*/
                case 3:
                    queryWrapper.le("c.price", 10.0);
                    break;

                /*todo*/
                case 4:
                    break;

                /*todo*/
                case 5:
                    break;

                /*todo*/
                case 6:
                    break;

                /*todo*/
                case 7:
                    break;
                default:
            }
        }

        /*处理分页条件*/
        Page<Commodity> page;
        if (condition.getPageIndex() == null || condition.getPageIndex() <= 0) {
            page = new Page<>(Nums.pageSize, 1);
        } else {
            page = new Page<>(Nums.pageSize, condition.getPageIndex());
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

        if (condition.recent) {
            timestamp = new Timestamp(timestamp.getTime() - 1000 * 60 * 60 * 24 * Nums.recentDays);
            queryWrapper.ge("n.end_time", timestamp);
        } else if (condition.getOutdated() != null && condition.getOutdated() > 0) {
            timestamp = new Timestamp(timestamp.getTime() - 1000 * 60 * 60 * 24 * condition.getOutdated());
            queryWrapper.ge("n.end_time", timestamp);
        }

        /*处理地址*/

        if (condition.getUserAddress() != null || condition.getUserAddress() != "全校") {
            int addressCode;
            String address = condition.getUserAddress();
            try {
                addressCode = Address.valueOf(address).getCode();
            } catch (Exception e) {
                e.printStackTrace();
                return responseFromServer.error();
            }
            /*这里没有用到地址码,枚举只是用了检测该地址是否有效*/
            queryWrapper.eq("n.address", address);
        }

        /*处理搜索串*/
        String searchStr = condition.getKeyword();
        if (searchStr != null) {
            queryWrapper.like("c.name", "%" + searchStr + "%");
        }

        /*价格区间*/
        if (condition.getLowPrice() != null) {
            queryWrapper.ge("c.price", condition.getLowPrice());
        }
        if (condition.getHighPrice() != null) {
            queryWrapper.le("c.price", condition.getHighPrice());
        }

        /*处理类型*/
        if (condition.getType() != null) {
//todo            queryWrapper.eq("c.id", "t.commodity_id");
            queryWrapper.eq("t.value", condition.getType());
        }

        try {
            IPage<Commodity> resultPage = commodityDAO.search(page, queryWrapper);
            if (resultPage == null) {
                throw new Exception();
            }
            MyPage myPage = new MyPage<Commodity>(resultPage);
            /*将查询的截止时间返回*/
            myPage.setEndTime(timestamp);
            return responseFromServer.success(myPage);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }


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
        return responseFromServer.success();
    }

    @Override
    public responseFromServer getDetailedCommodity(Integer id) {
        return getById(id);
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
        Page<Commodity> page = new Page<>(pageIndex, Nums.pageSize);
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
        Page<Commodity> page = new Page<>(pageIndex, Nums.pageSize);
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
        Page<Commodity> page = new Page<>(pageIndex, Nums.pageSize);
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
        Page<Commodity> page = new Page<>(pageIndex, Nums.pageSize);
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
        if (commodity.getTypes() == null || commodity.getTypes().size() == 0) {
            return true;
        }

        List<Type> typeList = commodity.getTypes();
        for (Type type : typeList) {
            type.setCommodityId(commodity.getId());
            if (typeDAO.insert(type) != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
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
        if (commodity.getTypes() == null || commodity.getTypes().size() == 0) {
            return true;
        }

        List<Type> typeList = commodity.getTypes();
        for (Type type : typeList) {
            type.setCommodityId(commodity.getId());
            if (typeDAO.updateById(type) != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
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
        String filePath = updateToCommodity ? ResourcePath.imagePath : ResourcePath.imageTempPath;
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            /*获得文件名*/
            responseFromServer response = FileUtil.checkImageFile(file, updateToCommodity);
            if (response.isFailure()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return responseFromServer.error();
            }
            String filename = (String) response.getData();

            if (updateToCommodity) {
                /*上传到已经创建的商品中*/
                if (commodityImageDAO.insert((new CommodityImage(filePath, commodityId))) != 1) {
                    /*插入数据库错误*/
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return responseFromServer.error();
                }
            }
            /*保存文件*/
            if (FileUtil.saveFile(file, updateToCommodity, filename).isFailure()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return responseFromServer.error(0, "保存文件异常");
            }
        }
        return responseFromServer.success(fileNames);
    }
}
