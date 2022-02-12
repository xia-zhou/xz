package com.cydeer.boot.starter.properties;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author song.z
 * @date 2022/2/12 9:33 下午
 */

@ConfigurationProperties(prefix = "xz.http.convert")
@Data
public class HttpConverterProperties {

    /**
     * JSON 序列化包含的方式
     */
    private JsonInclude.Include serializationInclusion = JsonInclude.Include.NON_NULL;

    /**
     * 请求结果是否开启long转为string
     */
    private Boolean long2StringEnable = Boolean.TRUE;

    /**
     * 请求结果是否开启bigInteger转为string
     */
    private Boolean bigInteger2StringEnable = Boolean.TRUE;

    /**
     * 请求结果是否开启bigDecimal转为string
     */
    private Boolean bigDecimal2StringEnable = Boolean.TRUE;

    /**
     * 请求结果是否开启localDate转为string eg：2022-01-12
     */
    private Boolean localDate2StringEnable = Boolean.TRUE;

    /**
     * 请求结果是否开启localTime转为string eg: 08:12:18
     */
    private Boolean localTime2StringEnable = Boolean.TRUE;

    /**
     * 请求结果是否开启localDateTime转为时间戳 毫秒数
     */
    private Boolean localDateTime2TimestampEnable = Boolean.TRUE;

}
