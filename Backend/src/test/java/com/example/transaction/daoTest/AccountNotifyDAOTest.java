package com.example.transaction.daoTest;

import com.example.transaction.dao.AccountNotifyDAO;
import com.example.transaction.pojo.AccountNotify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/25 19:46
 * @Content: AccountNotifyDAO测试
 */

@SpringBootTest
public class AccountNotifyDAOTest {
    @Autowired
    private AccountNotifyDAO accountNotifyDAO;

    @Test
    void testGetUnreadNotifyByAccountId(){
        List<AccountNotify> accountNotifies = accountNotifyDAO.getUnreadNotifyByAccountId(1);
        System.out.println(accountNotifies.toString());
    }

    @Test
    void testGetAllNotifyByAccountId(){
        List<AccountNotify> accountNotifies = accountNotifyDAO.getAllNotifyByAccountId(1);
        System.out.println(accountNotifies.toString());
    }

    @Test
    void testGetDetailedNotifyById(){
        AccountNotify accountNotify = accountNotifyDAO.getDetailedNotifyById(1);
        System.out.println(accountNotify);
    }

    @Test
    void testSetNotifyRead(){
        System.out.println(accountNotifyDAO.setNotifyRead(1));
    }
}
