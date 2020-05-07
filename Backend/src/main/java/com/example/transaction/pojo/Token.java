package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @ClassName: Token
 * @Description: token实体类
 * @Author: 曾志昊
 * @Date: 2020/5/7 16:46
 */
@Data
public class Token {
    @TableId(value = "account_id")
    private int accountId;

    @TableField(value = "create_time")
    private Timestamp createTime;
    @TableField(value = "token_str")
    private String tokenStr;

    @TableField(exist = false)
    private Account account;
}
