package com.example.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.*;
import com.example.transaction.dto.account.SimpleAccount;
import com.example.transaction.dto.commodity.SimpleCommodity;
import com.example.transaction.dto.notify.NotifyCondition;
import com.example.transaction.dto.notify.SimpleNotify;
import com.example.transaction.pojo.*;
import com.example.transaction.service.NotifyService;
import com.example.transaction.util.MyPage;
import com.example.transaction.util.code.NotifyTargetCode;
import com.example.transaction.util.code.Nums;
import com.example.transaction.util.code.ResourcePath;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
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
     *
     * @param notifyId
     * @return
     */
    @Override
    public responseFromServer getNotifyByNotifyId(Integer notifyId) {
        Notify notify = notifyDAO.selectById(notifyId);
        return notify == null ? responseFromServer.error() : responseFromServer.success(notify);
    }


    /**
     * 根据id获取notify
     *
     * @param id
     * @return
     */
    @Override
    public responseFromServer getSimpleAccountNotifyById(Integer id) {
        if (id == null) {
            return responseFromServer.error();
        }
        AccountNotify tempNotify = new AccountNotify();
        tempNotify.setId(id);
        AccountNotify accountNotify = accountNotifyDAO.selectById(id);
        return responseFromServer.success(accountNotify);
    }

    /**
     * 根据id获取notify
     *
     * @param id
     * @return
     */
    @Override
    public responseFromServer getSimpleAccountNotifyByNotifyId(Integer id) {
        if (id == null) {
            return responseFromServer.error();
        }
        AccountNotify tempNotify = new AccountNotify();
        tempNotify.setId(id);
        AccountNotify accountNotify = accountNotifyDAO.getSimpleNotifyByNotifyId(id);
        return responseFromServer.success(accountNotify);
    }


    /**
     * 根据id获取详细的notify
     *
     * @param id
     * @return
     */
    @Override
    public responseFromServer getDetailedAccountNotifyById(Integer id) {
        if (id == null) {
            return responseFromServer.error();
        }
        AccountNotify accountNotify = accountNotifyDAO.getDetailedNotifyById(id);
        return responseFromServer.success(accountNotify);
    }

    /**
     * 获取未读的消息
     *
     * @param id
     * @return
     */
    @Override
    public responseFromServer getUnreadNotifyByAccountId(Integer id) {
        if (id == null) {
            return responseFromServer.error();
        }
        List<AccountNotify> accountNotifies = accountNotifyDAO.getUnreadNotifyByAccountId(id);
        return responseFromServer.success(accountNotifies);
    }


    /**
     * 获取所有的消息
     *
     * @param id
     * @return
     */
    @Override
    public responseFromServer getAllNoticeByAccountId(Integer id) {
        if (id == null) {
            return responseFromServer.error();
        }
        List<AccountNotify> accountNotifies = accountNotifyDAO.getAllNotifyByAccountId(id);
        return responseFromServer.success(accountNotifies);
    }

    /**
     * 查询新消息个数
     *
     * @param accountId
     * @return
     */
    @Override
    public responseFromServer getUnreadNotifyCount(Integer accountId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account_id", accountId);
        Integer count = accountNotifyDAO.selectCount(queryWrapper);
        return count == null ? responseFromServer.error() : responseFromServer.success(count);
    }

    @Override
    @Transactional
    public responseFromServer insertNotify(Notify notify) {
        if (notify == null) {
            return responseFromServer.error();
        }
//        notify.setAccountNotifyId(null);
        notify.setId(null);
        if (notifyDAO.insert(notify) != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }

    /**
     * 插入一条notify
     *
     * @param accountNotify
     * @return
     */
    @Transactional
    @Override
    public responseFromServer insertAccountNotify(AccountNotify accountNotify) {
        if (accountNotify == null) {
            return responseFromServer.error();
        }
        accountNotify.setId(null);
        accountNotify.setNotifyId(null);
        Notify notify = accountNotify.getNotify();
        if (insertNotify(notify).isSuccess()) {
            Integer notifyId = notify.getId();
            accountNotify.setNotifyId(notifyId);
            if (accountNotifyDAO.insert(accountNotify) == 1) {
//                notify.setAccountNotifyId(accountNotify.getId());
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
     *
     * @param accountNotify
     * @return
     */
    @Override
    @Transactional
    public responseFromServer deleteNotify(AccountNotify accountNotify) {
        if (accountNotify.getId() == null || accountNotifyDAO.deleteById(accountNotify.getId()) != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }


    @Override
    public responseFromServer getNotifyPage(QueryWrapper queryWrapper, Integer pageIndex) {
        Page<AccountNotify> page = new Page<>(pageIndex, Nums.pageSize);
        queryWrapper.orderByAsc("is_read");
        queryWrapper.orderByDesc("create_time");
        IPage<AccountNotify> notifyIPage = accountNotifyDAO.getNotifyPage(page, queryWrapper);
        MyPage<AccountNotify> myPage = new MyPage(notifyIPage);
        List<SimpleNotify> simpleNotifyList = new ArrayList<>();
        for (AccountNotify accountNotify : myPage.getPageList()) {
            try {
                simpleNotifyList.add(new SimpleNotify(accountNotify));
            } catch (Exception e) {
                return responseFromServer.error();
            }
        }
        MyPage<SimpleNotify> myPage2 = new MyPage(notifyIPage);
        myPage2.setPageList(simpleNotifyList);
        return responseFromServer.success(myPage2);
    }

    /**
     * 设置通知为已读
     *
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
        if (accountNotifyDAO.updateById(accountNotify) != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }

    /**
     * 将对应的对象填充到返回的简单notify对象中
     *
     * @param simpleNotify
     * @return
     */
    @Override
    public responseFromServer fillInSimpleNotifyData(SimpleNotify simpleNotify) {
        int targetType = simpleNotify.getTargetType();
        if (targetType == NotifyTargetCode.COMMODITY.getCode()) {
            /*当前的target是商品,放入商品信息*/
            Commodity commodity = commodityDAO.getSimpleCommodityById(simpleNotify.getTargetId());
            if (commodity == null) {
                return responseFromServer.error();
            }
            List<CommodityImage> images = commodityImageDAO.getAllImageByCommodityId(commodity.getId());
            SimpleCommodity simpleCommodity = new SimpleCommodity(commodity);
            if(images!=null && images.size()>0){
                simpleCommodity.setImg(ResourcePath.commodityImageRequestPath + images.get(0).getImageUrl());
            }
            simpleNotify.setCommodity(simpleCommodity);
        } else if (targetType == NotifyTargetCode.RESERVATION.getCode()) {
            /*当前的target是预约,也要放入商品信息*/
            Reservation reservation = reservationDAO.selectById(simpleNotify.getTargetId());
            if (reservation == null) {
                return responseFromServer.error();
            }
            Commodity commodity = commodityDAO.getSimpleCommodityById(reservation.getCommodityId());
            if (commodity == null) {
                return responseFromServer.error();
            }
            List<CommodityImage> images = commodityImageDAO.getAllImageByCommodityId(commodity.getId());
            SimpleCommodity simpleCommodity = new SimpleCommodity(commodity);
            if(images!=null && images.size()>0){
                simpleCommodity.setImg(ResourcePath.commodityImageRequestPath + images.get(0).getImageUrl());
            }
            simpleNotify.setCommodity(simpleCommodity);
        } else if (targetType == NotifyTargetCode.NOTICE.getCode()) {
            /*当前target是通告,只需要放入标题*/
            Notice notice = noticeDAO.selectById(simpleNotify.getTargetId());
            if (notice == null) {
                return responseFromServer.error();
            }
            simpleNotify.setTitle(notice.getTitle());
            /*data为string*/
        }
        return responseFromServer.success();

    }

    @Override
    @Transactional
    public responseFromServer searchSimpleAccountNotifyPage(NotifyCondition condition) {
        if(condition == null){
            return responseFromServer.error();
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        if(condition.getType() == null){
            condition.setType(0);
        }
        switch (condition.getType()) {
            case 0:
                /**未读消息*/
                queryWrapper.eq("acc_notify.is_read", false);break;
            case 1:
                /**全部消息*/
                break;
            case 2:
                /**交易相关消息*/
                queryWrapper.in("notify.target_type", Arrays.asList(0, 1));
                break;
            case 3:
                /**通告相关消息*/
                queryWrapper.in("notify.target_type", Arrays.asList(2, 3));
                break;
            default:
        }
        Page<AccountNotify> page;
        if (condition.getPageIndex() == null || condition.getPageIndex() <= 0) {
            page = new Page<>(1,Nums.pageSize);
        } else {
            page = new Page<>(condition.getPageIndex(),Nums.pageSize);
        }
        Timestamp timestamp;
        if (condition.getEndTime() == null) {
            timestamp = new Timestamp(System.currentTimeMillis());
        } else {
            timestamp = new Timestamp(condition.getEndTime().getTime());
        }
        queryWrapper.le("acc_notify.create_time", timestamp);
        queryWrapper.eq("acc_notify.account_id", condition.getAccountId());

        IPage<AccountNotify> notifyIPage = accountNotifyDAO.searchNotify(page, queryWrapper);
        MyPage<AccountNotify> myPage = new MyPage<>(notifyIPage);
        List<SimpleNotify> simpleNotifyList = new ArrayList<>();
        for (AccountNotify accountNotify : myPage.getPageList()) {
            try {
                /**填充显示所需的数据*/
                SimpleAccount simpleAccount = accountDAO.getSimpleAccountById(accountNotify.getNotify().getSender());
                accountNotify.getNotify().setAccount(simpleAccount);
                SimpleNotify simpleNotify = new SimpleNotify(accountNotify);
                if (fillInSimpleNotifyData(simpleNotify).isFailure()) {
                    throw new Exception();
                }
                simpleNotifyList.add(simpleNotify);
            } catch (Exception e) {
                return responseFromServer.error();
            }
        }
        MyPage<SimpleNotify> myPage2 = new MyPage(notifyIPage);
        myPage2.setPageList(simpleNotifyList);
        myPage2.setEndTime(timestamp);
        return responseFromServer.success(myPage2);
    }

    private AccountNotifyDAO accountNotifyDAO;
    private NotifyDAO notifyDAO;
    private CommodityDAO commodityDAO;
    @Autowired
    NoticeDAO noticeDAO;
    @Autowired
    ReservationDAO reservationDAO;
    @Autowired
    CommodityImageDAO commodityImageDAO;
    @Autowired
    AccountDAO accountDAO;
    @Autowired
    public NotifyServiceImpl(NotifyDAO notifyDAO, AccountNotifyDAO accountNotifyDAO, CommodityDAO commodityDAO) {
        this.accountNotifyDAO = accountNotifyDAO;
        this.notifyDAO = notifyDAO;
        this.commodityDAO = commodityDAO;
    }
}
