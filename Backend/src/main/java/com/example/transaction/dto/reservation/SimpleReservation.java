package com.example.transaction.dto.reservation;

import com.example.transaction.dto.account.SimpleAccount;
import com.example.transaction.pojo.Reservation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName: SimpleReservation
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/5/24 19:58
 */
@Data
public class SimpleReservation {
    Integer id = -1;
    SimpleAccount account;
    Double price = -1.0D;
    String note = "";
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    Date createTime;

    public SimpleReservation() {
    }

    public SimpleReservation(Reservation reservation) {
        this.id = reservation.getId();
//        this.price = reservation.
        this.note = reservation.getNote();
        this.createTime = reservation.getStartTime();
//        this.account = re
    }
}
