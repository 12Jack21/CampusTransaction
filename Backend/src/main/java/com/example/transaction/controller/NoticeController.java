package com.example.transaction.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Notice;
import com.example.transaction.service.NoticeService;
import com.example.transaction.service.impl.AccountVerify;
import com.example.transaction.util.code.NoticeCode;
import com.example.transaction.util.jsonParamResolver.handler.RequestJson;
import com.example.transaction.util.responseFromServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ClassName: NoticeController
 * @Author: 曾志昊
 * @Date: 2020/4/26 16:44
 */
@RestController
@RequestMapping("/notices")
@Api(tags = "NoticeController")
public class NoticeController {


    @Autowired
    NoticeService noticeService;
    @Autowired
    AccountVerify accountVerify;

    /**
     * 创建一个空的通告
     *
     * @param notice
     * @param request
     * @return 通告
     */
//    @RequestMapping("/setupNotice")
    @ApiOperation("创建通告")
    @PostMapping
    public responseFromServer setupNotice(@RequestBody Notice notice, HttpServletRequest request) {
        Account account = new Account(notice.getAccountId());
        if (accountVerify.verify(account, request)) {
            /*此时account已更新*/
            notice.setAccountId(account.getId());
            /*必须传入通告类型*/
            if (notice.getType() == null)
                return responseFromServer.error();
            return noticeService.setupNotice(notice);
        } else {
            /*非法操作：为他人创建通告*/
            return responseFromServer.illegal();
        }
        /*创建成功返回通告，添加商品根据通告id进行添加*/
    }

    /**
     * 取消一个通告:::修改通告状态为CANCELLED
     * @param notice
     * @param request
     * @return
     */
//    @RequestMapping("/cancelNotice")
    @ApiOperation(value = "取消通告")
    @ApiImplicitParam(name = "notice_id", value = "通告Id",  paramType = "Integer", dataType = "Integer")
    @DeleteMapping("/{notice_id}/cancel")
    public responseFromServer cancelNotice(@RequestBody Notice notice,HttpServletRequest request){
       return updateNoticeState(notice,request,NoticeCode.CANCELLED.getCode());
    }


    /**
     * 将已经上传完的通告发布
     * @param notice
     * @param request
     * @return
     */
//    @RequestMapping("/publishNotice")
    @ApiOperation(value = "发布通告")
    @ApiImplicitParam(name = "notice_id", value = "通告Id",  paramType = "Integer", dataType = "Integer")
    @PutMapping("/{notice_id}")
    public responseFromServer publishNotice(@RequestBody Notice notice,HttpServletRequest request){
        return updateNoticeState(notice,request,NoticeCode.PUBLISHED.getCode());
    }

    /**
     * 修改状态，进行用户校验，非法操作检查，
     * @param notice
     * @param request
     * @param code
     * @return
     */
    @ApiOperation(value = "修改通告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "notice_id", value = "通告Id",  paramType = "Integer", dataType = "Integer"),
            @ApiImplicitParam(name = "code", value = "状态码",  paramType = "Integer", dataType = "Integer")
    })
    @PutMapping("/{notice_id}/{code}")
    public responseFromServer updateNoticeState(@RequestBody  Notice notice, HttpServletRequest request, int code){
        Account account = new Account(notice.getAccountId());
        if (accountVerify.verify(account, request)) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("account_id", account.getId());
            queryWrapper.eq("id", notice.getId());
            Notice updateNotice = new Notice();
            updateNotice.setAccountId(account.getId());
            updateNotice.setId(notice.getId());
            updateNotice.setStateEnum(code);
            return noticeService.updateNotice(updateNotice, queryWrapper);
        } else {
            /*非法操作：操作他人通告*/
            return responseFromServer.illegal();
        }
    }


    /**
     * 删除通告
     * @param notice
     * @param request
     * @return
     */
//    @RequestMapping("/deleteNotice")
    @ApiOperation(value = "删除通告")
    @ApiImplicitParam(name = "notice_id", value = "通告Id",  paramType = "Integer", dataType = "Integer")
    @DeleteMapping("/{notice_id}/delete")
    public responseFromServer deleteNotice(@RequestBody Notice notice,HttpServletRequest request){
        Account account = new Account(notice.getAccountId());
        if (accountVerify.verify(account, request)) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("account_id", account.getId());
            queryWrapper.eq("id", notice.getId());
            return noticeService.deleteNotice(queryWrapper);
        } else {
            return responseFromServer.illegal();
        }
    }


    /**
     * 获取首页最近通告
     *
     * @param pageIndex
     * @param isCommodity
     * @return
     */
//    @RequestMapping("/getRecentNoticePage")
    @ApiOperation("获取首页通告")
    @GetMapping
    public responseFromServer getRecentNoticePage(@RequestJson Integer pageIndex, @RequestJson Boolean isCommodity) {
//        Integer pageIndex = (Integer) map.get("pageIndex");
//        Boolean isCommodity = (Boolean)map.get("isCommodity");
        if (pageIndex == null) return responseFromServer.error();
        QueryWrapper queryWrapper = new QueryWrapper();
        /*按照时间倒序排序*/
        queryWrapper.orderByDesc("update_time");
        /*只查看确认发布的通告*/
        queryWrapper.eq("state_enum", NoticeCode.PUBLISHED.getCode());
        /*是商品还是需求*/
        if (isCommodity != null) {
            queryWrapper.eq("type", isCommodity);
        }
        return noticeService.getNoticePage(queryWrapper, pageIndex);
    }

    /**
     * 通过用户id获取通告分页
     *
     * @param pageIndex
     * @param request
     * @return
     */
//    @RequestMapping("/getNoticePageForAccount")
    @ApiOperation(value = "获取用户公告列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "notice_id", value = "通告Id", paramType = "Integer", dataType = "Integer"),
            @ApiImplicitParam(name = "page_index", value = "页面索引", paramType = "Integer", dataType = "Integer")
    })
    @GetMapping("/account/{account_id}")
    public responseFromServer getNoticePageByAccountId(@RequestJson Integer pageIndex, HttpServletRequest request) {
//        Integer pageIndex = (Integer) map.get("pageIndex");
        Account account = (Account) request.getAttribute("currentAccount");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account_id", account.getId());
        return noticeService.getNoticePage(queryWrapper, pageIndex);
    }

}
