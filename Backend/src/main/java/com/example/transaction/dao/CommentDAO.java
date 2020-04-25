package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Comment;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Notify;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/25 18:41
 * @Content:
 */

@Repository
public interface CommentDAO extends BaseMapper<Comment> {
    @Results(id = "accountInfo", value = {
            @Result(property = "sender", column = "from_id", javaType = Account.class, one = @One(
                    select = "com.example.transaction.dao.AccountDAO.selectById"
            )),
            @Result(property = "receiver", column = "to_id", javaType = Account.class, one = @One(
                    select = "com.example.transaction.dao.AccountDAO.selectById"
            ))
    })
    @Select("select * from comment where commodity_id=#{id}")
    List<Comment> getAllByCommodityId(Integer id);

    @Results(id = "commodityInfo", value = {
            @Result(property = "commodity", column = "commodity_id", javaType = Commodity.class, one = @One(
                    select = "com.example.transaction.dao.CommodityDAO.selectById"
            ))
    })
    @Select("select * from comment where from_id=#{id}")
    List<Comment> getAllBySenderId(Integer id);

    @ResultMap(value = {"commodityInfo"})
    @Select("select * from comment where to_id=#{id}")
    List<Comment> getAllByToId(Integer id);
}
