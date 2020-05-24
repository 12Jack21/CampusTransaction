package com.example.transaction.dto.commodity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName: Pagination
 * @Description: 分页搜索对象
 * @Author: 曾志昊
 * @Date: 2020/5/21 17:51
 */
@Data
public class Pagination {
    Integer pageIndex = -1;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    Date endTime;


}
