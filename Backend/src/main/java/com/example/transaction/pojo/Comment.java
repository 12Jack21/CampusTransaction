package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 15:44
 * @Content: 评价
 */

@Data
public class Comment {
    private Integer id;
    private Boolean type;
    private String content;
    private Timestamp date;

    @TableField(exist = false)
    private Account sender;
    @TableField(exist = false)
    private Account receiver;
    @TableField(exist = false)
    private Commodity commodity;
}
