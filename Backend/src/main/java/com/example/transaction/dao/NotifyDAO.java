package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.AccountNotify;
import com.example.transaction.pojo.Notify;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/25 18:42
 * @Content:
 */

@Repository
public interface NotifyDAO extends BaseMapper<Notify> {
}
