package com.example.transaction.util.code;

public enum NoticeCode {
    UNSAVED(-2,"UNSAVED"),
    UNPUBLISHED(-1,"UNPUBLISHED"),
    PUBLISHED(0,"PUBLISHED"),
    CANCELLED(1,"CANCELLED");

    private final int code;
    private final String desc;

    public static String getDescription(int code){
        try{
            return values()[code+2].toString();
        }catch(Exception e){
            return "";
        }
    }

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
