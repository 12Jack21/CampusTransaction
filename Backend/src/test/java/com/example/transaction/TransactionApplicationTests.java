package com.example.transaction;

import com.example.transaction.dao.CommentDAO;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Type;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class TransactionApplicationTests {
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;
    @Test
    void testObject(){
        Type type = new Type();
        type.setId(1);
        type.setValue("电子");
        //存储对象
        redisTemplate.opsForValue().set(type.getId(), type);
        //取出对象
        Object object = redisTemplate.opsForValue().get(1);
        System.out.println((Type)object);
    }

    @Test
    // 存储数组
    void testList(){
        Type type = new Type();
        type.setId(1);
        type.setValue("电子");
        Type type2 = new Type();
        type2.setId(2);
        type2.setValue("生活");
        List<Type> types = new ArrayList<>();
        types.add(type);
        types.add(type2);

        Commodity commodity = new Commodity();
        commodity.setId(1);
        commodity.setName("电脑");
        commodity.setTypes(types);

        Commodity commodity2 = new Commodity();
        commodity2.setId(2);
        commodity2.setName("手机");
        commodity2.setTypes(types);

        List<Commodity> comddities = new ArrayList<>();
        comddities.add(commodity);
        comddities.add(commodity2);
        for(Commodity c:comddities)
            redisTemplate.opsForList().rightPush("commodites", c);
    }

    @Test
    //取出数组
    void testGetList(){
        List<Object> list = redisTemplate.opsForList().range("commodites", 0, -1);
        System.out.println(list.toString());
    }
}
