package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 15:37
 * @Content: 商品
 */

@Data
public class Commodity {
    private Integer id;
    private Double originalPrice;
    private Double expectedPrice;
    private String name;
    private Integer newness;
    private String description;

    @TableField(exist = false)
    private List<Type> types;
    @TableField(exist = false)
    private List<CommodityImage> commodityImages;
    @TableField(exist = false)
    private CommodityList commodityList;
}
