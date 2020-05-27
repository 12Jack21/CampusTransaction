package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.pojo.Search;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/25 6:39
 * @Content: 历史记录表数据层
 */

@Repository
public interface SearchDAO extends BaseMapper<Search> {

    @Select("select * from search where account_id = #{id}")
    List<Search> getAllSearchByAccountId(Integer id);

    @Select("select * from search  ${ew.customSqlSegment}")
    IPage<Search> getSearchPage(Page<?> page, @Param("ew") QueryWrapper<Search> queryWrapper);


    @Delete("delete from search where account_id = #{accountId}")
    int deleteAllSearchByAccountId(Integer accountId);

    @Delete("delete from search where account_id = #{accountId} and id = #{searchId}")
    int deleteOneSearchByAccountId(Integer accountId, Integer searchId);
}
