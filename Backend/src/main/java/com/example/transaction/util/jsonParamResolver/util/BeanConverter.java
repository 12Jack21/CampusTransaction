package com.example.transaction.util.jsonParamResolver.util;


import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * 普通javabean
 *
 * @author: qiumin
 **/
public class BeanConverter implements Converter {

    /**
     * 只支持bean json 转 javabean对象，即value为json字符串
     *
     * @param clazz
     * @param value
     * @return
     */
    @Override
    public Object convert(Type clazz, Object value) {
        if (value == null) {
            return null;
        }
        if ((value instanceof String) && "".equals(String.valueOf(String.valueOf(value)))) {
            return null;
        }
        try {
            Gson gson = new Gson();
            return gson.fromJson(String.valueOf(value), clazz);
        } catch (Exception e) {
            throw new ClassCastException(clazz.getTypeName() + " can not cast Bean type!");
        }
    }

    @Override
    public boolean support(Class<?> clazz) {
        return ConverterUtil.isBeanType(clazz);
    }
}
