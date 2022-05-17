package com.hjrpc.enums.converter;

import com.hjrpc.enums.AutoEnum;

import java.util.Map;

public interface EnumConverter {
    <T extends AutoEnum> T constructEnum(String code, Class<T> clazz);

    Map<String, Object> getObjectMap(String code, Object value);

    Map<String, Object> getObjectMap(AutoEnum autoEnum);
}