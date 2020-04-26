package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.Commodity;
import org.apache.ibatis.annotations.Param;
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
}
