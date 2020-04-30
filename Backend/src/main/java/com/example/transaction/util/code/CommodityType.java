package com.example.transaction.util.code;

/**
 * @ClassName: CommodityType
 * @Author: 曾志昊
 * @Date: 2020/4/30 14:23
 */
public enum CommodityType {
    数学(1,"数学"),
    英语(2,"英语"),
    计算机(3,"计算机"),
    本科(4,"本科"),
    研究生(5,"研究生"),
    博士(6,"博士"),
    大一(7,"大一"),
    大二(8,"大二"),
    大三(9,"大三"),
    大四(10,"大四"),
    公选(11,"公选"),
    书籍(12,"书籍"),
    生活用品(13,"生活用品"),
    化妆品(14,"化妆品"),
    日用品(15,"日用品"),
    文具(16,"文具");


    private final int code;
    private final String desc;


    CommodityType(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode(){
        return code;
    }
    public String getDesc(){
        return desc;
    }
}
