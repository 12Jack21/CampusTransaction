package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.Subscription;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/25 18:36
 * @Content: 订阅表业务层
 */

@Repository
public interface SubscriptionDAO  extends BaseMapper<Subscription> {
    @Select("select * from subscription where account_id = #{id}")
    List<Subscription> getAllSubscriptionByAccountId(Integer id);
}
