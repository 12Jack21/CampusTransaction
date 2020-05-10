package com.example.transaction.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "com.example.transaction.pojo",
                "com.example.transaction.dao",
                "com.example.transaction.controller",
                "com.example.transaction.config",
                "com.example.transaction.service",
                "com.example.transaction.interceptor",
                "com.example.transaction.util"
        })
public class TransactionApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransactionApplication.class, args);
    }

}
