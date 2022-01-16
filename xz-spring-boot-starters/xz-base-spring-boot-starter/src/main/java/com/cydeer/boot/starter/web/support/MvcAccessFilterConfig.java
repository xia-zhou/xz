package com.cydeer.boot.starter.web.support;

import com.cydeer.boot.starter.properties.AccessLogFilterProperties;
import com.cydeer.boot.starter.web.log.AccessLogFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author song.z
 * @date 2022/1/16 5:56 下午
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Configuration
@ConditionalOnProperty(name = "xz.web.access.filter.enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({AccessLogFilterProperties.class})
public class MvcAccessFilterConfig {

    private final AccessLogFilterProperties accessLogFilterProperties;

    public MvcAccessFilterConfig(AccessLogFilterProperties accessLogFilterProperties) {
        this.accessLogFilterProperties = accessLogFilterProperties;
    }

    @Bean
    public FilterRegistrationBean slAccessLogFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        // 过滤掉的url filter本身不支持，在拦截器中做支持
        AccessLogFilter accessLogFilter = new AccessLogFilter(accessLogFilterProperties.getExcludeUrls());
        filterRegistrationBean.setFilter(accessLogFilter);

        // 获取要走拦截器的url
        List<String> urlPatterns = accessLogFilterProperties.getUrlPatterns();
        if (urlPatterns == null || urlPatterns.size() <= 0) {
            filterRegistrationBean.addUrlPatterns("/*");
        } else {
            filterRegistrationBean.setUrlPatterns(urlPatterns);
        }
        filterRegistrationBean.setName(accessLogFilter.getFilterName());
        filterRegistrationBean.setAsyncSupported(true);
        filterRegistrationBean.setOrder(accessLogFilterProperties.getFilterOrder());
        return filterRegistrationBean;
    }
}
