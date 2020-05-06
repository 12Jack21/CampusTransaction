package com.example.transaction.controller;

import com.example.transaction.dao.SearchDAO;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Search;
import com.example.transaction.service.SearchService;
import com.example.transaction.util.AccountVerify;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @ClassName: SearchController
 * @Author: 曾志昊
 * @Date: 2020/4/29 1:02
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    SearchService searchService;
    @RequestMapping("addSearchRecord")
    public responseFromServer addSearchRecord(@RequestBody Search search, HttpSession session){
        if(search.getContent()==null)
            return responseFromServer.error();
        return searchService.addSearchRecord(
                ((Account)session.getAttribute("currentAccount")).getId(),
                search.getContent()
        );

    }

}
