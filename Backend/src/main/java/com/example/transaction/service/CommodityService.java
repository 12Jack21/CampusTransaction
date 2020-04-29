package com.example.transaction.service;

import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Notice;
import com.example.transaction.util.responseFromServer;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

/**
 * @Author: 高战立
 * @Date: 2020/4/26 15:42
 * @Content: 商品业务层
 */

public interface CommodityService {
    //根据id获取商品
    public responseFromServer getById(Integer id);
    //商品名称模糊检索，崭新程度排序
    public responseFromServer getByNameSortedByNewness(Integer pageIndex, String name);
    //根据类型分类
    public responseFromServer getByTypeId(Integer pageIndex, Integer typeId);
    //根据价格区间筛选物品
    public responseFromServer getBetweenPrice(Integer pageIndex, String name, Integer low, Integer high);
    //根据所有者信誉排序
    public responseFromServer sortByCredit(Integer pageIndex, String name);
    //插入商品
    public responseFromServer insertCommodity(Commodity commodity);
    //更新商品信息
    public responseFromServer updateCommodity(Commodity commodity);
    //删除商品信息
    public responseFromServer deleteCommodity(Commodity commodity);
    //查询某一notice下所有商品
    public responseFromServer selectAllByNotice(Notice notice);
    //删除某一notice下所有商品
    public responseFromServer deleteAllByNotice(Notice notice);
    //返回商品图片路径
    public responseFromServer imageUrl(MultipartFile[] files);
}
