package com.example.transaction.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName: A2a
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/30 14:31
 */
@Data
@TableName(value = "a2a")
public class A2a {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "account_id_1")
    private int accountId1;
    @TableField(value = "account_id_2")
    private int accountId2;

    @TableField(exist = false)
    private Account account1;
    @TableField(exist = false)
    private Account account2;

}
