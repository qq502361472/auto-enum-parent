package com.hjrpc.enums.json.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hjrpc.enums.AutoEnum;
import com.hjrpc.enums.constant.AutoConstant;
import com.hjrpc.enums.converter.EnumConverter;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class JacksonDeserializer<T extends AutoEnum> extends JsonDeserializer<T> {
    private final Class<T> clazz;
    private final EnumConverter enumConverter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JacksonDeserializer(Class<T> clazz, EnumConverter enumConverter) {
        this.clazz = clazz;
        this.enumConverter = enumConverter;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException {
        String code = p.getText();
        if (code == null || "".equals(code.trim())) {
            return null;
        }
        T t = enumConverter.constructEnum(code, clazz);
        if (Objects.isNull(t)) {
            Map<?, ?> map = objectMapper.readValue(code, Map.class);
            if (Objects.isNull(map)) {
                return null;
            }
            code = (String) map.get(AutoConstant.CODE_STRING);
            t = enumConverter.constructEnum(code, clazz);
        }
        return t;
    }
}