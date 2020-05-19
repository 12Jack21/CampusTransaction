package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

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
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
    @TableField(value = "start_time")
    private Timestamp startTime;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
    @TableField(value = "update_time")
    private Timestamp updateTime;
    @TableField(value = "state_enum")
    private Integer stateEnum;
    private Integer count;
    @TableField(value = "account_id")
    private Integer accountId;
    @TableField(value = "commodity_id")
    private Integer commodityId;
    @TableField(value = "seller_finished")
    private Boolean sellerFinished;
    @TableField(value = "")
    private Boolean buyerFinished;


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
