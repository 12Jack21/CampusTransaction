package com.example.transaction.daoTest;

import com.example.transaction.dao.AccountDAO;
import com.example.transaction.pojo.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class AccountDAOTest {

    @Autowired
    private AccountDAO accountDAO;

    @Test
    public void testAccAdd(){
        Account a = new Account();
//        a.setId(100);
        a.setUsername("IOIO");
        int index = accountDAO.insert(a);
        List<Account> l = accountDAO.getByPassword("1111");
        System.out.println("Affected Row my : " + index);
    }

    @Test
    public void getAll(){
        List<Account> accounts = accountDAO.getAllAccount();
    }
}