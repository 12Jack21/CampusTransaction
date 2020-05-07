package com.example.transaction.service;

import com.example.transaction.util.responseFromServer;
import org.springframework.transaction.annotation.Transactional;

/**
 * @InterfaceName: TokenService
 * @Description: token操作接口
 * @Author: 曾志昊
 * @Date: 2020/5/7 20:29
 */
public interface TokenService {
    responseFromServer loginOperationOnToken(Integer accountId);

    @Transactional
    responseFromServer logoutOperationOnToken(Integer accountId);
}
