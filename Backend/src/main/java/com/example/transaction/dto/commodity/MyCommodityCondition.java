package com.example.transaction.dto.commodity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName: MyCommodityCondition
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/5/28 1:01
 */
@Data
public class MyCommodityCondition {
    Integer pageIndex;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    Date endTime;
    Integer type;
}
