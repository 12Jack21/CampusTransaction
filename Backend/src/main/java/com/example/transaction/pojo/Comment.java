package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 15:44
 * @Content: 评价
 */

@Data
public class Comment {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Boolean type;
    private String content;
    private Timestamp date;

    @TableField(value = "from_id")
    private Integer fromId;

    @TableField(value = "to_id")
    private Integer toId;

    @TableField(value = "commodity_id")
    private Integer commodityId;

    @TableField(exist = false)
    private Commodity commodity;
    @TableField(exist = false)
    private SimpleAccount sender;
    @TableField(exist = false)
    private SimpleAccount receiver;
}
