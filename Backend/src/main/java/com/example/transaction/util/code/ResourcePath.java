package com.example.transaction.util.code;

/**
 * @ClassName: ResourcePath
 * @Author: 曾志昊
 * @Date: 2020/5/4 13:31
 */
public class ResourcePath {
    public static final String basePath = System.getProperty("user.dir");


//    public static final String commodityImageRequestPath = "http://39.96.69.108:9999/static/images/home/goods/";
//    public static final String avatarRequestPath = "http://39.96.69.108:9999/static/images/avatar/";
//    public static final String imageTempPath = basePath + "/src/main/resources/static/images/temp/";
//    public static final String avatarFilePath = basePath + "/src/main/resources/static/images/avatar/";
//    public static final String goodsFilePath = basePath + "/src/main/resources/static/images/home/goods/";


    public static final String commodityImageRequestPath = "http://39.96.69.108:8080/static/images/home/goods/";
    public static final String avatarRequestPath = "http://39.96.69.108:8080/static/images/avatar/";
    public static final String imageTempPath = basePath + "/BOOT-INF/classes/static/images/temp/";
    public static final String avatarFilePath = basePath + "/BOOT-INF/classes/static/images/avatar/";
    public static final String goodsFilePath = basePath + "/BOOT-INF/classes/static/images/home/goods/";
}
