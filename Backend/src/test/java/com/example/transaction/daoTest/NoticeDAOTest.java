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
    void testDetailedPage(){
        Page<Notice> page = new Page<>(1, Nums.pageSize);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.ge("id",1);
//        IPage<Notice> noticeIPage = noticeDAO.testDetailed(page,queryWrapper);
//        MyPage myPage = new MyPage(noticeIPage);
    }

    @Test
    void selectById(){
        Notice notice = noticeDAO.selectById(1);
        System.out.printf(notice.toString());
    }

    @Test
    void testGetNoticeByOwnerId(){
        List<Notice> list = noticeDAO.getNoticeByOwnerId(21);
        System.out.println(list.size());
        System.out.println(list.get(0));
    }

    @Test
    void testGetNoticeWithCondition(){
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq(true,"state_enum", 1);
        Timestamp timestamp = Timestamp.valueOf("2020-04-09 11:47:19.000000");
//        String strn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
        queryWrapper.le("end_time", timestamp);
        List<Notice> notices = noticeDAO.getNoticeWithCondition(queryWrapper);
        System.out.println(notices.size());
        System.out.println(notices.get(0));
    }

    @Test
    void testGetNoticeWithAllCommodityListById(){
        Notice notice = noticeDAO.getNoticeWithAllCommodityById(1);
        System.out.println(notice);
    }

    @Test
    void testGetCreditByAccountId(){
        Notice notice = noticeDAO.getCreditByNoticeId(1);
        System.out.println(notice);
    }
}