package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Objects;

@Data
public class Notice {
    private int id;
    private boolean type;
    private Timestamp endTime;
    private String title;
    private String condition;
    private String description;
    private Integer stateEnum;
    private Timestamp updateTime;
    private Timestamp createTime;

    @TableField(value = "owner_id", exist = false)
    private Account owner;
}
