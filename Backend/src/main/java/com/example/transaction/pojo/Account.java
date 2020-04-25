package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

@Data
public class Account {

    private int id;
    private String username;
    private String password;
    private boolean gender;
    private String address;
    private String institute;
    private String mail;
    private String qq;
    private String wechat;
    private String avatarUrl;
    private Timestamp createTime;
    private Timestamp updateTime;

    @TableField(exist = false)
    private List<Notice> noticeList;
    @TableField(exist = false)
    private List<Reservation> reservationList;
    @TableField(exist = false)
    private List<Search> searchList;
    @TableField(exist = false)
    private List<AccountNotify> accountNotifyList;
    @TableField(exist = false)
    private List<Subscription> subscriptionList;
}
