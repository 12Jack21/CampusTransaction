package com.example.transaction.util;

public enum ReservationCode {

    CANCELED(-1,"CANCELED"),
    WAITING(0,"WAITING"),
    SUCCESS(1,"SUCCESS"),
    FAIL(2,"FAIL");

    private final int code;
    private final String desc;


    ReservationCode(int code, String desc){
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
