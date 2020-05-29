package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 15:47
 * @Content: 信誉
 */

@Data
public class Estimate implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField(value = "success_rate")
    private Double successRate;
    @TableField(value = "purchase_credit")
    private Double purchaseCredit;
    @TableField(value = "credit")
    private Double credit;
    @TableField(value = "account_id")
    private Integer accountId;
    @TableField(value = "reservation_count")
    private Double reservationCount;
    @TableField(value = "success_count")
    private Double successCount;
    @TableField(value = "grading_count")
    private Double gradingCount;
    @TableField(value = "grading_mass")
    private Double gradingMass;

    public Double getCredit(){
        if(gradingCount!=null && gradingCount >0 && gradingMass != null && gradingMass >= 0){
            return gradingMass/gradingCount;
        }else{
            return 0.75;
        }
    }


    public Double getSuccessRate(){
        if(reservationCount!=null && reservationCount >0 && successCount != null && successCount >= 0){
            return successCount/reservationCount;
        }else{
            return 0.75;
        }
    }

}
