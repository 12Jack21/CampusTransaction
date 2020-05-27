package com.example.transaction.dto.commodity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.transaction.pojo.Commodity;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: SimpleCommodity
 * @Description: 简单商品对象
 * @Author: 曾志昊
 * @Date: 2020/5/19 23:38
 */
@Data
@TableName("commodity")
public class SimpleCommodity implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    protected Integer id=-1;
    protected String name="";
    protected String img="";



    public SimpleCommodity() {
    }

    public SimpleCommodity(Commodity commodity) {
        this.id = commodity.getId();
        this.name = commodity.getName();
        if (commodity.getCommodityImages() == null || commodity.getCommodityImages().size() == 0) {
            this.img = null;
        } else {
            this.img = commodity.getCommodityImages().get(0).getImageUrl();
        }

    }
}
