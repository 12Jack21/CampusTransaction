package com.example.transaction.util;

import com.example.transaction.dao.CommodityDAO;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Commodity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.servlet.http.HttpSession;

/**
 * @ClassName: AccountVerify
 * @Description: 验证用户是否请求查看或操作其他用户的信息
 * @Author: 曾志昊
 * @Date: 2020/4/26 15:57
 */
public class AccountVerify {
    @Autowired
    CommodityDAO commodityDAO;

    /**
     * 验证当前用户是否是登录用户
     * @param account
     * @param session
     * @return
     */
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

    public static Account verifyWithReturn( Account account,HttpSession session){
        if(account==null)
            return null;
        Account currentAccount = (Account) session.getAttribute("currentAccount");
        if(account.getId()!=null&&currentAccount.getId()!=currentAccount.getId()){
            /*非法操作*/
            return null;
        }
        else{
            /*当用户id为空时*/
            return currentAccount;
        }
    }

    public static boolean verifySellerByCommodityId(responseFromServer responseFromServer,HttpSession session){
        if(responseFromServer.isSuccess()){
            Commodity commodity = (Commodity) responseFromServer.getData();
            if(commodity == null||commodity.getNotice() == null||commodity.getNotice().getAccountId()==null)
                return false;
            Account account = new Account(commodity.getNotice().getAccountId());
            if(!verify(account,session))
                return false;
            return true;
        }
        return false;

    }
}
