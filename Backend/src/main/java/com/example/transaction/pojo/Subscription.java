package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 16:13
 * @Content: 订阅
 */

@Data
public class Subscription implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer target;
    @TableField(value = "target_type")
    private Integer targetType;
    //@JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField(value = "create_time")
    private Timestamp createTime;
    @TableField(value = "account_id")
    private Integer accountId;

    @TableField(exist = false)
    private Account user;
}
