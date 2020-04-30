package com.example.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.transaction.dao.A2aDAO;
import com.example.transaction.dao.AccountDAO;
import com.example.transaction.pojo.A2a;
import com.example.transaction.pojo.Account;
import com.example.transaction.service.AccountService;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: AccountServiceImpl
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/25 17:52
 */
@Service("AccountService")
public class AccountServiceImpl implements AccountService {
    private AccountDAO accountDAO;
    private A2aDAO a2aDAO;
    @Autowired
    public AccountServiceImpl(AccountDAO accountDAO, A2aDAO a2aDAO){
        this.accountDAO = accountDAO;
        this.a2aDAO = a2aDAO;
    }


    /**
     * 注册用户  TODO：完成校验
     * @param newAccout
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public responseFromServer register(Account newAccout){
        accountDAO.insert(newAccout);
        return responseFromServer.success();
    }

    /**
     * 更新用户信息
     * @param account
     * @return
     */
    @Transactional
    public responseFromServer updateAccount(Account account){
        if(accountDAO.updateById(account)!=1){
            /*回滚事务*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }else{
            return responseFromServer.success();
        }
    }

    /**
     * (修改密码时)验证密码是否一致
     * @param account
     * @return
     */
    public responseFromServer verifyPassword(Account account){
        Account account1 = accountDAO.selectById(account.getId());
        /*检验密码*/
        if(account.getPassword()!=null && account.getPassword().equals(account1.getPassword())){
            return responseFromServer.error();
        }else{
            return responseFromServer.success();
        }
    }

    /**
     * 检查是否存在对应的用户名
     * @param userName
     * @return
     */
    public responseFromServer verifyUserName(String userName){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",userName);
        if(accountDAO.selectCount(queryWrapper)!=1){
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }

    /**
     * 根据用户名返回账号  ==>  Shiro
     * @param userName
     * @return responseFromServer
     */
    public responseFromServer selectByUserName(String userName){
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("username",userName);
        List<Account> account = accountDAO.selectByMap(queryMap);
        if(account.size()!=1){
            return responseFromServer.error();
        }else{
            Account account1 = account.get(0);
            return responseFromServer.success(account1);
        }
    }


    /**
     * 获取a2a
     * @param accountId1
     * @param accountId2
     * @return
     */
    @Override
    public responseFromServer getA2a(Integer accountId1,Integer accountId2){
        if(accountId1==null||accountId2 == null)
            return responseFromServer.error();
        A2a a2a = a2aDAO.getA2a(accountId1,accountId2);
        if(a2a==null)return responseFromServer.error();
        return responseFromServer.success(a2a);
    }


}
