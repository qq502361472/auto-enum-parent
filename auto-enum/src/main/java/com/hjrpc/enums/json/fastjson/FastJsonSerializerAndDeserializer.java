package com.hjrpc.enums.json.fastjson;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.hjrpc.enums.AutoEnum;
import com.hjrpc.enums.constant.AutoConstant;
import com.hjrpc.enums.converter.EnumConverter;

import java.io.IOException;
import java.lang.reflect.Type;

public class FastJsonSerializerAndDeserializer implements ObjectSerializer, ObjectDeserializer {
    private final EnumConverter enumConverter;

    public FastJsonSerializerAndDeserializer(EnumConverter enumConverter) {
        this.enumConverter = enumConverter;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Object parse = parser.parse(fieldName);
        if (parse == null) {
            return null;
        }
        String code;
        if (parse instanceof String || parse instanceof Integer) {
            code = String.valueOf(parse);
        } else if (parse instanceof JSONObject) {
            code = (String) ((JSONObject) parse).get(AutoConstant.CODE_STRING);
        } else {
            return null;
        }
        return (T) enumConverter.constructEnum(code, (Class<AutoEnum>) type);
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        serializer.write(enumConverter.getObjectMap((AutoEnum) object));
    }
}