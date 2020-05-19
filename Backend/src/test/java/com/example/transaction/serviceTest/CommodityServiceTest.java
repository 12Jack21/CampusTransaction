package com.example.transaction.serviceTest;

import com.example.transaction.service.CommodityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @ClassName: CommodityServiceTest
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/5/19 14:56
 */
@SpringBootTest
public class CommodityServiceTest {
    @Autowired
    CommodityService commodityService;

    @Test
    public void validateCommodityImage() {
        commodityService.validateCommodityImageUrl(1, "1.txt");
    }
}
