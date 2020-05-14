package com.example.transaction.interceptor;

import com.example.transaction.util.jsonParamResolver.filter.RequestJsonFilter;
import com.example.transaction.util.jsonParamResolver.handler.RequestJsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

/**
 * @ClassName: InterceptorConfiguration
 * @Description: 拦截器配置
 * @Author: 曾志昊
 * @Date: 2020/5/7 18:32
 */
/**
 * ZZH
 * TODO : 去掉注释
 */
//@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {


    /*
    @Bean
    LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry ) {
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**").excludePathPatterns("/login", "/register", "/static");
    }
*/

    //所有的WebMvcConfigurerAdapter组件都会一起起作用
    @Bean //将组件注册在容器中
    public WebMvcConfigurer webMvcConfigurerAdapter(){
        return new WebMvcConfigurer(){

            //注册拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                //静态资源； *.css,*.js
                //SpringBoot已经做好了静态资源映射
//                registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
//                .excludePathPatterns("/index.html","/","/user/login","/static/**","/webjars/**");
                // /**  表示拦截所有路径下的所有请求
                registry.addInterceptor(new LoginInterceptor())
                        .excludePathPatterns("/login", "/register", "/static")
                        .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
            }
        };
    }


//    @Autowired
//    private RequestJsonHandler requestJsonHandler;
//
//
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(0,requestJsonHandler);
//    }
//
//    @Bean
//    public FilterRegistrationBean filterRegister() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(new RequestJsonFilter());
//        //拦截路径
//        registration.addUrlPatterns("/");
//        //过滤器名称
//        registration.setName("requestJsonFilter");
//        //是否自动注册 false 取消Filter的自动注册
//        registration.setEnabled(false);
//        //过滤器顺序,需排在第一位
//        registration.setOrder(1);
//        return registration;
//    }
//
//    @Bean(name = "requestJsonFilter")
//    public Filter requestFilter(){
//        return new RequestJsonFilter();
//    }

}
