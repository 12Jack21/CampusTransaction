package com.example.transaction.controller;

import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Notice;
import com.example.transaction.service.CommodityService;
import com.example.transaction.service.NoticeService;
import com.example.transaction.service.impl.AccountVerify;
import com.example.transaction.util.responseFromServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
                    @ApiImplicitParam(name = "commodity_id", value = "商品Id",  paramType = "Integer", dataType = "Integer"),
                    @ApiImplicitParam(name = "files", value = "商品图片列表",  paramType = "MultipartFile[]", dataType = "MultipartFile[]")
            }
    )
    @PostMapping("/{commodity_id}/{files}")
    public responseFromServer upload(@RequestParam(name = "file") MultipartFile[] files,
                                     @RequestParam(name = "commodityId") Integer commodityId,
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
     * @param id 商品id
     * @return 执行结果
     */
    @ApiOperation(value = "获取商品信息")
    @ApiImplicitParam(name = "commodity_id", value = "商品Id",  paramType = "Integer", dataType = "Integer")
    @GetMapping("/{commodity_id}")
    public responseFromServer getById(@RequestBody Integer id){
        return commodityService.getById(id);
    }

    /**
     * 商品名称模糊检索，崭新程度排序
     * @param map
     * @return
     */
//    @RequestMapping("/search/SortByNewness")
    @ApiOperation(value = "商品名称模糊查找，崭新程度排序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commodity_name", value = "商品名称",  paramType = "String", dataType = "String"),
            @ApiImplicitParam(name = "page_index", value = "页面索引",  paramType = "Integer", dataType = "Integer")
    })
    @GetMapping("/{commodity_name}/newness")
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
//    @RequestMapping("/search/getByType")
    @ApiOperation(value = "商品类型查找")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type_id", value = "类型Id",  paramType = "Integer", dataType = "Integer"),
            @ApiImplicitParam(name = "page_index", value = "页面索引",  paramType = "Integer", dataType = "Integer")
    })
    @GetMapping("/type/{type_id}")
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
//    @RequestMapping("/search/betweenPrice")
    @ApiOperation(value = "根据价格区间筛选物品")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "commodity_name", value = "商品名称",  paramType = "String", dataType = "String"),
                    @ApiImplicitParam(name = "low", value = "最低价",  paramType = "Integer", dataType = "Integer"),
                    @ApiImplicitParam(name = "high", value = "最高价",  paramType = "Integer", dataType = "Integer"),
                    @ApiImplicitParam(name = "page_index", value = "页面索引",  paramType = "Integer", dataType = "Integer")
            }
    )
    @GetMapping("/{commodity_name}/price")
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
//    @RequestMapping("/search/sortByCredit")
    @ApiOperation(value = "根据所有者信誉排序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commodity_name", value = "商品名称",  paramType = "String", dataType = "String"),
            @ApiImplicitParam(name = "page_index", value = "页面索引",  paramType = "Integer", dataType = "Integer")
    })
    @GetMapping("/{commodity_name}/credit")
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
     * @param commodity 商品
     * @return 执行结果
     */
    @ApiOperation(value = "更新商品信息")
    @ApiImplicitParam(name = "commodity_id", value = "商品Id",  paramType = "Integer", dataType = "Integer")
    @PutMapping("/{commodity_id}")
//    @RequestMapping("/updateCommodity")
    public responseFromServer updateCommodity(@RequestBody Commodity commodity, HttpServletRequest request){
        responseFromServer response = getById(commodity.getId());
        if (accountVerify.verifySellerByCommodityId(response, request)) {
            return commodityService.updateCommodity(commodity);
        } else {
            return responseFromServer.illegal();
        }
    }

    /**
     * 删除商品
     * @param commodity 商品
     * @param request HttpServletRequest
     * @return 执行结果
     */
//    @RequestMapping("/deleteCommodity")
    @ApiOperation(value = "删除商品")
    @ApiImplicitParam(name = "commodity_id", value = "商品Id",  paramType = "Integer", dataType = "Integer")
    @DeleteMapping("/{commodity_id}")
    public responseFromServer deleteCommodity(@RequestBody Commodity commodity, HttpServletRequest request){
        responseFromServer response = getById(commodity.getId());
        if (accountVerify.verifySellerByCommodityId(response, request)) {
            return commodityService.deleteCommodity((Commodity) response.getData());
        }
        return responseFromServer.illegal();
    }

    /**
     * 查询某一notice下所有商品
     * @param notice 通告
     * @return 执行结果
     */
//    @RequestMapping("/selectByNoticeId")
    @ApiOperation(value = "查询某一notice下所有商品")
    @ApiImplicitParam(name = "notice_id", value = "通告Id",  paramType = "Integer", dataType = "Integer")
    @GetMapping("/notice/{notice_id}")
    public responseFromServer selectAllByNoticeId(@RequestBody Notice notice){
        return commodityService.selectAllByNotice(notice);
    }

    /**
     * 删除某一notice下所有商品
     * @param notice 通告
     * @param request HttpServletRequest
     * @return 执行结果
     */
//    @RequestMapping("/deleteByNoticeId")
    @ApiOperation(value = "删除某一notice下所有商品")
    @ApiImplicitParam(name = "notice_id", value = "通告Id",  paramType = "Integer", dataType = "Integer")
    @DeleteMapping("/notice/{notice_id}")
    public responseFromServer deleteAllByNoticeId(@RequestBody Notice notice, HttpServletRequest request){
        Account account = new Account(notice.getAccountId());
        if (!accountVerify.verify(account, request))  //用户合法性检查
            return responseFromServer.error();
        return  commodityService.deleteAllByNotice(notice);
    }

    /**
     * 返回图片路径
     * @param files 文件数组
     * @return 执行结果
     */
    @ApiOperation(value = "返回上传图片路径")
    @ApiImplicitParam(name = "files", value = "商品图片列表",  paramType = "MultipartFile[]", dataType = "MultipartFile[]")
    @PostMapping("/{files}")
    public responseFromServer imageUrl(MultipartFile[] files){
        return commodityService.imageUrl(files);
    }
}