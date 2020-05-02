package com.example.transaction.daoTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.CommodityDAO;
import com.example.transaction.pojo.Commodity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
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
    void testSelectWithCondition(){
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "%ello%");  //模糊查找测试, queryWrapper条件是叠加的

        List<Commodity> commodities = commodityDAO.selectWithCondition(queryWrapper);
        System.out.println(commodities.size());
        System.out.println(commodities);
    }

    @Test
    //测试商品名模糊分页查询
    void testSortByNewness(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis()); //当前时间
        Page<Commodity> page = new Page<>(2,1);
        IPage<Commodity> iPage = commodityDAO.sortByNewness(page, "ello");
        System.out.println(page.getPages());
        System.out.println(page.getTotal());
        System.out.println(page.getRecords());
    }

    @Test
    //测试价格区间查询
    void testBetweenPrice(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis()); //当前时间
        Page<Commodity> page = new Page<>(2,1);
        IPage<Commodity> iPage = commodityDAO.betweenPrice(page,"ello", 1, 50);
        System.out.println(page.getPages());
        System.out.println(page.getTotal());
        System.out.println(page.getRecords());
    }

    @Test
    //测试类型分页
    void testSortByType(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis()); //当前时间
        Page<Commodity> page = new Page<>(1,1);
        IPage<Commodity> iPage = commodityDAO.sortByType(page, 1);
        System.out.println(page.getPages());
        System.out.println(page.getTotal());
        System.out.println(page.getRecords());
    }

    @Test
    //测试商品名模糊分页查询，信誉排序
    void testSortByCredit(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis()); //当前时间
        Page<Commodity> page = new Page<>(2,1);
        IPage<Commodity> iPage = commodityDAO.sortByCredit(page, "yell");
        System.out.println(page.getPages());
        System.out.println(page.getTotal());
        System.out.println(page.getRecords());
    }
}