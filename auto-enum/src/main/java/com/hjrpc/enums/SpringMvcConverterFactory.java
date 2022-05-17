package com.hjrpc.enums;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class SpringMvcConverterFactory implements ConverterFactory<String, AutoEnum> {
    @Override
    public <T extends AutoEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new SpringMvcEnumConverter<>(targetType);
    }
}