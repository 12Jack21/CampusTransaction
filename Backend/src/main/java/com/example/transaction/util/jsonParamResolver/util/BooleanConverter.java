package com.example.transaction.util.jsonParamResolver.util;


import java.lang.reflect.Type;

/**
 * 布尔类型
 **/
public class BooleanConverter implements Converter {

    @Override
    public Object convert(Type clazz, Object value) {
        if (value == null) {
            return null;
        }
        if ((value instanceof String) && "".equals(String.valueOf(value))) {
            return null;
        }
        if (clazz == boolean.class || clazz == Boolean.class) {
            return Boolean.valueOf(String.valueOf(value));
        }
        throw new ClassCastException(clazz.getTypeName() + "can not cast Boolean type!");
    }

    @Override
    public boolean support(Class<?> clazz) {
        return ConverterUtil.isBooleanType(clazz);
    }
}
