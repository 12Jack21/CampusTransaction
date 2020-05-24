package com.example.transaction.controller;

import com.example.transaction.dto.CommoditySearch;
import com.example.transaction.dto.Condition;
import com.example.transaction.dto.commodity.Pagination;
import com.example.transaction.pojo.*;
import com.example.transaction.service.CommodityService;
import com.example.transaction.service.NoticeService;
import com.example.transaction.service.SearchService;
import com.example.transaction.service.impl.AccountVerify;
import com.example.transaction.util.code.Nums;
import com.example.transaction.util.jsonParamResolver.handler.RequestJson;
import com.example.transaction.util.responseFromServer;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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
    SearchService searchService;


    @Autowired
    CommodityController(SearchService searchService, CommodityService commodityService, NoticeService noticeService, AccountVerify accountVerify) {
        this.commodityService = commodityService;
        this.noticeService = noticeService;
        this.accountVerify = accountVerify;
        this.searchService = searchService;
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
    @ApiOperation(value = "为已经创建的商品上传新的图片")
    @ApiImplicitParam(name = "files", value = "商品图片列表", paramType = "MultipartFile[]", dataType = "MultipartFile[]")
    @PostMapping("/images/{commodityId}")
    public responseFromServer upload(@RequestParam(name = "file") MultipartFile[] files,
                                     @PathVariable Integer commodityId,
                                     HttpServletRequest request) {
        if (files == null) {
            return responseFromServer.error(0, "请选择要上传的图片");
        }
        if (files.length > Nums.maxUploadingFilesCount) {
            return responseFromServer.error();
        }
        for (MultipartFile file : files) {
            if (file.getSize() > 1024 * 1024 * 10) {
                return responseFromServer.error(0, "文件大小不能大于10M");
            }
        }

        responseFromServer response = commodityService.getDetailedCommodity(commodityId);
        if (!response.isSuccess()) {
            return responseFromServer.error();
        }
        Commodity commodity = (Commodity) response.getData();
        Account account = new Account(commodity.getNotice().getAccountId());
        if (!accountVerify.verify(account, request)) {
            return responseFromServer.error();
        }
        return commodityService.uploadCommodityImages(files, commodityId, true);
    }


    @ApiOperation(value = "搜索商品")
    @ApiImplicitParam(name = "condition", value = "condition", paramType = "Condition", dataType = "Condition")
    @PostMapping("/search")
    public responseFromServer search(@RequestBody CommoditySearch commoditySearch, HttpServletRequest request) {
        Condition condition = commoditySearch.getCondition();
        condition.setPageIndex(commoditySearch.getPagination()==null?-1:commoditySearch.getPagination().getPageIndex());
        condition.setEndTime(commoditySearch.getPagination()==null?(new Date()):commoditySearch.getPagination().getEndTime());
        condition.setKeyword(commoditySearch.getKeyword()==null?null:commoditySearch.getKeyword());
        if(condition.getLowPrice().equals(-1)){
            condition.setLowPrice(null);
        }
        if(condition.getHighPrice().equals(-1)){
            condition.setHighPrice(null);
        }
        responseFromServer response = commodityService.search(commoditySearch.getCondition());
        if (response.isSuccess()) {
            if (condition.getKeyword() != null || condition.getKeyword() != "") {
                Account account = accountVerify.getCurrentAccount(request);
                /*如果用户登录则添加到搜索记录中*/
                if (account !=null && (searchService.addSearchRecord(account.getId(), condition.getKeyword()).isSuccess())) {
                    return response;
                }
            }
            /*暂时先:插入搜索记录失败时也返回成功*/
        }
        return response;
    }

    @ApiOperation(value = "商品排序")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "pageIndex", value = "pageIndex", paramType = "Integer", dataType = "Integer"),
                    @ApiImplicitParam(name = "endTime", value = "endTime", paramType = "String", dataType = "String"),
                    @ApiImplicitParam(name = "userAddress", value = "userAddress", paramType = "String", dataType = "String")
            }
    )
    @GetMapping("/sort/{sortType}")
    public responseFromServer getSortedCommodity(@PathVariable Integer sortType,
                                                 Condition condition,
                                                 HttpServletRequest request) {
        condition.setSortType(sortType);
        return commodityService.search(condition);
    }


    /**
     * 根据id获取商品信息
     *
     * @param commodityId 商品id
     * @return 执行结果
     */
    @ApiOperation(value = "获取商品信息")
    @GetMapping("/{commodityId}")
    public responseFromServer getById(@PathVariable Integer commodityId) {
        return commodityService.getDetailedCommodity(commodityId);
    }

    /**
     * 商品名称模糊检索，崭新程度排序
     *
     * @param pageIndex
     * @return
     */
    @ApiOperation(value = "商品名称模糊查找，崭新程度排序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页面索引", paramType = "Integer", dataType = "Integer")
    })
    @GetMapping("/{searchStr}/newness")
    public responseFromServer getByNameSortedByNewness(@RequestJson Integer pageIndex, @PathVariable String searchStr) {
//        Integer pageIndex = (Integer) map.get("pageIndex");
//        String searchStr = (String) map.get("searchStr");
        if (StringUtil.isNullOrEmpty(searchStr)) {
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
//    @ApiOperation(value = "商品类型查找")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "typeId", value = "类型Id", paramType = "Integer", dataType = "Integer"),
//            @ApiImplicitParam(name = "pageIndex", value = "页面索引", paramType = "Integer", dataType = "Integer")
//    })
//    @GetMapping("/type/{typeId}")
//    public responseFromServer getByTypeId(@RequestJson Integer pageIndex, @PathVariable Integer typeId) {
////        Integer pageIndex = (Integer) map.get("pageIndex");
////        Integer typeId = (Integer) map.get("typeId");
//        if (typeId == null) {
//            return responseFromServer.error();
//        }
//        pageIndex = pageIndex == null || pageIndex <= 0 ? 1 : pageIndex;
//        return commodityService.getByTypeId(pageIndex, typeId);
//    }


    /**
     * 根据价格区间筛选物品
     *
     * @param pageIndex
     * @param low
     * @param high
     * @param searchStr
     * @return
     */
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
     * 插入商品
     *
     * @param commodity 商品
     * @param request   HttpServletRequest
     * @return 执行结果
     */
    @ApiOperation(value = "插入商品")
    @PostMapping
    public responseFromServer insertCommodity(@RequestBody Commodity commodity, HttpServletRequest request) {
        if (commodity.getNoticeId() == null) {
            return responseFromServer.error();
        }
        responseFromServer response = noticeService.getSimpleNotice(commodity.getNoticeId());
        if (!response.isSuccess()) {
            return responseFromServer.error();
        } else {
            Notice notice = (Notice) response.getData();
            Account account = (Account) request.getAttribute("currentAccount");
            if (notice.getAccountId() == null || notice.getAccountId() == account.getId().intValue()) {
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
    @ApiOperation(value = "删除商品")
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
    @ApiOperation(value = "查询某一notice下所有商品")
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
    @ApiOperation(value = "删除某一notice下所有商品")
    @DeleteMapping("/notice/{noticeId}")
    public responseFromServer deleteAllByNoticeId(@PathVariable Integer noticeId, HttpServletRequest request) {
        Notice notice = new Notice(noticeId);
        Account account = new Account(notice.getAccountId());
        //用户合法性检查
        if (!accountVerify.verify(account, request)) {
            return responseFromServer.error();
        }
        return commodityService.deleteAllByNotice(notice);
    }


    /**
     * 返回图片路径
     * @param image
     * @return 执行结果
     */
    @ApiOperation(value = "上传一张商品图像")
    @ApiImplicitParam(name = "image", value = "商品图像文件", paramType = "MultipartFile", dataType = "MultipartFile")
    @PostMapping("/image")
    public responseFromServer uploadImage(@RequestParam MultipartFile image) {
        MultipartFile[] images = {image};
        responseFromServer response = commodityService.uploadCommodityImages(images, null, false);
        if (response.isSuccess()) {
            CommodityImage commodityImage = (CommodityImage) response.getData();
            if (commodityImage != null && commodityImage.getImageUrl() != null && commodityImage.getImageUrl() != "") {
                return responseFromServer.success(commodityImage);
            }
        }
        return responseFromServer.error();
    }


    /**
     * 返回多个图片路径
     *
     * @param images
     * @return 执行结果
     */
    @ApiOperation(value = "上传多张商品图像")
    @ApiImplicitParam(name = "images", value = "商品图像文件", paramType = "MultipartFile[]", dataType = "MultipartFile")
    @PostMapping("/images")
    public responseFromServer uploadImages(@RequestParam MultipartFile[] images) {
        return commodityService.uploadCommodityImages(images, null, false);
    }


}
