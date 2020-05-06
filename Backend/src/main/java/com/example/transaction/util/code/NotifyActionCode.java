package com.example.transaction.util.code;

public enum NotifyActionCode {
    COMMENTS(0,"评论了"),
    SETS_UP(1,"创建了"),
    CANCELS(2,"取消了"),
    VALIDATES(3,"确认了"),
    SUBMITS(4,"提交了"),
    FINISHS(5,"成功结束了"),;

    private final int code;
    private final String desc;


    NotifyActionCode(int code, String desc){
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
