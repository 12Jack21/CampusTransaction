package com.example.transaction.config;

import com.example.transaction.util.code.ResourcePath;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.FileNotFoundException;

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
//        registry.addResourceHandler("/static/images/avatar/**")
//                .addResourceLocations("file:"+ ResourcePath.basePath + "/src/main/resources/static/images/avatar/");
//        registry.addResourceHandler("/static/images/home/goods/**")
//                .addResourceLocations("file:"+ ResourcePath.basePath + "/src/main/resources/static/images/home/goods/");
        /*打包后的路径*/
//        registry.addResourceHandler("/static/images/avatar/**")
//                .addResourceLocations("file:" + ResourcePath.basePath + "/BOOT-INF/classes/static/images/avatar/");
//        registry.addResourceHandler("/static/images/home/goods/**")
//                .addResourceLocations("file:" + ResourcePath.basePath + "/BOOT-INF/classes/static/images/home/goods/");
        registry.addResourceHandler("/static/images/avatar/**")
                .addResourceLocations("classpath:/static/images/avatar/");
        registry.addResourceHandler("/static/images/home/goods/**")
                .addResourceLocations("classpath:/static/images/home/goods/");


    }
}
