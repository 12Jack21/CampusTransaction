package com.example.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.CommodityDAO;
import com.example.transaction.dao.CommodityImageDAO;
import com.example.transaction.dao.NoticeDAO;
import com.example.transaction.dao.TypeDAO;
import com.example.transaction.pojo.*;
import com.example.transaction.service.CommodityService;
import com.example.transaction.util.AccountVerify;
import com.example.transaction.util.MyPage;
import com.example.transaction.util.Nums;
import com.example.transaction.util.responseFromServer;
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
import java.sql.Timestamp;
import java.util.ArrayList;
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
    CommodityServiceImpl(CommodityDAO commodityDAO, TypeDAO typeDAO, CommodityImageDAO commodityImageDAO,NoticeDAO noticeDAO){
        this.commodityDAO = commodityDAO;
        this.typeDAO = typeDAO;
        this.commodityImageDAO = commodityImageDAO;
        this.noticeDAO = noticeDAO;
    }

    /**
     * 根据id获取商品信息
     * @param id 商品id
     * @return 执行结果
     */
    public responseFromServer getById(Integer id){
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        //id查找是唯一的
        List<Commodity> commodities = commodityDAO.selectWithCondition(queryWrapper);
        if(commodities == null||commodities.size()!=1)
            return responseFromServer.error();
        return responseFromServer.success(commodities.get(0));
    }

    public responseFromServer getSimpleCommodity(Integer id){
        Commodity commodity = commodityDAO.getSimpleCommodityById(id);
        if(commodity==null){
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }

    public responseFromServer getDetailedCommodity(Integer id){
        return getById(id);
    }

    /**
     * 商品名称模糊检索，崭新程度排序
     * @param pageIndex 当前页数
     * @param name 商品名
     * count为偶数:顺序（从小到大）；奇数:倒序
     * @return Commodity数组
     */
    public responseFromServer getByNameSortedByNewness(Integer pageIndex, String name){
        Page<Commodity> page = new Page<>(pageIndex, Nums.pageSize);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis()); //当前时间
        IPage<Commodity> iPage = commodityDAO.sortByNewness(page, name, timestamp);
        MyPage<Commodity> myPage = new MyPage<>(iPage);
        return responseFromServer.success(myPage);
    }

    /**
     * 根据类型分类
     * @param typeId 标签
     * @return Commodity数组
     */
    public responseFromServer getByTypeId(Integer pageIndex, Integer typeId){
        Page<Commodity> page = new Page<>(pageIndex, Nums.pageSize);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis()); //当前时间
        IPage<Commodity> iPage = commodityDAO.sortByType(page, typeId, timestamp);
        MyPage<Commodity> myPage = new MyPage<>(iPage);
        return responseFromServer.success(myPage);
    }

    /**
     * 根据价格区间筛选物品
     * @param low 最低价
     * @param high 最高价
     * @return Commodity数组
     */
    public responseFromServer getBetweenPrice(Integer pageIndex, String name, Integer low, Integer high){
        Page<Commodity> page = new Page<>(pageIndex, Nums.pageSize);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis()); //当前时间
        IPage<Commodity> iPage = commodityDAO.betweenPrice(page, name, low, high, timestamp);
        MyPage<Commodity> myPage = new MyPage<>(iPage);
        return responseFromServer.success(myPage);
    }

    /**
     * 根据所有者信誉排序
     * @param name 商品名
     * @return Commodity数组
     */
    public responseFromServer sortByCredit(Integer pageIndex, String name){
        Page<Commodity> page = new Page<>(pageIndex, Nums.pageSize);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis()); //当前时间
        IPage<Commodity> iPage = commodityDAO.sortByCredit(page, name, timestamp);
        MyPage<Commodity> myPage = new MyPage<>(iPage);
        return responseFromServer.success(myPage);
    }

    /**
     * 插入商品
     * @param commodity 商品
     * @return 执行结果
     */
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    @Transactional
    public responseFromServer insertCommodity(Commodity commodity){
        if(commodityDAO.insert(commodity) != 1 || !insertCommodityInfo(commodity)){
            /*回滚事务*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }

    /**
     * 更新商品
     * @param commodity 商品
     * @return 执行结果
     */
    @Transactional
    public responseFromServer updateCommodity(Commodity commodity){
/*        if(isIdentityError(commodity, session))  //身份检查
            return responseFromServer.illegal();*/
        if(commodityDAO.updateById(commodity) != 1 || !updateCommodityInfo(commodity)){
            /*回滚事务*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        else{
            return responseFromServer.success();
        }
    }

    /**
     * 删除商品
     * @param commodity 商品名
     * @return 执行结果
     */
    @Transactional
    public responseFromServer deleteCommodity(Commodity commodity){
        /*身份检查放到controller层，避免批量删除时重复多次验证信息*/
/*
        if(isIdentityError(commodity, session))  //身份检查
            return responseFromServer.illegal();
*/
        deleteCommodityInfo(commodity);
        if(commodityDAO.deleteById(commodity.getId()) != 1){
            /*回滚事务*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        else{
            return responseFromServer.success();
        }
    }

    /**
     * 添加商品类型、图片等信息
     * @param commodity 商品
     * @return 执行结果
     */
    @Transactional
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    public boolean insertCommodityInfo(Commodity commodity){
//        List<CommodityImage> commodityImageList = commodity.getCommodityImages();
        List<Type> typeList = commodity.getTypes();

//        for(CommodityImage commodityImage:commodityImageList){
//            commodityImage.setCommodityId(commodity.getId());
//            if(commodityImageDAO.insert(commodityImage) != 1){
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                return false;
//            }
//        }
        for(Type type:typeList){
            type.setCommodityId(commodity.getId());
            if(typeDAO.insert(type) != 1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
        return true;
    }

    /**
     * 更新商品信息
     * @param commodity 商品
     * @return 执行结果
     */
    @Transactional
    public boolean updateCommodityInfo(Commodity commodity){
//        List<CommodityImage> commodityImageList = commodity.getCommodityImages();
        List<Type> typeList = commodity.getTypes();

        /*for(CommodityImage commodityImage:commodityImageList){
            commodityImage.setCommodityId(commodity.getId());
            if(commodityImageDAO.updateById(commodityImage) != 1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }*/
        for(Type type:typeList){
            type.setCommodityId(commodity.getId());
            if(typeDAO.updateById(type) != 1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
        return true;
    }

    /**
     * 查询某一notice下所有商品
     * @param notice 通告
     * @return 执行结果
     */
    public responseFromServer selectAllByNotice(Notice notice){
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("notice_id", notice.getId());
        List<Commodity> commodities = commodityDAO.selectWithCondition(queryWrapper);
        return responseFromServer.success(commodities);
    }

    /**
     * 删除某一notice下所有商品
     * @param notice 通告
     * @return 执行结果
     */
    @Transactional
    public responseFromServer deleteAllByNotice(Notice notice){
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("notice_id", notice.getId());
        List<Commodity> commodities = commodityDAO.selectWithCondition(queryWrapper);
        for(Commodity commodity:commodities)
            if(!deleteCommodity(commodity).isSuccess()){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return responseFromServer.error();
            }
        return responseFromServer.success();
    }

    /**
     * 删除图片、类型等信息,由于外键方式为CASCADE，因此只需删除本地缓存文件资源
     * @param commodity 商品
     */
    public void deleteCommodityInfo(Commodity commodity){
        List<CommodityImage> commodityImageList = commodity.getCommodityImages();
        for(CommodityImage commodityImage:commodityImageList){
            String path = commodityImage.getImageUrl();
            File file = new File(path);
            file.delete();
        }
    }

    /**
     * 返回图片路径
     * @param files 文件数组
     * @return 执行结果
     */
    public responseFromServer imageUrl(MultipartFile[] files){
        QueryWrapper<CommodityImage> queryWrapper = new QueryWrapper<>();
        List<CommodityImage> commodityImages = commodityImageDAO.selectList(queryWrapper);
        List<String> paths = new ArrayList<>(); //存储附件路径
        for(MultipartFile file:files) {
            try {
                byte[] bytes = file.getBytes();
                String base = System.getProperty("user.dir") + "\\images\\";
                Path path = Paths.get(base + commodityImages.size() + 1);
                //如果没有files文件夹，则创建
                if (!Files.isWritable(path)) {
                    Files.createDirectories(Paths.get(System.getProperty("user.dir")));
                }
                //文件写入指定路径
                Files.write(path, bytes);
                paths.add(base + commodityImages.size() + 1);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseFromServer.success(paths);
    }

    /**
     * 用户身份检查
     * @param commodity 商品
     * @param session HttpSession
     * @return 结果
     */
    public boolean isIdentityError(Commodity commodity, HttpSession session){
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", commodity.getId());
        Account account = commodityDAO.selectWithCondition(queryWrapper).get(0).getNotice().getUser();
        return !AccountVerify.verify(account, session);
    }
}
