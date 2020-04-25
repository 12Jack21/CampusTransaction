package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 15:25
 * @Content: 用户通知列表
 */

@Data
public class AccountNotify {
    private Integer id;
    private boolean isRead;
    private Timestamp createTime;
    private Timestamp readTime;

    @TableField(exist = false)
    private List<Notify> notify;
    @TableField(exist = false)
    private Account owner;
}
