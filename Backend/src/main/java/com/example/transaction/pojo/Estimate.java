package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 15:47
 * @Content: 信誉
 */

@Data
public class Estimate {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "success_rate")
    private Double successRate;
    @TableField(value = "purchase_credit")
    private Double purchaseCredit;
    @TableField(value = "sell_credit")
    private Double sellCredit;
    @TableField(value = "account_id")
    private Integer accountId;

    @TableField(exist = false)
    private Account user;
}
