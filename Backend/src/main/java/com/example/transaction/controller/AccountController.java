package com.example.transaction.controller;

import com.example.transaction.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: AccountController
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/25 17:57
 */
@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;






}
