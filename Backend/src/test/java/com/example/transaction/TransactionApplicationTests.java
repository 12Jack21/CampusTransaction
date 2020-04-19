package com.example.transaction;

import com.example.transaction.dao.AccountDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TransactionApplicationTests {
    @Autowired
    private AccountDAO accountDAO;
    @Test
    void contextLoads() {
        System.out.println(accountDAO.toString());
    }

}
