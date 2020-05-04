package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "image_url")
    private String imageUrl;
    @TableField(value = "commodity_id")
    private Integer commodityId;

    public CommodityImage(){}

    public CommodityImage(String imageUrl,Integer commodityId){
        this.imageUrl = imageUrl;
        this.commodityId = commodityId;
    }
}
