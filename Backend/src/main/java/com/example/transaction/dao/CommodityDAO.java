package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.pojo.Commodity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.example.transaction.pojo.Notice;
import com.example.transaction.pojo.Notify;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/25 9:14
 * @Content: 商品表数据层
 */

@Repository
public interface CommodityDAO extends BaseMapper<Commodity> {

    @Select("select * from commodity where id = #{id}")
    Commodity getSimpleCommodityById(Integer id);

    @Results(id = "detailedCommodity_map", value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "noticeId", column = "notice_id"),
            @Result(property = "notice", column = "notice_id", javaType = Notice.class, one = @One(
                    select = "com.example.transaction.dao.NoticeDAO.selectById"
            )),
            @Result(property = "commodityImages", column = "id", javaType = List.class, many = @Many(
                    select = "com.example.transaction.dao.CommodityImageDAO.getAllImageByCommodityId"
            )),
            @Result(property = "types", column = "id", javaType = List.class, many = @Many(
                    select = "com.example.transaction.dao.TypeDAO.getAllTypeByCommodityId"
            ))
    })
    @Select("select * from commodity where id = #{id}")
    Commodity getDetailedCommodityById(Integer id);

    @Results(id = "detailedCommodity_map2", value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "commodityImages", column = "id", javaType = List.class, many = @Many(
                    select = "com.example.transaction.dao.CommodityImageDAO.getAllImageByCommodityId"
            )),
            @Result(property = "types", column = "id", javaType = List.class, many = @Many(
                    select = "com.example.transaction.dao.TypeDAO.getAllTypeByCommodityId"
            ))
    })
    @Select("select * from commodity where notice_id = #{noticeId}")
    List<Commodity> getDetailedCommodityByNoticeId(Integer noticeId);

    //利用queryWrapper查找
    List<Commodity> selectWithCondition(@Param("ew") QueryWrapper<Commodity> wrapper);
    //商品名模糊分页查询，新旧程度排序
    IPage<Commodity> sortByNewness(Page<?> page, String name);
    //商品类型分页查询
    IPage<Commodity> sortByType(Page<?> page, Integer typeId);
    //商品价格区间分页查询
    IPage<Commodity> betweenPrice(Page<?> page, String name, Integer low, Integer high);
    //商品名模糊分页查询, 所有者信誉排序
    IPage<Commodity> sortByCredit(Page<?> page, String name);


}
