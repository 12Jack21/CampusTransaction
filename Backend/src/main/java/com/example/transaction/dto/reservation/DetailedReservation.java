package com.example.transaction.dto.reservation;

import com.example.transaction.dto.commodity.SimpleCommodity2;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Reservation;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName: DetailedReservation
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/5/26 11:42
 */
@Data
public class DetailedReservation {
    Integer id = -1;
    Double price = -1.0D;
    Integer count = 10;
    String note = "";
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    Date createTime;
    Integer stateEnum = -3;
    Double evaluationSell = -1.0D;
    Double evaluationBuy = -1.0D;
    SimpleCommodity2 commodity;
    Integer accountId = -1;
    String accountName = "";
    String avatar = "";
    String buyerName="";
    String buyerAvatar="";
    Integer buyerId = -1;
    String detailedAddress = "";

    public DetailedReservation(){}
    public DetailedReservation(Reservation reservation){
        this.id = reservation.getId();
        this.count = reservation.getCount();
        this.note = reservation.getNote();
        this.createTime = reservation.getStartTime();
        this.stateEnum = reservation.getStateEnum();
        try{
            Account seller = reservation.getCommodity().getNotice().getUser();
            this.evaluationSell = seller.getEstimate().getCredit();
        }catch(Exception e){
            e.printStackTrace();
        }
        this.commodity = new SimpleCommodity2(reservation.getCommodity());
        this.buyerId = reservation.getAccountId();
    }

}
