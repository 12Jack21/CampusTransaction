package com.example.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.AccountDAO;
import com.example.transaction.dao.CommentDAO;
import com.example.transaction.dto.account.SimpleAccount;
import com.example.transaction.dto.comment.SimpleComment;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.AccountNotify;
import com.example.transaction.pojo.Comment;
import com.example.transaction.service.CommentService;
import com.example.transaction.service.NotifyService;
import com.example.transaction.util.MyPage;
import com.example.transaction.util.code.Nums;
import com.example.transaction.util.code.NotifyActionCode;
import com.example.transaction.util.code.NotifyTargetCode;
import com.example.transaction.util.code.ResourcePath;
import com.example.transaction.util.responseFromServer;
import org.apache.ibatis.annotations.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;

/**
 * @ClassName: CommentServiceImpl
 * @Author: 曾志昊
 * @Date: 2020/4/29 1:43
 */
@Service("CommentService")
public class CommentServiceImpl implements CommentService {
    CommentDAO commentDAO;
    AccountDAO accountDAO;
    NotifyService notifyService;

    @Autowired
    public CommentServiceImpl(CommentDAO commentDAO, AccountDAO accountDAO, NotifyService notifyService) {
        this.accountDAO = accountDAO;
        this.commentDAO = commentDAO;
        this.notifyService = notifyService;
    }

    /**
     * 获取关于商品的评论
     * @param pageIndex 页数
     * @param commodityId 商品id
     * @return 执行结果
     */
    @Override
    public responseFromServer  getCommentByCommodityId(Integer pageIndex, Integer commodityId){
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("commodity_id", commodityId);
        queryWrapper.orderByAsc("time");
//        queryWrapper.orderByDesc("time");
        Page<Comment> page = new Page<>(pageIndex, 100);
        IPage<Comment> iPage = commentDAO.getCommentWithAccountInfo(page, queryWrapper);
        MyPage myPage = new MyPage(iPage);
        return responseFromServer.success(myPage);
    }

    /**
     * 发布评论
     * @param comment 评论
     * @return 执行结果
     */
    @Override
    @Transactional
    @Options(useGeneratedKeys = true)
    public responseFromServer sendComment(Comment comment) {
        if (comment.getToId() == null || comment.getContent() == null || comment.getCommodityId() == null) {
            return responseFromServer.error();
        }

        if(comment.getDate()==null){
            comment.setDate(new Date());
        }

        if(comment.getToId()==-1){
            comment.setToId(null);
        }

        if (commentDAO.insert(comment) != 1) {
            /*回滚事务*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }

        /*添加到notify*/
        AccountNotify accountNotify = new AccountNotify(
                comment.getFromId(),
                comment.getToId(),
                NotifyTargetCode.COMMODITY.getCode(),
                comment.getCommodityId(),
                comment.getToId() == null ?
                        NotifyActionCode.COMMENTS.getCode() :
                        NotifyActionCode.REPLIES.getCode()
        );

        Account account = accountDAO.selectById(comment.getFromId());
        accountNotify.getNotify().setContent("用户" + account.getUsername() + "向你发送了一条评论");
        if (notifyService.insertAccountNotify(accountNotify).isFailure()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        SimpleAccount sender = new SimpleAccount(accountDAO.selectById(comment.getFromId()));
        comment.setSender(sender);
        if(comment.getToId()!=null&&comment.getToId()>0){
            comment.setReceiver(new SimpleAccount(accountDAO.selectById(comment.getToId())));
        }
        comment.getSender().setAvatar(sender.getAvatar());
        return responseFromServer.success(new SimpleComment(comment));
    }

    //删除评论
    @Override
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
