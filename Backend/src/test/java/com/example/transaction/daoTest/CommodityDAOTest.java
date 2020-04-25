package com.example.transaction.daoTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.transaction.dao.CommodityDAO;
import com.example.transaction.pojo.Commodity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/25 9:56
 * @Content: CommodityDAO测试
 */

@SpringBootTest
public class CommodityDAOTest {

    @Autowired
    private CommodityDAO commodityDAO;

    @Test
    void testSelectById(){
        Commodity commodity = commodityDAO.selectById(1);
        System.out.println(commodity);
    }

    @Test
    void testSelectAllInfoById(){
        Commodity commodity = commodityDAO.selectAllInfoById(2);
        System.out.println(commodity);
    }

    @Test
    void testSelectWithCondition(){
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "%ello%");  //模糊查找测试, queryWrapper条件是叠加的
//        List<Commodity> commodities = commodityDAO.selectWithCondition(queryWrapper);
//        System.out.println(commodities.size());
//        System.out.println(commodities.get(0));

        queryWrapper.between("expected_price",5,20); //价格区间检索
        List<Commodity> commodities = commodityDAO.selectWithCondition(queryWrapper);
        System.out.println(commodities.size());
        System.out.println(commodities.toString());
    }
}