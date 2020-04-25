package com.example.transaction.pojo;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 15:32
 * @Content: 通知
 */

@Data
public class Notify {
    private Integer id;
    private Integer type;
    private Integer sender;
    private Integer target;
    private Integer target_type;
    private Integer action;
    private String content;
    private Timestamp createTime;
}
