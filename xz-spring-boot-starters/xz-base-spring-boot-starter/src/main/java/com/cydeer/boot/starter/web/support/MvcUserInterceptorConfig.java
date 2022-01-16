package com.cydeer.boot.starter.web.support;

import com.cydeer.boot.starter.properties.UserInterceptorProperties;
import com.cydeer.boot.starter.web.user.IUserProcessor;
import com.cydeer.boot.starter.web.user.UserInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author song.z
 * @date 2022/1/11 8:33 上午
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Configuration
@EnableConfigurationProperties({UserInterceptorProperties.class})
@ConditionalOnBean(IUserProcessor.class)
public class MvcUserInterceptorConfig implements WebMvcConfigurer {

    private final IUserProcessor iUserProcessor;

    private final UserInterceptorProperties userInterceptorProperties;

    public MvcUserInterceptorConfig(IUserProcessor iUserProcessor,
                                    UserInterceptorProperties userInterceptorProperties) {
        this.iUserProcessor = iUserProcessor;
        this.userInterceptorProperties = userInterceptorProperties;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 如果没有配置，默认拦截所有请求
        if (CollectionUtils.isEmpty(userInterceptorProperties.getIncludePatterns())) {
            userInterceptorProperties.getIncludePatterns().add("/**");
        }
        // 跟路径，health 和actuator路径不会解析header中的token
        registry.addInterceptor(new UserInterceptor(iUserProcessor)).addPathPatterns(
                userInterceptorProperties.getIncludePatterns()).excludePathPatterns(
                userInterceptorProperties.getExcludePatterns()).excludePathPatterns("/").excludePathPatterns(
                "/health/*").excludePathPatterns("/actuator/*");
    }

}
