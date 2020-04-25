package com.example.transaction.daoTest;

import com.example.transaction.dao.CommentDAO;
import com.example.transaction.pojo.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/25 20:20
 * @Content:
 */

@SpringBootTest
public class CommentDAOTest {
    @Autowired
    private CommentDAO commentDAO;

    @Test
    void testGetAllByCommodityId(){
        List<Comment> comments = commentDAO.getAllByCommodityId(1);
        System.out.println(comments);
    }
    @Test
    void testGetAllBySenderId(){
        List<Comment> comments = commentDAO.getAllBySenderId(1);
        System.out.println(comments);
    }
    @Test
    void testGetAllByToId(){
        List<Comment> comments = commentDAO.getAllByToId(2);
        System.out.println(comments);
    }
}
