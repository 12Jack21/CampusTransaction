package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Estimate;
import com.example.transaction.pojo.SimpleAccount;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDAO extends BaseMapper<Account> {
    //根据用户id查询账号信息，涉及多表查询
    Account getAccountCreditById(Integer id);

    @Results(
            @Result(property = "estimate", column = "id", javaType = Estimate.class, one = @One(
                    select = "com.example.transaction.dao.EstimateDAO.getByAccountId"
            ))
    )
    @Select("select id,username,gender from account where id = #{id}")
    Account getAccountWithPublicInfoById(Integer id);

    @Results(
            @Result(property = "estimate", column = "id", javaType = Estimate.class, one = @One(
                    select = "com.example.transaction.dao.EstimateDAO.getByAccountId"
            ))
    )
    @Select("select * from account where id = #{id}")
    SimpleAccount getSimpleAccountById(Integer id);
}

