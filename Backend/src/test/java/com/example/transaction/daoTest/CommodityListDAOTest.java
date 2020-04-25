package com.example.transaction.daoTest;

import com.example.transaction.dao.CommodityListDAO;
import com.example.transaction.pojo.CommodityList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/25 9:12
 * @Content: CommodityListDAO测试
 */

@SpringBootTest
public class CommodityListDAOTest {

    @Autowired
    private CommodityListDAO commodityListDAO;

    @Test
    void testGetAllCommodityListByNoticeId(){
        List<CommodityList> lists = commodityListDAO.getAllCommodityListByNoticeId(1);
        System.out.println(lists.size());
        System.out.println(lists.get(0));
    }

    @Test
    void testGetAllByCommodityId(){
        CommodityList commodityList = commodityListDAO.getAllByCommodityId(2);
        System.out.println(commodityList);
    }

    @Test
    void testSelectById(){
        CommodityList commodityList = commodityListDAO.selectById(1);
        System.out.println(commodityList);
    }
}
