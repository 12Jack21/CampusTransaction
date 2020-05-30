package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Reservation;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/25 14:55
 * @Content: 预约表数据层
 */

@Repository
public interface ReservationDAO  extends BaseMapper<Reservation> {

    @Results(id = "reservationMap", value = {
            @Result(property = "commodityId", column = "commodity_id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "user", column = "account_id", javaType = Account.class, one = @One(
                    select = "com.example.transaction.dao.AccountDAO.selectById"
            )),
            @Result(property = "commodity", column = "commodity_id", javaType = Commodity.class, one = @One(
                    select = "com.example.transaction.dao.CommodityDAO.selectById"
            ))
    })
    @Select("select * from reservation where account_id=#{id}")
    List<Reservation> getAllReservationByAccountId(Integer id);

    @ResultMap({"reservationMap"})
    @Select("select * from reservation where commodity_id=#{id}")
    List<Reservation> getAllReservationByCommodityId(Integer id);

    @ResultMap(value = {"reservationMap"}) //复用上述外键查找
    @Select("select * from reservation ${ew.customSqlSegment}")
    List<Reservation> getWithCondition(@Param("ew")QueryWrapper<Reservation> queryWrapper);

    @Results(id = "reservation-detailedCommodity-map",value = {
            @Result(property = "commodityId", column = "commodity_id"),
            /*这里用noticeId来查找commodity*/
            @Result(property = "commodity",column = "commodity_id",javaType = Commodity.class, one = @One(
                    select = "com.example.transaction.dao.CommodityDAO.getDetailedCommodityById"
            ))
    })
    @Select("select * from reservation where id = #{id}")
    Reservation selectWithDetailedCommodityById(Integer id);

    /*获取我收到的预约*/
    @Results(id = "reservation-detailedCommodity-map2",value = {
            @Result(property = "commodity", column = "commodity_id", javaType = Commodity.class, one = @One(
                    select = "com.example.transaction.dao.CommodityDAO.getDetailedCommodityWithoutNoticeById"
            ))
    })
    @Select("select * from reservation r,commodity c,notice n  ${ew.customSqlSegment} and " +
            "r.commodity_id = c.id and c.notice_id = n.id and n.account_id = #{id} order by r.update_time desc")
    IPage<Reservation> getReservationRequestPage(Page<?> page, Integer id,@Param("ew")QueryWrapper<Reservation> queryWrapper);

    @Select("select count(*) from reservation where commodity_id = #{commodity_id}")
    Integer getCountByCommodityId(Integer commodityId);


    /*获取我收到的预约*/
    @Select("select * from reservation r,commodity c,notice n where r.state_enum in (1,2)" +
            "r.commodity_id = c.id and c.notice_id = n.id and n.account_id = #{id} order by r.update_time desc")
    IPage<Reservation> getSuccessReservationRequestPage(Page<?> page, Integer id);


    @Update("update reservation set state_enum = 3 where commodity_id = #{commodityId} and count > #{count}")
    Integer failWaiting(Integer commodityId, Integer count);

    @Update("update reservation set state_enum = 3 where notice.id = #{noticeId} and notice.id = commodity.notice_id and commodity.id = reservation.commodity_id")
    Integer failWaiting(Integer noticeId);


    /*获取我收到的预约*/
    @Results(id = "reservation-detailedCommodity-map3",value = {
            @Result(property = "commodity", column = "commodity_id", javaType = Commodity.class, one = @One(
                    select = "com.example.transaction.dao.CommodityDAO.getDetailedCommodityById"
            ))
    })
    @Select("select * from reservation  ${ew.customSqlSegment} order by update_time desc")
    IPage<Reservation> getReservationPageQuery(Page<?> page, @Param("ew")QueryWrapper<Reservation> queryWrapper);


}
