package com.example.transaction.daoTest;

import com.example.transaction.dao.NotifyDAO;
import com.example.transaction.pojo.Notify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: 高战立
 * @Date: 2020/5/5 21:12
 * @Content:
 */

@SpringBootTest
public class NotifyDAOTest {
    @Autowired
    private NotifyDAO notifyDAO;

    @Test
    void test(){
        Notify notify = notifyDAO.selectById(1);
        System.out.println(notify);
    }
}
