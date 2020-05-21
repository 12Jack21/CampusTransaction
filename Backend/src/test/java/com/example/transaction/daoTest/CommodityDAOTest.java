package com.example.transaction.daoTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.CommodityDAO;
import com.example.transaction.pojo.Commodity;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.desktop.QuitEvent;
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
    void testGetSimpleCommodityById(){
        Commodity commodity = commodityDAO.getSimpleCommodityById(1);
        System.out.println(commodity);
    }

    @Test
    void testGetDetailedCommodityById(){
        Commodity commodity = commodityDAO.getDetailedCommodityById(1);
        System.out.println(commodity);
    }

    @Test
    void testGetDetailedCommodityByNoticeId(){
        List<Commodity> commodities = commodityDAO.getDetailedCommodityByNoticeId(1);
        System.out.println(commodities.size());
        System.out.println(commodities.toString());
    }

    @Test
    void testSelectWithCondition(){
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("notice_id", "1");

        List<Commodity> commodities = commodityDAO.selectWithCondition(queryWrapper);
        System.out.println(commodities.size());
        System.out.println(commodities);
    }

    @Test
    //测试商品名模糊分页查询
    void testSortByNewness(){
        Page<Commodity> page = new Page<>(1,2);
        IPage<Commodity> iPage = commodityDAO.sortByNewness(page, "ello");
        System.out.println(iPage.getPages());
        System.out.println(iPage.getTotal());
        System.out.println(iPage.getRecords());
    }

    @Test
    //测试价格区间查询
    void testBetweenPrice(){
        Page<Commodity> page = new Page<>(1,2);
        IPage<Commodity> iPage = commodityDAO.betweenPrice(page,"ello", 1, 50);
        System.out.println(page.getPages());
        System.out.println(page.getTotal());
        System.out.println(page.getRecords());
    }

    @Test
    //测试类型分页
    void testSortByType(){
        Page<Commodity> page = new Page<>(1,1);
        IPage<Commodity> iPage = commodityDAO.sortByType(page, 1);
        System.out.println(page.getPages());
        System.out.println(page.getTotal());
        System.out.println(page.getRecords());
    }

    @Test
    //测试商品名模糊分页查询，信誉排序
    void testSortByCredit(){
        Page<Commodity> page = new Page<>(1,2);
        IPage<Commodity> iPage = commodityDAO.sortByCredit(page, "yell");
        System.out.println(page.getPages());
        System.out.println(page.getTotal());
        System.out.println(page.getRecords());
    }

    @Test
    void searchTest(){
        Page<Commodity> page = new Page<>(1,2);
        QueryWrapper queryWrapper = new QueryWrapper();
        IPage<Commodity> iPage = commodityDAO.search(page, queryWrapper);
        System.out.println(page.getPages());
        System.out.println(page.getTotal());
        System.out.println(page.getRecords());
    }
}