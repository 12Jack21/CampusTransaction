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
    void testGetAllByNotifyId(){
        List<AccountNotify> accountNotifies = accountNotifyDAO.getAllByNotifyId(1);
        System.out.println(accountNotifies);
    }

    @Test
    void testGetAllByAccountId(){
        List<AccountNotify> accountNotifies = accountNotifyDAO.getAllByAccountId(1);
        System.out.println(accountNotifies);
    }
}
