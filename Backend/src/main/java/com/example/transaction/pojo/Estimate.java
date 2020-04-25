package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 15:47
 * @Content: 信誉
 */

@Data
public class Estimate {
    private Integer id;
    private Double successRate;
    private Double purchaseCredit;
    private Double sellCredit;

    @TableField(exist = false)
    private Account user;
}
