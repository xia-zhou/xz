package com.cydeer.boot.starter.web.support.format;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Locale;

/**
 * @author song.z
 * @date 2022/2/12 4:21 下午
 */
@Component
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.ANY)
public class LocalDateFormat implements Formatter<LocalDate> {
    @Override
    public LocalDate parse(String text, Locale locale) {
        if (!StringUtils.hasText(text)) {
            return null;
        }
        return LocalDate.parse(text);
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return object.toString();
    }
}
