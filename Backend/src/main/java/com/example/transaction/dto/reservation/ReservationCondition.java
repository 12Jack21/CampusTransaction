package com.example.transaction.dto.reservation;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName: ReservationCondition
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/5/26 13:49
 */
public class ReservationCondition {
    Integer type = 0;
    Integer pageIndex;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    Date endTime;
}
