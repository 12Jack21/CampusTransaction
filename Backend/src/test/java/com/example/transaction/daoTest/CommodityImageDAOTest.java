package com.example.transaction.daoTest;

import com.example.transaction.dao.CommodityImageDAO;
import com.example.transaction.pojo.CommodityImage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 20:29
 * @Content: 测试CommodityImageDAO接口
 */

@SpringBootTest
public class CommodityImageDAOTest {

    @Autowired
    private CommodityImageDAO commodityImageDAO;

    @Test
    void testGetAllImageByCommodityId(){
        List<CommodityImage> commodityImages = commodityImageDAO.getAllImageByCommodityId(1);
        System.out.println(commodityImages.size());
        System.out.println(commodityImages);
    }

    @Test
    void testSelectById(){
        CommodityImage commodityImage = commodityImageDAO.selectById(1);
        System.out.println(commodityImage);
    }

    @Test
    void insert(){
        for(int i = 29; i <=60;i++){
            CommodityImage commodityImage = new CommodityImage();
            commodityImage.setImageUrl(i+".jpg");
            commodityImage.setCommodityId(i);
            commodityImageDAO.insert(commodityImage);
        }
    }
}
