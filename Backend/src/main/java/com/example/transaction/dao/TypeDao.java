package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.Type;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 20:56
 * @Content: Type表业务层
 */

@Repository
public interface TypeDao extends BaseMapper<Type> {

    @Select("select * from type where commodity_id = #{id}")
    List<Type> getAllTypeByCommodityId(Integer id);
}
