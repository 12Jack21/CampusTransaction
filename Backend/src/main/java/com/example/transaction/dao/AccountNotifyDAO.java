package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.AccountNotify;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Notify;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/25 18:40
 * @Content:
 */

@Repository
public interface AccountNotifyDAO extends BaseMapper<AccountNotify> {
    @Results(id = "notifyInfo", value = {
            @Result(property = "notify", column = "notify_id", javaType = Notify.class, one = @One(
                    select = "com.example.transaction.dao.NotifyDAO.selectById"
            ))
    })
    @Select("select * from acc_notify where notify_id=#{id}")
    List<AccountNotify> getAllByNotifyId(Integer id);

    @Results(id = "accountInfo", value = {
            @Result(property = "owner", column = "account_id", javaType = Account.class, one = @One(
                    select = "com.example.transaction.dao.AccountDAO.selectById"
            ))
    })
    @Select("select * from acc_notify where account_id=#{id}")
    List<AccountNotify> getAllByAccountId(Integer id);
}
