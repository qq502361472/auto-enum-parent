package com.hjrpc.enums.cache;

import com.hjrpc.enums.AutoEnum;
import com.hjrpc.enums.util.ObjectUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultAutoEnumCache implements AutoEnumCache {
    Map<Object, AutoEnum> enumCache = new ConcurrentHashMap<>();
    final Object obj = new Object();

    @Override
    public <T extends AutoEnum> T getEnum(String code, Class<T> clazz) {
        String key = clazz.getName() + "-" + code;
        AutoEnum esopEnum = enumCache.get(key);
        if (Objects.isNull(esopEnum)) {
            synchronized (obj) {
                esopEnum = enumCache.get(key);
                if (Objects.isNull(esopEnum)) {
                    AutoEnum[] enums = clazz.getEnumConstants();
                    for (AutoEnum customEnum : enums) {
                        if (customEnum.getKey().toString().equals(code)) {
                            esopEnum = customEnum;
                            enumCache.put(key, esopEnum);
                            return ObjectUtils.objParse(clazz, esopEnum);
                        }
                    }
                }
            }
        }
        return ObjectUtils.objParse(clazz, esopEnum);
    }
}