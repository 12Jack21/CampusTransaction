package com.example.transaction.daoTest;

import com.example.transaction.dao.SearchDAO;
import com.example.transaction.pojo.Search;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/25 6:42
 * @Content: SearchDAO测试
 */

@SpringBootTest
public class SearchDAOTest {

    @Autowired
    private SearchDAO searchDAO;

    @Test
    void testGetAllSearchByAccountId(){
        List<Search> list = searchDAO.getAllSearchByAccountId(1);
        System.out.println(list.get(0));
    }
}
