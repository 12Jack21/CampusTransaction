package com.example.transaction.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date endTime;

    public boolean recent = false;

    /**
     * 要使用的排序规则
     */
    public Integer sortType;

    /**
     * 地址
     */
    public String userAddress;

    /**
     * 搜索关键词
     */
    public String keyword;

    /**
     * 商品类别
     */
    public String type;

    /**
     * 价格区间
     */
    public Integer highPrice;
    public Integer lowPrice;

    /**
     * 还剩几天过期
     */
    public Integer outdated;
}
