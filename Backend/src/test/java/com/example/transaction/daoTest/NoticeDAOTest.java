package com.example.transaction.daoTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.transaction.dao.NoticeDAO;
import com.example.transaction.pojo.Notice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Transactional
class NoticeDAOTest {

    @Autowired
    private NoticeDAO noticeDAO;
    @Test
    void getAllNotice() {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(true, "type", true);
        List<Notice> notices = noticeDAO.getAllNotice(queryWrapper);
        System.out.println(notices.size());
    }

    @Test
//    @Transactional(rollbackFor = Throwable.class)
    void updateNotice(){
        Notice notice = new Notice();
        notice.setId(2);
        notice.setDescription("iqweqjndi12");
        notice.setType(false);

        int num = noticeDAO.updateXML(notice);

        System.out.println(num);
    }
}