package com.example.transaction.dto.account;

import com.example.transaction.pojo.Account;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: AccountInfo
 * @Description: 用户信息
 * @Author: 曾志昊
 * @Date: 2020/5/24 20:41
 */
@Data
public class AccountInfo extends SimpleAccount implements Serializable {
    String qq = "";
    String weChat = "";
    Double rate = -1.0D;
    Double evaluation = -1.0D;

    public AccountInfo(){
        super();
    }
    public AccountInfo(Account account){
        super(account);
        this.qq = account.getQq();
        this.weChat = account.getWechat();
        if(account.getEstimate()!=null){
            this.rate = account.getEstimate().getSuccessRate();
            this.evaluation = account.getEstimate().getCredit();
        }
    }
}
