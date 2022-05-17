package com.hjrpc.enums.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.util.TypeUtils;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hjrpc.enums.AutoEnum;
import com.hjrpc.enums.bean.ClassSetBean;
import com.hjrpc.enums.cache.AutoEnumCache;
import com.hjrpc.enums.cache.DefaultAutoEnumCache;
import com.hjrpc.enums.converter.DefaultEnumConverter;
import com.hjrpc.enums.converter.EnumConverter;
import com.hjrpc.enums.json.fastjson.FastJsonSerializerAndDeserializer;
import com.hjrpc.enums.json.jackson.JacksonDeserializer;
import com.hjrpc.enums.json.jackson.JacksonSerializer;
import com.hjrpc.enums.util.AutoEnumSpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;

@Configuration
@ConditionalOnBean(ClassSetBean.class)
public class AutoEnumAutoConfiguration {
    private final ClassSetBean classSetBean;

    @Autowired(required = false)
    public AutoEnumAutoConfiguration(ClassSetBean classSetBean) {
        this.classSetBean = classSetBean;
    }

    @Bean
    @ConditionalOnClass(JSON.class)
    @ConditionalOnProperty(name = "spring.disableFastJson", havingValue = "false", matchIfMissing = true)
    public HttpMessageConverters fastJsonHttpMessageConverter(@Value("${spring.jackson.serialization.WRITE_DATES_AS_TIMESTAMPS:false}") boolean writeDatesAsTimestamps) {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        TypeUtils.compatibleWithJavaBean = true;
        //配置
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);
        FastJsonSerializerAndDeserializer fastJsonSerializerAndDeserializer = new FastJsonSerializerAndDeserializer(enumConverter());
        ParserConfig parserConfig = fastJsonConfig.getParserConfig();
        for (Class<AutoEnum> autoEnumClass : classSetBean.getAutoEnumClassSet()) {
            parserConfig.putDeserializer(autoEnumClass, fastJsonSerializerAndDeserializer);
        }
        SerializeConfig serializeConfig = new SerializeConfig();
        for (Class<AutoEnum> autoEnumClass : classSetBean.getAutoEnumClassSet()) {
            serializeConfig.put(autoEnumClass, fastJsonSerializerAndDeserializer);
        }
        fastJsonConfig.setSerializeConfig(serializeConfig);
        if (!writeDatesAsTimestamps) {
            fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        }
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        return new HttpMessageConverters(fastJsonHttpMessageConverter);
    }

    @Bean
    @ConditionalOnMissingBean(HttpMessageConverters.class)
    public SimpleModule simpleModule() {
        SimpleModule simpleModule = new SimpleModule();
        JacksonSerializer jacksonSerializer = new JacksonSerializer(enumConverter());
        Set<Class<AutoEnum>> autoEnumClassSet = classSetBean.getAutoEnumClassSet();
        autoEnumClassSet.forEach(clazz -> {
            simpleModule.addSerializer(clazz, jacksonSerializer);
            JacksonDeserializer<AutoEnum> autoEnumJacksonDeserializer = new JacksonDeserializer<>(clazz, enumConverter());
            simpleModule.addDeserializer(clazz, autoEnumJacksonDeserializer);
        });
        return simpleModule;
    }

    /**
     * 注册springmvc的类型转换器配置类 * * @return EnumWebMvcConfig
     */
    @Bean
    public EnumWebMvcConfig enumWebMvcConfig() {
        return new EnumWebMvcConfig();
    }

    @Bean
    public EnumMybatisConfig enumMybatisConfig() {
        return new EnumMybatisConfig(classSetBean.getAutoEnumClassSet());
    }

    @Bean
    public AutoEnumCache autoEnumCache() {
        return new DefaultAutoEnumCache();
    }

    @Bean
    public AutoEnumSpringUtils autoEnumSpringUtils() {
        return new AutoEnumSpringUtils();
    }

    @Bean
    @ConditionalOnMissingBean(EnumConverter.class)
    public EnumConverter enumConverter() {
        return new DefaultEnumConverter(autoEnumCache());
    }
}
