package com.example.transaction.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.transaction.pojo.Notice;
import com.example.transaction.util.responseFromServer;

import java.util.Map;

/**
 * @ClassName: NoticeService
 * @Author: 曾志昊
 * @Date: 2020/4/26 16:45
 */
public interface NoticeService {
    /*获得通告*/
    responseFromServer getSimpleNotice(Integer noticeId);
    /*获得详细通告内容*/
    responseFromServer getDetailedNotice(Integer noticeId);
    /*添加通告*/
    responseFromServer setupNotice(Notice notice);
    /*删除通告*/
    responseFromServer deleteNotice(QueryWrapper queryWrapper);
    /*关闭通告=>不可交易*/
    responseFromServer updateNotice(Notice notice, QueryWrapper queryWrapper);
/*    *//*获取最近的通告分页*//*
    responseFromServer getRecentNoticePage(Map<String,Object> map);
    *//*根据卖家id获取通告分页*//*
    responseFromServer getNoticePageByAccountId(Map<String,Object> map);*/
    /*获取通告分页*/
    responseFromServer getNoticePage(QueryWrapper queryWrapper, int pageIndex);

}
