package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dto.notify.SimpleNotify;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.AccountNotify;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Notify;
import org.apache.ibatis.annotations.*;
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
            @Result(property = "notifyId", column = "notify_id"),
            @Result(property = "notify", column = "notify_id", javaType = Notify.class, one = @One(
                    select = "com.example.transaction.dao.NotifyDAO.selectById"
            ))
    })
    @Select("select * from acc_notify where account_id = #{account_id} and is_read = false order by create_time DESC")
    List<AccountNotify> getUnreadNotifyByAccountId(Integer accountId);

    @ResultMap({"notifyInfo"})
    @Select("select * from acc_notify where account_id = #{account_id} order by create_time DESC")
    List<AccountNotify> getAllNotifyByAccountId(Integer accountId);

    @ResultMap({"notifyInfo"})
    @Select("select * from notify acc_notify where id = #{id} ")
    AccountNotify getDetailedNotifyById(Integer id);

    @Select("select * from notify acc_notify where notify_id = #{id} ")
    AccountNotify getSimpleNotifyByNotifyId(Integer id);


    @Update("update acc_notify set is_read = true where id = #{id}")
    int setNotifyRead(Integer id);

    @Results(
            @Result(property = "notify", column = "notify_id", javaType = Notify.class, one = @One(
                    select = "com.example.transaction.dao.NotifyDAO.selectById"
            ))
    )
    @Select("select * from notify,acc_notify ${ew.customSqlSegment}")
    IPage<AccountNotify> getNotifyPage(Page<?> page, @Param("ew") QueryWrapper<Commodity> wrapper);


    @Results(
            @Result(property = "notify", column = "notify_id", javaType = Notify.class, one = @One(
                    select = "com.example.transaction.dao.NotifyDAO.selectNotifyWithSimpleAccountById"
            ))
    )
    @Select("select * from acc_notify,notify  ${ew.customSqlSegment} and acc_notify.notify_id = notify.id order by  acc_notify.is_read ASC,acc_notify.create_time desc")
    IPage<AccountNotify> searchNotify(Page<?> page, @Param("ew") QueryWrapper<Commodity> wrapper);
}

