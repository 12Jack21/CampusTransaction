package com.example.transaction.util;

import com.example.transaction.pojo.Account;

import javax.servlet.http.HttpSession;

/**
 * @ClassName: AccountVerify
 * @Description: 验证用户是否请求查看或操作其他用户的信息
 * @Author: 曾志昊
 * @Date: 2020/4/26 15:57
 */
public class AccountVerify {
    public static boolean verify( Account account,HttpSession session){
        if(account==null)
            return false;
        Account currentAccount = (Account) session.getAttribute("currentAccount");
        if(account.getId()!=null&&currentAccount.getId()!=currentAccount.getId()){
            /*非法操作*/
            return false;
        }
        else{
            /*当用户id为空时*/
            account.setId(currentAccount.getId());
            return true;
        }
    }
}
