package com.example.transaction.config;

import com.example.transaction.util.code.ResourcePath;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName: MyWebAppConfigurator
 * @Description: 图片资源映射器
 * @Author: 曾志昊
 * @Date: 2020/5/3 16:19
 */
@Configuration
public class ImageResourcesConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/images/avatar/**").addResourceLocations("file:E:/CampusTransactionResources/avatars/");
        registry.addResourceHandler("/static/images/avatar/**")
                .addResourceLocations("file:"+ ResourcePath.basePath + "/src/main/resources/static/images/avatar/");
//        registry.addResourceHandler("/static/images/home/goods/**").addResourceLocations("file:E:/CampusTransactionResources/images/");
        registry.addResourceHandler("/static/images/home/goods/**")
                .addResourceLocations("file:"+ ResourcePath.basePath + "/src/main/resources/static/images/home/goods/");
    }
}
