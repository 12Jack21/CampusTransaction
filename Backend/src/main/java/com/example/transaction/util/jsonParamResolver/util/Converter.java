package com.example.transaction.util.jsonParamResolver.util;

import java.lang.reflect.Type;

/**
 * 类型转换接口
 **/
public interface Converter {

    /**
     * 将value转为clazz类型
     *
     * @param clazz
     * @param value
     * @return
     */
    Object convert(Type clazz, Object value);

    /**
     * 是否支持此种类型转换
     *
     * @param clazz
     * @return
     */
    boolean support(Class<?> clazz);
}
