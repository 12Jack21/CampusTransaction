package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 16:12
 * @Content: 历史记录
 */

@Data
public class Search {
    private Integer id;
    private String content;
    private Integer count;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Integer accountId;
}
