package com.example.transaction.util;

import com.example.transaction.dao.TokenDAO;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Token;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

/**
 * @ClassName: TokenUtil
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/5/7 19:25
 */
public class TokenUtil {

    @Autowired
    TokenDAO tokenDAO;
    private responseFromServer operateToKen(Map<String, Object> map, Account account, Integer accountId) {
        //根据数据库的用户信息查询Token
        Token token = tokenDAO.getTokenByAccountId(accountId);
        //为生成Token准备
        String TokenStr = "";
        Date date = new Date();
        int nowTime = (int) (date.getTime() / 1000);
        //生成Token
        TokenStr = creatToken(accountId, date);
        if (null == token) {
            //第一次登陆
            token = new Token();
            token.setTokenStr(TokenStr);
            token.setAccountId(accountId);
//            token.setId(Long.valueOf(IdUtils.getPrimaryKey()));
            tokenDAO.insert(token);
        }else{
            //登陆就更新Token信息
            TokenStr = creatToken(accountId, date);
            token.setTokenStr(TokenStr);
            tokenDAO.updateById(token);
        }
        return responseFromServer.success(TokenStr);

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



    private String creatToken(Integer accountId, Date date) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT") // 设置header
                .setHeaderParam("alg", "HS256").setIssuedAt(date) // 设置签发时间
                .setExpiration(new Date(date.getTime() + 1000 * 60 * 60))
                .claim("userId",String.valueOf(accountId) ) // 设置内容
                .setIssuer("lws")// 设置签发人
                .signWith(signatureAlgorithm, "签名"); // 签名，需要算法和key
        String jwt = builder.compact();
        return jwt;
    }

}
