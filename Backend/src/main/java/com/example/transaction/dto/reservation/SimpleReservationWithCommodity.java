package com.example.transaction.dto.reservation;

import com.example.transaction.dto.commodity.SimpleCommodity;
import com.example.transaction.dto.commodity.SimpleCommodity2;
import com.example.transaction.pojo.Reservation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: SimpleReservationWithCommodity
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/5/30 1:46
 */
@Data
public class SimpleReservationWithCommodity implements Serializable {
    Integer id;
    Double price;
    Integer count;
    String note = "";
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    Date createTime;
    SimpleCommodity2 commodity;

    public SimpleReservationWithCommodity(){}

    public SimpleReservationWithCommodity(Reservation reservation){
        this.id = reservation.getId()==null? -1:reservation.getId();
        this.count = reservation.getCount();
        this.note = reservation.getNote();
        this.createTime = reservation.getStartTime();
        if(reservation.getCommodity()!=null){
            this.commodity = new SimpleCommodity2(reservation.getCommodity());
            this.price = reservation.getCommodity().getExpectedPrice() * this.count;

        }
    }
}
