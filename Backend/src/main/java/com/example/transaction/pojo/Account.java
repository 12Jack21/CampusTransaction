package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.transaction.dto.account.SimpleAccount;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.netty.util.internal.StringUtil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
public class Account  implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id = -1;
    private String username;
    private String password;
    private Boolean gender;
    private String address;
    private String institute;
    private String mail;
    private String qq;
    private String wechat;
    private String avatar;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date updateTime;
    private String introduction;

    @TableField(exist = false)
    private Estimate estimate;
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

    public Account(Integer accountId){
        this.id = accountId;
    }
    public Account(){}
    public void rectifyAccount(){
        this.id = this.id.equals(-1)  ? null : this.id;
        this.username = StringUtil.isNullOrEmpty(this.username) ? null : this.username;
        this.password = StringUtil.isNullOrEmpty(this.password) ? null : this.password;
        this.address = StringUtil.isNullOrEmpty(this.address) ? null : this.address;
        this.institute = StringUtil.isNullOrEmpty(this.institute) ? null : this.institute;
        this.mail = StringUtil.isNullOrEmpty(this.mail) ? null : this.mail;
        this.qq = StringUtil.isNullOrEmpty(this.qq) ? null : this.qq;
        this.wechat = StringUtil.isNullOrEmpty(this.wechat) ? null : this.wechat;
        this.avatar = StringUtil.isNullOrEmpty(this.avatar) ? null : this.avatar;
        this.introduction = StringUtil.isNullOrEmpty(this.introduction) ? null : this.introduction;
    }
}
