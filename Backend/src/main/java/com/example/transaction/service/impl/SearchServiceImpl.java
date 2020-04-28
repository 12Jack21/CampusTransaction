package com.example.transaction.service.impl;

import com.example.transaction.dao.SearchDAO;
import com.example.transaction.service.SearchService;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: SearchServiceImpl
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/29 1:03
 */
@Service("SearchService")
public class SearchServiceImpl implements  SearchService{


    public responseFromServer getSearchRecordsPage(Integer accountId){
        return null;
    }

    public responseFromServer addSearchRecord(Integer accountId, String searchStr){
        return null;
    }

    public responseFromServer deleteAllSearchRecords(Integer accountId){
        return null;
    }

    public responseFromServer deleteOneSearchRecord(Integer accountId,Integer searchId){
        /*检查对应searchid的用户是否是当前登录用户*/
        return null;
    }


    private SearchDAO searchDAO;

    @Autowired
    public SearchServiceImpl(SearchDAO searchDAO){
        this.searchDAO = searchDAO;
    }
}
