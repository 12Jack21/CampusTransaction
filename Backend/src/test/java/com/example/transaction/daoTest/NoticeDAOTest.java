package com.example.transaction.daoTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.NoticeDAO;
import com.example.transaction.pojo.Notice;
import com.example.transaction.util.MyPage;
import com.example.transaction.util.Nums;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.List;

@SpringBootTest
class NoticeDAOTest {

    @Autowired
    private NoticeDAO noticeDAO;

    @Test
    void selectById(){
        Notice notice = noticeDAO.selectById(1);
        System.out.println(notice);
    }

    @Test
    void testGetNoticeByOwnerId(){
        List<Notice> list = noticeDAO.getNoticeByOwnerId(2);
        System.out.println(list.size());
        System.out.println(list.toString());
    }

    @Test
    void testGetNoticeWithCondition(){
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq(true,"state_enum", 1);
        Timestamp timestamp = Timestamp.valueOf("2020-04-09 11:47:19.000000");
        queryWrapper.le("end_time", timestamp);
        List<Notice> notices = noticeDAO.getNoticeWithCondition(queryWrapper);
        System.out.println(notices.size());
        System.out.println(notices.toString());
    }

    @Test
    void testGetNoticeWithAllCommodityListById(){
        Notice notice = noticeDAO.getNoticeWithAllCommodityById(1);
        System.out.println(notice);
    }

    @Test
    void testGetNoticeWithAllCommodity(){
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", 1);
        Notice notice = noticeDAO.getNoticeWithAllCommodity(queryWrapper);
        System.out.println(notice);
    }

    @Test
    void testGetCreditByAccountId(){
        Notice notice = noticeDAO.getCreditByNoticeId(1);
        System.out.println(notice);
    }

    @Test
    void testGetDetailedNoticePage(){
        Page<Notice> page = new Page<>(1,2);
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", 1);
        IPage<Notice> iPage = noticeDAO.getDetailedNoticePage(page, queryWrapper);
        System.out.println(iPage.getPages());
        System.out.println(iPage.getTotal());
        System.out.println(iPage.getRecords());
    }
}