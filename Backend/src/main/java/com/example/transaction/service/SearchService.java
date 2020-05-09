package com.example.transaction.service;

import com.example.transaction.util.responseFromServer;

/**
 * @InterfaceName: SearchService
 * @Author: 曾志昊
 * @Date: 2020/4/29 1:03
 */
public interface SearchService {

    responseFromServer getSearchRecordsPage(Integer accountId, Integer pageIndex);

    responseFromServer addSearchRecord(Integer accountId, String content);

    responseFromServer deleteAllSearchRecords(Integer accountId);

    responseFromServer deleteOneSearchRecord(Integer accountId, Integer searchId);
}
