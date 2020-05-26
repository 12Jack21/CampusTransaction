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
 * @ClassName: Token
 * @Description: token实体类
 * @Author: 曾志昊
 * @Date: 2020/5/7 16:46
 */
@Data
public class Token implements Serializable {
    @TableId(value = "account_id")
    private int accountId;

    //@JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField(value = "create_time")
    private Timestamp createTime;
    @TableField(value = "token_str")
    private String tokenStr;

    @TableField(exist = false)
    private Account account;
}
