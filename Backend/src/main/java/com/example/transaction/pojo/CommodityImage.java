package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 15:40
 * @Content: 商品图片
 */

@Data
@TableName(value = "com_image")
public class CommodityImage {
    private Integer id;
    private String imageUrl;
    private Integer commodityId;
}
