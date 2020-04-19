package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Notice;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeDAO extends BaseMapper<Notice> {
    @Results(id = "noticeMap", value = {
            @Result(id = true, column = "id"), // can ignore property without duplicated string definitions
            @Result(property = "owner", column = "owner_id", javaType = Account.class,one = @One(
                    select = "com.example.transaction.dao.AccountDAO.selectById"
            ))
    })
    @Select("SELECT * FROM notice WHERE 1=1")
    List<Notice> getAllNotice(@Param("ew")QueryWrapper<Notice> wrapper);

    int updateXML(Notice notice);

    @Select("SELECT * FROM notice WHERE owner_id=#{owner_id}")
    List<Notice> getByOwnerId(@Param("owner_id")int ownerId);
}
