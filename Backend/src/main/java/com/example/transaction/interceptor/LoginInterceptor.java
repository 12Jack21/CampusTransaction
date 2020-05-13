package com.example.transaction.interceptor;

import com.example.transaction.dao.TokenDAO;
import com.example.transaction.pojo.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @ClassName: LoginInterceptor
 * @Description: 登录拦截器
 * @Author: 曾志昊
 * @Date: 2020/5/7 16:59
 */
/**
 * ZZH
 * TODO : 去掉注释
 */
//@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    protected TokenDAO tokenDAO;

    //提供查询
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
        //此处为不需要登录的接口放行
        if (arg0.getRequestURI().contains("/login")
                || arg0.getRequestURI().contains("/register")
                || arg0.getRequestURI().contains("/error")
                || arg0.getRequestURI().contains("/static")
                || arg0.getRequestURI().contains("/test")) {
            return true;
        }
        //权限路径拦截
        //PrintWriter resultWriter = arg1.getOutputStream();

        // 有时候用PrintWriter 回报 getWriter() has already been called for this response
        //换成ServletOutputStream就OK了
        arg1.setContentType("text/html;charset=utf-8");
        ServletOutputStream resultWriter = arg1.getOutputStream();
        final String headerToken = arg0.getHeader("token");
        //判断请求信息
        if (null == headerToken || headerToken.trim().equals("")) {
            resultWriter.write("你没有token,需要登录".getBytes());
            resultWriter.flush();
            resultWriter.close();
            /**
             * ZZH
             * TODO : 此处先默认无token为登录状态,之后修改为返回false
             * return false;
             */
            return true;
        }
        //解析Token信息
        try {
            Claims claims = Jwts.parser().setSigningKey("preRead").parseClaimsJws(headerToken).getBody();
            String tokenUserId=(String)claims.get("accountId");
            Integer accountId = Integer.parseInt(tokenUserId);
            //根据客户Token查找数据库Token
            Token myToken= tokenDAO.getTokenByAccountId(accountId);

            //数据库没有Token记录
            if(null==myToken) {
                resultWriter.write("我没有你的token？,需要登录".getBytes());
                resultWriter.flush();
                resultWriter.close();
                return false;
            }
            //数据库Token与客户Token比较
            if( !headerToken.equals(myToken.getTokenStr()) ){
                resultWriter.print("你的token修改过？,需要登录");
                resultWriter.flush();
                resultWriter.close();
                return false;
            }
            //判断Token过期
            Date tokenDate= claims.getExpiration();
            int overTime=(int)(new Date().getTime()-tokenDate.getTime())/1000;
            if(overTime>60*60*24*3){
                resultWriter.write("你的token过期了？,需要登录".getBytes());
                resultWriter.flush();
                resultWriter.close();
                return false;
            }

        } catch (Exception e) {
            resultWriter.write("反正token不对,需要登录".getBytes());
            resultWriter.flush();
            resultWriter.close();
            return false;
        }
        //最后才放行
        return true;
    }


}
