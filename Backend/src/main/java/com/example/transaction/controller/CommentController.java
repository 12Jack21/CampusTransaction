package com.example.transaction.controller;

import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Comment;
import com.example.transaction.service.CommentService;
import com.example.transaction.util.AccountVerify;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @ClassName: CommentController
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/29 1:42
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    /**
     * 获取关于商品的评论
     * @param pageIndex 页数
     * @param commodityId 商品id
     * @return 执行结果
     */
    @RequestMapping("/getComment")
    public responseFromServer getCommentByCommodityId(@RequestParam(value = "pageIndex") Integer pageIndex, @RequestParam(value = "commodityId") Integer commodityId){
        return commentService.getCommentByCommodityId(pageIndex, commodityId);
    }

    /**
     * 发布评论
     * @param comment 评论
     * @param session HttpSession
     * @return 执行结果
     */
    @RequestMapping("/sendComment")
    public responseFromServer sendComment(Comment comment, HttpSession session){
        Account account = new Account(comment.getFromId());
        if(!AccountVerify.verify(account,session))
            return responseFromServer.illegal();
        return commentService.sendComment(comment);
    }

    /**
     * 删除评论
     * @param comment 评论
     * @param session HttpSession
     * @return 执行结果
     */
    @RequestMapping("/deleteComment")
    public responseFromServer deleteComment(Comment comment, HttpSession session){
        Account account = new Account(comment.getFromId());
        if(!AccountVerify.verify(account,session))
            return responseFromServer.illegal();
        return commentService.deleteComment(comment);
    }
}
