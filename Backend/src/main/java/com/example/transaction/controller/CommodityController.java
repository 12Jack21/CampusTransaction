package com.example.transaction.controller;

import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Notice;
import com.example.transaction.service.CommodityService;
import com.example.transaction.service.NoticeService;
import com.example.transaction.service.impl.AccountVerify;
import com.example.transaction.util.jsonParamResolver.handler.RequestJson;
import com.example.transaction.util.responseFromServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ClassName: CommodityController
 * @Author: 曾志昊
 * @Date: 2020/4/25 20:06
 */
@RestController
@CrossOrigin
@RequestMapping("/commodities")
@Api(tags = "CommodityController")
public class CommodityController {
    CommodityService commodityService;
    NoticeService noticeService;
    AccountVerify accountVerify;

    @Autowired
    CommodityController(CommodityService commodityService, NoticeService noticeService, AccountVerify accountVerify) {
        this.commodityService = commodityService;
        this.noticeService = noticeService;
        this.accountVerify = accountVerify;
    }

    /**
     * 上传商品图片
     *
     * @param files
     * @param commodityId
     * @param request
     * @return responseFromServer
     */
//    @PostMapping("/uploadPicture")
    @ApiOperation(value = "上传商品图片")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "commodity_id", value = "商品Id", paramType = "Integer", dataType = "Integer"),
                    @ApiImplicitParam(name = "files", value = "商品图片列表", paramType = "MultipartFile[]", dataType = "MultipartFile[]")
            }
    )
    @PostMapping("/pictures/{commodityId}")
    public responseFromServer upload(@RequestParam(name = "file") MultipartFile[] files,
                                     @PathVariable Integer commodityId,
                                     HttpServletRequest request) {
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
        if (!accountVerify.verify(account, request)) {
            return responseFromServer.error();
        }
        return commodityService.uploadCommodityImages(files,commodityId);
    }


    /**
     * 根据id获取商品信息
     * @param commodityId 商品id
     * @return 执行结果
     */
    @ApiOperation(value = "获取商品信息")
    @ApiImplicitParam(name = "commodityId", value = "商品Id", paramType = "Integer", dataType = "Integer")
    @GetMapping("/{commodityId}")
    public responseFromServer getById(@PathVariable Integer commodityId) {
        return commodityService.getById(commodityId);
    }

    /**
     * 商品名称模糊检索，崭新程度排序
     *
     * @param pageIndex
     * @return
     */
//    @RequestMapping("/search/SortByNewness")
    @ApiOperation(value = "商品名称模糊查找，崭新程度排序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchStr", value = "搜索字符串", paramType = "String", dataType = "String"),
            @ApiImplicitParam(name = "pageIndex", value = "页面索引", paramType = "Integer", dataType = "Integer")
    })
    @GetMapping("/{searchStr}/newness")
    public responseFromServer getByNameSortedByNewness(@RequestJson Integer pageIndex, @PathVariable String searchStr) {
//        Integer pageIndex = (Integer) map.get("pageIndex");
//        String searchStr = (String) map.get("searchStr");
        if (searchStr == null || searchStr == "") {
            return responseFromServer.error();
        }
        pageIndex = pageIndex == null || pageIndex <= 0 ? 1 : pageIndex;
        return commodityService.getByNameSortedByNewness(pageIndex, searchStr);
    }

    /**
     * 根据类型分类
     *
     * @param pageIndex
     * @return
     */
//    @RequestMapping("/search/getByType")
    @ApiOperation(value = "商品类型查找")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "typeId", value = "类型Id", paramType = "Integer", dataType = "Integer"),
            @ApiImplicitParam(name = "pageIndex", value = "页面索引", paramType = "Integer", dataType = "Integer")
    })
    @GetMapping("/type/{typeId}")
    public responseFromServer getByTypeId(@RequestJson Integer pageIndex, @PathVariable Integer typeId) {
//        Integer pageIndex = (Integer) map.get("pageIndex");
//        Integer typeId = (Integer) map.get("typeId");
        if (typeId == null) {
            return responseFromServer.error();
        }
        pageIndex = pageIndex == null || pageIndex <= 0 ? 1 : pageIndex;
        return commodityService.getByTypeId(pageIndex, typeId);
    }


    /**
     * 根据价格区间筛选物品
     * @param pageIndex
     * @param low
     * @param high
     * @param searchStr
     * @return
     */
//    @RequestMapping("/search/betweenPrice")
    @ApiOperation(value = "根据价格区间筛选物品")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "searchStr", value = "商品名称", paramType = "String", dataType = "String"),
                    @ApiImplicitParam(name = "low", value = "最低价", paramType = "Integer", dataType = "Integer"),
                    @ApiImplicitParam(name = "high", value = "最高价", paramType = "Integer", dataType = "Integer"),
                    @ApiImplicitParam(name = "pageIndex", value = "页面索引", paramType = "Integer", dataType = "Integer")
            }
    )
    @GetMapping("/{searchStr}/price")
    public responseFromServer getBetweenPrice(@RequestJson Integer pageIndex,
                                              @RequestJson Integer low,
                                              @RequestJson Integer high,
                                              @PathVariable(name = "searchStr") String searchStr) {
//        Integer pageIndex = (Integer) map.get("pageIndex");
//        Integer low = (Integer) map.get("low"), high = (Integer) map.get("high");
//        String searchStr = (String) map.get("searchStr");
        if (searchStr == null || searchStr == "") {
            return responseFromServer.error();
        }
        pageIndex = pageIndex == null || pageIndex <= 0 ? 1 : pageIndex;
        return commodityService.getBetweenPrice(pageIndex, searchStr, low, high);
    }

    /**
     * 根据所有者信誉排序
     *
     * @param pageIndex
     * @param searchStr
     * @return
     */
