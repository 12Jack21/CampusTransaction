package com.example.transaction.controller;

import com.example.transaction.pojo.Account;
import com.example.transaction.service.AccountService;
import com.example.transaction.util.responseFromServer;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: AccountController
 * @Author: 曾志昊
 * @Date: 2020/4/25 17:57
 */
@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    /**
     * 使用shiro进行登录验证
     * @param account
     * @param request
     * @return
     */
    @RequestMapping("/login")
    public responseFromServer login(@RequestBody Account account, HttpRequest request){
        //验证参数，用户名和密码是否为空
        if(account==null||account.getUsername()==null||account.getPassword()==null)
            return responseFromServer.error();

        //添加用户认证信息
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                account.getUsername(),
                account.getPassword()
        );

        try {
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(usernamePasswordToken);
            /*subject.checkRole("admin");
            subject.checkPermissions("query", "add");*/
        } catch (AuthorizationException e) {
            e.printStackTrace();
            return responseFromServer.error("没有权限");
        }catch (UnknownAccountException uae) {
            uae.printStackTrace();
            return responseFromServer.error("未知账户");
        } catch (IncorrectCredentialsException ice) {
            ice.printStackTrace();
            return responseFromServer.error("密码不正确");
        } catch (LockedAccountException lae) {
            lae.printStackTrace();
            return responseFromServer.error("账户已锁定");
        } catch (ExcessiveAttemptsException eae) {
            eae.printStackTrace();
            return responseFromServer.error("用户名或密码错误次数过多");
        } catch (AuthenticationException ae) {
            ae.printStackTrace();
            return responseFromServer.error("账号或密码错误");
        }

        if (subject.isAuthenticated()) {
            /*登录成功*/
            return responseFromServer.success();
        } else {
            usernamePasswordToken.clear();
            return responseFromServer.error();
        }

    }





}
