package com.example.transaction.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.AccountNotify;
import com.example.transaction.pojo.Notify;
import com.example.transaction.util.responseFromServer;

/**
 * @InterfaceName: NotifyService
 * @Author: 曾志昊
 * @Date: 2020/4/28 2:07
 */
public interface NotifyService {
    responseFromServer getSimpleAccountNotifyById(Integer id);
    responseFromServer getDetailedAccountNotifyById(Integer id);

    responseFromServer getUnreadNotifyByAccountId(Integer id);
    responseFromServer getAllNoticeByAccountId(Integer id);
    responseFromServer getUnreadNotifyCount(Integer accountId);
    responseFromServer getNotifyPage(QueryWrapper queryWrapper,Integer pageIndex);

    responseFromServer insertNotify(Notify notify);
    responseFromServer insertAccountNotify(AccountNotify accountNotify);

    responseFromServer readNotify(Integer notifyId);

    responseFromServer deleteNotify(AccountNotify accountNotify);

}
