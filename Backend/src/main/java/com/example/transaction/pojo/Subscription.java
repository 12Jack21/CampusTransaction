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
    private Integer id;
    private Integer target;
    private Integer targetType;
    private Timestamp createTime;
    private Integer accountId;
}
