package com.example.transaction.util.code;

/**
 * @ClassName: Nums
 * @Description: 一些全局的变量
 * @Author: 曾志昊
 * @Date: 2020/4/27 15:51
 */
public class Nums {
    /*分页大小*/
    public static int pageSize = 15;
    /*时间限制大小*/
    public static int recentDays = 2;
    /*每次最大上传的文件数量*/
    public static int maxUploadingFilesCount = 6;

    public static final long year = (long) 1000 * 60 * 60 * 24 * 365;
    public static final long month = (long) 1000 * 60 * 60 * 24 * 30;
    public static final long weak = (long) 1000 * 60 * 60 * 24 * 7;
    public static final long day = (long) 1000 * 60 * 60 * 24;

    public static final String commodityImagePath = "http://39.96.69.108:9999/static/images/home/goods/";
    public static final String avatarPath = "http://39.96.69.108:9999/static/images/avatar/";
    public static String basePath = System.getProperty("user.dir");


}
