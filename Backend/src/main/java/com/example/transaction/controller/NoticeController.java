package com.example.transaction.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.transaction.dto.notice.DataWrapper;
import com.example.transaction.dto.notice.NoticeCondition;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Notice;
import com.example.transaction.service.NoticeService;
import com.example.transaction.service.impl.AccountVerify;
import com.example.transaction.util.code.NoticeCode;
import com.example.transaction.util.responseFromServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
     * 创建通告(包含上传的商品信息)
     *
     * @param data
     * @param request
     * @return 通告
     */
//    @RequestMapping("/setupNotice")
    @ApiOperation("创建通告(包含上传的商品信息)")
    @PostMapping("/add")
    public responseFromServer addNotice(@RequestBody DataWrapper data, HttpServletRequest request) {
        Notice notice = data.getData();
        Account account = new Account(notice.getAccountId());
        if (accountVerify.verify(account, request)) {
            /*此时account已更新*/
                notice.setAccountId(account.getId());
                /*必须传入通告类型*/
                if (notice.getType() == null) {
                return responseFromServer.error();
            }
            /**
             * 处理空串等参数
             */
            notice.rectifyNotify();
            notice.setStateEnum(0);
            notice.setStateEnum(NoticeCode.UNPUBLISHED.getCode());
            return noticeService.addNotice(notice);
        } else {
            /*非法操作：为他人创建通告*/
            return responseFromServer.illegal();
        }
        /*创建成功返回通告，添加商品根据通告id进行添加*/
    }

    /**
     * 取消一个通告:::修改通告状态为CANCELLED
     *
     * @param noticeId
     * @param request
     * @return
     */
//    @RequestMapping("/cancelNotice")
    @ApiOperation(value = "取消通告")
    @ApiImplicitParam(name = "noticeId", value = "通告Id", paramType = "Integer", dataType = "Integer")
    @DeleteMapping("/{noticeId}/cancel")
    public responseFromServer cancelNotice(@PathVariable Integer noticeId, HttpServletRequest request) {
        return updateNoticeState(noticeId, NoticeCode.CANCELLED.getCode(), request);
    }


    /**
     * 将已经上传完的通告发布
     *
     * @param notice
     * @param request
     * @return
     */
//    @RequestMapping("/publishNotice")
    @ApiOperation(value = "发布通告")
    @ApiImplicitParam(name = "notice_id", value = "通告Id", paramType = "Integer", dataType = "Integer")
    @PutMapping()
    public responseFromServer publishNotice(@RequestBody Notice notice, HttpServletRequest request) {
        return updateNoticeState(notice.getId(), NoticeCode.PUBLISHED.getCode(), request);
    }

    /**
     * 修改状态，进行用户校验，非法操作检查
     *
     * @param noticeId
     * @param code
     * @param request
     * @return
     */
    @ApiOperation(value = "修改通告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "notice_id", value = "通告Id", paramType = "Integer", dataType = "Integer"),
            @ApiImplicitParam(name = "code", value = "状态码", paramType = "Integer", dataType = "Integer")
    })
    @PutMapping("/{noticeId}/{code}")
    public responseFromServer updateNoticeState(@PathVariable Integer noticeId,
                                                @PathVariable Integer code,
                                                HttpServletRequest request) {
        /**
         * ZZH
         * TODO : 修改
         */
        Account account = new Account();
        if (accountVerify.verify(account, request)) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("account_id", account.getId());
            queryWrapper.eq("id", noticeId);
            Notice updateNotice = new Notice();
            updateNotice.setAccountId(account.getId());
            updateNotice.setId(noticeId);
            updateNotice.setStateEnum(code);
            return noticeService.updateNotice(updateNotice, queryWrapper);
        } else {
            /*非法操作：操作他人通告*/
            return responseFromServer.illegal();
        }
    }


    /**
     * 删除通告
     *
     * @param noticeId
     * @param request
     * @return
     */
//    @RequestMapping("/deleteNotice")
    @ApiOperation(value = "删除通告")
    @ApiImplicitParam(name = "noticeId", value = "通告Id", paramType = "Integer", dataType = "Integer")
    @DeleteMapping("/{noticeId}")
    public responseFromServer deleteNotice(@PathVariable Integer noticeId, HttpServletRequest request) {
        Account account = new Account(noticeId);
        Notice notice = new Notice(noticeId);
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
     * @param condition
     * @return
     */
//    @RequestMapping("/getRecentNoticePage")
    @ApiOperation("获取首页通告")
    @GetMapping
    public responseFromServer getRecentNoticePage(NoticeCondition condition) {
        if (condition.getPageIndex() == null || condition.getPageIndex() <= 0) {
            condition.setPageIndex(1);
        }
        condition.setAccountId(null);
        return noticeService.getRecentNotice(condition);
    }

    /**
     * 根据id查询通告
     *
     * @param noticeId
     * @param request
     * @return
     */
//    @RequestMapping("/getRecentNoticePage")
    @ApiOperation("根据id查询通告")
    @GetMapping("/{noticeId}")
    public responseFromServer getNoticeById(@PathVariable Integer noticeId, HttpServletRequest request) {
        responseFromServer response = noticeService.getDetailedNotice(noticeId);
        if(response.isFailure()){
            return responseFromServer.error();
        }
        if(noticeService.addBrowseCount(noticeId).isFailure()){
            return responseFromServer.error();
        }
        return response;
    }

    /**
     * 通过用户id获取通告分页
     *
     * @param condition
     * @param accountId
     * @param request
     * @return
     */
//    @RequestMapping("/getNoticePageForAccount")
    @ApiOperation(value = "获取用户公告列表")
    @ApiImplicitParam(name = "pageIndex", value = "页面索引", paramType = "Integer", dataType = "Integer")
    @GetMapping("/account/{accountId}")
    public responseFromServer getNoticePageByAccountId(NoticeCondition condition,
                                                       @PathVariable Integer accountId,
                                                       HttpServletRequest request) {
//        Integer pageIndex = (Integer) map.get("pageIndex");
        if (condition.getPageIndex() == null || condition.getPageIndex() <= 0) {
            condition.setPageIndex(1);
        }
        /*todo 获取用户自己的通告暂时先不处理时间问题*/
        condition.setEndTime(null);
        /**
         * ZZH
         * TODO :
         * Account account = (Account) accountVerify.verifyWithReturn(new Account(accountId));
         */
        condition.setAccountId(accountId);

//        Account account = new Account(accountId);
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("account_id", account.getId());
//        return noticeService.getNoticePage(queryWrapper, condition.getPageIndex());
        return noticeService.getRecentNotice(condition);
    }


    /**
     * 创建一个空的通告
     *
     * @param notice
     * @param request
     * @return 通告
     */
//    @RequestMapping("/setupNotice")
    @ApiOperation("创建通告")
    @PostMapping("/empty")
    public responseFromServer setupEmptyNotice(@RequestBody Notice notice, HttpServletRequest request) {
        Account account = new Account(notice.getAccountId());
        if (accountVerify.verify(account, request)) {
            /*此时account已更新*/
            notice.setAccountId(account.getId());
            /*必须传入通告类型*/
            if (notice.getType() == null) {
                return responseFromServer.error();
            }
            return noticeService.setupNotice(notice);
        } else {
            /*非法操作：为他人创建通告*/
            return responseFromServer.illegal();
        }
        /*创建成功返回通告，添加商品根据通告id进行添加*/
    }


}
