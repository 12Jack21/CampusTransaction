package com.example.transaction.serviceTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.transaction.pojo.Reservation;
import com.example.transaction.service.ReservationService;
import com.example.transaction.util.responseFromServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @ClassName: ReservationServiceTest
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/27 16:13
 */
@SpringBootTest
public class ReservationServiceTest {
    @Autowired
    ReservationService reservationService;

    @Test
    public void getReservationsPageForUser(){
        QueryWrapper<Reservation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account_id",1);
        responseFromServer response = reservationService.getReservationsPage(queryWrapper,2);
        queryWrapper.eq("account_id",1);
    }
}
