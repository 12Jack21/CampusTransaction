package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.transaction.dto.account.SimpleAccount;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 15:44
 * @Content: 评价
 */

@Data
public class Comment implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Boolean type;
    private String content;

    @TableField(value = "time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date date;

    @TableField(value = "from_id")
    private Integer fromId;

    @TableField(value = "to_id")
    private Integer toId = -1;

    @TableField(value = "commodity_id")
    private Integer commodityId;

    @TableField(exist = false)
    private Commodity commodity;
    @TableField(exist = false)
    private SimpleAccount sender;
    @TableField(exist = false)
    private SimpleAccount receiver;
}
