package com.hjrpc.enums.bean;

import com.hjrpc.enums.AutoEnum;
import lombok.Data;

import java.util.Set;

@Data
public class ClassSetBean {
    private Set<Class<AutoEnum>> autoEnumClassSet;
}
