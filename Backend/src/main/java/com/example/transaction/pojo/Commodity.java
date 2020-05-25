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



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getExpectedPrice() {
        return expectedPrice;
    }

    public void setExpectedPrice(Double expectedPrice) {
        this.expectedPrice = expectedPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNewness() {
        return newness;
    }

    public void setNewness(String newness) {
        this.newness = newness;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

    public Integer getInitialCount() {
        return initialCount;
    }

    public void setInitialCount(Integer initialCount) {
        this.initialCount = initialCount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<CommodityImage> getCommodityImages() {
        return commodityImages;
    }

    public void setCommodityImages(List<CommodityImage> commodityImages) {
        this.commodityImages = commodityImages;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
