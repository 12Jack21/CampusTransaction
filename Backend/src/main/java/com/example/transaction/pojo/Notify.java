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
    private Integer type;
    private Integer sender;
    private Integer target;
    @TableField(value = "target_type")
    private Integer targetType;
    private Integer action;
    private String content;
    private Timestamp createTime;
}
