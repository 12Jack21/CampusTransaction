package com.example.transaction.daoTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.CommentDAO;
import com.example.transaction.pojo.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    void testGetCommentWithAccountInfo(){
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("commodity_id",1);
        Page<Comment> page = new Page<>(1,1);
        IPage<Comment> iPage = commentDAO.getCommentWithAccountInfo(page, queryWrapper);
        System.out.println(iPage.getRecords());
    }
}
