package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 15:39
 * @Content: 商品种类
 */

@Data
public class Type {
    private Integer id;
    private String value;
    private Integer commodityId;
}
