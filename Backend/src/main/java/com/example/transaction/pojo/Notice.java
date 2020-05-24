package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.transaction.dto.commodity.CommodityInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class Notice implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Boolean type;
    private String address;
    @TableField(value = "detailed_address")
    private String detailedAddress;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
    @TableField(value = "end_time")
    private Timestamp endTime;
    private String title;
    private String condition;
    private String description;
    @TableField(value = "state_enum")
    private Integer stateEnum;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
    @TableField(value = "update_time")
    private Timestamp updateTime;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
    @TableField(value = "create_time")
    private Timestamp createTime;
    @TableField(value = "account_id")
    private Integer accountId;

    /**
     * ZZH
     * TODO : 返回的commodity类型,属性?
     */
    @TableField(exist = false)
    private List<Commodity> comList;

    @TableField(exist = false)
    private List<CommodityInfo> comInfoList;
    @TableField(exist = false)
    private Account user;

    public Notice() {
    }

    public Notice(Integer id) {
        this.id = id;
    }
}
