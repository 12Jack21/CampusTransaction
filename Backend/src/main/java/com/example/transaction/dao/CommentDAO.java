package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @InterfaceName: CommentDao
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/25 15:45
 */

@Repository
public interface CommentDAO extends BaseMapper<Comment> {
    @Results(id="commentInfo", value = {
            @Result(property = "fromId", column = "from_id"),
            @Result(property = "toId", column = "to_id"),
            @Result(property = "sender", column = "from_id", javaType = Account.class, one = @One(
                    select = "com.example.transaction.dao.AccountDAO.getSimpleAccountById"
            )),
            @Result(property = "receiver", column = "to_id", javaType = Account.class, one = @One(
                    select = "com.example.transaction.dao.AccountDAO.getSimpleAccountById"
            ))
    })
    @Select("select * from comment ${ew.customSqlSegment}")
    IPage<Comment> getCommentWithAccountInfo(Page<?> page, @Param("ew") QueryWrapper<Comment> wrapper);
    /*获取分页*/


    /*评论不可删除*/
}
