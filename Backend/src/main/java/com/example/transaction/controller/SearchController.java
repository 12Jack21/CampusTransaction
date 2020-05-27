package com.example.transaction.controller;

import com.example.transaction.pojo.Account;
import com.example.transaction.service.SearchService;
import com.example.transaction.service.impl.AccountVerify;
import com.example.transaction.util.jsonParamResolver.handler.RequestJson;
import com.example.transaction.util.responseFromServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: SearchController
 * @Author: 曾志昊
 * @Date: 2020/4/29 1:02
 */
@RestController
@RequestMapping("/histories")
@Api(tags = "SearchController")
public class SearchController {

    @Autowired
    SearchService searchService;
    @Autowired
    AccountVerify accountVerify;

    /**
     * 获取搜索记录分页
     *
     * @param accountId
     * @param request
     * @return
     */
    @ApiOperation(value = "获取搜索记录分页")
    @ApiImplicitParam(name = "pageIndex", value = "分页下标", paramType = "Integer", dataType = "Integer")
    @GetMapping("/account/{accountId}")
    public responseFromServer getSearchRecordsPage(@PathVariable Integer accountId, HttpServletRequest request) {
        Account account = new Account(accountId);
        accountVerify.verifyWithReturn(account, request);
        return searchService.getSearchRecordsPage(account.getId(), 1);
    }

    @ApiOperation(value = "删除一条搜索记录")
    @ApiImplicitParam(name = "searchId", value = "搜索记录id", paramType = "Integer", dataType = "Integer")
    @DeleteMapping("/{searchId}")
    public responseFromServer deleteOneSearch(@PathVariable Integer searchId, HttpServletRequest request) {
        Account account = new Account();
        accountVerify.verifyWithReturn(account, request);
        return searchService.deleteOneSearchRecord(account.getId(), searchId);
    }


    @ApiOperation(value = "删除用户所有搜索记录")
    @ApiImplicitParam(name = "searchId", value = "搜索记录id", paramType = "Integer", dataType = "Integer")
    @DeleteMapping("/account/{accountId}")
    public responseFromServer deleteAllSearches(@PathVariable Integer accountId, HttpServletRequest request) {
        Account account = new Account(accountId);
        accountVerify.verifyWithReturn(account, request);
        return searchService.deleteAllSearchRecords(account.getId());
    }
}
