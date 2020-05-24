package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Estimate;
import com.example.transaction.dto.account.SimpleAccount;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDAO extends BaseMapper<Account> {
    //根据用户id查询账号信息，涉及多表查询
    Account getAccountCreditById(Integer id);

    @Results(id = "accountEstimateInfo", value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "estimate", column = "id", javaType = Estimate.class, one = @One(
                    select = "com.example.transaction.dao.EstimateDAO.getByAccountId"
            ))
    })
    @Select("select * from account where id = #{id}")
    //该函数附带信用等信息，和上面的getAccountCreditById返回结果相同
    Account getAccountWithEstimate(Integer id);

    @Results(id = "accountEstimateInfo", value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "estimate", column = "id", javaType = Estimate.class, one = @One(
                    select = "com.example.transaction.dao.EstimateDAO.getByAccountId"
            ))
    })
    @Select("select * from account where id = #{id}")
    SimpleAccount getSimpleAccountById(Integer id);
}

