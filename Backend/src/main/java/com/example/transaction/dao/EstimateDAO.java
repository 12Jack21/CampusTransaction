package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.Estimate;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Author: 高战立
 * @Date: 2020/4/25 18:37
 * @Content: 评估表操作层
 */

@Repository

public interface EstimateDAO extends BaseMapper<Estimate> {
    @Select("select * from estimate where account_id=#{id}")
    Estimate getByAccountId(Integer id); //多表查询会用到，不能被替代
}
