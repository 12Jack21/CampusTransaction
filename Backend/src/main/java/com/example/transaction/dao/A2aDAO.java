package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.A2a;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Notice;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @InterfaceName: A2aDAO
 * @Author: 曾志昊
 * @Date: 2020/4/30 14:28
 */
@Repository
public interface A2aDAO extends BaseMapper<A2a> {
    @Results(id = "a2a_map", value = {
//            @Result(property = "account1", column = "account_id_1", javaType = Account.class, one = @One(
//                    select = "com.example.transaction.dao.AccountDAO.selectById"
//            )),
            @Result(property = "account2", column = "account_id_2", javaType = Account.class, one = @One(
                    select = "com.example.transaction.dao.AccountDAO.getAccountWithEstimate"
            ))
    })
    @Select("select * from a2a where account_id_1 = #{accountId1} and account_id_2 = #{accountId2}")
    A2a getA2a(Integer accountId1, Integer accountId2);




}
