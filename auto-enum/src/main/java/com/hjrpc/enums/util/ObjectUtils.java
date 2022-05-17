package com.hjrpc.enums.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectUtils {
    public static <T> T objParse(Class<T> returnClass, Object value) {
        try {
            if (value.getClass().equals(returnClass)) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.convertValue(value, returnClass);
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}