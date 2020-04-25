package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.CommodityImage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 20:24
 * @Content: CommodityImage表业务层
 */

@Repository
public interface CommodityImageDAO extends BaseMapper<CommodityImage> {
    @Select("select * from com_image where commodity_id = #{id}")
    List<CommodityImage> getAllImageByCommodityId(Integer id);


}
