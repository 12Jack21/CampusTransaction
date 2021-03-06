package com.example.transaction.dto.notify;

import com.example.transaction.dto.account.SimpleAccount;
import com.example.transaction.dto.commodity.SimpleCommodity;
import com.example.transaction.pojo.AccountNotify;
import com.example.transaction.pojo.Notify;
import com.example.transaction.util.PathUtil;
import com.example.transaction.util.code.ResourcePath;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * @ClassName: SimpleNotify
 * @Description: 用于返回的简单通知对象
 * @Author: 曾志昊
 * @Date: 2020/5/20 0:16
 */
@Data
public class SimpleNotify<T> {
    /**
     * 接收者
     */
    Integer receiverId;
    /**
     * 发送者
     */
    Integer sender = -1;
    String avatar = "";
    String accountName = "";
    Integer accountGender = 0;
    /**
     * notify属性
     */
    Integer id = -1;
    Boolean isRead = false;
    //    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    Date createTime;

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
        this.id = accountNotify.getId();
        this.createTime = accountNotify.getCreateTime();
        SimpleAccount simpleAccount = notify.getAccount();
        this.sender =  notify.getSender() == null ||  notify.getSender() <= 0 ? -1: notify.getSender();
        this.action = notify.getAction();
        this.targetId = notify.getTarget();
        this.targetType = notify.getTargetType();

        if(simpleAccount !=null){
            if(PathUtil.isPath(simpleAccount.getAvatar())){
                this.avatar = simpleAccount.getAvatar();

            }else{
                this.avatar = ResourcePath.avatarRequestPath + simpleAccount.getAvatar();

            }
            this.accountName = simpleAccount.getUsername();
            this.accountGender = simpleAccount.getGender();
            this.receiverId = accountNotify.getAccountId();
            this.isRead = accountNotify.getIsRead();
            this.createTime = accountNotify.getCreateTime();

        }

    }
}
