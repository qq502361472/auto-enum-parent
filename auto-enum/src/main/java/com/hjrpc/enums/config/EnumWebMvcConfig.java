package com.hjrpc.enums.config;

import com.hjrpc.enums.SpringMvcConverterFactory;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class EnumWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new SpringMvcConverterFactory());
    }
}