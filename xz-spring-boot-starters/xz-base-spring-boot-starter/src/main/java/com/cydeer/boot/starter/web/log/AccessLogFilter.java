package com.cydeer.boot.starter.web.log;

import com.cydeer.boot.starter.web.user.UserContext;
import com.cydeer.common.util.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author song.z
 * @date 2022/1/16 6:01 下午
 */
public class AccessLogFilter extends OncePerRequestFilter {
    private final Logger LOGGER = LoggerFactory.getLogger("APP_ACCESS_PARAM");

    /**
     * 参数中最多获取的数据量
     */
    private final Integer payloadMaxLength = 1024;

    /**
     * 要过滤掉的URL
     */
    private final List<String> excludeUrls;

    public AccessLogFilter(List<String> excludeUrls) {
        if (CollectionUtils.isEmpty(excludeUrls)) {
            excludeUrls = new ArrayList<>();
        }
        excludeUrls.add("/");
        excludeUrls.add("/health/readiness");
        excludeUrls.add("/actuator/readiness");
        excludeUrls.add("/health/slb");
        this.excludeUrls = excludeUrls;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 过滤掉一些不希望记录输入输出的url
        if (excludeUrls.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(requestWrapper, responseWrapper);
        String requestPayload = getPayLoad(requestWrapper.getContentAsByteArray(), request.getCharacterEncoding());
        String responsePayload = getPayLoad(responseWrapper.getContentAsByteArray(), response.getCharacterEncoding());
        responseWrapper.copyBodyToResponse();
        LogUtils.info(LOGGER, "{},{},{},{}", UserContext.getKey(), request.getRequestURI(), requestPayload,
                      responsePayload);
    }

    private String getPayLoad(byte[] buf, String characterEncoding) {
        String payload = "";
        if (buf == null) {
            return payload;
        }
        if (buf.length > 0) {
            int length = Math.min(buf.length, payloadMaxLength);
            try {
                payload = new String(buf, 0, length, characterEncoding);
            } catch (UnsupportedEncodingException ex) {
                payload = "[unknown]";
            }
        }
        return payload;
    }

    @Override
    public String getFilterName() {
        return "AccessLogFilter";
    }
}
