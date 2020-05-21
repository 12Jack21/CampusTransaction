package com.example.transaction.util.code;

public enum NotifyTargetCode {
    /***/
    COMMODITY(0,"商品"),
    RESERVATION(1,"预约"),
    COMMENT(2,"评论"),
    NOTICE(3,"通告"),
    ACCOUNT(4,"用户");


    private final int code;
    private final String desc;


    NotifyTargetCode(int code, String desc){
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
