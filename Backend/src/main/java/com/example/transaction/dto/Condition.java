package com.example.transaction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Time;
import java.util.Date;

/**
 * @ClassName: Condition
 * @Description: 商品搜索条件
 * @Author: 曾志昊
 * @Date: 2020/5/11 16:49
 */
@Data
public class Condition {
    public Integer pageIndex;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
    public Date endTime;

    public boolean recent = false;

    /*要使用的排序规则*/
    public Integer sortType;

    /*地址*/
    public String userAddress;

    /*搜索关键词*/
    public String keyword;

    /*商品类别*/
    public Integer type;

    /*价格区间*/
    public Integer highPrice;
    public Integer lowPrice;

    /*还剩几天过期*/
    public Integer outdated;
}
