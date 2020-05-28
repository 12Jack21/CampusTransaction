package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.transaction.pojo.base.BaseCommodity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 15:37
 * @Content: 商品
 */

@Data
public class Commodity extends BaseCommodity implements Serializable {

    @TableField(exist = false)
    private Notice notice;
    @TableField(exist = false)
    private List<Reservation> reservation;

    public Commodity() {
    }

    public Commodity(Integer id) {
        this.id = id;
    }

    public void clear(){
        if(description == "") {
            description = null;
        }
        if (expectedPrice <= 0) {
            expectedPrice = null;
        }
        if(type == ""){
            type = null;
        }
        if(count<0){
            count = null;
        }
    }



    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Double getOriginalPrice() {
        return originalPrice;
    }

    @Override
    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    @Override
    public Double getExpectedPrice() {
        return expectedPrice;
    }

    @Override
    public void setExpectedPrice(Double expectedPrice) {
        this.expectedPrice = expectedPrice;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getNewness() {
        return newness;
    }

    @Override
    public void setNewness(String newness) {
        this.newness = newness;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Integer getNoticeId() {
        return noticeId;
    }

    @Override
    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

    @Override
    public Integer getInitialCount() {
        return initialCount;
    }

    @Override
    public void setInitialCount(Integer initialCount) {
        this.initialCount = initialCount;
    }

    @Override
    public Integer getCount() {
        return count;
    }

    @Override
    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public List<CommodityImage> getCommodityImages() {
        return commodityImages;
    }

    @Override
    public void setCommodityImages(List<CommodityImage> commodityImages) {
        this.commodityImages = commodityImages;
    }

    @Override
    public List<String> getImages() {
        return images;
    }

    @Override
    public void setImages(List<String> images) {
        this.images = images;
    }
}
