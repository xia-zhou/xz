package com.cydeer.boot.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author song.z
 * @date 2022/1/16 9:58 下午
 */
@ConfigurationProperties(prefix = "xz.http.client")
@Data
public class HttpClientProperties {
    /**
     * http 连接池的总大小
     */
    private int poolMaxTotal = 50;

    /**
     * 设计每个站点路由下允许的最大连接占用数（底层默认值为2）
     */
    private int poolMaxPerRoute = 50;

    /**
     * 网络失败后，重试请求的次数，默认值3次
     */
    private int retryTimes = 3;

    /**
     * 与目标站点创建连接的最大时间（底层默认值无限等待）
     */
    private int connectTimeout = 10 * 1000;

    /**
     * 设置与目标站点的等待数据的最大时间，包括连续数据传输的时间（底层默认值为-1，表示操作系统默认）
     */
    private int socketTimeout = 30 * 1000;

    /**
     * 从池中获取请求连接的时间（底层默认值无限等待）
     */
    private int connectionRequestTimeout = 5 * 1000;
}
