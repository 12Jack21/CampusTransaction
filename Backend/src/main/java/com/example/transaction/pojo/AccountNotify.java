package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 15:25
 * @Content: 用户通知列表
 */

@Data
@TableName(value = "acc_notify")
public class AccountNotify {
    private Integer id;
    private boolean isRead;
    private Timestamp createTime;
    private Timestamp readTime;
    private Integer notifyId;
    private Integer accountId;

    @TableField(exist = false)
    private Notify notify;
    @TableField(exist = false)
    private Account owner;
}
