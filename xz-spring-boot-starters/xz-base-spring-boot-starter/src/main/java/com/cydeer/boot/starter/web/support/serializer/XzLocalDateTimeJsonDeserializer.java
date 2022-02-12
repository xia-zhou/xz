package com.cydeer.boot.starter.web.support.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author song.z
 * @date 2022/2/12 10:04 下午
 */
public class XzLocalDateTimeJsonDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctx) throws IOException, JacksonException {
        return XzString2LocalDateTimeUtils.get(p.getValueAsString());
    }
}
