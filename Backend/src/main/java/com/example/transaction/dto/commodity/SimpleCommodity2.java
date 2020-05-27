package com.example.transaction.dto.commodity;

import com.example.transaction.pojo.Commodity;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: SimpleCommodity2
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/5/27 14:45
 */
@Data
public class SimpleCommodity2 extends SimpleCommodity implements Serializable {
    String description = "";
    Double expectedPrice = -1.0D;

    public SimpleCommodity2(){}

    public SimpleCommodity2(Commodity commodity){
        super(commodity);
        this.description = commodity.getDescription();
        this.expectedPrice = commodity.getExpectedPrice();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getExpectedPrice() {
        return expectedPrice;
    }

    public void setExpectedPrice(Double expectedPrice) {
        this.expectedPrice = expectedPrice;
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getImg() {
        return img;
    }

    @Override
    public void setImg(String img) {
        this.img = img;
    }
}
