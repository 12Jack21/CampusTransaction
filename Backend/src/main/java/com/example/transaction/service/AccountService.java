package com.example.transaction.service;

import com.example.transaction.pojo.Account;
import com.example.transaction.util.responseFromServer;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * @InterfaceName: AccountService
 * @Author: 曾志昊
 * @Date: 2020/4/25 17:52
 */

public interface AccountService {
    public responseFromServer selectByUserName(String userName);

    public responseFromServer verifyUserName(String userName);

    responseFromServer test(HttpServletRequest request);

    responseFromServer login(Account account);

    @Transactional
    responseFromServer logout(Account account);

    public responseFromServer register(Account account);

    public responseFromServer updateAccount(Account account);

    public responseFromServer verifyPassword(Account account);

    public responseFromServer getA2a(Integer accountId1, Integer accountId2);
}
