package com.hjrpc.enums;

import com.hjrpc.enums.bean.ClassSetBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class AutoEnumRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        log.info("Enum handler init ...");
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(EnableAutoEnum.class.getCanonicalName());
        Set<Class<?>> autoEnumClassSet = new HashSet<>();
        for (String pkg : (String[]) Objects.requireNonNull(attributes).get("value")) {
            if (StringUtils.hasText(pkg)) {
                autoEnumClassSet.addAll(convertToClass(pkg));
            }
        }
        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(ClassSetBean.class);
        definition.addPropertyValue("autoEnumClassSet", autoEnumClassSet);
        registry.registerBeanDefinition("classSetBean", definition.getBeanDefinition());
    }

    private Set<Class<?>> convertToClass(String packagePath) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Set<Class<?>> autoEnumClasses = new HashSet<>();
        packagePath = packagePath.replaceAll("\\.", "/");
        packagePath = "classpath*:" + packagePath + "/**/*.class";
        try {
            Resource[] resources = resolver.getResources(packagePath);
            for (Resource res : resources) {
                // 先获取resource的元信息，然后获取class元信息，最后得到 class 全路径
                String clsName = new SimpleMetadataReaderFactory().getMetadataReader(res).getClassMetadata().getClassName();
                // 通过名称加载
                Class<?> clazz = Class.forName(clsName);
                if (AutoEnum.class.isAssignableFrom(clazz) && !AutoEnum.class.equals(clazz)) {
                    autoEnumClasses.add(clazz);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalArgumentException("parse packagePath error : {}" + e);
        }
        return autoEnumClasses;
    }
}