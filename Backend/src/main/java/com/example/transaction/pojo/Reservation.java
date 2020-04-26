package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 16:03
 * @Content: 预约记录
 */

@Data
public class Reservation {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String note;
    @TableField(value = "start_time")
    private Timestamp startTime;
    @TableField(value = "end_time")
    private Timestamp endTime;
    @TableField(value = "state_enum")
    private Integer stateEnum;
    private Integer count;
    @TableField(value = "account_id")
    private Integer accountId;
    @TableField(value = "commodity_id")
    private Integer commodityId;

    @TableField(exist = false)
    private Account user;
    @TableField(exist = false)
    private Commodity commodity;
}
