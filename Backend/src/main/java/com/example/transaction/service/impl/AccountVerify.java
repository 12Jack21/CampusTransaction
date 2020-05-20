package com.example.transaction.service.impl;

import com.example.transaction.dao.AccountDAO;
import com.example.transaction.dao.CommodityDAO;
import com.example.transaction.dao.TokenDAO;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Token;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @ClassName: AccountVerify
 * @Description: 验证用户是否请求查看或操作其他用户的信息
 * @Author: 曾志昊
 * @Date: 2020/4/26 15:57
 */
@Service("AccountVerify")
public class AccountVerify {
    @Autowired
    CommodityDAO commodityDAO;
    @Autowired
    TokenDAO tokenDAO;
    @Autowired
    AccountDAO accountDAO;

    /*todo:先把所有的token验证都置为通过*/

    public Account getCurrentAccount(HttpServletRequest request) {
        final String headerToken = request.getHeader("token");
        Token token = tokenDAO.getTokenByTokenStr(headerToken);
        Account currentAccount = token == null ? null : token.getAccount();
        return currentAccount;
    }

    public boolean verify(Account account, HttpServletRequest request) {
        if (account == null) {
            return false;
        }
        final String headerToken = request.getHeader("token");
        Token token = tokenDAO.getTokenByTokenStr(headerToken);
        Account currentAccount = token == null ? null : token.getAccount();
        /*此时未登录*/
        if (currentAccount == null || currentAccount.getId() == null) {
            /**
             * ZZH
             * TODO : 不使用token时直接返回true
             */
            return true;
//            return false;
        }
        if (account.getId() != null && account.getId().intValue() != currentAccount.getId().intValue()) {
            /*非法操作*/
            return false;
        } else {
            /*当用户id为空时*/
            account.setId(currentAccount.getId());
            return true;
        }
    }

    public Account verifyWithReturn(Account account, HttpServletRequest request) {
        if (account == null) {
            return null;
        }
        final String headerToken = request.getHeader("token");
/*        Token token = tokenDAO.getTokenByTokenStr(headerToken);
        Account currentAccount = token == null?null:token.getAccount();*/
        Account currentAccount = accountDAO.selectById(account.getId());
        /**
         * ZZH
         * TODO : 不使用token时直接从数据库中根据accountid查询信息并且返回
         */
        if (currentAccount == null || (account.getId() != null && !currentAccount.getId().equals(currentAccount.getId()))) {
            /*非法操作*/
            return null;
        } else {
            /*当用户id为空时*/
            return currentAccount;
        }
    }

    public boolean verifySellerByCommodityId(responseFromServer responseFromServer, HttpServletRequest request) {
        if (responseFromServer.isSuccess()) {
            Commodity commodity = (Commodity) responseFromServer.getData();
            if (commodity != null && commodity.getNotice() != null && commodity.getNotice().getAccountId() != null) {
                Account account = new Account(commodity.getNotice().getAccountId());
                return verify(account, request);
            } else {
                return false;
            }
        }
        return false;

    }


    /**
     * 验证当前用户是否是登录用户
     *
     * @param account
     * @param session
     * @return
     */
    public boolean verify(Account account, HttpSession session) {
        if (account == null) {
            return false;
        }
        Account currentAccount = (Account) session.getAttribute("currentAccount");

        /*此时未登录*/
        if (currentAccount == null || currentAccount.getId() == null) {
            return false;
        }

        if (account.getId() != null && account.getId().intValue() != currentAccount.getId().intValue()) {
            /*非法操作*/
            return false;
        } else {
            /*当用户id为空时*/
            account.setId(currentAccount.getId());
            return true;
        }
    }

    public static Account verifyWithReturn(Account account, HttpSession session) {
        if (account == null) {
            return null;
        }
        Account currentAccount = (Account) session.getAttribute("currentAccount");

        if (currentAccount == null || account.getId() != null && currentAccount.getId() != currentAccount.getId()) {
            /*非法操作*/
            return null;
        } else {
            /*当用户id为空时*/
            return currentAccount;
        }
    }

    public boolean verifySellerByCommodityId(responseFromServer responseFromServer, HttpSession session) {
        if (responseFromServer.isSuccess()) {
            Commodity commodity = (Commodity) responseFromServer.getData();
            if (commodity == null || commodity.getNotice() == null || commodity.getNotice().getAccountId() == null) {
                return false;
            }
            Account account = new Account(commodity.getNotice().getAccountId());
            return verify(account, session);
        }
        return false;

    }
}
