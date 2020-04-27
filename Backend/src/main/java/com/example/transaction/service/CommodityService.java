package com.example.transaction.service;

import com.example.transaction.pojo.Commodity;
import com.example.transaction.util.responseFromServer;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/26 15:42
 * @Content: 商品业务层
 */

public interface CommodityService {
    //商品名称模糊检索，崭新程度排序
    public List<Commodity> getByNameSortedByNewness(String name);
    //根据类型分类
    public List<Commodity> getByTypeId(Integer typeId);
    //根据价格区间筛选物品
    public List<Commodity> getBetweenPrice(String name, Integer low, Integer high);
    //根据所有者信用排序
    public List<Commodity> sortByCredit(String name);
    //插入商品
    public responseFromServer insertCommodity(Commodity commodity);
    //更新商品信息
    public responseFromServer updateCommodity(Commodity commodity);
}
