package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.A2a;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @InterfaceName: A2aDAO
 * @Author: 曾志昊
 * @Date: 2020/4/30 14:28
 */
public interface A2aDAO extends BaseMapper<A2a> {
    @Results(id = "a2a_map", value = {
            @Result(property = "notice", column = "account_id_1", javaType = Account.class, one = @One(
                    select = "com.example.transaction.dao.AccountDAO.selectById"
            )),
            @Result(property = "notice", column = "account_id_2", javaType = Account.class, one = @One(
                    select = "com.example.transaction.dao.AccountDAO.selectById"
            ))
    })
    @Select("select * from a2a ${ew.customSqlSegment}")
    A2a getA2a(@Param("ew") QueryWrapper<Commodity> wrapper);




}
