package com.example.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.NotifyDAO;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Notify;
import com.example.transaction.pojo.Reservation;
import com.example.transaction.service.NotifyService;
import com.example.transaction.util.MyPage;
import com.example.transaction.util.Nums;
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



    /*todo::此处需要一对多关联查询 返回*/
    @Override
    public responseFromServer getNotifyPage(QueryWrapper queryWrapper,Integer pageIndex) {
        Page<Notify> page = new Page<>(pageIndex, Nums.pageSize);
        IPage<Notify> notifyIPage = notifyDAO.selectPage(page,queryWrapper);
        MyPage myPage = new MyPage(notifyIPage);
        return responseFromServer.success(myPage);
    }


    @Override
    public responseFromServer getUnreadNotifyCount(Integer accountId){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account_id",accountId);
        queryWrapper.eq("is_read",false);
        Integer count = notifyDAO.selectCount(queryWrapper);
        return responseFromServer.success(count);
    }



    @Override
    public responseFromServer getUnreadNotify(Integer accountId,Integer pageIndex){
        Page<Notify> page = new Page<>(pageIndex, Nums.pageSize);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account_id",accountId);
        queryWrapper.eq("is_read",false);
        queryWrapper.orderByDesc("create_time");
        return getNotifyPage(queryWrapper,pageIndex);
    }

    @Override
    public responseFromServer getAllNotify(Integer accountId,Integer pageIndex){
        Page<Notify> page = new Page<>(pageIndex, Nums.pageSize);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account_id",accountId);
        queryWrapper.orderByDesc("create_time");
        return getNotifyPage(queryWrapper,pageIndex);
    }

    @Override
    public responseFromServer readNotify(Integer notifyId) {
        Notify notify = new Notify();
        notify.setId(notifyId);
        return null;
    }


    private NotifyDAO notifyDAO;

    @Autowired
    public NotifyServiceImpl(NotifyDAO notifyDAO){
        this.notifyDAO = notifyDAO;
    }
}
