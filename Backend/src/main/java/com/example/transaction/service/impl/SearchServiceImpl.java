package com.example.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.AccountDAO;
import com.example.transaction.dao.SearchDAO;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Search;
import com.example.transaction.service.SearchService;
import com.example.transaction.util.MyPage;
import com.example.transaction.util.code.Nums;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * @ClassName: SearchServiceImpl
 * @Author: 曾志昊
 * @Date: 2020/4/29 1:03
 */
@Service("SearchService")
public class SearchServiceImpl implements SearchService {


    /**
     * 获取搜索记录分页
     *
     * @param accountId
     * @param pageIndex
     * @return
     */
    @Override
    public responseFromServer getSearchRecordsPage(Integer accountId, Integer pageIndex) {
        pageIndex = pageIndex == null || pageIndex < 0 ? 1 : pageIndex;
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account_id", accountId);
        queryWrapper.orderByDesc("update_time");
        Page<Search> page = new Page<>(pageIndex, Nums.pageSize);
        IPage<Search> searchIPage = searchDAO.getSearchPage(page, queryWrapper);
        MyPage resultPage = new MyPage(searchIPage);
        return responseFromServer.success(resultPage);
    }

    @Override
    @Transactional
    public responseFromServer addSearchRecord(Integer accountId, String searchStr) {
        Search search = new Search();
        search.setAccountId(accountId);
        search.setContent(searchStr);
        if (searchDAO.insert(search) != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }

    @Override
    public responseFromServer deleteAllSearchRecords(Integer accountId) {
        searchDAO.deleteAllSearchByAccountId(accountId);
        return responseFromServer.success();
    }

    /**
     * 删除一条搜索记录，需要保证该搜索记录是对应的用户
     *
     * @param accountId
     * @param searchId
     * @return
     */
    @Override
    public responseFromServer deleteOneSearchRecord(Integer accountId, Integer searchId) {
        if (searchId == null || accountId == null) {
            return responseFromServer.error();
        } else if (searchDAO.deleteOneSearchByAccountId(accountId, searchId) != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }


    private SearchDAO searchDAO;
    private AccountDAO accountDAO;

    @Autowired
    public SearchServiceImpl(SearchDAO searchDAO, AccountDAO accountDAO) {
        this.searchDAO = searchDAO;
        this.accountDAO = accountDAO;
    }
}
