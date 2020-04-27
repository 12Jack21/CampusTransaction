package com.example.transaction.daoTest;

import com.example.transaction.dao.EstimateDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: 高战立
 * @Date: 2020/4/27 9:46
 * @Content:
 */

@SpringBootTest
public class EstimateDAOTest {
    @Autowired
    private EstimateDAO estimateDAO;

    @Test
    void testGetByAccountId(){
        System.out.println(estimateDAO.getByAccountId(1));
    }
}
