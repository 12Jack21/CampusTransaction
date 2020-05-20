package com.example.transaction.dto.notify;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.transaction.dto.account.SimpleAccount;
import com.example.transaction.dto.commodity.SimpleCommodity;
import com.example.transaction.pojo.AccountNotify;
import com.example.transaction.pojo.Notify;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

/**
 * @ClassName: SimpleNotify
 * @Description: 用于返回的简单通知对象
 * @Author: 曾志昊
 * @Date: 2020/5/20 0:16
 */
public class SimpleNotify {
    /**
     * 接收者
     */
    Integer receiverId;

    /**
     * 发送者
     */
    Integer sender = -1;
    String avatarURL = "";
    String accountName = "";
    Boolean accountGender = false;
    /**
     * notify属性
     */
    Integer id = -1;
    Boolean isRead = false;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
    Timestamp createTime;

    Integer action = -1;
    Integer targetId = -1;
    Integer targetType = -1;

    SimpleCommodity commodity = null;

    /**
     * 通告标题
     */
    String title = "";

    public SimpleNotify() {
    }

    public SimpleNotify(AccountNotify accountNotify) {
        Notify notify = accountNotify.getNotify();

        SimpleAccount simpleAccount = notify.getAccount();
        this.sender = notify.getSender();
        this.action = notify.getAction();
        this.targetId = notify.getTarget();
        this.targetType = notify.getTargetType();

        this.avatarURL = simpleAccount.getAvatarUrl();
        this.accountName = simpleAccount.getUsername();
        this.accountGender = simpleAccount.getGender();
        this.receiverId = accountNotify.getAccountId();
        this.isRead = accountNotify.getIsRead();
        this.createTime = accountNotify.getCreateTime();

    }
}
