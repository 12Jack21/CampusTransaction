package com.example.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.CommentDAO;
import com.example.transaction.pojo.Comment;
import com.example.transaction.pojo.Notify;
import com.example.transaction.service.CommentService;
import com.example.transaction.util.MyPage;
import com.example.transaction.util.Nums;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: CommentServiceImpl
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/29 1:43
 */
@Service("CommentService")
public class CommentServiceImpl implements CommentService {

    public responseFromServer getCommentPage(QueryWrapper queryWrapper, Integer pageIndex){
        Page<Comment> page = new Page<>(pageIndex, Nums.pageSize);
        IPage<Comment> notifyIPage = commentDAO.selectPage(page,queryWrapper);
        MyPage myPage = new MyPage(notifyIPage);
        return responseFromServer.success(myPage);
    }

    public responseFromServer getMyComment(Integer accountId, Integer pageIndex){
        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.and(Wrapper -> Wrapper.eq("from_id",accountId).or().eq("to_id",accountId));
//        queryWrapper.eq("from_id",accountId).or(i->i.eq("to_id",accountId));
        /*todo or用不了*/
        queryWrapper.orderByDesc("id");
        return getCommentPage(queryWrapper,pageIndex);
    }

    public responseFromServer getCommentForCommodity(Integer commodityId){
        return null;
    }


    private CommentDAO commentDAO;

    @Autowired
    public CommentServiceImpl(CommentDAO commentDAO){
        this.commentDAO = commentDAO;
    }

}
