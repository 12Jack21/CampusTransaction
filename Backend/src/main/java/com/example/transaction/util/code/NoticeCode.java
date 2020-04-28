package com.example.transaction.util.code;

public enum NoticeCode {
    UNPUBLISHED(-1,"UNPUBLISHED"),
    PUBLISHED(0,"PUBLISHED"),
    CANCELLED(1,"CANCELLED");

    private final int code;
    private final String desc;


    NoticeCode(int code, String desc){
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
