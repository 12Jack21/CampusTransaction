package com.example.transaction.daoTest;

import com.example.transaction.dao.SubscriptionDAO;
import com.example.transaction.pojo.Subscription;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/25 19:55
 * @Content:
 */

@SpringBootTest
public class SubscriptionDAOTest {

    @Autowired
    private SubscriptionDAO subscriptionDAO;

    @Test
    void testGetAllSubscriptionByAccountId(){
        List<Subscription> subscriptions = subscriptionDAO.getAllSubscriptionByAccountId(1);
        System.out.println(subscriptions.toString());
    }
}
