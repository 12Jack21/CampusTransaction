package com.example.transaction.service;

import com.example.transaction.pojo.Comment;
import com.example.transaction.util.responseFromServer;

/**
 * @InterfaceName: CommentService
 * @Author: 曾志昊
 * @Date: 2020/4/29 1:43
 */
public interface CommentService {
    //发布评论、删除评论（发布者）、评论的提醒
    //获取关于商品的评论
    responseFromServer getCommentByCommodityId(Integer pageIndex, Integer commodityId);
    //发布评论
    responseFromServer sendComment(Comment comment);
    //删除评论
    responseFromServer deleteComment(Comment comment);
    //评论及回复提醒
}
