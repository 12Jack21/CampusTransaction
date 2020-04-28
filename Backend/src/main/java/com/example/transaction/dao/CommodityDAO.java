package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Notice;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/25 9:14
 * @Content: 商品表数据层
 */

@Repository
public interface CommodityDAO extends BaseMapper<Commodity> {
    List<Commodity> selectAllInfo();
    // 利用queryWrapper查找
    List<Commodity> selectWithCondition(@Param("ew") QueryWrapper<Commodity> wrapper);

    /*查询查询完整商品信息，包括notice*/
    @Results(id = "commodity-notice-map", value = {
        @Result(property = "notice", column = "notice_id", javaType = Notice.class, one = @One(
                select = "com.example.transaction.dao.NoticeDAO.selectById"
        ))
    })
    @Select("select * from acc_notify where account_id=#{id}")
    Commodity selectWithAllInfoById(Integer Id);
}
