package com.hjrpc.enums;

import com.hjrpc.enums.converter.EnumConverter;
import com.hjrpc.enums.util.AutoEnumSpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import javax.annotation.Resource;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ibatis 自动枚举入库和出库封装 * * @param <E>
 */
@Slf4j
public class MyBatisEnumTypeHandler<E extends AutoEnum> extends BaseTypeHandler<E> {
    private final Class<E> clazz;

    public MyBatisEnumTypeHandler(Class<E> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        //BaseTypeHandler 进行非空校验
        log.debug("index : {}, parameter : {},jdbcType : {} ", i, parameter.getKey(), jdbcType);
        if (jdbcType == null) {
            ps.setObject(i, parameter.getKey());
        } else {
            ps.setObject(i, parameter.getKey(), jdbcType.TYPE_CODE);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object code = rs.getObject(columnName);
        if (rs.wasNull()) {
            return null;
        }
        return getEnmByCode(code);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object code = rs.getObject(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        return getEnmByCode(code);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object code = cs.getObject(columnIndex);
        if (cs.wasNull()) {
            return null;
        }
        return getEnmByCode(code);
    }

    private E getEnmByCode(Object code) {
        if (code == null) {
            throw new NullPointerException("the result code is null ");
        }
        EnumConverter enumConverter = AutoEnumSpringUtils.getBean(EnumConverter.class);
        E e = enumConverter.constructEnum(String.valueOf(code), clazz);
        if (e == null) {
            throw new IllegalArgumentException("Unknown enumeration type , please check the enumeration code : " + code);
        }
        return e;
    }
}