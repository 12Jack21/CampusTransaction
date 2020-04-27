package com.example.transaction.daoTest;

import com.example.transaction.dao.AccountDAO;
import com.example.transaction.pojo.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccountDAOTest {

    @Autowired
    private AccountDAO accountDAO;

    @Test
    void testGetAccountCreditById(){
        Account account = accountDAO.getAccountCreditById(1);
        System.out.println(account);
    }
}