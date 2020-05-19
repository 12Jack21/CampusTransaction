package com.example.transaction.dto.notify;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.transaction.dto.account.SimpleAccount;
import com.example.transaction.dto.commodity.SimpleCommodity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

/**
 * @ClassName: SimpleNotify
 * @Description: 用于返回的简单通知对象
 * @Author: 曾志昊
 * @Date: 2020/5/20 0:16
 */
public class SimpleNotify<T> {
    /**
     * 接收者
     */
    Integer receiverId;

    /**
     * 发送者
     */
    Integer senderId;
    String senderAvatar;
    Boolean senderGender;

    /**
     * notify属性
     */
    Integer accountNotifyId;
    Boolean isRead;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
    private Timestamp createTime;

    SimpleCommodity commodity;
    SimpleAccount sender;


}
