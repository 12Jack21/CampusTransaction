package com.example.transaction.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Reservation;
import com.example.transaction.util.responseFromServer;

import java.util.Map;

/**
 * @InterfaceName: ReservationService
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/26 16:32
 */
public interface ReservationService {

    public responseFromServer setUpReservation(Reservation reservation);
    public responseFromServer cancelReservation(Reservation reservation);
    public responseFromServer deleteReservation(Reservation reservation);
    public responseFromServer getReservationsPage(QueryWrapper queryWrapper, Integer pageIndex);
    public responseFromServer updateReservation(Reservation reservation);

}
