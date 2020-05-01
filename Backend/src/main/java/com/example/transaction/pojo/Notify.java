package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 15:32
 * @Content: 通知
 */

@Data
public class Notify {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /*公告，提醒，信息*/
    private Integer type;
    /*发送者id*/
    private Integer sender;
    /*目标id: comment_id reservation_id*/
    private Integer target;
    /*目标类型: comment  reservation*/
    @TableField(value = "target_type")
    private Integer targetType;

    @TableField(value = "acc_notify_id")
    private Integer accountNotifyId;

    /*动作？*/
    private Integer action;
    /*内容：用于公告*/
    private String content;
}
