package com.example.transaction.serviceTest;

import com.example.transaction.service.CommodityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommodityServiceTest {
    @Autowired
    CommodityService commodityService;

    @Test
    public void validateCommodityImage() {
        commodityService.validateCommodityImageUrl(1, "1.txt");
    }
}
