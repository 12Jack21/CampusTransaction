package com.example.transaction.controller;

import com.example.transaction.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: NoticeController
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/26 16:44
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {


    @Autowired
    NoticeService noticeService;

}
