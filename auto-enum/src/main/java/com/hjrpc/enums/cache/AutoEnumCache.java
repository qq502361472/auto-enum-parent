package com.hjrpc.enums.cache;

import com.hjrpc.enums.AutoEnum;

public interface AutoEnumCache {
    <T extends AutoEnum> T getEnum(String code, Class<T> clazz);
}
