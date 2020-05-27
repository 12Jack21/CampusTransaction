package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.transaction.dto.commodity.CommodityInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.netty.util.internal.StringUtil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
public class Notice implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Boolean type;
    private String address;
    @TableField(value = "detailed_address")
    private String detailedAddress;

    //    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField(value = "end_time")
    private Date endTime;
    private String title;
    private String condition;
    private String description;
    @TableField(value = "browse_count")
    private Integer browseCount;

    @TableField(value = "state_enum")
    private Integer stateEnum;

    //    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField(value = "update_time")
    private Date updateTime;

    //    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField(value = "create_time")
    private Date createTime;
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

    public void rectifyNotify() {
        type = type.equals(0) || type.equals(1) ? type : null;
        address = StringUtil.isNullOrEmpty(address) ? null : address;
        detailedAddress = StringUtil.isNullOrEmpty(detailedAddress) ? null : detailedAddress;
        title = StringUtil.isNullOrEmpty(title) ? null : title;
        condition = StringUtil.isNullOrEmpty(condition) ? null : condition;
        description = StringUtil.isNullOrEmpty(description) ? null : description;
        accountId = accountId < 0 ? null : accountId;
        stateEnum = stateEnum <= 1 && stateEnum >= -2 ? stateEnum : null;
    }
}
