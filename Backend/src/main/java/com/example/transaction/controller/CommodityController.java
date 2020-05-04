package com.example.transaction.controller;

import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Notice;
import com.example.transaction.service.CommodityService;
import com.example.transaction.service.NoticeService;
import com.example.transaction.service.impl.CommodityServiceImpl;
import com.example.transaction.util.AccountVerify;
import com.example.transaction.util.code.ResourcePath;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Map;
import java.util.UUID;

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
    private final NoticeService noticeService;
    @Autowired
    CommodityController(CommodityService commodityService,NoticeService noticeService){
        this.commodityService = commodityService;
        this.noticeService = noticeService;
    }


    /**
     * 上传商品图片
     * @param files
     * @param commodityId
     * @param session
     * @return
     */
    @PostMapping("/uploadPicture")
    public responseFromServer upload(@RequestParam(name = "file") MultipartFile[] files,
                                     @RequestParam(name = "commodityId") Integer commodityId,
                                     HttpSession session) {
        if (files == null) {
            return responseFromServer.error(0, "请选择要上传的图片");
        }
        if( files.length >6 ){
            return responseFromServer.error();
        }
        for(MultipartFile file:files){
            if(file.getSize()> 1024 * 1024 * 10){
                return responseFromServer.error(0, "文件大小不能大于10M");
            }
        }

        responseFromServer response = commodityService.getDetailedCommodity(commodityId);
        if(!response.isSuccess()){
            return responseFromServer.error();
        }
        Commodity commodity = (Commodity) response.getData();
        Account account = new Account(commodity.getNotice().getAccountId());
        if(!AccountVerify.verify(account,session)){
            return responseFromServer.error();
        }
        return commodityService.uploadCommodityImages(files,commodityId);
    }


    /**
     * 根据id获取商品信息
     * @param id 商品id
     * @return 执行结果
     */
    public responseFromServer getById(@RequestBody Integer id){
        return commodityService.getById(id);
    }

    /**
     * 商品名称模糊检索，崭新程度排序
     * @param map
     * @return
     */
    @RequestMapping("/search/SortByNewness")
    public responseFromServer getByNameSortedByNewness(@RequestBody Map<String,Object> map){
        Integer pageIndex = (Integer) map.get("pageIndex");
        String searchStr = (String) map.get("searchStr");
        if(searchStr == null||searchStr == ""){
            return responseFromServer.error();
        }
        pageIndex = pageIndex == null||pageIndex<=0?1:pageIndex;
        return commodityService.getByNameSortedByNewness(pageIndex, searchStr);
    }

    /**
     * 根据类型分类
     * @param map
     * @return
     */
    @RequestMapping("/search/getByType")
    public responseFromServer getByTypeId(@RequestBody Map<String,Object> map){
        Integer pageIndex = (Integer) map.get("pageIndex");
        Integer typeId = (Integer) map.get("typeId");
        if(typeId == null){
            return responseFromServer.error();
        }
        pageIndex = pageIndex == null||pageIndex<=0?1:pageIndex;
        return commodityService.getByTypeId(pageIndex, typeId);
    }

    /**
     * 根据价格区间筛选物品
     * @param map
     * @return
     */
    @RequestMapping("/search/betweenPrice")
    public responseFromServer getBetweenPrice(@RequestBody Map<String,Object> map){
        Integer pageIndex = (Integer) map.get("pageIndex");
        Integer low = (Integer) map.get("low"),high = (Integer)map.get("high");
        String searchStr = (String) map.get("searchStr");
        if(searchStr == null||searchStr == ""){
            return responseFromServer.error();
        }
        pageIndex = pageIndex == null||pageIndex<=0?1:pageIndex;
        return commodityService.getBetweenPrice(pageIndex, searchStr, low, high);
    }

    /**
     * 根据所有者信誉排序
     * @param map
     * @return
     */
    @RequestMapping("/search/sortByCredit")
    public responseFromServer sortByCredit(@RequestBody Map<String,Object> map){
        Integer pageIndex = (Integer) map.get("pageIndex");
        String searchStr = (String) map.get("searchStr");
        if(searchStr == null||searchStr == ""){
            return responseFromServer.error();
        }
        return commodityService.sortByCredit(pageIndex, searchStr);
    }

    /**
     * 插入商品
     * @param commodity 商品
     * @param session HttpSession
     * @return 执行结果
     */
    @RequestMapping("/insertCommodity")
    public responseFromServer insertCommodity(@RequestBody Commodity commodity, HttpSession session){
        if(commodity.getNoticeId()==null)
            return responseFromServer.error();
        responseFromServer response = noticeService.getSimpleNotice(commodity.getNoticeId());
        if(!response.isSuccess()){
            return responseFromServer.error();
        }else{
            Notice notice = (Notice) response.getData();
            Account account = (Account) session.getAttribute("currentAccount");
            if(notice.getAccountId()==null || notice.getAccountId() == account.getId().intValue()){
                return responseFromServer.error();
            }
            /*当前插入商品的notice不属于该用户*/
            return commodityService.insertCommodity(commodity);
        }
    }

    /**
     * 更新商品信息
     * @param commodity 商品
     * @return 执行结果
     */
    @RequestMapping("/updateCommodity")
    public responseFromServer updateCommodity(@RequestBody Commodity commodity, HttpSession session){
        responseFromServer response = getById(commodity.getId());
        if(AccountVerify.verifySellerByCommodityId(response,session)) {
            return commodityService.updateCommodity(commodity);
        }else{
            return responseFromServer.illegal();
        }
    }

    /**
     * 删除商品
     * @param commodity 商品
     * @param session HttpSession
     * @return 执行结果
     */
    @RequestMapping("/deleteCommodity")
    public responseFromServer deleteCommodity(@RequestBody Commodity commodity, HttpSession session){
        responseFromServer response = getById(commodity.getId());
        if(AccountVerify.verifySellerByCommodityId(response,session))
        {
            return commodityService.deleteCommodity((Commodity)response.getData());
        }
        return responseFromServer.illegal();
    }

    /**
     * 查询某一notice下所有商品
     * @param notice 通告
     * @return 执行结果
     */
    @RequestMapping("/selectByNoticeId")
    public responseFromServer selectAllByNoticeId(@RequestBody Notice notice){
        return commodityService.selectAllByNotice(notice);
    }

    /**
     * 删除某一notice下所有商品
     * @param notice 通告
     * @param session HttpSession
     * @return 执行结果
     */
    @RequestMapping("/deleteByNoticeId")
    public responseFromServer deleteAllByNoticeId(@RequestBody Notice notice, HttpSession session){
        Account account = new Account(notice.getAccountId());
        if(!AccountVerify.verify(account, session))  //用户合法性检查
            return responseFromServer.error();
        return  commodityService.deleteAllByNotice(notice);
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
