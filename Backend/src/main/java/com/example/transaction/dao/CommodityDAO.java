package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dto.Condition;
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
            @Result(property = "noticeId", column = "notice_id"),
            @Result(property = "commodityImages", column = "id", javaType = List.class, many = @Many(
                    select = "com.example.transaction.dao.CommodityImageDAO.getAllImageByCommodityId"
            ))
    })
    @Select("select * from commodity where notice_id = #{noticeId}")
    Commodity getDetailedCommodityWithoutNoticeById(Integer noticeId);


    @Results(id = "detailedCommodity_map3", value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "commodityImages", column = "id", javaType = List.class, many = @Many(
                    select = "com.example.transaction.dao.CommodityImageDAO.getAllImageByCommodityId"
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

    /*搜索*/
    @ResultMap(value = "detailedCommodity_map2")
//    @Select("select * from commodity c, notice n, type t ${ew.customSqlSegment} and ")
    @Select("select * from commodity c, notice n ${ew.customSqlSegment} and c.notice_id = n.id  ORDER BY n.create_time DESC ")
    IPage<Commodity> search(Page<?> page, @Param("ew") QueryWrapper<Commodity> wrapper);

    /*搜索*/
    /*价格低到高*/
    @ResultMap(value = "detailedCommodity_map2")
//    @Select("select * from commodity c, notice n, type t ${ew.customSqlSegment} and ")
    @Select("select * from commodity c, notice n ${ew.customSqlSegment} and c.notice_id = n.id  ORDER BY n.create_time DESC,c.expected_price ASC")
    IPage<Commodity> searchPriceASC(Page<?> page, @Param("ew") QueryWrapper<Commodity> wrapper);

    /*搜索*/
    /*失效时间少到多*/
    @ResultMap(value = "detailedCommodity_map2")
//    @Select("select * from commodity c, notice n, type t ${ew.customSqlSegment} and ")
    @Select("select * from commodity c, notice n ${ew.customSqlSegment} and c.notice_id = n.id  ORDER BY n.create_time DESC,n.end_time DESC")
    IPage<Commodity> searchEndTimeDESC(Page<?> page, @Param("ew") QueryWrapper<Commodity> wrapper);

    /*搜索*/
    /*信誉低到高*/
    @ResultMap(value = "detailedCommodity_map2")
//    @Select("select * from commodity c, notice n, type t ${ew.customSqlSegment} and ")
    @Select("select * from commodity c, notice n, account a,estimate e ${ew.customSqlSegment} and c.notice_id = n.id and a.id = e.account_id ORDER BY n.create_time DESC,n.end_time ASC,e.success_rate ASC")
    IPage<Commodity> searchEstimateASC(Page<?> page, @Param("ew") QueryWrapper<Commodity> wrapper);

}
