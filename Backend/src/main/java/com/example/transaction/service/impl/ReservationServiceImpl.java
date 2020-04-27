package com.example.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.ReservationDAO;
import com.example.transaction.pojo.Reservation;
import com.example.transaction.service.ReservationService;
import com.example.transaction.util.MyPage;
import com.example.transaction.util.Nums;
import com.example.transaction.util.code.ReservationCode;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
     * @param reservation
     * @return
     */
    @Override
    @Transactional
    public responseFromServer setUpReservation(Reservation reservation) {
        if(reservationDAO.insert(reservation)!=1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }

    /**
     * 取消预约：state = CANCELED
     * @param reservation
     * @return
     */
    @Override
    @Transactional
    public responseFromServer cancelReservation(Reservation reservation) {
        reservation.setStateEnum(ReservationCode.CANCELLED.getCode());
        if(reservationDAO.updateById(reservation)!=1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }

    /**
     * 删除预约信息
     * @param reservation
     * @return
     */
    @Override
    @Transactional
    public responseFromServer deleteReservation(Reservation reservation) {
        if(reservationDAO.deleteById(reservation.getId())!=1){
            /*手动回滚事务*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }else{
            return responseFromServer.success();
        }
    }

    /**
     * 查询预约信息分页
     * @param queryWrapper
     * @param pageIndex
     * @return
     */
    @Override
    public responseFromServer getReservationsPage(QueryWrapper queryWrapper,Integer pageIndex) {
        Page<Reservation> page = new Page<>(pageIndex, Nums.pageSize);
        IPage<Reservation> reservationPage = reservationDAO.selectPage(page,queryWrapper);
        MyPage myPage = new MyPage(reservationPage);
        return responseFromServer.success(myPage);
    }

    /**
     * 更新预约
     * @param reservation
     * @return
     */
    @Override
    @Transactional
    public responseFromServer updateReservation(Reservation reservation) {
        if(reservationDAO.updateById(reservation)!=1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return  responseFromServer.error();
        }
        return responseFromServer.success();
    }

    ReservationDAO reservationDAO;

    @Autowired
    public ReservationServiceImpl(ReservationDAO reservationDAO){
        this.reservationDAO = reservationDAO;
    }


}
