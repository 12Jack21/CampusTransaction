package com.example.transaction.controller;

import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Notice;
import com.example.transaction.service.CommodityService;
import com.example.transaction.service.impl.CommodityServiceImpl;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

/**
 * @ClassName: CommodityController
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/25 20:06
 */
@RestController
@CrossOrigin
@RequestMapping("/commodity")
public class CommodityController {
    private final CommodityService commodityService;
    @Autowired
    CommodityController(CommodityServiceImpl commodityServiceImpl){
        this.commodityService = commodityServiceImpl;
    }


    /**
     * 根据id获取商品信息
     * @param id 商品id
     * @return 执行结果
     */
    public responseFromServer getById(Integer id){
        return commodityService.getById(id);
    }

    /**
     * 商品名称模糊检索，崭新程度排序
     * @param name 商品名称
     * @return Commodity数组
     */
    @RequestMapping("/sortByNewness")
    public responseFromServer getByNameSortedByNewness(@RequestParam(value = "pageIndex")Integer pageIndex, @RequestParam(value = "name") String name){
        return commodityService.getByNameSortedByNewness(pageIndex, name);
    }

    /**
     * 根据类型分类
     * @param typeId 标签
     * @return Commodity数组
     */
    @RequestMapping("/getByType")
    public responseFromServer getByTypeId(@RequestParam(value = "pageIndex") Integer pageIndex, @RequestParam(value = "typeId")Integer typeId){
        return commodityService.getByTypeId(pageIndex, typeId);
    }

    /**
     * 根据价格区间筛选物品
     * @param name 商品名称
     * @param low 最低价
     * @param high 最高价
     * @return Commodity数组
     */
    @RequestMapping("/betweenPrice")
    public responseFromServer getBetweenPrice(@RequestParam(value = "pageIndex") Integer pageIndex, @RequestParam(value = "name") String name, @RequestParam(value = "low")Integer low, @RequestParam(value = "high")Integer high){
        return commodityService.getBetweenPrice(pageIndex, name, low, high);
    }

    /**
     * 根据所有者信誉排序
     * @param name 商品名称
     * @return Commodity数组
     */
    @RequestMapping("/sortByCredit")
    public responseFromServer sortByCredit(@RequestParam(value = "pageIndex") Integer pageIndex, @RequestParam(value = "name") String name){
        return commodityService.sortByCredit(pageIndex, name);
    }

    /**
     * 插入商品
     * @param commodity 商品
     * @param session HttpSession
     * @return 执行结果
     */
    public responseFromServer insertCommodity(Commodity commodity, HttpSession session){
        return commodityService.insertCommodity(commodity, session);
    }

    /**
     * 更新商品信息
     * @param commodity 商品
     * @return 执行结果
     */
    @RequestMapping("/update")
    public responseFromServer updateCommodity(@RequestBody Commodity commodity, HttpSession session){
        return commodityService.updateCommodity(commodity, session);
    }

    /**
     * 删除商品
     * @param commodity 商品
     * @param session HttpSession
     * @return 执行结果
     */
    @RequestMapping("/delete")
    public responseFromServer deleteCommodity(@RequestBody Commodity commodity, HttpSession session){
        return commodityService.deleteCommodity(commodity, session);
    }

    /**
     * 查询某一notice下所有商品
     * @param notice 通告
     * @return 执行结果
     */
    @RequestMapping("/selectByNoticeId")
    public responseFromServer selectAllByNoticeId(@RequestBody Notice notice){
        return commodityService.selectAllByNoticeId(notice);
    }

    /**
     * 删除某一notice下所有商品
     * @param notice 通告
     * @param session HttpSession
     * @return 执行结果
     */
    @RequestMapping("/deleteByNoticeId")
    public responseFromServer deleteAllByNoticeId(@RequestBody Notice notice, HttpSession session){
        return  commodityService.deleteAllByNoticeId(notice, session);
    }

    /**
     * 返回图片路径
     * @param files 文件数组
     * @return 执行结果
     */
    public responseFromServer imageUrl(MultipartFile[] files){
        return commodityService.imageUrl(files);
    }
}
