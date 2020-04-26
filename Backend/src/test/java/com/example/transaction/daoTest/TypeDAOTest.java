package com.example.transaction.daoTest;

import com.example.transaction.dao.TypeDAO;
import com.example.transaction.pojo.Type;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author: 高战立
 * @Date: 2020/4/24 21:05
 * @Content: 测试TypeDAO接口
 */

@SpringBootTest
public class TypeDAOTest {

    @Autowired
    private TypeDAO typeDao;

    @Test
    void testGetAllTypeByCommodityId(){
        List<Type> types = typeDao.getAllTypeByCommodityId(1);
        System.out.println(types.get(0));
    }

    @Test
    void testInsert(){
        Type type = new Type();
        type.setId(3);
        type.setValue("娱乐");
        type.setCommodityId(1);

        int result = typeDao.insert(type);
        System.out.println(result);
    }

    @Test
    void testUpdate(){
        Type type = new Type();
        type.setId(2);
        type.setValue("生活用品");
        int result = typeDao.updateById(type);
        System.out.println(result);
    }

    @Test
    void testDelete(){
//        Type type = typeDao.selectById(2);
        typeDao.deleteById(2);
    }
}
