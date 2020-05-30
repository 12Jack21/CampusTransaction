package com.example.transaction.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.transaction.dto.notify.NotifyCondition;
import com.example.transaction.dto.notify.SimpleNotify;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.AccountNotify;
import com.example.transaction.pojo.Notify;
import com.example.transaction.service.NotifyService;
import com.example.transaction.service.impl.AccountVerify;
import com.example.transaction.util.jsonParamResolver.handler.RequestJson;
import com.example.transaction.util.responseFromServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: NotifyController
 * @Author: 曾志昊
 * @Date: 2020/4/28 2:07
 */
@RestController
@RequestMapping("/messages")
@Api(tags = "NotifyController")
public class NotifyController {

    @Autowired
    NotifyService notifyService;
    @Autowired
    AccountVerify accountVerify;

    @ApiOperation(value = "获取用户的通知")
    @ApiImplicitParam(name = "accountId", value = "用户Id", paramType = "Integer", dataType = "Integer")
    @GetMapping("/account/{accountId}")
    public responseFromServer getAccountNotifyWithCondition(@PathVariable Integer accountId,
                                                            NotifyCondition condition,
                                                            HttpServletRequest request) {
        Account account = new Account(accountId);
        condition = new NotifyCondition();
        condition.setAccountId(accountId);
        if (accountVerify.verify(account, request)) {

            return notifyService.searchSimpleAccountNotifyPage(condition);
        } else {
            return responseFromServer.illegal();
        }
    }

    @ApiOperation(value = "设置为已读")
    @ApiImplicitParam(name = "ids", value = "用户Id", paramType = "Integer", dataType = "Integer")
    @PutMapping("/read/{id}")
    public responseFromServer setIsRead(@PathVariable Integer id,
                                        HttpServletRequest request) {
        if (readNotify(id, request).isFailure()) {
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }

    /**
     * 设置为已读
     *
     * @param accountNotifyId
     * @param request
     * @return
     */
//    @RequestMapping("/readNotify")
    @ApiOperation(value = "通知设为已读")
    @ApiImplicitParam(name = "accountNotifyId", value = "通知Id", paramType = "Integer", dataType = "Integer")
    @PutMapping("/{accountNotifyId}")
    public responseFromServer readNotify(@PathVariable Integer accountNotifyId,
                                         HttpServletRequest request) {
        Account account = new Account();
        account = accountVerify.verifyWithReturn(account, request);
        /**
         * todo: 不使用token时直接忽略用户验证
         * if (account == null) {
         *  return responseFromServer.error();
         }*/
        responseFromServer response = notifyService.getSimpleAccountNotifyById(accountNotifyId);
        if (response.isFailure()) {
            return responseFromServer.error();
        }
        AccountNotify notify = (AccountNotify) response.getData();
        if (notify.getAccountId().intValue() != account.getId().intValue()) {
            return responseFromServer.error();
        }
        return notifyService.readNotify(notify.getId());
    }


    /**
     * 根据用户id获取未读notify
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "根据用户id获取未读notify")
    @ApiImplicitParam(name = "accountId", value = "用户Id", paramType = "Integer", dataType = "Integer")
    public responseFromServer getUnreadNotify(@PathVariable Integer accountId, HttpServletRequest request) {
        Account account = new Account(accountId);
        if (accountVerify.verify(account, request)) {
            NotifyCondition condition = new NotifyCondition();
            return notifyService.getUnreadNotifyByAccountId(account.getId());
        } else {
            return responseFromServer.illegal();
        }
    }

    /**
     * 获取当前登录用户未读新消息的个数
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "获取用户未读消息数")
    @GetMapping("/account/{accountId}/count")
    public responseFromServer getUnreadNotifyCount(@PathVariable Integer accountId, HttpServletRequest request) {
        Account account = new Account(accountId);
        if (!accountVerify.verify(account, request)) {
            return responseFromServer.error();
        }
        return notifyService.getUnreadNotifyCount(account.getId());
    }

    /**
     * 根据notify——id 或者id 查询整个详细的AccountNotify
     *
     * @param accountNotify
     * @param request
     * @return
     */
    @ApiOperation(value = "获取详细的AccountNotify")
    @ApiImplicitParam(name = "accountNotifyId", value = "通知Id", paramType = "Integer", dataType = "Integer")
    @GetMapping("/{accountNotifyId}")
    public responseFromServer getDetailedAccountNotifyById(@PathVariable AccountNotify accountNotify,
                                                           HttpServletRequest request) {
        responseFromServer response;
//        AccountNotify accountNotify = new AccountNotify(accountNotifyId);
        if (accountNotify.getId() != null) {
            response = notifyService.getSimpleAccountNotifyById(accountNotify.getId());
            if (response.isFailure()) {
                return responseFromServer.error();
            }
        } else if (accountNotify.getNotifyId() == null) {
            response = notifyService.getSimpleAccountNotifyByNotifyId(accountNotify.getNotifyId());
            if (response.isFailure()) {
                return responseFromServer.error();
            }
        } else {
            return responseFromServer.error();
        }
        accountNotify = (AccountNotify) response.getData();
        /*验证用户信息*/
        Account account = new Account(accountNotify.getAccountId());
        if (!accountVerify.verify(account, request)) {
            return responseFromServer.illegal();
        }
        response = notifyService.getNotifyByNotifyId(accountNotify.getNotifyId());
        if (response.isFailure()) {
            return responseFromServer.error();
        } else {
            accountNotify.setNotify((Notify) response.getData());
        }
        SimpleNotify simpleNotify;
        try {
            simpleNotify = new SimpleNotify(accountNotify);
        } catch (Exception e) {
            return responseFromServer.error();
        }
        return responseFromServer.success(simpleNotify);
    }

    /**
     * 获取所有的通知
     *
     * @param pageIndex
     * @param request
     * @return
     */
//    @RequestMapping("/getAllNotifyPage")
    @ApiOperation(value = "获取所有的通知")
    @GetMapping
    public responseFromServer getAllNotifyPage(@RequestJson Integer pageIndex, HttpServletRequest request) {
//        Integer pageIndex = (Integer) map.get("pageIndex");
        if (pageIndex == null || pageIndex <= 0) {
            pageIndex = 1;
        }
        Account account = new Account();
        accountVerify.verify(account, request);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account_id", account.getId());
        return notifyService.getNotifyPage(queryWrapper, pageIndex);
    }




}
