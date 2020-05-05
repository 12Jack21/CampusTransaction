package com.example.transaction.service;

import com.example.transaction.util.responseFromServer;

/**
 * @InterfaceName: SearchService
 * @Author: 曾志昊
 * @Date: 2020/4/29 1:03
 */
public interface SearchService {

    public responseFromServer addSearchRecord(Integer accountId,String content);
}
