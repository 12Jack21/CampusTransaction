package com.example.transaction.service.impl;

import com.example.transaction.dao.ReservationDAO;
import com.example.transaction.pojo.Reservation;
import com.example.transaction.service.ReservationService;
import com.example.transaction.util.ReservationCode;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Map;

/**
 * @ClassName: ReservationServiceImpl
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/26 16:32
 */
@Service(value = "ReservationService")
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
        reservation.setStateEnum(ReservationCode.CANCELED.getCode());
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

    @Override
    public responseFromServer getReservationsPageForUser(Map<String, Object> map) {
        return null;
    }

    @Override
    public responseFromServer updateReservation(Reservation reservation) {
        return null;
    }

    ReservationDAO reservationDAO;

    @Autowired
    public ReservationServiceImpl(ReservationDAO reservationDAO){
        this.reservationDAO = reservationDAO;
    }


}
