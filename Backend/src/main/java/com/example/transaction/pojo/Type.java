package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 15:39
 * @Content: 商品种类
 */

@Data
public class Type {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String value;
    @TableField(value = "commodity_id")
    private Integer commodityId;
}
