package com.example.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.transaction.dao.NotifyDAO;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Notify;
import com.example.transaction.service.NotifyService;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * @ClassName: NotifyServiceImpl
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/28 2:07
 */

@Service("NotifyService")
public class NotifyServiceImpl implements NotifyService {

    @Override
    @Transactional
    public responseFromServer pushNotify(Notify notify) {
        if(notifyDAO.insert(notify)!=1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }else{
            return responseFromServer.success();
        }
    }

    @Override
    public responseFromServer getNotifyPage(QueryWrapper queryWrapper, int pageIndex) {
        return null;
    }

    @Override
    public responseFromServer readNotify(Notify notify) {

        return null;
    }

    @Override
    public responseFromServer getUnreadNotify(Account account) {
        return null;
    }

    private NotifyDAO notifyDAO;

    @Autowired
    public NotifyServiceImpl(NotifyDAO notifyDAO){
        this.notifyDAO = notifyDAO;
    }
}
