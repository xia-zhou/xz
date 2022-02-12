package com.cydeer.boot.starter.web.support;

import com.cydeer.boot.starter.properties.HttpConverterProperties;
import com.cydeer.boot.starter.web.support.serializer.XzLocalDateTimeJsonDeserializer;
import com.cydeer.boot.starter.web.support.serializer.XzLocalDateTimeSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * @author song.z
 * @date 2022/1/11 8:33 上午
 */
@Configuration
@EnableConfigurationProperties({HttpConverterProperties.class})
public class MvcMessageConverterConfig implements WebMvcConfigurer {

    private final HttpConverterProperties httpConverterProperties;

    public MvcMessageConverterConfig(HttpConverterProperties httpConverterProperties) {
        this.httpConverterProperties = httpConverterProperties;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> c : converters) {
            if (c instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) c;
                ObjectMapper objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
                objectMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
                objectMapper.setSerializationInclusion(httpConverterProperties.getSerializationInclusion());
                SimpleModule module = new SimpleModule();
                //修复Long类型太长，丢失精度问题
                if (httpConverterProperties.getLong2StringEnable()) {
                    module.addSerializer(Long.class, ToStringSerializer.instance);
                    module.addSerializer(Long.TYPE, ToStringSerializer.instance);
                }
                if (httpConverterProperties.getBigDecimal2StringEnable()) {
                    module.addSerializer(BigDecimal.class, ToStringSerializer.instance);
                }
                if (httpConverterProperties.getBigInteger2StringEnable()) {
                    module.addSerializer(BigInteger.class, ToStringSerializer.instance);
                }
                if (httpConverterProperties.getLocalDateTime2TimestampEnable()) {
                    module.addSerializer(LocalDateTime.class, new XzLocalDateTimeSerializer());
                }
                if (httpConverterProperties.getLocalDate2StringEnable()) {
                    module.addSerializer(LocalDate.class, ToStringSerializer.instance);
                }
                if (httpConverterProperties.getLocalTime2StringEnable()) {
                    module.addSerializer(LocalTime.class, ToStringSerializer.instance);
                }
                module.addDeserializer(LocalDateTime.class, new XzLocalDateTimeJsonDeserializer());
                objectMapper.registerModule(module);
            }
        }
    }
}