//    @RequestMapping("/search/sortByCredit")
    @ApiOperation(value = "根据所有者信誉排序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchStr", value = "搜索字符串", paramType = "String", dataType = "String"),
            @ApiImplicitParam(name = "page_index", value = "页面索引", paramType = "Integer", dataType = "Integer")
    })
    @GetMapping("/{searchStr}/credit")
    public responseFromServer sortByCredit(@RequestJson Integer pageIndex, @PathVariable String searchStr) {
//        Integer pageIndex = (Integer) map.get("pageIndex");
        if (searchStr == null || searchStr == "") {
            return responseFromServer.error();
        }
        return commodityService.sortByCredit(pageIndex, searchStr);
    }

    /**
     * 插入商品
     * @param commodity 商品
     * @param request HttpServletRequest
     * @return 执行结果
     */
//    @RequestMapping("/insertCommodity")
    @ApiOperation(value = "插入商品")
    @PostMapping
    public responseFromServer insertCommodity(@RequestBody Commodity commodity, HttpServletRequest request){
        if(commodity.getNoticeId()==null)
            return responseFromServer.error();
        responseFromServer response = noticeService.getSimpleNotice(commodity.getNoticeId());
        if(!response.isSuccess()){
            return responseFromServer.error();
        }else{
            Notice notice = (Notice) response.getData();
            Account account = (Account) request.getAttribute("currentAccount");
            if(notice.getAccountId()==null || notice.getAccountId() == account.getId().intValue()){
                return responseFromServer.error();
            }
            /*当前插入商品的notice不属于该用户*/
            return commodityService.insertCommodity(commodity);
        }
    }

    /**
     * 更新商品信息
     *
     * @param commodityId
     * @param request
     * @return
     */
    @ApiOperation(value = "更新商品信息")
    @ApiImplicitParam(name = "commodityId", value = "商品Id", paramType = "Integer", dataType = "Integer")
    @PutMapping("/{commodityId}")
//    @RequestMapping("/updateCommodity")
    public responseFromServer updateCommodity(@PathVariable Integer commodityId, HttpServletRequest request) {
        Commodity commodity = new Commodity(commodityId);
        responseFromServer response = getById(commodity.getId());
        if (accountVerify.verifySellerByCommodityId(response, request)) {
            return commodityService.updateCommodity(commodity);
        } else {
            return responseFromServer.illegal();
        }
    }

    /**
     * 删除商品
     *
     * @param commodityId
     * @param request
     * @return
     */
//    @RequestMapping("/deleteCommodity")
    @ApiOperation(value = "删除商品")
    @ApiImplicitParam(name = "commodityId", value = "商品Id", paramType = "Integer", dataType = "Integer")
    @DeleteMapping("/{commodityId}")
    public responseFromServer deleteCommodity(@PathVariable Integer commodityId, HttpServletRequest request) {
        Commodity commodity = new Commodity(commodityId);
        responseFromServer response = getById(commodity.getId());
        if (accountVerify.verifySellerByCommodityId(response, request)) {
            return commodityService.deleteCommodity((Commodity) response.getData());
        }
        return responseFromServer.illegal();
    }

    /**
     * 查询某一notice下所有商品
     *
     * @param noticeId
     * @return
     */
//    @RequestMapping("/selectByNoticeId")
    @ApiOperation(value = "查询某一notice下所有商品")
    @ApiImplicitParam(name = "noticeId", value = "通告Id", paramType = "Integer", dataType = "Integer")
    @GetMapping("/notice/{noticeId}")
    public responseFromServer selectAllByNoticeId(@PathVariable Integer noticeId) {
        Notice notice = new Notice(noticeId);
        return commodityService.selectAllByNotice(notice);
    }

    /**
     * 删除某一notice下所有商品
     *
     * @param noticeId
     * @param request
     * @return
     */
//    @RequestMapping("/deleteByNoticeId")
    @ApiOperation(value = "删除某一notice下所有商品")
    @ApiImplicitParam(name = "noticeId", value = "通告Id", paramType = "Integer", dataType = "Integer")
    @DeleteMapping("/notice/{noticeId}")
    public responseFromServer deleteAllByNoticeId(@PathVariable Integer noticeId, HttpServletRequest request) {
        Notice notice = new Notice(noticeId);
        Account account = new Account(notice.getAccountId());
        if (!accountVerify.verify(account, request))  //用户合法性检查
            return responseFromServer.error();
        return commodityService.deleteAllByNotice(notice);
    }

    /**
     * 返回图片路径
     * @param files 文件数组
     * @return 执行结果
     */
    @ApiOperation(value = "返回上传图片路径")
    @ApiImplicitParam(name = "files", value = "商品图片列表", paramType = "MultipartFile[]", dataType = "MultipartFile[]")
    @PostMapping("/{files}")
    public responseFromServer imageUrl(MultipartFile[] files) {
        return commodityService.imageUrl(files);
    }


    @RequestMapping("/{firstVariable}/test")
    public responseFromServer test(@RequestJson String age, @PathVariable(name = "firstVariable") Integer firstVariable) {
        String hahahahaha = "";
        return responseFromServer.success();
    }



}
