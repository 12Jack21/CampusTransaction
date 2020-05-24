package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 16:12
 * @Content: 历史记录
 */

@Data
public class Search implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String content;
    private Integer count;
    //    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-mm-dd HH:mm")
    @TableField(value = "update_time")
    private Timestamp updateTime;
    //@JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-mm-dd HH:mm")
    @TableField(value = "account_id")
    private Integer accountId;
}
