package com.example.transaction.daoTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.ReservationDAO;
import com.example.transaction.pojo.Reservation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 * @Author: 高战立
 * @Date: 2020/4/25 15:11
 * @Content:
 */

@SpringBootTest
public class ReservationDAOTest {

    @Autowired
    private ReservationDAO reservationDAO;

    @Test
    void testGetAllReservationByAccountId(){
        List<Reservation> reservations = reservationDAO.getAllReservationByAccountId(1);
        System.out.println(reservations.toString());
    }

    @Test
    void testGetAllReservationByCommodityId(){
        List<Reservation> reservations = reservationDAO.getAllReservationByCommodityId(1);
        System.out.println(reservations.toString());
    }

    @Test
    void testGetWithCondition(){
        QueryWrapper<Reservation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state_enum", 0);  //根据state_enum进行条件查询
        List<Reservation> reservations = reservationDAO.getWithCondition(queryWrapper);
        System.out.println(reservations.size());
        System.out.println(reservations.toString());
    }

    @Test
    void testSelectWithDetailedCommodityById(){
        Reservation reservation = reservationDAO.selectWithDetailedCommodityById(1);
        System.out.println(reservation);
    }


}
