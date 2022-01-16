package com.cydeer.boot.starter.config;

import com.cydeer.boot.starter.properties.HttpClientProperties;
import com.cydeer.common.util.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.*;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * @author song.z
 * @date 2022/1/16 10:27 下午
 */
@Configuration
@EnableConfigurationProperties({HttpClientProperties.class})
@Slf4j
public class HttpClientAutoConfiguration {

    private final HttpClientProperties httpClientProperties;

    public HttpClientAutoConfiguration(HttpClientProperties httpClientProperties) {
        this.httpClientProperties = httpClientProperties;
    }

    /**
     * 可配置化的httpClient
     */
    private static CloseableHttpClient httpClient;

    /**
     * 默认的http客户端
     */
    private static CloseableHttpClient defaultHttpClient;


    /**
     * 获取httpClient，如果配置化的客户端还未初始化完成，则使用默认的客户端，配置化的HttpClient已经初始化完成则释放默认的HttpClient
     *
     * @return 客户端工具
     */
    public static CloseableHttpClient getClient() {
        if (httpClient != null) {
            // 有配置化的http client 则释放默认的客户端，让垃圾收集器回收
            defaultHttpClient = null;
            return httpClient;
        }
        LogUtils.info(log, "http client还未初始化,使用默认配置的client");
        if (defaultHttpClient != null) {
            // 默认client 已经构建过
            return defaultHttpClient;
        }

        // 构建默认的Http client
        HttpClientProperties defaultProperties = new HttpClientProperties();
        defaultHttpClient = build(defaultProperties);
        return defaultHttpClient;
    }

    @Bean
    public CloseableHttpClient getCloseableHttpClient() {
        return build(httpClientProperties);
    }

    private static CloseableHttpClient build(HttpClientProperties clientProperties) {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContexts.custom().loadTrustMaterial(KeyStore.getInstance(KeyStore.getDefaultType()),
                                                                (TrustStrategy) (chain, authType) -> true).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            log.warn(">> 初始化HttpClient配置失败 : ", e);
        }

        // 设置默认Socket Config : 包括可以指定默认的超时时间等等
        SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
        // Create message constraints
        MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200).setMaxLineLength(
                2000).build();
        // Create connection configuration : 包括可以指定默认的通信字符集等等
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8)
                .setMessageConstraints(messageConstraints)
                .build();

        // 重点是设置NoopHostnameVerifier.INSTANCE,解决异常：javax.net.ssl.SSLPeerUnverifiedException
        Registry<ConnectionSocketFactory> normalSocketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https",
                          new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"}, null,
                                                         new NoopHostnameVerifier()))
                .build();

        PoolingHttpClientConnectionManager normalCm = new PoolingHttpClientConnectionManager(
                normalSocketFactoryRegistry);
        normalCm.setDefaultConnectionConfig(connectionConfig);
        normalCm.setDefaultSocketConfig(socketConfig);
        normalCm.setMaxTotal(clientProperties.getPoolMaxTotal());
        normalCm.setDefaultMaxPerRoute(clientProperties.getPoolMaxPerRoute());
        // 默认请求配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(clientProperties.getConnectTimeout())
                .setSocketTimeout(clientProperties.getSocketTimeout())
                .setConnectionRequestTimeout(clientProperties.getConnectionRequestTimeout())
                .build();
        // 默认请求重试
        HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(clientProperties.getRetryTimes(),
                                                                                  false);
        // trace 拦截器
        httpClient = HttpClients.custom()
                .setRetryHandler(retryHandler)
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(normalCm)
                .evictExpiredConnections()
                .evictIdleConnections(5L, TimeUnit.SECONDS)
                .build();
        return httpClient;
    }
}
