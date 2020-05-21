package com.example.transaction.util.code;


public enum NotifyActionCode {
    /**
     * 预约:
     * a 预约了 你的 b 商品
     * {
     * id: accountNotify的id
     * idRead: true/false,
     * createTime: 创建时间,
     * notify:{
     * sender: a的id
     * action: 9 - 预约了
     * targetType: 0 - 商品
     * targetId: 商品id
     * data:{
     * id: 预约id
     * commodity:{
     * id: 商品id
     * name: 商品名称
     * imageUrl: 商品图片
     * }
     * }
     * }
     * }
     * <p>
     * <p>
     * a 取消了预约 (b)
     * {
     * id: accountNotify的id
     * idRead: true/false,
     * createTime: 创建时间,
     * notify:{
     * sender: a的id
     * action: 1 - 取消了
     * targetType: 1 - 预约
     * targetId: 预约id
     * data:{
     * id: 预约id
     * commodity:{
     * id: 商品id
     * name: 商品名称
     * imageUrl: 商品图片
     * }
     * }
     * }
     * }
     * <p>
     * a 确认了你的预约 (b)
     * {
     * id: accountNotify的id
     * idRead: true/false,
     * createTime: 创建时间,
     * notify:{
     * sender: a的id
     * action: 3 - 确认了
     * targetType: 1 - 预约
     * targetId: 预约id
     * data:{
     * id: 预约id
     * commodity:{
     * id: 商品id
     * name: 商品名称
     * imageUrl: 商品图片
     * }
     * }
     * }
     * }
     * 你(买家卖家)的交易(b)成功结束了
     * {
     * id: accountNotify的id
     * idRead: true/false,
     * createTime: 创建时间,
     * notify:{
     * sender: null
     * action: 5 - 成功结束了
     * targetType: 1 - 预约 (交易)
     * targetId: 预约id (交易)
     * data:{
     * id: 预约id
     * commodity:{
     * id: 商品id
     * name: 商品名称
     * imageUrl: 商品图片
     * }
     * }
     * }
     * }
     * 您预约的商品(b)已失效
     * {
     * id: accountNotify的id
     * idRead: true/false,
     * createTime: 创建时间,
     * notify:{
     * sender: null
     * action: 7 - 失效了
     * targetType: 0 - 商品
     * targetId: 商品id
     * data:{
     * id: 商品id
     * name: 商品名称
     * imageUrl: 商品图片
     * }
     * }
     * }
     * <p>
     * 评论:
     * a评论了你的商品(b)
     * {
     * id: accountNotify的id
     * idRead: true/false,
     * createTime: 创建时间,
     * notify:{
     * sender: a的id
     * action: 2 - 评论了
     * targetType: 0 - 商品
     * targetId: 商品id
     * data:{
     * <p>
     * id: 商品id
     * name: 商品名称
     * imageUrl: 商品图片
     * }
     * }
     * }
     * a回复了你"     "
     * {
     * id: accountNotify的id
     * idRead: true/false,
     * createTime: 创建时间,
     * notify:{
     * sender: a的id
     * action: 8 - 回复了
     * targetType: 2 - 评论(被回复的评论)
     * targetId: 评论id
     * data:{评论对象(a回复的评论对象)
     * {},
     * <p>
     * content: 评论内容
     * commodity:{
     * id: 商品id
     * name: 商品名称
     * imageUrl: 商品图片
     * }
     * }
     * }
     * }
     * <p>
     * 通告:
     * 您的通告(b)已失效
     * {
     * id: accountNotify的id
     * idRead: true/false,
     * createTime: 创建时间,
     * notify:{
     * sender: null
     * action: 7 - 已失效
     * targetType: 3 - 通告
     * targetId: 通告id
     * data:{
     * title: 通告标题
     * }
     * }
     * }
     * 您的通告(b)即将失效
     * {
     * id: accountNotify的id
     * idRead: true/false,
     * createTime: 创建时间,
     * notify:{
     * sender: a的id
     * action: 6 - 即将失效
     * targetType: 3 - 通告
     * targetId: 通告id
     * data:{
     * title: 通告标题
     * }
     * }
     * }
     */

    COMMENTS(0, "评论了"),
    SETSUP(1, "创建了"),
    CANCELS(2, "取消了"),
    VALIDATES(3, "确认了"),
    SUBMITS(4, "提交了"),
    FINISHS(5, "成功结束了"),
    EXPIREDSOON(6, "即将失效"),
    EXPIRED(7, "失效了"),
    REPLIES(8, "回复了"),
    RESERVES(9, "预约了"),
    FAIL(10, "失败");

    private final int code;
    private final String desc;


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
