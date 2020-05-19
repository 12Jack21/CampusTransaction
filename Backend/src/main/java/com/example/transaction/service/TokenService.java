package com.example.transaction.service;

import com.example.transaction.pojo.Account;
import com.example.transaction.util.responseFromServer;
import org.springframework.transaction.annotation.Transactional;

/**
 * @InterfaceName: TokenService
 * @Description: token操作接口
 * @Author: 曾志昊
 * @Date: 2020/5/7 20:29
 */
public interface TokenService {

    @Transactional
    responseFromServer loginOperationOnToken(Account account);

    @Transactional
    responseFromServer reloginOperationOnToken(String tokenStr);

    @Transactional
    responseFromServer logoutOperationOnToken(Account account);
}
