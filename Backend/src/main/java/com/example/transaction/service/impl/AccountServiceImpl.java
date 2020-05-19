package com.example.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.transaction.dao.A2aDAO;
import com.example.transaction.dao.AccountDAO;
import com.example.transaction.pojo.A2a;
import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.CommodityImage;
import com.example.transaction.service.AccountService;
import com.example.transaction.service.TokenService;
import com.example.transaction.util.FileUtil;
import com.example.transaction.util.code.ResourcePath;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName: AccountServiceImpl
 * @Author: 曾志昊
 * @Date: 2020/4/25 17:52
 */
@Service("AccountService")
public class AccountServiceImpl implements AccountService {
    private AccountDAO accountDAO;
    private A2aDAO a2aDAO;
    private TokenService tokenService;

    @Autowired
    public AccountServiceImpl(AccountDAO accountDAO, A2aDAO a2aDAO, TokenService tokenService) {
        this.accountDAO = accountDAO;
        this.a2aDAO = a2aDAO;
        this.tokenService = tokenService;
    }

    @Override
    public responseFromServer test(HttpServletRequest request) {
        return responseFromServer.success();
    }


    /**
     * 登录 调用loginservice操作
     * @param account
     * @return
     */
    @Override
    public responseFromServer login(Account account) {
        responseFromServer response = selectByUserName(account.getUsername());
        if (response.isSuccess()) {
            Account account1 = (Account) response.getData();
            if (account.getPassword().equals(account1.getPassword())) {
                return tokenService.loginOperationOnToken(account1);
            } else {
                return responseFromServer.error();
            }
        } else {
            return responseFromServer.error();
        }
    }

    /**
     * 退登 调用tokenservice操作
     * @param account
     * @return
     */
    @Override
    @Transactional
    public responseFromServer logout(Account account) {
        if (tokenService.logoutOperationOnToken(account).isFailure()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }


    /**
     * 注册用户
     * @param newAccout
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public responseFromServer register(Account newAccout) {
        accountDAO.insert(newAccout);
        return responseFromServer.success();
    }

    /**
     * 更新用户信息
     * @param account
     * @return
     */
    @Override
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
    @Override
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
    @Override
    public responseFromServer verifyUserName(String userName){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",userName);

        int count = accountDAO.selectCount(queryWrapper);
        if(count==0){
            /*该用户名可以使用*/
            return responseFromServer.success();
        }
        return responseFromServer.error();
    }

    /**
     * 根据用户名返回账号  ==>  Shiro
     * @param userName
     * @return responseFromServer
     */
    @Override
    public responseFromServer selectByUserName(String userName){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",userName);
        List<Account> account = accountDAO.selectList(queryWrapper);
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
    public responseFromServer getA2a(Integer accountId1,Integer accountId2) {
        if (accountId1 == null || accountId2 == null) {
            return responseFromServer.error();
        }
        A2a a2a = a2aDAO.getA2a(accountId1, accountId2);
        if (a2a == null) {
            return responseFromServer.error();
        }
        return responseFromServer.success(a2a);
    }

    /**
     * @Description: 上传头像, 并且更新到数据库中, 返回头像图片文件名
     * @Date: 2020/5/18 16:56
     */
    @Override
    public responseFromServer uploadAvatar(MultipartFile file, Integer accountId) {
        responseFromServer response = FileUtil.checkImageFile(file, true);
        if (response.isFailure()) {
            return response;
        }
        String filename = (String) response.getData();
        Account account = new Account(accountId);
        account.setAvatarUrl(filename);
        if (accountDAO.updateById(account) != 1) {
            /*插入数据库错误*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return FileUtil.saveFile(file, true, filename);
    }


}
