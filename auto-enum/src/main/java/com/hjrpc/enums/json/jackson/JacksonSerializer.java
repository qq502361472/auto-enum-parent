package com.hjrpc.enums.json.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hjrpc.enums.AutoEnum;
import com.hjrpc.enums.converter.EnumConverter;

import java.io.IOException;
import java.io.Serializable;

public class JacksonSerializer extends JsonSerializer<AutoEnum> implements Serializable {
    private final EnumConverter enumConverter;

    public JacksonSerializer(EnumConverter enumConverter) {
        this.enumConverter = enumConverter;
    }

    @Override
    public void serialize(AutoEnum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeObject(null);
            return;
        }
        gen.writeObject(enumConverter.getObjectMap(value));
    }
}