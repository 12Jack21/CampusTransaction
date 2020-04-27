package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.Estimate;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @InterfaceName: EstimateDAO
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/25 19:51
 */

@Repository
public interface EstimateDAO extends BaseMapper<Estimate> {
    @Select("select * from estimate where account_id=#{id}")
    Estimate getByAccountId(Integer id); //多表查询会用到，不能被替代
}
