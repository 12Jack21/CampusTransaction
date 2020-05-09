package com.example.transaction.controller;

import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Search;
import com.example.transaction.service.SearchService;
import com.example.transaction.util.responseFromServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @ClassName: SearchController
 * @Author: 曾志昊
 * @Date: 2020/4/29 1:02
 */
@RestController
@RequestMapping("/searchs")
@Api(tags = "SearchController")
public class SearchController {

    @Autowired
    SearchService searchService;

    @ApiOperation(value = "添加搜索记录")
    @ApiImplicitParam(name = "search", value = "历史记录请求体", paramType = "Search", dataType = "Search")
    @PostMapping
    public responseFromServer addSearchRecord(@RequestBody Search search, HttpServletRequest request){
        if(search.getContent()==null)
            return responseFromServer.error();
        /*todo 修改*/
//        return searchService.addSearchRecord(
//                ((Account)session.getAttribute("currentAccount")).getId(),
//                search.getContent()
//        );
        return responseFromServer.error();

    }

}
