package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.CommodityList;
import com.example.transaction.pojo.Notice;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/25 7:08
 * @Content: CommodityList数据层
 */

@Repository
public interface CommodityListDAO extends BaseMapper<CommodityList> {
    @Results(id = "commodityInfo", value = {
            @Result(property = "commodity", column = "commodity_id", javaType = Commodity.class, one = @One(
                    select = "com.example.transaction.dao.CommodityDAO.selectById"
            ))
    })
    @Select("select * from com_list where notice_id=#{id}")
    List<CommodityList> getAllCommodityListByNoticeId(Integer id);

    @Results(id = "noticeInfo", value = {
            @Result(property = "notice", column = "notice_id", javaType = Notice.class, one = @One(
                    select = "com.example.transaction.dao.NoticeDAO.getCreditByNoticeId"
            ))
    })
    @Select("select * from com_list where commodity_id=#{id}")
    CommodityList getAllByCommodityId(Integer id);
}
