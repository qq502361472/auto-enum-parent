package com.hjrpc.enums.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.hjrpc.enums.AutoEnum;
import com.hjrpc.enums.MyBatisEnumTypeHandler;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.util.Set;

@Slf4j
@Setter
public class EnumMybatisConfig implements ConfigurationCustomizer {
    private Set<Class<AutoEnum>> autoEnumClasses;

    public EnumMybatisConfig(Set<Class<AutoEnum>> autoEnumClasses) {
        this.autoEnumClasses = autoEnumClasses;
    }

    @Override
    public void customize(Configuration configuration) {
        log.info("ConfigurationCustomizer init....");
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        for (Class<?> clazz : autoEnumClasses) {
            typeHandlerRegistry.register(clazz, MyBatisEnumTypeHandler.class);
            log.debug("扫描到实现 AutoEnum 的枚举类：{}，自动注册对应的类型转换器", clazz.getName());
        }
    }
}