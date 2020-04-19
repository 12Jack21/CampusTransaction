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
    private Byte gender;
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
}
