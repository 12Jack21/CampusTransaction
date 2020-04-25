package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Notice;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountDAO extends BaseMapper<Account> {
    //获取用户所有通告记录
    Account getAllNotice(Integer id);
    //获取某用户所有预约记录
    Account getAllReservation(Integer id);
}

