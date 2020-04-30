package com.example.transaction.controller;

import com.example.transaction.pojo.Account;
import com.example.transaction.service.AccountService;
import com.example.transaction.util.AccountVerify;
import com.example.transaction.util.responseFromServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

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

    Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    /**
     * 使用shiro进行登录验证
     * @param account
     * @param session
     * @return
     */
    @RequestMapping("/login")
    public responseFromServer login(@RequestBody Account account, HttpSession session){
        //验证参数，用户名和密码是否为空
        if(account==null||account.getUsername()==null||account.getPassword()==null)
            return responseFromServer.error();

        responseFromServer response  = accountService.selectByUserName(account.getUsername());
        if (response.isSuccess()) {
            Account account1 = (Account) response.getData();
            if(account.getPassword().equals(account1.getPassword())){
                session.setAttribute("currentAccount",account1);
                return responseFromServer.success();
            }else{
                return responseFromServer.error();
            }
        }else{
            return responseFromServer.error();
        }
        //添加用户认证信息
       /* Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                account.getUsername(),
                account.getPassword()
        );

        try {
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(usernamePasswordToken);
            *//*subject.checkRole("admin");
            subject.checkPermissions("query", "add");*//*
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
            *//*登录成功*//*
            return responseFromServer.success();
        } else {
            usernamePasswordToken.clear();
            return responseFromServer.error();
        }
*/
    }

    /**
     * 检查当前用户名是否被使用
     * @param account
     * @return
     */
    @RequestMapping("/verifyUserName")
    public responseFromServer verifyUserName(@RequestBody Account account){
        String userName = account.getUsername();
        if(userName==null||userName==""){
            return responseFromServer.error();
        }else{
            return accountService.verifyUserName(userName);
        }
    }

    /**
     * 注册
     * @param account
     * @return
     */
    @RequestMapping("/register")
    public responseFromServer register(@RequestBody Account account){

        if(account.getPassword()==null||
                !accountService.verifyUserName(account.getUsername()).isSuccess()){
            return responseFromServer.error();
        }
        return accountService.register(account);
    }


    /**
     * 更新用户信息
     * @param account
     * @param session
     * @return
     */
    @RequestMapping("/updateUser")
    public responseFromServer updateUser(@RequestBody Account account, HttpSession session){
        /*验证当前用户id与更新信息中id是否相同
         * 避免用户非法修改其他用户信息*/
        if(AccountVerify.verify(account,session)){
            return accountService.updateAccount(account);
        }
        return responseFromServer.error("非法操作");

    }


    /**
     * 获得账号信息，在a2a中验证
     * @param account
     * @param session
     * @return
     */
    @RequestMapping("/getAccountInfo")
    public responseFromServer getAccountInfo(@RequestBody Account account,HttpSession session){
        Account account1 = AccountVerify.verifyWithReturn(account,session);
        if(account1 == null){
            return responseFromServer.error();
        }
        if(account1.getId().intValue()!=account.getId().intValue()){
            responseFromServer response = accountService.getA2a(account.getId(),account1.getId());
            if(response.isSuccess()){
                account = (Account) response.getData();
                return responseFromServer.success(account);
            }else{
                return responseFromServer.error();
            }
        }else{
            /*此时验证返回的是自己的账户信息*/
            return responseFromServer.success(account1);
        }
    }
}
