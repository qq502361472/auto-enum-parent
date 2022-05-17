package com.hjrpc.test.controller;

import com.hjrpc.test.enums.TestEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
@Slf4j
public class TestController {

    @GetMapping("{testValue}")
    public TestEnum getTestEnum(@PathVariable("testValue") TestEnum testEnum) {
        log.info("读取到的枚举为{}，key：{},value：{}", testEnum, testEnum.getKey(), testEnum.getValue());
        return testEnum;
    }
}
