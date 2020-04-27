package com.example.transaction.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Notify;
import com.example.transaction.util.responseFromServer;

/**
 * @InterfaceName: NotifyService
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/28 2:07
 */
public interface NotifyService {
    public responseFromServer pushNotify(Notify notify);
    public responseFromServer getNotifyPage(QueryWrapper queryWrapper, int pageIndex);
    public responseFromServer readNotify(Notify notify);
    public responseFromServer getUnreadNotify(Account account);
}
