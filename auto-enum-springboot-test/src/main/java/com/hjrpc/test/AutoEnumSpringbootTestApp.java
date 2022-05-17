package com.hjrpc.test;

import com.hjrpc.enums.EnableAutoEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableAutoEnum("com.hjrpc.test.enums")
public class AutoEnumSpringbootTestApp {
    public static void main(String[] args) {
        SpringApplication.run(AutoEnumSpringbootTestApp.class, args);
    }
}
