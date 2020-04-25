package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 16:12
 * @Content: 历史记录
 */

@Data
public class Search {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String content;
    private Integer count;
    @TableField(value = "notify_id")
    private Timestamp createTime;
    @TableField(value = "notify_id")
    private Timestamp updateTime;
    @TableField(value = "notify_id")
    private Integer accountId;
}
