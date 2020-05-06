package com.example.transaction.daoTest;

import com.example.transaction.dao.A2aDAO;
import com.example.transaction.pojo.A2a;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: 高战立
 * @Date: 2020/5/5 20:10
 * @Content:
 */

@SpringBootTest
public class A2aDAOTest {
    @Autowired
    private A2aDAO a2aDAO;

    @Test
    void testGetA2a(){
        A2a a2a = a2aDAO.getA2a(1, 2);
        System.out.println(a2a);
    }
}
