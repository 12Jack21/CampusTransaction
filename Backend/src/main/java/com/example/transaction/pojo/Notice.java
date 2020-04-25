package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

@Data
public class Notice {
    private int id;
    private boolean type;
    private Timestamp endTime;
    private String title;
    private String conditions;
    private String description;
    private Integer stateEnum;
    private Timestamp updateTime;
    private Timestamp createTime;
    private Integer accountId;

    @TableField(exist = false)
    private List<CommodityList> commodityLists;
}
