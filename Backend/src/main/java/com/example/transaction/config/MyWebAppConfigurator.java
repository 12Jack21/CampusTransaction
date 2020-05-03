package com.example.transaction.config;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName: MyWebAppConfigurator
 * @Description: 图片资源映射器
 * @Author: 曾志昊
 * @Date: 2020/5/3 16:19
 */
public class MyWebAppConfigurator   implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**").addResourceLocations("file:E:/CampusTransactionImages/");
    }
}
