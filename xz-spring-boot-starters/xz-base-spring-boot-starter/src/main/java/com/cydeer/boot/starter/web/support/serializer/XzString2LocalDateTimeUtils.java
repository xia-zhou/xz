package com.cydeer.boot.starter.web.support.serializer;

import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author song.z
 * @date 2022/2/12 10:09 下午
 */
public class XzString2LocalDateTimeUtils {
    
    private final static String DEFAULT_SPLIT = "T";

    private final static String CUSTOMER_SPLIT = " ";

    private final static String CUSTOMER_LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 支持输入的三种格式:
     * 第一种：2022-01-12T08:15:30
     * 第二种：2022-01-12 08:15:30
     * 第三种：时间戳
     *
     * @param text 字符串，支持如上三种格式
     * @return localDateTime
     */
    public static LocalDateTime get(String text) {
        if (!StringUtils.hasText(text)) {
            return null;
        }
        if (text.contains(DEFAULT_SPLIT)) {
            return LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        if (text.contains(CUSTOMER_SPLIT)) {
            return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(CUSTOMER_LOCAL_DATE_TIME_FORMAT));
        }
        Long timestamp = NumberUtils.parseNumber(text, Long.class);
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }
}
