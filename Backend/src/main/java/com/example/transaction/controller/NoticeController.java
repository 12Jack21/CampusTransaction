package com.example.transaction.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Notice;
import com.example.transaction.service.NoticeService;
import com.example.transaction.util.AccountVerify;
import com.example.transaction.util.code.NoticeCode;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @ClassName: NoticeController
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/26 16:44
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {


    @Autowired
    NoticeService noticeService;

    /**
     * 创建一个空的通告
     * @param notice
     * @param session
     * @return 通告
     */
    @RequestMapping("/setupNotice")
    public responseFromServer setupNotice(@RequestBody Notice notice, HttpSession session){
        Account account = new Account(notice.getAccountId());
        if(AccountVerify.verify(account,session)){
            /*此时account已更新*/
            notice.setAccountId(account.getId());
            return noticeService.setupNotice(notice);
        }else{
            /*非法操作：为他人创建通告*/
            return responseFromServer.illegal();
        }
        /*创建成功返回通告，添加商品根据通告id进行添加*/
    }

    /**
     * 取消一个通告:::修改通告状态为CANCELLED
     * @param notice
     * @param session
     * @return
     */
    @RequestMapping("/cancelNotice")
    public responseFromServer cancelNotice(@RequestBody Notice notice,HttpSession session){
       return updateNoticeState(notice,session,NoticeCode.CANCELLED.getCode());
    }


    /**
     * 将已经上传完的通告发布
     * @param notice
     * @param session
     * @return
     */
    @RequestMapping("/publishNotice")
    public responseFromServer publishNotice(@RequestBody Notice notice,HttpSession session){
        return updateNoticeState(notice,session,NoticeCode.PUBLISHED.getCode());
    }

    /**
     * 修改状态，进行用户校验，非法操作检查，
     * @param notice
     * @param session
     * @param code
     * @return
     */
    public responseFromServer updateNoticeState(Notice notice,HttpSession session,int code){
        Account account = new Account(notice.getAccountId());
        if(AccountVerify.verify(account,session)){
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("account_id",account.getId());
            queryWrapper.eq("id",notice.getId());
            Notice updateNotice = new Notice();
            updateNotice.setAccountId(account.getId());
            updateNotice.setId(notice.getId());
            updateNotice.setStateEnum(code);
            return noticeService.updateNotice(updateNotice,queryWrapper);
        }else{
            /*非法操作：操作他人通告*/
            return responseFromServer.illegal();
        }
    }


    /**
     * 删除通告
     * @param notice
     * @param session
     * @return
     */
    @RequestMapping("/deleteNotice")
    public responseFromServer deleteNotice(@RequestBody Notice notice,HttpSession session){
        Account account = new Account(notice.getAccountId());
        if(AccountVerify.verify(account,session)){
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("account_id",account.getId());
            queryWrapper.eq("id",notice.getId());
            return noticeService.deleteNotice(queryWrapper);
        }else{
            return responseFromServer.illegal();
        }
    }


    /**
     * 获取首页最近通告
     * @param pageIndex
     * @return
     */
    @RequestMapping("/getRecentNoticePage")
    public responseFromServer getRecentNoticePage(@RequestBody Integer pageIndex){
        QueryWrapper queryWrapper = new QueryWrapper();
        /*按照时间倒序排序*/
        queryWrapper.orderByDesc("update_time");
        /*只查看确认发布的通告*/
        queryWrapper.eq("state_enum",NoticeCode.PUBLISHED.getCode());
        return noticeService.getNoticePage(queryWrapper,pageIndex);
    }


    @RequestMapping("/getNoticePageForAccount")
    public responseFromServer getNoticePageByAccountId(@RequestBody Integer pageIndex, HttpSession session){
        Account account = (Account)session.getAttribute("currentAccount");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account_id",account.getId());
        return noticeService.getNoticePage(queryWrapper,pageIndex);
    }

}
