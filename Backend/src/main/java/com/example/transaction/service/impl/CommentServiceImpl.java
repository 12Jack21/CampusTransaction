package com.example.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.CommentDAO;
import com.example.transaction.pojo.Comment;
import com.example.transaction.service.CommentService;
import com.example.transaction.util.MyPage;
import com.example.transaction.util.Nums;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * @ClassName: CommentServiceImpl
 * @Author: 曾志昊
 * @Date: 2020/4/29 1:43
 */
@Service("CommentService")
public class CommentServiceImpl implements CommentService {
    private final CommentDAO commentDAO;
    @Autowired
    public CommentServiceImpl(CommentDAO commentDAO){
        this.commentDAO = commentDAO;
    }

    /**
     * 获取关于商品的评论
     * @param pageIndex 页数
     * @param commodityId 商品id
     * @return 执行结果
     */
    public responseFromServer getCommentByCommodityId(Integer pageIndex, Integer commodityId){
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("commodity_id", commodityId);
        queryWrapper.orderByDesc("time");
        Page<Comment> page = new Page<>(pageIndex, Nums.pageSize);
        IPage<Comment> iPage = commentDAO.getCommentWithAccountInfo(page, queryWrapper);
        MyPage myPage = new MyPage(iPage);
        return responseFromServer.success(myPage);
    }

    /**
     * 发布评论
     * @param comment 评论
     * @return 执行结果
     */
    @Transactional
    public responseFromServer sendComment(Comment comment){
        if(comment.getToId()==null||comment.getContent()==null||comment.getCommodityId()==null)
            return responseFromServer.error();
        if(commentDAO.insert(comment) != 1){
            /*回滚事务*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        /*todo 插入notify提醒*/
        return  responseFromServer.success();
    }

    //删除评论
    @Transactional
    public responseFromServer deleteComment(Comment comment){
        if(commentDAO.deleteById(comment.getId()) != 1){
            /*回滚事务*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return  responseFromServer.success();
    }

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





}
