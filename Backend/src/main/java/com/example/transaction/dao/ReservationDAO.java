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
//            @Result(property = "user", column = "account_id", javaType = Account.class, one = @One(
//                    select = "com.example.transaction.dao.AccountDAO.selectById"
//            )),
            @Result(property = "commodity", column = "commodity_id", javaType = Commodity.class, one = @One(
                    select = "com.example.transaction.dao.CommodityDAO.selectById"
            ))
    })
    @Select("select * from reservation where account_id=#{id}")
    List<Reservation> getAllReservationByAccountId(Integer id);

    @Results(id = "userMap", value = {
            @Result(property = "user", column = "account_id", javaType = Account.class, one = @One(
                    select = "com.example.transaction.dao.AccountDAO.getAccountCreditById"
            ))
    })
    @Select("select * from reservation where commodity_id=#{id}")
    List<Reservation> getAllReservationByCommodityId(Integer id);

    @Select("select * from reservation ${ew.customSqlSegment}")
    @ResultMap(value = {"reservationMap"}) //复用上述外键查找
    List<Reservation> getWithCondition(@Param("ew")QueryWrapper<Reservation> queryWrapper);

    /**
     * 查询商品，包括notice
     * @param id
     * @return
     */
    @Select("select from reservation where id = #{id}")
    @Results(
            id = "reservation-detailedCommodity-map",value = {
                    @Result(property = "commodity",column = "commodity_id",javaType = Commodity.class, one = @One(
                            select = "com.examp;e.transaction.dao.CommodityDAO.selectWithAllInfoById"
                    ))
    })
    Reservation selectWithDetailedCommodityById(Integer id);

    @Select("select * from reservation r,commodity c,notice n where" +
            "r.commodity_id = c.id and c.notice_id = n.id and n.account_id = #{id}")
    IPage<Reservation> getReservationRequestPage(Page<?> page, Integer id);
}
