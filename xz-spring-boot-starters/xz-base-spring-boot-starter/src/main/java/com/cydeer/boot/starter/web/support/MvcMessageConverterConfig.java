package com.cydeer.boot.starter.web.support;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author song.z
 * @date 2022/1/11 8:33 上午
 */
@Configuration
public class MvcMessageConverterConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> c : converters) {
            if (c instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) c;
                ObjectMapper objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
                objectMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

                // 注册Long专用的json转换器
                SimpleModule module = new SimpleModule();
                //修复Long类型太长，丢失精度问题
                module.addSerializer(Long.class, com.fasterxml.jackson.databind.ser.std.ToStringSerializer.instance);
                module.addSerializer(Long.TYPE, com.fasterxml.jackson.databind.ser.std.ToStringSerializer.instance);
                module.addSerializer(BigDecimal.class,
                                     com.fasterxml.jackson.databind.ser.std.ToStringSerializer.instance);
                module.addSerializer(BigInteger.class,
                                     com.fasterxml.jackson.databind.ser.std.ToStringSerializer.instance);
                module.addSerializer(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE);
                module.addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE);
                objectMapper.registerModule(module);

            }
        }
        // ResponseBody不带引号返回, 所以StringHttpMessageConverter一定要放在FastJson前面先注册
        converters.add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        // 使用FastJson解析JSON数据
        FastJsonHttpMessageConverter fastConvert = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        SerializeConfig serializeConfig = fastJsonConfig.getSerializeConfig();
        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        fastConvert.setFastJsonConfig(fastJsonConfig);
        //application/json;charset=UTF-8 交给FastJson处理
        fastConvert.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        converters.add(1, fastConvert);
    }
}
