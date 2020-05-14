package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.CommodityImage;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 20:24
 * @Content: CommodityImage表数据层
 */

@Repository
public interface CommodityImageDAO extends BaseMapper<CommodityImage> {

    @Select("select * from com_image where commodity_id = #{id}")
    List<CommodityImage> getAllImageByCommodityId(Integer id);

    @Update("update com_image set commodity_id = #{commodityId} where image_url = #{url} ")
    Integer updateByUrl(String url, Integer commodityId);


}
