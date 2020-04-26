package com.example.transaction.controller;

import com.example.transaction.pojo.Commodity;
import com.example.transaction.service.CommodityService;
import com.example.transaction.service.impl.CommodityServiceImpl;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: CommodityController
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/25 20:06
 */
@RestController
@RequestMapping("/commodity")
public class CommodityController {
    private final CommodityService commodityService;
    @Autowired
    CommodityController(CommodityServiceImpl commodityServiceImpl){
        this.commodityService = commodityServiceImpl;
    }

    /**
     * 商品名称模糊检索，崭新程度排序
     * @param name 商品名称
     * @return Commodity数组
     */
    @RequestMapping("/sortByNewness")
    public List<Commodity> getByNameSortedByNewness(@RequestParam(value = "name") String name){
        return commodityService.getByNameSortedByNewness(name);
    }

    /**
     * 根据类型分类
     * @param typeId 标签
     * @return Commodity数组
     */
    @RequestMapping("/getByType")
    public List<Commodity> getByTypeId(@RequestParam(value = "typeId")Integer typeId){
        return commodityService.getByTypeId(typeId);
    }

    /**
     * 根据价格区间筛选物品
     * @param low 最低价
     * @param high 最高价
     * @return Commodity数组
     */
    @RequestMapping("/betweenPrice")
    public List<Commodity> getBetweenPrice(@RequestParam(value = "name") String name, @RequestParam(value = "low")Integer low, @RequestParam(value = "high")Integer high){
        return commodityService.getBetweenPrice(name, low, high);
    }

    /**
     * 更新商品信息
     * @param commodity 商品
     * @return 执行结果
     */
    @RequestMapping("/update")
    public responseFromServer updateCommodity(@RequestBody Commodity commodity){
        return commodityService.updateCommodity(commodity);
    }
}
