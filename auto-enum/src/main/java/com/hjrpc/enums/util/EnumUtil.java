package com.hjrpc.enums.util;

import com.hjrpc.enums.AutoEnum;
import com.hjrpc.enums.cache.AutoEnumCache;
import com.hjrpc.enums.constant.AutoConstant;
import com.hjrpc.enums.converter.EnumConverter;

import java.util.LinkedHashMap;
import java.util.Map;

public class EnumUtil {
    public static <T extends AutoEnum> T constructEnum(String code, Class<T> clazz) {
        if (code == null || "".equals(code)) {
            return null;
        }
        return AutoEnumSpringUtils.getBean(AutoEnumCache.class).getEnum(code, clazz);
    }

    public static Map<String, Object> getObjectMap(String code, Object value) {
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put(AutoConstant.CODE_STRING, code);
        map.put(AutoConstant.VALUE_STRING, value);
        return map;
    }

    public static Map<String, Object> getObjectMap(AutoEnum autoEnum) {
        return getObjectMap(String.valueOf(autoEnum.getKey()), autoEnum.getValue());
    }
}