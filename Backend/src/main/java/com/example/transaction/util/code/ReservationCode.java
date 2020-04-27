package com.example.transaction.util.code;

public enum ReservationCode {

    CANCELLED(-1,"CANCELLED"),
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
