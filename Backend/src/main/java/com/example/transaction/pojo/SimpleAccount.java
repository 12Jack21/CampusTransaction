package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @ClassName: SimpleAccount
 * @Author: 曾志昊
 * @Date: 2020/5/2 20:15
 */
@Data
@TableName("account")
public class SimpleAccount {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private Boolean gender;
    private String avatarUrl;

    @TableField(exist = false)
    private Estimate estimate;

    public SimpleAccount(Integer accountId){
        this.id = accountId;
    }
    public SimpleAccount(){}
}
