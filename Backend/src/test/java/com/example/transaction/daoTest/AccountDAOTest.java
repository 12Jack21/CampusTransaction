package com.example.transaction.daoTest;

import com.example.transaction.dao.AccountDAO;
import com.example.transaction.pojo.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class AccountDAOTest {

    @Autowired
    private AccountDAO accountDAO;

    @Test
    public void testAccAdd(){
//        Account a = new Account();
//        a.setId(100);
//        a.setUsername("IOIO");
//        int index = accountDAO.insert(a);
    }

    @Test
    public void getAll(){
        List<Account> accounts = accountDAO.getAllAccount();
        System.out.println(accounts.toString());
    }
}