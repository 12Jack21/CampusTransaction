//package com.example.transaction.util.shiro;
//
//import org.apache.shiro.cache.CacheManager;
//import org.apache.shiro.session.mgt.SessionManager;
//import org.apache.shiro.spring.LifecycleBeanPostProcessor;
//import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
//import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//import org.apache.shiro.mgt.SecurityManager;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
///**
// * @ClassName: ShiroConfig
// * @Description: shiro配置文件
// * @Author: 曾志昊
// * @Date: 2020/4/25 20:56
// */
////@Configuration
//public class ShiroConfig {
//    /**
//     * Session Manager：会话管理
//     * 即用户登录后就是一次会话，在没有退出之前，它的所有信息都在会话中；
//     * 会话可以是普通JavaSE环境的，也可以是如Web环境的；
//     */
//    @Bean("sessionManager")
//    public SessionManager sessionManager(){
//        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
//        //设置session过期时间
//        sessionManager.setGlobalSessionTimeout(60 * 60 * 1000);
//        sessionManager.setSessionValidationSchedulerEnabled(true);
//        // 去掉shiro登录时url里的JSESSIONID
//        sessionManager.setSessionIdUrlRewritingEnabled(false);
//        return sessionManager;
//    }
//
//    /**
//     * 管理Shiro中一些bean的生命周期
//     */
//    @Bean("lifecycleBeanPostProcessor")
//    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
//        return new LifecycleBeanPostProcessor();
//    }
//
//    //不加这个注解不生效，具体不详
//    @Bean
//    @ConditionalOnMissingBean
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
//        defaultAAP.setProxyTargetClass(true);
//        return defaultAAP;
//    }
//
//    //将自己的验证方式加入容器
//    @Bean("myShiroRealm")
//    public MyShiroRealm myShiroRealm() {
//        MyShiroRealm myShiroRealm = new MyShiroRealm();
//        return myShiroRealm;
//    }
//
//
//    /**
//     * 权限管理，配置主要是Realm的管理认证，可配置一个或多个realm
//     * @return
//     */
//    @Bean("securityManager")
//    public SecurityManager securityManager() {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(myShiroRealm());
//        /*可以配置一个或多个rewalm*/
//        return securityManager;
//    }
//
//
//
//    /**
//     * ShiroFilter是整个Shiro的入口点，用于拦截需要安全控制的请求进行处理
//     * Filter工厂，设置对应的过滤条件和跳转条件，
//     * @param securityManager
//     * @return
//     */
//    @Bean("shiroFilter")
//    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager")SecurityManager securityManager) {
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        shiroFilterFactoryBean.setSecurityManager(securityManager());
//        shiroFilterFactoryBean.setLoginUrl("/account/login");
//
//        /*  配置拦截过滤器链，
//            map的键 : 资源地址 ;
//            map的值 : 所有默认Shiro过滤器实例名 默认Shiro过滤器实例*/
//        // authc:所有url都必须认证通过才可以访问;
//        // anon:所有url都都可以匿名访问
//        Map<String, String> filterMap = new HashMap<>();
//        /*公开地址*/
//        filterMap.put("/static/**", "anon"); // 公开访问的资源
//        //filterMap.put("/account/login", "anon"); // 登录地址放开
//        filterMap.put("/notice/getRecentNoticePage", "anon"); // 获取首页最新通告
//
//        filterMap.put("/account/logout", "logout"); // 配置登出页,shiro已经帮我们实现了跳转
//
//        //这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截 剩余的都需要认证
//        filterMap.put("/**", "authc"); // 所有资源都需要经过验证
//        /*
//        //登出
//        map.put("/logout", "logout");
//        //对所有用户认证
//        map.put("/**", "authc");
//        //首页
//        shiroFilterFactoryBean.setSuccessUrl("/index");
//        //错误页面，认证不通过跳转
//        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
//        */
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
//        return shiroFilterFactoryBean;
//    }
//
//    //加入注解的使用，不加入这个注解不生效
//    /**
//     * 开启shiro 注解支持. 使以下注解能够生效 :
//     * 需要认证 {@link org.apache.shiro.authz.annotation.RequiresAuthentication RequiresAuthentication}
//     * 需要用户 {@link org.apache.shiro.authz.annotation.RequiresUser RequiresUser}
//     * 需要访客 {@link org.apache.shiro.authz.annotation.RequiresGuest RequiresGuest}
//     * 需要角色 {@link org.apache.shiro.authz.annotation.RequiresRoles RequiresRoles}
//     * 需要权限 {@link org.apache.shiro.authz.annotation.RequiresPermissions RequiresPermissions}
//     */
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
//        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
//        return authorizationAttributeSourceAdvisor;
//    }
//
//
//}
