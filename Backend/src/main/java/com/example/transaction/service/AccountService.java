package com.example.transaction.service;

import com.example.transaction.util.responseFromServer;

/**
 * @InterfaceName: AccountService
 * @Author: 曾志昊
 * @Date: 2020/4/25 17:52
 */
public interface AccountService {
    public responseFromServer selectByUserName(String userName);
}
