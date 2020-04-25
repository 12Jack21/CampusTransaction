package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 16:03
 * @Content: 预约记录
 */

@Data
public class Reservation {
    private Integer id;
    private String note;
    private Timestamp startTime;
    private Timestamp endTime;
    private Integer stateEnum;
    private Integer count;
    private Integer accountId;
    private Integer commodityId;

    @TableField(exist = false)
    private Account user;
    @TableField(exist = false)
    private Commodity commodity;
}
