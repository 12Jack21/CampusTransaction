package com.example.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.*;
import com.example.transaction.dto.SimpleCondition;
import com.example.transaction.dto.account.SimpleAccount;
import com.example.transaction.dto.commodity.SimpleCommodity;
import com.example.transaction.dto.reservation.DetailedReservation;
import com.example.transaction.dto.reservation.SimpleReservation;
import com.example.transaction.dto.reservation.SimpleReservationWithCommodity;
import com.example.transaction.pojo.*;
import com.example.transaction.service.CommodityService;
import com.example.transaction.service.NotifyService;
import com.example.transaction.service.ReservationService;
import com.example.transaction.util.MyPage;
import com.example.transaction.util.PathUtil;
import com.example.transaction.util.code.*;
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
 * @ClassName: ReservationServiceImpl
 * @Description: 预约
 * @Author: 曾志昊
 * @Date: 2020/4/26 16:32
 */
@Service("ReservationService")
public class ReservationServiceImpl implements ReservationService {

    /**
     * 建立预约
     *
     * @param reservation
     * @return
     */
    @Override
    @Transactional
    public responseFromServer setUpReservation(Reservation reservation) {
        responseFromServer response = commodityService.getDetailedCommodity(reservation.getCommodityId());
        if (response.isFailure()) {
            return responseFromServer.error();
        }
        Commodity commodity = (Commodity) response.getData();
        if (commodity.getCount() < reservation.getCount()) {
            return responseFromServer.error();
        }
        if (commodity.getNotice().getEndTime().before(new Timestamp(System.currentTimeMillis()))) {
            /*已经过了时间期限*/
            return responseFromServer.error();
        }
        if (reservationDAO.insert(reservation) != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        AccountNotify accountNotify = new AccountNotify(
                reservation.getAccountId(),
                commodity.getNotice().getAccountId(),
                NotifyTargetCode.COMMODITY.getCode(),
                commodity.getId(),
                NotifyActionCode.RESERVES.getCode()
        );

        Account account = accountDAO.selectById(reservation.getAccountId());
        if (account == null) {
            return responseFromServer.error();
        }
        accountNotify.getNotify().setContent("用户" + account.getUsername() + "预约了你的" + commodity.getName());

        if (notifyService.insertAccountNotify(accountNotify).isFailure()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }

    /**
     * 取消预约：state = CANCELED
     *
     * @param reservationId
     * @param accountId
     * @return
     */
    @Override
    @Transactional
    public responseFromServer cancelReservation(Integer reservationId, Integer accountId) {
        Integer receiverId = null;
        Reservation reservation = reservationDAO.selectWithDetailedCommodityById(reservationId);
        /**
         * ZZH
         * TODO : 当accountId为空时先视作买家
         */
        if (accountId == null || accountId.intValue() == reservation.getAccountId().intValue()) {
            receiverId = reservation.getCommodity().getNotice().getAccountId();
        } else if (accountId.intValue() == reservation.getCommodity().getNotice().getAccountId()) {
            receiverId = reservation.getAccountId().intValue();
            if (reservation.getStateEnum() == ReservationCode.WAITING.getCode()) {
                /*卖家无法取消买家的预约队列请求*/
                return responseFromServer.illegal();
            }
        } else {
            /*既不是卖家也不是买家*/
            return responseFromServer.illegal();
        }


        /*身份验证成功*/
        if (reservation.getStateEnum() == ReservationCode.VALIDATE.getCode()) {
            /*如果已经validate，则需要恢复库存*/
            Commodity commodity = reservation.getCommodity();
            Commodity newCommodity = new Commodity();
            newCommodity.setId(commodity.getId());
            newCommodity.setCount(commodity.getCount() + reservation.getCount());
            if (commodityDAO.updateById(newCommodity) != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return responseFromServer.error();
            }
        } else if (reservation.getStateEnum() == ReservationCode.FINISHED.getCode()) {
            /*如果finish则不可以进行修改取消操作*/
            return responseFromServer.illegal("不可取消已完成的订单");
        }

        reservation.setStateEnum(ReservationCode.CANCELLED.getCode());
        if (reservationDAO.updateById(reservation) != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error("修改状态错误");
        }
        /**
         * ZZH
         * 添加到notify
         */
        if (accountId != null) {
            AccountNotify accountNotify = new AccountNotify(
                    receiverId,
                    accountId,
                    NotifyTargetCode.RESERVATION.getCode(),
                    reservation.getId(),
                    NotifyActionCode.CANCELS.getCode()
            );

            Account account = accountDAO.selectById(accountId);
            if (account == null) {
                return responseFromServer.error();
            }

            accountNotify.getNotify().setContent("用户" + account.getUsername() + "取消了你的预约");
            if (notifyService.insertAccountNotify(accountNotify).isFailure()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return responseFromServer.error();
            }
        }

        return responseFromServer.success();
    }

    /**
     * 删除预约信息
     *
     * @param reservation
     * @return
     */
    @Override
    @Transactional
    public responseFromServer deleteReservation(Reservation reservation) {
        if (reservationDAO.deleteById(reservation.getId()) != 1) {
            /*手动回滚事务*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        } else {
            return responseFromServer.success();
        }
    }

    /**
     * 查询简单的预约信息
     *
     * @param queryWrapper
     * @param pageIndex
     * @return
     */
    @Override
    public responseFromServer getSimpleReservationPage(QueryWrapper queryWrapper, Integer pageIndex) {
        /**
         * ZZH
         * TODO : 这里一次性返回该商品的所有预约, 所以先设置大小为40
         */
        Page<Reservation> page = new Page<>(pageIndex, 40);
        IPage<Reservation> reservationPage = reservationDAO.selectPage(page, queryWrapper);
        MyPage myPage = new MyPage(reservationPage);

        //将查询的分页结果中的reservation转化成simplereservation类型
        List<Reservation> reservations = (List<Reservation>) myPage.getPageList();
        List<SimpleReservation> simpleReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            SimpleReservation simpleReservation = new SimpleReservation(reservation);
            SimpleAccount simpleAccount = accountDAO.getSimpleAccountById(reservation.getAccountId());
            simpleAccount.setAvatar((PathUtil.isPath(simpleAccount.getAvatar()) ? "" : ResourcePath.avatarRequestPath) + simpleAccount.getAvatar());
            Commodity commodity = commodityDAO.getSimpleCommodityById(reservation.getCommodityId());
            simpleReservation.setAccount(simpleAccount);
            /*计算notice 的价格*/
            try {
                simpleReservation.setPrice(commodity.getExpectedPrice() * reservation.getCount());
                simpleReservations.add(simpleReservation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        myPage.setPageList(simpleReservations);
        return responseFromServer.success(simpleReservations);
    }

    /**
     * 查询详细预约信息（包含商品信息）
     *
     * @param reservationId
     * @return
     */
    @Override
    public responseFromServer getDetailedReservation(Integer reservationId) {
        Reservation reservation = reservationDAO.selectWithDetailedCommodityById(reservationId);
        if (reservation == null) {
            return responseFromServer.error();
        } else {
            return responseFromServer.success(reservation);
        }
    }


    @Override
    public responseFromServer getDetailedReservationInfo(Integer reservationId) {
        responseFromServer response = getDetailedReservation(reservationId);
        if (response.isFailure()) {
            return response;
        }
        //将查询的分页结果中的reservation转化成simplereservation类型
        Reservation reservation = (Reservation) response.getData();
        DetailedReservation detailedReservation;
        Notice notice = noticeDAO.getCreditByNoticeId(reservation.getCommodity().getNoticeId());
        reservation.getCommodity().setNotice(notice);
        try {
            detailedReservation = new DetailedReservation(reservation);
            /**
             * 设置买家信息
             */
            SimpleAccount simpleAccount = accountDAO.getSimpleAccountById(reservation.getAccountId());
            detailedReservation.setBuyerId(reservation.getAccountId());
            detailedReservation.setBuyerName(simpleAccount.getUsername());
            detailedReservation.setBuyerAvatar((PathUtil.isPath(simpleAccount.getAvatar()) ? "" : ResourcePath.avatarRequestPath) + simpleAccount.getAvatar());
//            detailedReservation.setEvaluationBuy(estimateDAO.getByAccountId(simpleAccount.getId()).getCredit());
            /**
             * 设置卖家信息
             */
            Commodity commodity = reservation.getCommodity();
            notice = noticeDAO.selectById(commodity.getNoticeId());
            simpleAccount = accountDAO.getSimpleAccountById(notice.getAccountId());
            detailedReservation.setDetailedAddress(notice.getDetailedAddress());
            detailedReservation.setAccountId(notice.getAccountId());
            detailedReservation.setAccountName(simpleAccount.getUsername());
            detailedReservation.setAvatar((PathUtil.isPath(simpleAccount.getAvatar()) ? "" : ResourcePath.avatarRequestPath) + simpleAccount.getAvatar());
            detailedReservation.setPrice(commodity.getExpectedPrice() * reservation.getCount());
//            detailedReservation.setEvaluationBuy(estimateDAO.getByAccountId(notice.getAccountId()).getCredit());
        } catch (Exception e) {
            e.printStackTrace();
            return responseFromServer.error();
        }
        return responseFromServer.success(detailedReservation);
    }


    /**
     * 查询预约信息分页
     *
     * @param queryWrapper
     * @param pageIndex
     * @return
     */
    @Override
    public responseFromServer getReservationsPage(QueryWrapper queryWrapper, Integer pageIndex) {
        Page<Reservation> page = new Page<>(pageIndex, Nums.pageSize);
        IPage<Reservation> reservationPage = reservationDAO.getReservationPageQuery(page, queryWrapper);
        MyPage myPage = new MyPage(reservationPage);
        List<SimpleReservationWithCommodity> simpleReservations = new ArrayList<>();
        if (reservationPage.getRecords() != null && reservationPage.getRecords().size() > 0) {
            for (Reservation reservation : reservationPage.getRecords()) {
                simpleReservations.add(
                        new SimpleReservationWithCommodity(reservation));
            }
        }
        myPage.setPageList(simpleReservations);
        return responseFromServer.success(myPage);
    }

    /**
     * 获取向我的预约请求
     *
     * @param accountId
     * @param condition
     * @return
     */
    @Override
    public responseFromServer getReservationRequest(Integer accountId, SimpleCondition condition) {
        Page<Reservation> page = new Page<>(condition.getPageIndex(), Nums.pageSize);
        if (condition.getType() == null) {
            condition.setType(4);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        switch (condition.getType().intValue()) {
            /**
             * 待确认
             */
            case 0:
                queryWrapper.eq("r.state_enum", 0);
                break;
            /**
             * 预约中
             */
            case 1:
                queryWrapper.eq("r.state_enum", 1);
                break;
            /**
             * 已完成/失败
             */
            case 3:
                queryWrapper.in("r.state_enum", Arrays.asList(2, 3));
                break;
            /**
             * 全部
             */
            default:
                break;

        }
        IPage<Reservation> reservationPage = reservationDAO.getReservationRequestPage(page, accountId, queryWrapper);
        MyPage myPage = new MyPage(reservationPage);
        List<SimpleReservation> simpleReservations = new ArrayList<>();
        for (Reservation reservation : reservationPage.getRecords()) {
            SimpleReservation simpleReservation = new SimpleReservation(reservation);
            SimpleAccount simpleAccount = accountDAO.getSimpleAccountById(reservation.getAccountId());
            Commodity commodity = commodityDAO.getSimpleCommodityById(reservation.getCommodityId());
            simpleReservation.setAccount(simpleAccount);
            /*计算notice 的价格*/
            simpleReservation.setPrice(commodity.getExpectedPrice() * reservation.getCount());
            simpleReservations.add(simpleReservation);
        }
        myPage.setPageList(simpleReservations);
        return responseFromServer.success(myPage);
    }

    /**
     * 更新预约
     *
     * @param reservation
     * @return
     */
    @Override
    @Transactional
    public responseFromServer updateReservation(Reservation reservation) {
        if (reservationDAO.updateById(reservation) != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }

    /**
     * 更新预约
     *
     * @param reservation
     * @return
     */
    @Override
    @Transactional
    public responseFromServer updateBuyerReservation(Reservation reservation) {
        Reservation oldReservation = reservationDAO.selectById(reservation.getId());
        if (oldReservation.getStateEnum() != ReservationCode.WAITING.getCode()) {
            return responseFromServer.illegal();
        }
        if (reservationDAO.updateById(reservation) != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }


    /**
     * 设置预约成功
     *
     * @param reservation
     * @return
     */
    @Override
    @Transactional
    public responseFromServer validateReservation(Reservation reservation, Integer sellerId) {
        /*用户验证在controller层中处理*/
        /*获取reservation 检查id和用户id*/
        if (reservation.getCommodityId() == null) {
            return responseFromServer.error();
        }
        /*验证reservation状态是否是等待状态*/
        if (reservation.getStateEnum() != ReservationCode.WAITING.getCode()) {
            return responseFromServer.error("预约状态错误");
        }
        /*验证状态成功*/

        /*验证commodity库存*/
        Commodity commodity = commodityDAO.getDetailedCommodityById(reservation.getCommodityId());
        if (commodity.getCount() < reservation.getCount()) {
            /*库存不足*/
            return responseFromServer.error("库存不足");
        }
        /*库存充足*/

        Integer buyerId = reservation.getAccountId();
        /*修改reservation状态*/
        reservation.setStateEnum(ReservationCode.VALIDATE.getCode());
        if (reservationDAO.updateById(reservation) != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error("修改预约状态错误");
        }
        Commodity newCommodity = new Commodity();
        newCommodity.setCount(commodity.getCount() - reservation.getCount());
        newCommodity.setId(commodity.getId());
        if (commodityDAO.updateById(newCommodity) != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error("修改商品库存出错");
        }


        reservation = reservationDAO.selectById(reservation.getId());
        if (sellerId != null) {
            /**
             * ZZH
             * TODO : 当sellerid不为空时(即有验证token时)
             */
            A2a a2a1 = new A2a(reservation.getAccountId(), sellerId);
            A2a a2a2 = new A2a(sellerId, reservation.getAccountId());
            if (a2aDAO.insert(a2a1) != 1 || a2aDAO.insert(a2a2) != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return responseFromServer.error();
            }

            /*添加到notify*/
            AccountNotify accountNotify = new AccountNotify(
                    sellerId,
                    reservation.getId(),
                    NotifyTargetCode.RESERVATION.getCode(),
                    reservation.getId(),
                    NotifyActionCode.VALIDATES.getCode()
            );
            accountNotify.getNotify().setContent("你预约的" + commodity.getName() + "被确认");
            if (notifyService.insertAccountNotify(accountNotify).isFailure()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return responseFromServer.error();
            }
        }


        return responseFromServer.success();
    }

    /**
     * 设置订单完成
     *
     * @param reservationId
     * @return
     */
    @Override
    @Transactional
    public responseFromServer finishReservation(Integer reservationId) {
        Reservation reservation = reservationDAO.selectWithDetailedCommodityById(reservationId);
        if (reservation.getStateEnum() == ReservationCode.FINISHED.getCode()) {
            return responseFromServer.error("该预约已经成功结束");
        }
        Reservation newReservation = new Reservation(reservationId);
        newReservation.setStateEnum(ReservationCode.FINISHED.getCode());
        if (reservationDAO.updateById(newReservation) != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        } else {
            AccountNotify accountNotify = new AccountNotify(
                    reservation.getCommodity().getNotice().getAccountId(),
                    reservation.getAccountId(),
                    NotifyTargetCode.RESERVATION.getCode(),
                    reservation.getId(),
                    NotifyActionCode.FINISHS.getCode());
            accountNotify.setAccountId(reservation.getAccountId());
            accountNotify.getNotify().setContent("你预约的" + reservation.getCommodity().getName() + "成功结束交易");
            if (notifyService.insertAccountNotify(accountNotify).isFailure()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return responseFromServer.error();
            }
            if (failWaitingOtherReservation(reservation.getCommodityId()).isFailure()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return responseFromServer.error();
            }
            return responseFromServer.success();
        }
    }

    /**
     * 将其他所有的预约都设置为FAILWAITING
     *
     * @return
     */
    @Override
    @Transactional
    public responseFromServer failWaitingOtherReservation(Integer commodityId) {
        Commodity commodity = commodityDAO.selectById(commodityId);
        try {
            reservationDAO.failWaiting(commodityId, commodity.getCount());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }


    /**
     * 查询简单预约信息
     *
     * @param reservationId
     * @return
     */
    @Override
    public responseFromServer getSimpleReservation(Integer reservationId) {
        Reservation reservation = reservationDAO.selectById(reservationId);
        if (reservation == null) {
            return responseFromServer.error();
        } else {
            return responseFromServer.success(reservation);
        }
    }

    @Override
    public responseFromServer checkIfUserHasReservation(Integer accountId, Integer commodityId){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("commodity_id",commodityId);
        queryWrapper.eq("account_id",accountId);
        queryWrapper.eq("state_enum",ReservationCode.WAITING);
        Integer count = reservationDAO.selectCount(queryWrapper);
        if(count == null||count.equals(0)){
            return responseFromServer.error();
        }else{
            return responseFromServer.success();
        }

    }

    NotifyService notifyService;
    A2aDAO a2aDAO;
    ReservationDAO reservationDAO;
    AccountDAO accountDAO;
    CommodityDAO commodityDAO;
    CommodityService commodityService;
    @Autowired
    NoticeDAO noticeDAO;
    @Autowired
    EstimateDAO estimateDAO;

    @Autowired
    public ReservationServiceImpl(NotifyService notifyService, AccountDAO accountDAO, ReservationDAO reservationDAO, CommodityDAO commodityDAO, A2aDAO a2aDAO, CommodityService commodityService) {
        this.notifyService = notifyService;
        this.accountDAO = accountDAO;
        this.reservationDAO = reservationDAO;
        this.commodityDAO = commodityDAO;
        this.a2aDAO = a2aDAO;
        this.commodityService = commodityService;
    }

}
