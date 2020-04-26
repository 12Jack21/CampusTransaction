package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.Comment;

import java.util.List;

/**
 * @InterfaceName: CommentDao
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/25 15:45
 */
public interface CommentDAO extends BaseMapper<Comment> {

    /*获取分页*/

    /*评论不可删除*/
}
