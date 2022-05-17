package com.hjrpc.enums;

import com.hjrpc.enums.cache.AutoEnumCache;
import com.hjrpc.enums.util.AutoEnumSpringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

public class SpringMvcEnumConverter<T extends AutoEnum> implements Converter<String, T> {
    private final Class<T> clazz;

    public SpringMvcEnumConverter(Class<T> enumType) {
        this.clazz = enumType;
    }

    @Override
    public T convert(@NonNull String source) {
        AutoEnumCache autoEnumCache = AutoEnumSpringUtils.getBean(AutoEnumCache.class);
        return autoEnumCache.getEnum(source, clazz);
    }
}