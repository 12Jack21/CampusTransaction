package com.example.transaction.util.code;

public enum Address {
    文理学部(1, "文理学部"),
    工学部(2, "工学部"),
    信息学部(3, "信息学部"),
    医学部(4, "医学部");

    private final int code;
    private final String desc;
    
    public String getDescription(int code){
        return values()[code].toString();
    }


    Address(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
