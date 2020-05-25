package com.example.transaction.controller;

import com.example.transaction.pojo.Account;
import com.example.transaction.service.AccountService;
import com.example.transaction.util.jsonParamResolver.handler.RequestJson;
import com.example.transaction.util.responseFromServer;
import com.example.transaction.service.impl.AccountVerify;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


/**
 * @ClassName: AccountController
 * @Author: 曾志昊
 * @Date: 2020/4/25 17:57
 */
@RestController
@RequestMapping("/accounts")
@Api(tags = "AccountController")
public class AccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    AccountVerify accountVerify;

    @PostMapping("/testLogin")
    public responseFromServer testLogin(@RequestJson String username, @RequestJson String password, HttpServletRequest request) {
        System.out.printf(username + password);
        return responseFromServer.success();
    }

    /**
     * 使用shiro进行登录验证
     *
     * @param account
     * @param request
     * @return
     */
//    @RequestMapping("/login")
    @ApiOperation(value = "登录验证")
    @ApiImplicitParam(name = "account", value = "用户实体", paramType = "Account", dataType = "Account")
    @PostMapping("/login")
    public responseFromServer login(@RequestBody Account account, HttpServletRequest request) {
        //验证参数，用户名和密码是否为空
        if (account == null || account.getUsername() == null || account.getPassword() == null) {
            return responseFromServer.error();
        }
        return accountService.login(account);
        //添加用户认证信息
       /* Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                account.getUsername(),
                account.getPassword()
        );

        try {
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(usernamePasswordToken);
            subject.checkRole("admin");
            subject.checkPermissions("query", "add");
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
            //登录成功
            return responseFromServer.success();
        } else {
            usernamePasswordToken.clear();
            return responseFromServer.error();
        }
        */
    }


    @ApiOperation(value = "重新登录")
    @ApiImplicitParam(name = "account", value = "用户实体", paramType = "Account", dataType = "Account")
    @PostMapping("/relogin")
    public responseFromServer relogin(HttpServletRequest request) {
        //验证参数，用户名和密码是否为空
        String tokenStr = request.getHeader("token");
        return accountService.reLogin(tokenStr);
    }


    /**
     * 退登
     *
     * @param request
     * @return
     */
//    @RequestMapping("/logout")
    @ApiOperation(value = "退出登录")
    @DeleteMapping()
    public responseFromServer logout(HttpServletRequest request) {
        Account account = new Account();
        if (accountVerify.verify(account, request)) {
            /*登录状态*/
            return accountService.logout(account);
        } else {
            return responseFromServer.error();
        }
//        session.removeAttribute("currentAccount");
    }


    /*todo 上传图片*/
    //上传图片可归入个人信息修改，用户账号刚创建时初始化头像
    @PostMapping("/{accountId}/avatar/upload")
    public responseFromServer uploadAvatar(@PathVariable(required = true) Integer accountId, @RequestParam(required = true) MultipartFile avatar, HttpServletRequest request) {
        return accountService.uploadAvatar(avatar, accountId);
    }


    /**
     * 检查当前用户名是否被使用
     *
     * @param accountId
     * @return
     */
//    @RequestMapping("/verifyUserName")
    @ApiOperation(value = "检查用户名是否被使用")
    @GetMapping("/{accountId}/name")
    public responseFromServer verifyUserName(@PathVariable Integer accountId) {
//    public responseFromServer verifyUserName(@RequestBody Account account){
        Account account = new Account(accountId);
        String userName = account.getUsername();
        if (userName == null || userName == "") {
            return responseFromServer.error();
        } else {
            return accountService.verifyUserName(userName);
        }
    }

    /**
     * 注册
     * @param account
     * @return
     */
//    @RequestMapping("/register")
    @ApiOperation(value = "用户注册")
    @ApiImplicitParam(name = "account", value = "用户实体", paramType = "Account", dataType = "Account")
    @PostMapping
    public responseFromServer register(@RequestBody Account account) {

        if (account.getPassword() == null ||
                accountService.verifyUserName(account.getUsername()).isFailure()) {
            return responseFromServer.error();
        }
        return accountService.register(account);
    }


    /**
     * 更新用户信息
     *
     * @param account
     * @param request
     * @return
     */
//    @RequestMapping("/updateUser")
    @ApiOperation(value = "更新用户信息")
    @ApiImplicitParam(name = "account", value = "用户实体", paramType = "Account", dataType = "Account")
    @PutMapping("/{accountId}")
    public responseFromServer updateUser(@RequestBody Account account,
                                         @PathVariable Integer accountId,
                                         HttpServletRequest request) {
        /*验证当前用户id与更新信息中id是否相同
         * 避免用户非法修改其他用户信息*/
        if (accountVerify.verify(account, request)) {
            return accountService.updateAccount(account);
        }
        return responseFromServer.error("非法操作");
    }


    /**
     * 获得账号信息，在a2a中验证
     *
     * @param accountId
     * @param request
     * @return
     */
//    @RequestMapping("/getAccountInfo")
    @ApiOperation(value = "获取账号信息，在a2a中验证")
    @GetMapping("/{accountId}")
    public responseFromServer getAccountInfo(@PathVariable Integer accountId, HttpServletRequest request) {
        Account account = new Account(accountId);
        Account account1 = accountVerify.verifyWithReturn(account, request);
        if (account1 == null) {
            return responseFromServer.error();
        }
        if (account1.getId().intValue() != account.getId().intValue()) {
            responseFromServer response = accountService.getA2a(account.getId(), account1.getId());
            if (response.isSuccess()) {
                account = (Account) response.getData();
                return responseFromServer.success(account);
            } else {
                return responseFromServer.error();
            }
        }else{
            /*此时验证返回的是自己的账户信息*/
            return responseFromServer.success(account1);
        }
    }



    @ApiOperation(value = "获取账号信息，在a2a中验证")
    @GetMapping("/other/{otherId}")
    public responseFromServer getOthersAccountInfo(@PathVariable Integer otherId, HttpServletRequest request) {
        Account account = new Account(otherId);
        Account account1 = accountVerify.verifyWithReturn(account, request);
        if (account1 == null) {
            return responseFromServer.error();
        }
        if (account1.getId().intValue() != account.getId().intValue()) {
            responseFromServer response = accountService.getA2a(account.getId(), account1.getId());
            if (response.isSuccess()) {
                account = (Account) response.getData();
                return responseFromServer.success(account);
            } else {
                return responseFromServer.error();
            }
        }else{
            /*此时验证返回的是自己的账户信息*/
            return responseFromServer.success(account1);
        }
    }

}
