package com.example.transaction.util.shiro;

import com.example.transaction.pojo.Account;
import com.example.transaction.service.AccountService;
import com.example.transaction.util.responseFromServer;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName: MyShiroRealm
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/25 20:59
 */
public class MyShiroRealm extends AuthorizingRealm {

    Boolean cachingEnabled=true;
    @Autowired
    AccountService accountService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {

        SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();

        simpleAuthorInfo.addStringPermission("how_are_you");//给当前用户授权url为hello的权限码
        System.out.println("经试验：并不是每次调用接口就会执行，而是调用需要操作码（permission）的接口就会执行");
        return simpleAuthorInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authcToken) throws AuthenticationException {
        //获取基于用户名和密码的令牌
        //实际上这个authcToken是从LoginController里面currentUser.login(token)传过来的
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String userName = token.getUsername();
        responseFromServer response = accountService.selectByUserName(userName);//根据登陆名account从库中查询user对象
        if(!response.isSuccess()){throw new AuthenticationException("用户不存在");}
        Account account = (Account) response.getData();
        //进行认证，将正确数据给shiro处理
        //密码不用自己比对，AuthenticationInfo认证信息对象，一个接口，new他的实现类对象SimpleAuthenticationInfo
        /*	第一个参数随便放，可以放user对象，程序可在任意位置获取 放入的对象
         * 第二个参数必须放密码，
         * 第三个参数放 当前realm的名字，因为可能有多个realm*/
        AuthenticationInfo authcInfo=new SimpleAuthenticationInfo(account, account.getPassword(), this.getName());
        //AuthenticationInfo authcInfo=new SimpleAuthenticationInfo(account,account.getPassword(),new MySimpleByteSource(account), this.getName());

        //清之前的授权信息
        super.clearCachedAuthorizationInfo(authcInfo.getPrincipals());
        SecurityUtils.getSubject().getSession().setAttribute("login", account);
        return authcInfo;//返回给安全管理器，securityManager，由securityManager比对数据库查询出的密码和页面提交的密码
        //如果有问题，向上抛异常，一直抛到控制器
    }

}
