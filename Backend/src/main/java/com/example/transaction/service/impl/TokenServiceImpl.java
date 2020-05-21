package com.example.transaction.service.impl;

import com.example.transaction.dao.TokenDAO;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Token;
import com.example.transaction.service.TokenService;
import com.example.transaction.util.JwtUtil;
import com.example.transaction.util.responseFromServer;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;

/**
 * @ClassName: TokenServiceImpl
 * @Description: 登录和退登的token操作
 * @Author: 曾志昊
 * @Date: 2020/5/7 20:30
 */
@Service("TokenService")
public class TokenServiceImpl implements TokenService {

    @Autowired
    TokenDAO tokenDAO;

    /**
     * 登录操作，插入新的token或是更新当前token
     *
     * @param account
     * @return
     */
    @Override
    @Transactional
    public responseFromServer loginOperationOnToken(Account account) {
        //根据数据库的用户信息查询Token
        Token token = tokenDAO.getTokenByAccountId(account.getId());
        return login(account, token);
    }

    @Override
    @Transactional
    public responseFromServer reloginOperationOnToken(String tokenStr) {
        //根据数据库的用户信息查询Token
        Token token = tokenDAO.getTokenByTokenStr(tokenStr);
        return login(null, token);
    }

    @Transactional
    responseFromServer login(Account account, Token token) {

        String tokenStr = JwtUtil.createJWT(System.currentTimeMillis(), account);
        //        tokenStr = creatToken(accountId, date);

        if (null == token) {
            //第一次登陆
            token = new Token();
            token.setTokenStr(tokenStr);
            token.setAccountId(account.getId());
//            token.setId(Long.valueOf(IdUtils.getPrimaryKey()));
            if (1 != tokenDAO.insert(token)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return responseFromServer.error();
            }
        } else {
            //登陆就更新Token信息
//            TokenStr = creatToken(accountId, date);
            token.setTokenStr(tokenStr);
            if (1 != tokenDAO.updateById(token)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return responseFromServer.error();
            }
        }
        /*将token返回给用户*/
        return responseFromServer.success(tokenStr);

        //        UserQueryForm queryForm = getUserInfo(user, TokenStr);
        /* 将用户信息存入session */
        /*SessionContext sessionContext = SessionContext.getInstance();
        HttpSession session = sessionContext.getSession();
        httpSession.setAttribute("userInfo", user);*/
        //返回Token信息给客户端
//        successful(map);
//        map.put("data", queryForm);
//        return map;
    }

    @Override
    @Transactional
    public responseFromServer logoutOperationOnToken(Account account) {
        //根据数据库的用户信息查询Token
        Token token = tokenDAO.getTokenByAccountId(account.getId());
        if (null == token) {
            /*此时未登录*/
            return responseFromServer.error();
        } else {
            if (1 != tokenDAO.deleteById(account.getId())) {
                return responseFromServer.error();
            }
        }
        return responseFromServer.success();
    }


}
