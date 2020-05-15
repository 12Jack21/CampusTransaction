package com.example.transaction.util.code;

public enum NotifyActionCode {
    COMMENTS(0, "评论了"),
    SETS_UP(1, "创建了"),
    CANCELS(2, "取消了"),
    VALIDATES(3, "确认了"),
    SUBMITS(4, "提交了"),
    FINISHS(5, "成功结束了");

    private final int code;
    private final String desc;

    /*
     * 预约:
     *   a 预约了 你的 b 商品
     *   a 取消了预约 (b)
     *   a 确认了你的预约 (b)
     *   你(买家卖家)的交易(b)成功结束了
     *   您预约的商品(b)已失效
     *
     * 评论:
     *   a评论了你的商品(b)
     *   a评论了你"     "
     *
     * 通告:
     *   您的通告(b)已失效
     *   您的通告(b)即将失效
     *
     * websocket 消息推送
     *
     *
     *
     * */


    NotifyActionCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }
    public String getDesc(){
        return desc;
    }
}
