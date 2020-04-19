package com.example.transaction.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Notice;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountDAO extends BaseMapper<Account> {

    @Results(id = "noticeMap", value = {
            @Result(id = true, property = "id",column = "id"), // cannot ignore property
            @Result(property = "noticeList", column = "id", many = @Many(
                    select = "com.example.transaction.dao.NoticeDAO.getByOwnerId"
            ))
    })
    @Select("SELECT * FROM account")
    List<Account> getAllAccount();

    @Select("select * from account where password = #{pass}")
    List<Account> getByPassword(String pass);


}

