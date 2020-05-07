package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Reservation;
import com.example.transaction.pojo.Token;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @InterfaceName: TokenDAO
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/5/7 16:51
 */
@Repository
public interface TokenDAO extends BaseMapper<Token> {
    @Results(value = {
            @Result(id = true, property = "accountId", column = "account_id"),
            @Result(
                    column = "account_id", property = "account", javaType = Account.class, one = @One(
                    select = "com.example.transaction.dao.AccountDAO.selectById"
            ))
    })
    @Select("select * from token where account_id = #{accountId}")
    Token getTokenByAccountId(Integer accountId);

    @Results(value = {
            @Result(id = true, property = "accountId", column = "account_id"),
            @Result(
                    column = "account_id", property = "account", javaType = Account.class, one = @One(
                    select = "com.example.transaction.dao.AccountDAO.selectById"
            ))
    })
    @Select("select * from token where token_str = #{tokenStr}")
    Token getTokenByTokenStr(String tokenStr);
}
