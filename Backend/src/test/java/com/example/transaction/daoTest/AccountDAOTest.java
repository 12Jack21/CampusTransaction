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
    void testGetAllNotice(){
        Account account = accountDAO.getAllNotice(21);
        System.out.println(account);
    }
    @Test
    void testGetAllReservation(){
        Account account = accountDAO.getAllReservation(1);
        System.out.println(account);
    }
}