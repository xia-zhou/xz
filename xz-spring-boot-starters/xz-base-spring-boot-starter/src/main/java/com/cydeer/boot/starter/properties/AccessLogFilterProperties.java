package com.cydeer.boot.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.List;

/**
 * @author song.z
 * @date 2022/1/16 5:55 下午
 */
@ConfigurationProperties("xz.web.access.filter")
@Data
public class AccessLogFilterProperties {
    /**
     * 是否启用accessLog，默认启用
     */
    private boolean enable = true;

    /***
     * filter的 优先级
     */
    private int filterOrder = Ordered.HIGHEST_PRECEDENCE + 3;

    /**
     * 拦截哪些url
     */
    private List<String> urlPatterns = new ArrayList<>();

    /**
     * 不拦截哪些url
     */
    private List<String> excludeUrls = new ArrayList<>();
}
