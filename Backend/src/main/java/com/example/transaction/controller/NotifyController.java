package com.example.transaction.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.AccountNotify;
import com.example.transaction.pojo.Notify;
import com.example.transaction.service.NotifyService;
import com.example.transaction.util.AccountVerify;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @ClassName: NotifyController
 * @Author: 曾志昊
 * @Date: 2020/4/28 2:07
 */
@RestController
@RequestMapping("/notify")
public class NotifyController {

    @Autowired
    NotifyService notifyService;

    /**
     * 根据用户id获取未读notify
     * 可以传入accountid参数也可不传入
     * @param account
     * @param session
     * @return
     */
    @RequestMapping("/getUnreadNotifyByAccountId")
    public responseFromServer getUnreadNotify(@RequestBody Account account, HttpSession session){
        if(AccountVerify.verify(account, session)){
            return notifyService.getUnreadNotifyByAccountId(account.getId());
        }else{
            return responseFromServer.illegal();
        }
    }

    /**
     * 根据notify——id 或者id 查询整个详细的AccountNotify
     * @param accountNotify
     * @param session
     * @return
     */
    @RequestMapping("/getDetailedAccountNotifyById")
    public responseFromServer getDetailedAccountNotifyById(@RequestBody AccountNotify accountNotify, HttpSession session){
        responseFromServer response;
        if(accountNotify.getId()!=null){
            response = notifyService.getSimpleAccountNotifyById(accountNotify.getId());
            if(response.isFailure()){
                return responseFromServer.error();
            }
        }else if(accountNotify.getNotifyId()==null){
            response = notifyService.getSimpleAccountNotifyByNotifyId(accountNotify.getNotifyId());
            if(response.isFailure()){
                return responseFromServer.error();
            }
        }else{
            return responseFromServer.error();
        }
        accountNotify = (AccountNotify)response.getData();
        /*验证用户信息*/
        Account account = new Account(accountNotify.getAccountId());
        if(!AccountVerify.verify(account,session)){
            return responseFromServer.illegal();
        }
        response = notifyService.getNotifyByNotifyId(accountNotify.getNotifyId());
        if(response.isFailure()){
            return responseFromServer.error();
        }else{
            accountNotify.setNotify((Notify)response.getData());
        }
        return responseFromServer.success(accountNotify);
    }

    /**
     * 获取所有的通知
     * @param map
     * @param session
     * @return
     */
    @RequestMapping("/getAllNotifyPage")
    public responseFromServer getAllNotifyPage(@RequestBody Map<String,Object> map,HttpSession session){
        Integer pageIndex = (Integer) map.get("pageIndex");
        if(pageIndex == null||pageIndex <=0 ){
            pageIndex = 1;
        }
        Account account = new Account();
        AccountVerify.verify(account,session);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account_id",account.getId());
        return notifyService.getNotifyPage(queryWrapper,pageIndex);
    }


    /**
     * 设置为已读
     * @param notify
     * @param session
     * @return
     */
    @RequestMapping("/readNotify")
    public responseFromServer readNotify(@RequestBody AccountNotify notify,HttpSession session){
        Account account = new Account();
        if(!AccountVerify.verify(account,session)){
            return responseFromServer.error();
        }
        if(notify.getId()==null)return responseFromServer.error();
        responseFromServer response = notifyService.getSimpleAccountNotifyById(notify.getId());
        if(response.isFailure())return responseFromServer.error();
        notify = (AccountNotify)response.getData();
        if(notify.getAccountId().intValue()!=account.getId().intValue())
            return responseFromServer.error();
        return notifyService.readNotify(notify.getId());
    }

    /**
     * 获取当前登录用户未读新消息的个数
     * @param session
     * @return
     */
    @RequestMapping("/getUnreadNotifyCount")
    public responseFromServer getUnreadNotifyCount(HttpSession session){
        Account account = new Account();
        if(!AccountVerify.verify(account,session)){
            return responseFromServer.error();
        }
        return notifyService.getUnreadNotifyCount(account.getId());
    }



}
