package com.cydeer.boot.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.List;

/**
 * @author song.z
 */
@Data
@ConfigurationProperties("xz.user.interceptor")
public class UserInterceptorProperties {

    /**
     * 包括哪些url
     */
    private List<String> includePatterns = new ArrayList<>();

    /**
     * 排除哪些url
     */
    private List<String> excludePatterns = new ArrayList<>();

    /**
     * 用户拦截器的优先级
     */
    private int order = Ordered.HIGHEST_PRECEDENCE + 3;
}
