package com.example.transaction.dto.notify;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName: NotifyCondition
 * @Description: notify查询条件
 * @Author: 曾志昊
 * @Date: 2020/5/20 17:15
 */
@Data
public class NotifyCondition {
    Integer pageIndex;
    Integer type;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date endTime;

    Integer accountId;

}
