package com.example.transaction.controller;

import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Comment;
import com.example.transaction.service.CommentService;
import com.example.transaction.service.impl.AccountVerify;
import com.example.transaction.util.responseFromServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ClassName: CommentController
 * @Author: 曾志昊
 * @Date: 2020/4/29 1:42
 */
@RestController
@RequestMapping("/comments")
@Api(tags = "CommentController")
public class CommentController {
    CommentService commentService;
    AccountVerify accountVerify;

    @Autowired
    public CommentController(CommentService commentService, AccountVerify accountVerify) {
        this.accountVerify = accountVerify;
        this.commentService = commentService;
    }

    /**
     * 获取关于商品的评论
     * @param map
     * @return
     */
//    @RequestMapping("/getCommentByCommodityId")
    @ApiOperation(value = "获取关于商品的评论")
    @ApiImplicitParam(name = "commodity_id", value = "商品Id",  paramType = "Integer", dataType = "Integer")
    @GetMapping("/commodity/{commodity_id}")
    //pageIndex以ParamRequest形式后缀
    public responseFromServer getCommentByCommodityId(@RequestBody Map<String,Object> map){
        Integer pageIndex = (Integer)map.get("pageIndex"),commodityId = (Integer)map.get("commodityId");
        if(pageIndex==null||pageIndex==0){
            pageIndex = 1;
        }
        if(commodityId == null){
            return responseFromServer.error();
        }
        return commentService.getCommentByCommodityId(pageIndex, commodityId);
    }

    /**
     * 发布评论
     * @param comment 评论
     * @param request HttpServletRequest
     * @return 执行结果
     */
//    @RequestMapping("/sendComment")
    @ApiOperation(value = "发布评论")
    @PostMapping
    public responseFromServer sendComment(@RequestBody Comment comment, HttpServletRequest request){
        Account account = new Account(comment.getFromId());
        if (!accountVerify.verify(account, request))
            return responseFromServer.illegal();
        return commentService.sendComment(comment);
    }

    /**
     * 删除评论
     * @param comment 评论
     * @param request HttpServletRequest
     * @return 执行结果
     */
//    @RequestMapping("/deleteComment")
    @ApiOperation(value = "删除评论")
    @DeleteMapping("/{comment_id}")
    public responseFromServer deleteComment(@RequestBody Comment comment, HttpServletRequest request){
        Account account = new Account(comment.getFromId());
        if (!accountVerify.verify(account, request))
            return responseFromServer.illegal();
        return commentService.deleteComment(comment);
    }
}
