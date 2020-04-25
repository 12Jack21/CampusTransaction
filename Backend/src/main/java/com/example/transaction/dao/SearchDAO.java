package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.Search;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 高战立
        * @Date: 2020/4/25 6:39
        * @Content: 历史记录表业务层
        */

@Repository
public interface SearchDAO extends BaseMapper<Search> {

    /*删除用户的所有搜索记录
    * delete()+queryWrapper
    * */

    @Select("select * from search where account_id = #{id}")
    List<Search> getAllSearchByAccountId(Integer id);
}
