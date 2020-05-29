package com.example.transaction.dto.reservation;

import com.example.transaction.dto.account.SimpleAccount;
import com.example.transaction.pojo.Reservation;
import com.example.transaction.util.code.NoticeCode;
import com.example.transaction.util.code.ReservationCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.netty.util.internal.StringUtil;
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
    Integer count;
    Double price = -1.0D;
    String note = "";
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    Date createTime;
    Integer stateEnum;
    private String stateEnumStr = "";
    public void setStateEnum(Integer stateEnum){
        try{
            this.stateEnum = stateEnum;
            this.stateEnumStr = ReservationCode.getDescription(stateEnum);
        }catch(Exception e){
            e.printStackTrace();
            this.stateEnumStr = null;
            this.stateEnum = -1;
        }
    }
    public SimpleReservation() {
    }

    public SimpleReservation(Reservation reservation) {
        this.id = reservation.getId()==null?-1:reservation.getId();
//        this.price = reservation.
        this.note = StringUtil.isNullOrEmpty(reservation.getNote())?"":reservation.getNote();
        this.createTime = reservation.getStartTime();
        this.count = reservation.getCount() == null? -1:reservation.getCount();
        this.setStateEnum(reservation.getStateEnum());
//        this.account = re
    }
}
