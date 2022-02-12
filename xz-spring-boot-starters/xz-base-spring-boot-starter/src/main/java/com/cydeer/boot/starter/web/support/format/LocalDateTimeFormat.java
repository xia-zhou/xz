package com.cydeer.boot.starter.web.support.format;

import com.cydeer.boot.starter.web.support.serializer.XzString2LocalDateTimeUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;

/**
 * 支持输入的三种格式:
 * 第一种：2022-01-12T08:15:30
 * 第二种：2022-01-12 08:15:30
 * 第三种：时间戳
 *
 * @author song.z
 * @date 2022/2/12 4:21 下午
 */
@Component
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.ANY)
public class LocalDateTimeFormat implements Formatter<LocalDateTime> {


    @Override
    public LocalDateTime parse(String text, Locale locale) {
        return XzString2LocalDateTimeUtils.get(text);
    }

    @Override
    public String print(LocalDateTime object, Locale locale) {
        return String.valueOf(object.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }
}
