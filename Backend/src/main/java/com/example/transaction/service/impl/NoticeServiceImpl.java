package com.example.transaction.service.impl;

import com.example.transaction.pojo.Notice;
import com.example.transaction.service.NoticeService;
import com.example.transaction.util.responseFromServer;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName: NoticeServiceImpl
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/26 16:46
 */
@Service(value = "noticeService")
public class NoticeServiceImpl implements NoticeService {
    @Override
    public responseFromServer addNotice(Notice notice) {
        return null;
    }

    @Override
    public responseFromServer deleteNotice(Notice notice) {
        return null;
    }

    @Override
    public responseFromServer cancelNotice(Notice notice) {
        return null;
    }

    @Override
    public responseFromServer getRecentNoticePage(Map<String, Object> map) {
        return null;
    }

    @Override
    public responseFromServer getNoticePageByAccountId(Map<String, Object> map) {
        return null;
    }
}
