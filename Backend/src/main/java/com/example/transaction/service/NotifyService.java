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
    responseFromServer pushNotify(Notify notify);
    responseFromServer getNotifyPage(QueryWrapper queryWrapper,Integer pageIndex);
    responseFromServer getUnreadNotifyCount(Integer accountId);
    responseFromServer getUnreadNotify(Integer accountId,Integer pageIndex);
    responseFromServer getAllNotify(Integer accountId,Integer pageIndex);
    responseFromServer readNotify(Integer notifyId);
}
