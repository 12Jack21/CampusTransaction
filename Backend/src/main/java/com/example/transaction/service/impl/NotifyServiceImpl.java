package com.example.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.AccountNotifyDAO;
import com.example.transaction.dao.NotifyDAO;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.AccountNotify;
import com.example.transaction.pojo.Notify;
import com.example.transaction.pojo.Reservation;
import com.example.transaction.service.NotifyService;
import com.example.transaction.util.MyPage;
import com.example.transaction.util.Nums;
import com.example.transaction.util.responseFromServer;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Timestamp;
import java.util.List;

/**
 * @ClassName: NotifyServiceImpl
 * @Author: 曾志昊
 * @Date: 2020/4/28 2:07
 */

@Service("NotifyService")
public class NotifyServiceImpl implements NotifyService {

    /**
     * 根据notifyid查询notify
     * @param notifyId
     * @return
     */
    @Override
    public responseFromServer getNotifyByNotifyId(Integer notifyId){
        Notify notify = notifyDAO.selectById(notifyId);
        return notify==null?responseFromServer.error():responseFromServer.success(notify);
    }


    /**
     * 根据id获取notify
     * @param id
     * @return
     */
    @Override
    public responseFromServer getSimpleAccountNotifyById(Integer id){
        if(id==null)
            return responseFromServer.error();
        AccountNotify tempNotify = new AccountNotify();
        tempNotify.setId(id);
        AccountNotify accountNotify = accountNotifyDAO.selectById(id);
        return responseFromServer.success(accountNotify);
    }

    /**
     * 根据id获取notify
     * @param id
     * @return
     */
    @Override
    public responseFromServer getSimpleAccountNotifyByNotifyId(Integer id){
        if(id==null)
            return responseFromServer.error();
        AccountNotify tempNotify = new AccountNotify();
        tempNotify.setId(id);
        AccountNotify accountNotify = accountNotifyDAO.getSimpleNotifyByNotifyId(id);
        return responseFromServer.success(accountNotify);
    }


    /**
     * 根据id获取详细的notify
     * @param id
     * @return
     */
    @Override
    public responseFromServer getDetailedAccountNotifyById(Integer id){
        if(id==null){
            return responseFromServer.error();
        }
        AccountNotify accountNotify = accountNotifyDAO.getDetailedNotifyById(id);
        return responseFromServer.success(accountNotify);
    }

    /**
     * 获取未读的消息
     * @param id
     * @return
     */
    @Override
    public responseFromServer getUnreadNotifyByAccountId(Integer id) {
        if(id == null){
            return responseFromServer.error();
        }
        List<AccountNotify> accountNotifies = accountNotifyDAO.getUnreadNotifyByAccountId(id);
        return responseFromServer.success(accountNotifies);
    }


    /**
     * 获取所有的消息
     * @param id
     * @return
     */
    @Override
    public responseFromServer getAllNoticeByAccountId(Integer id) {
        if(id == null){
            return responseFromServer.error();
        }
        List<AccountNotify> accountNotifies = accountNotifyDAO.getAllNotifyByAccountId(id);
        return responseFromServer.success(accountNotifies);
    }

    /**
     * 查询新消息个数
     * @param accountId
     * @return
     */
    @Override
    public responseFromServer getUnreadNotifyCount(Integer accountId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account_id",accountId);
        Integer count = accountNotifyDAO.selectCount(queryWrapper);
        return count==null?responseFromServer.error():responseFromServer.success(count);
    }

    @Override
    @Transactional
    public responseFromServer insertNotify(Notify notify) {
        if(notify == null){
            return responseFromServer.error();
        }
        notify.setAccountNotifyId(null);
        notify.setId(null);
        if(notifyDAO.insert(notify)!=1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }

    /**
     * 插入一条notify
     * @param accountNotify
     * @return
     */
    @Transactional
    @Override
    public responseFromServer insertAccountNotify(AccountNotify accountNotify){
        if(accountNotify==null)
            return responseFromServer.error();
        accountNotify.setId(null);
        accountNotify.setNotifyId(null);
        Notify notify = accountNotify.getNotify();
        if(insertNotify(notify).isSuccess()) {
            Integer notifyId = notify.getId();
            accountNotify.setNotifyId(notifyId);
            if (accountNotifyDAO.insert(accountNotify) == 1) {
                notify.setAccountNotifyId(accountNotify.getId());
                if (notifyDAO.updateById(notify) == 1) {
                    return responseFromServer.success();
                }
            }
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return responseFromServer.error();
    }

    /**
     * 删除
     * @param accountNotify
     * @return
     */
    @Override
    @Transactional
    public responseFromServer deleteNotify(AccountNotify accountNotify) {
        if(accountNotify.getId()==null||accountNotifyDAO.deleteById(accountNotify.getId())!=1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }



    @Override
    public responseFromServer getNotifyPage(QueryWrapper queryWrapper,Integer pageIndex) {
        Page<Notify> page = new Page<>(pageIndex, Nums.pageSize);
        queryWrapper.orderByDesc("create_time");
        IPage<Notify> notifyIPage = notifyDAO.selectPage(page,queryWrapper);
        MyPage myPage = new MyPage(notifyIPage);
        return responseFromServer.success(myPage);
    }

    /**
     * 设置通知为已读
     * @param notifyId
     * @return
     */
    @Override
    @Transactional
    public responseFromServer readNotify(Integer notifyId) {
        AccountNotify accountNotify = new AccountNotify();
        accountNotify.setId(notifyId);
        accountNotify.setIsRead(true);
        accountNotify.setReadTime(new Timestamp(System.currentTimeMillis()));
        if(accountNotifyDAO.updateById(accountNotify)!=1)
        {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }

    private AccountNotifyDAO accountNotifyDAO;
    private NotifyDAO notifyDAO;

    @Autowired
    public NotifyServiceImpl(NotifyDAO notifyDAO, AccountNotifyDAO accountNotifyDAO){
        this.accountNotifyDAO  = accountNotifyDAO;
        this.notifyDAO = notifyDAO;
    }
}
