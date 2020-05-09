package com.example.transaction.config;

import com.example.transaction.util.jsonParamResolver.filter.RequestJsonFilter;
import com.example.transaction.util.jsonParamResolver.handler.RequestJsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

/**
 * @ClassName: WebConfig
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/5/9 13:30
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private RequestJsonHandler requestJsonHandler;


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(0, requestJsonHandler);
    }

    @Bean
    public FilterRegistrationBean filterRegister() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RequestJsonFilter());
        //拦截路径
        registration.addUrlPatterns("/");
        //过滤器名称
        registration.setName("requestJsonFilter");
        //是否自动注册 false 取消Filter的自动注册
        registration.setEnabled(false);
        //过滤器顺序,需排在第一位
        registration.setOrder(1);
        return registration;
    }

    @Bean(name = "requestJsonFilter")
    public Filter requestFilter() {
        return new RequestJsonFilter();
    }
}
