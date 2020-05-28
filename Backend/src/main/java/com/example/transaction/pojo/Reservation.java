package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 16:03
 * @Content: 预约记录
 */

@Data
public class Reservation implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String note;
//    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField(value = "start_time")
    private Date startTime;
//    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField(value = "update_time")
    private Date updateTime;
    @TableField(value = "state_enum")
    private Integer stateEnum;
    private Integer count;
    @TableField(value = "account_id")
    private Integer accountId;
    @TableField(value = "commodity_id")
    private Integer commodityId;
    @TableField(value = "evaluation_buy")
    private Double evaluationBuy;
    @TableField(value = "evaluation_sell")
    private Double evaluationSell;


    @TableField(exist = false)
    private Account user;
    @TableField(exist = false)
    private Commodity commodity;

    public Reservation() {
    }

    public Reservation(Integer reservationId) {
        this.id = reservationId;
    }
}
