package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 16:13
 * @Content: 订阅
 */

@Data
public class Subscription {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Integer target;
    @TableField(value = "target_type")
    private Integer targetType;
    @TableField(value = "create_time")
    private Timestamp createTime;
    @TableField(value = "account_id")
    private Integer accountId;

    @TableField(exist = false)
    private Account user;
}
