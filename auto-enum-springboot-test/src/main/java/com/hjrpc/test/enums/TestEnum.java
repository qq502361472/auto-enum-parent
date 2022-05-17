package com.hjrpc.test.enums;

import com.hjrpc.enums.AutoEnum;
import lombok.Getter;

@Getter
public enum TestEnum implements AutoEnum {
    YES(0, "是"), NO(1, "否");
    private final Integer key;
    private final String value;

    TestEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }
}
