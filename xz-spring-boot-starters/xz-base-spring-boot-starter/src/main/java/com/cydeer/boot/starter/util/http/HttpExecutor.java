package com.cydeer.boot.starter.util.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.Function;

/**
 * @author song.z
 * @date 2022/1/16 9:58 下午
 */
public interface HttpExecutor {
    /**
     * GET 请求入口
     *
     * @param url 请求地址
     * @return 请求执行器
     */
    GetExecutor get(String url);

    /**
     * POST 请求入口
     *
     * @param url 请求地址
     * @return 请求执行器
     */
    PostExecutor post(String url);


    interface Executor<T> {

        /**
         * 添加头信息
         *
         * @param header {@link HttpHeaders}
         * @return 请求自身
         */
        T addHeader(final Header header);

        /**
         * 添加头信息
         *
         * @param name  {@link HttpHeaders}
         * @param value value值
         * @return 请求自身
         */
        T addHeader(String name, String value);

        /**
         * 请求各种参数扩展
         *
         * @param config 参见{@link RequestConfig}
         * @return 请求自身
         */
        T setConfig(final RequestConfig config);

        /**
         * 设置编码
         *
         * @param charset 输入参数编码设置
         * @return 自身
         */
        T reqCharset(Charset charset);

        /**
         * HTTP响应体的默认编码将以 Content-Type 中的字符集为准，
         *
         * @param charset 响应数据编码设置
         * @return 请求自身
         */
        T respCharset(Charset charset);

        /**
         * 设置是否支持GZip
         *
         * @param gzip 是否支持gzip
         * @return 请求自身
         */
        T gzip(boolean gzip);

        /**
         * 添加参数
         *
         * @param name  参数名称
         * @param value 参数值
         * @return 请求自身
         */
        T addParam(String name, String value);

        /**
         * 以字符串的方式返回结果
         *
         * @return 以字符串形式返回body结果集
         */
        String executeAsString();

        /**
         * 以二进制方式返回结果
         *
         * @return 以二进制形式返回body结果集
         */
        byte[] executeAsByte();

        /**
         * 针对高级用户使用
         *
         * @param result 结果处理器
         * @param <R>    结果
         * @return 自定义形式返回body结果集
         * @throws IOException IO异常
         */
        <R> R execute(Function<HttpResponse, R> result) throws IOException;

    }

    interface GetExecutor extends Executor<GetExecutor> {

    }

    interface PostExecutor extends Executor<PostExecutor> {

        /**
         * <pre>
         * 添加POST请求中的表单参数，默认将以 UrlEncodedFormEntity 对象存入 request 的 entity 值中。
         * 注意：与 {@link #setEntity(HttpEntity)} 不能同时使用
         * @param name 表单参数
         * @param value 表单值
         * @return 请求自身
         */
        PostExecutor addFormParam(String name, String value);

        /**
         * 设置请求参数
         *
         * @param params 请求参数
         * @return 请求自身
         */
        PostExecutor addFormParam(Map<String, String> params);

        /**
         * 设置POST请求的实体内容
         *
         * @param entity json实体内容
         * @return 请求自身
         */
        PostExecutor setEntity(final HttpEntity entity);

        /**
         * 设置Post请求的json数据 内部会使用StringEntity封装，默认使用UTF-8，以及Content-type:application/json
         *
         * @param json json内容
         * @return 请求自身
         */
        PostExecutor setBodyJson(final String json);

    }
}
