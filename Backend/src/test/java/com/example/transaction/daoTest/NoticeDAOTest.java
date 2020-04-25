package com.example.transaction.daoTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.transaction.dao.NoticeDAO;
import com.example.transaction.pojo.Notice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
class NoticeDAOTest {

    @Autowired
    private NoticeDAO noticeDAO;

    @Test
    void testGetNoticeByOwnerId(){
        List<Notice> list = noticeDAO.getNoticeByOwnerId(21);
        System.out.println(list.size());
        System.out.println(list.get(0));
    }

    @Test
    void testGetNoticeWithCondition(){
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(true,"state_enum", 1);
        List<Notice> notices = noticeDAO.getNoticeWithCondition(queryWrapper);
        System.out.println(notices.size());
        System.out.println(notices.get(0));
    }

    @Test
    void testGetNoticeWithAllCommodityListById(){
        Notice notice = noticeDAO.getNoticeWithAllCommodityById(1);
        System.out.println(notice);
    }
}