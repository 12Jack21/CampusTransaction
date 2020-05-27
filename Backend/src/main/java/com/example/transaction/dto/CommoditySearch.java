package com.example.transaction.dto;

import com.example.transaction.dto.commodity.Pagination;
import lombok.Data;

/**
 * @ClassName: CommoditySearch
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/5/21 18:16
 */
@Data
public class CommoditySearch {
    Condition condition;
    Pagination pagination;
    String keyword;
}
