package com.example.transaction.pojo.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.transaction.pojo.CommodityImage;
import com.example.transaction.pojo.Notice;
import com.example.transaction.pojo.Reservation;
import com.example.transaction.pojo.Type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: BaseCommodity
 * @Description: 商品基类
 * @Author: 曾志昊
 * @Date: 2020/5/24 15:25
 */
public class BaseCommodity implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    protected Integer id;
    @TableField(value = "original_price")
    protected Double originalPrice;
    @TableField(value = "expected_price")
    protected Double expectedPrice;
    protected String name;
    protected String newness;
    protected String description;
    @TableField(value = "notice_id")
    protected Integer noticeId;
    @TableField(value = "initial_count")
    protected Integer initialCount;
    protected Integer count;
    protected String type;

    @TableField(exist = false)
    protected List<CommodityImage> commodityImages;

    @TableField(exist = false)
    protected List<String> images;

    public BaseCommodity() {
    }

    public BaseCommodity(Integer id) {
        this.id = id;
    }

    public void setImagesList(){
        if(commodityImages!=null && !commodityImages.isEmpty()){
            images = new ArrayList<>();
            for(CommodityImage image : commodityImages){
                images.add(image.getImageUrl());
            }
        }
    }
}
