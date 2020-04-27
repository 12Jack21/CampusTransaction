package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDAO extends BaseMapper<Account> {
    //根据用户id查询账号信息，涉及多表查询
    Account getAccountCreditById(Integer id);
}

