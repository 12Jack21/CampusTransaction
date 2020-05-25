package com.example.transaction.dto.account;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Estimate;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: SimpleAccount
 * @Author: 曾志昊
 * @Date: 2020/5/2 20:15
 */
@Data
@TableName("account")
public class SimpleAccount implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String username;
    private Boolean gender;
    private String avatar;
    private String introduction;

    @TableField(exist = false)
    private Estimate estimate;

    public SimpleAccount(Integer accountId){
        this.id = accountId;
    }
    public SimpleAccount(){}
    public SimpleAccount(Account account){
        this.id = account.getId();
        this.avatar = account.getAvatar();
        this.username = account.getUsername();
        this.gender = account.getGender();
        this.introduction = account.getIntroduction();
    }
}
