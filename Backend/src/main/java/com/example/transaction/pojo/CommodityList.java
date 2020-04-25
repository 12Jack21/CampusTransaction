package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 15:41
 * @Content: 通告中包含的商品
 */

@Data
@TableName(value = "com_list")
public class CommodityList {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Integer count;
    @TableField(value = "initial_count")
    private Integer initialCount;
    @TableField(value = "commodity_id")
    private Integer commodityId;
    @TableField(value = "notice_id")
    private Integer noticeId;

    @TableField(exist = false)
    private Commodity commodity;
    @TableField(exist = false)
    private Notice notice;
}
