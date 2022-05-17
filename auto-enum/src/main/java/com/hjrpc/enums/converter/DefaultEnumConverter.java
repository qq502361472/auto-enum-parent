package com.hjrpc.enums.converter;

import com.hjrpc.enums.AutoEnum;
import com.hjrpc.enums.cache.AutoEnumCache;
import com.hjrpc.enums.constant.AutoConstant;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultEnumConverter implements EnumConverter {
    private final AutoEnumCache autoEnumCache;

    public DefaultEnumConverter(AutoEnumCache autoEnumCache) {
        this.autoEnumCache = autoEnumCache;
    }

    @Override
    public <T extends AutoEnum> T constructEnum(String code, Class<T> clazz) {
        if (code == null || "".equals(code)) {
            return null;
        }
        return autoEnumCache.getEnum(code, clazz);
    }

    @Override
    public Map<String, Object> getObjectMap(String code, Object value) {
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put(AutoConstant.CODE_STRING, code);
        map.put(AutoConstant.VALUE_STRING, value);
        return map;
    }

    @Override
    public Map<String, Object> getObjectMap(AutoEnum autoEnum) {
        if (autoEnum == null) {
            return null;
        }
        return getObjectMap(String.valueOf(autoEnum.getKey()), autoEnum.getValue());
    }
}