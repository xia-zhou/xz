package com.cydeer.boot.starter.util.http;

import com.cydeer.boot.starter.config.HttpClientAutoConfiguration;
import com.cydeer.boot.starter.constant.StarterErrorCodeEnum;
import com.cydeer.common.CommonException;
import com.cydeer.common.util.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author song.z
 * @date 2022/1/16 9:58 下午
 */
@Slf4j
public class HttpUtils {

    /**
     * GET 请求入口
     *
     * @param url 请举起地址
     * @return 请求执行器
     */
    public static HttpExecutor.GetExecutor get(String url) {
        return new GetExecutorImpl(url);
    }

    /**
     * POST 请求入口
     *
     * @param url 请求地址
     * @return 请求执行器
     */
    public static HttpExecutor.PostExecutor post(String url) {
        return new PostExecutorImpl(url);
    }


    public static abstract class ExecutorImpl<T> implements HttpExecutor.Executor<T> {
        protected static final String METHOD_GET = "get";
        protected static final String METHOD_POST = "post";
        protected HttpRequestBase request;
        protected List<NameValuePair> paramList = new ArrayList<>();
        protected Charset reqCharset = Charset.forName("UTF-8");
        protected Charset respCharset = Charset.forName("UTF-8");
        protected boolean gzip = true;

        /**
         * 添加头信息
         *
         * @param header {@link HttpHeaders}
         * @return 请求执行器
         */
        @Override
        public T addHeader(final Header header) {
            this.request.addHeader(header);
            return extracted();
        }

        /**
         * 添加头信息
         *
         * @param name  {@link HttpHeaders}
         * @param value 值
         * @return 请求执行器
         */
        @Override
        public T addHeader(String name, String value) {
            this.request.addHeader(name, value);
            return extracted();
        }

        /**
         * 请求各种参数扩展
         *
         * @param config 参见{@link RequestConfig}
         * @return 请求执行器
         */
        @Override
        public T setConfig(final RequestConfig config) {
            this.request.setConfig(config);
            return extracted();
        }

        /**
         * @param charset 输入参数编码设置
         * @return 请求执行器
         */
        @Override
        public T reqCharset(Charset charset) {
            this.reqCharset = charset;
            return extracted();
        }

        /**
         * @param charset 响应数据编码设置
         * @return 请求执行器
         */
        @Override
        public T respCharset(Charset charset) {
            this.respCharset = charset;
            return extracted();
        }

        /**
         * 设置是否支持GZip
         *
         * @param gzip 是否支持gzip
         * @return 请求执行器
         */
        @Override
        public T gzip(boolean gzip) {
            this.gzip = gzip;
            return extracted();
        }

        /**
         * 添加参数
         *
         * @param name  参数名
         * @param value 参数值
         * @return 请求执行器
         */
        @Override
        public T addParam(String name, String value) {
            if (!StringUtils.hasText(name)) {
                return extracted();
            }
            paramList.add(new BasicNameValuePair(name, value));
            return extracted();
        }

        /**
         * @return 以字符串形式返回body结果集
         */
        @Override
        public String executeAsString() {
            return this.execute(resp -> {
                try {
                    return EntityUtils.toString(resp.getEntity(), respCharset);
                } catch (IOException e) {
                    LogUtils.info(log, "请求IO异常", e);
                    throw new CommonException(StarterErrorCodeEnum.IO_EXCEPTION, e);
                }
            });
        }

        /**
         * @return 以二进制形式返回body结果集
         */
        @Override
        public byte[] executeAsByte() {
            return this.execute(resp -> {
                try {
                    return EntityUtils.toByteArray(resp.getEntity());
                } catch (IOException e) {
                    LogUtils.info(log, "请求IO异常", e);
                    throw new CommonException(StarterErrorCodeEnum.IO_EXCEPTION, e);
                }
            });
        }

        /**
         * 针对高级用户使用
         *
         * @return 自定义形式返回body结果集
         */
        @Override
        public <R> R execute(Function<HttpResponse, R> result) {
            try (CloseableHttpResponse response = realExecute()) {
                return result.apply(response);
            } catch (IOException e) {
                LogUtils.info(log, "请求IO异常", e);
                throw new CommonException(StarterErrorCodeEnum.IO_EXCEPTION, e);
            }
        }

        /**
         * 正式执行请求
         *
         * @return 执行结果
         */
        private CloseableHttpResponse realExecute() throws IOException {
            if (paramList.size() > 0) {
                // 处理参数
                String sb = this.request.getURI().toString() + (!StringUtils.hasText(
                        this.request.getURI().getRawQuery()) ? "?" : "&") + URLEncodedUtils.format(paramList,
                                                                                                   reqCharset);
                this.request.setURI(URI.create(sb));
            }
            if (this.gzip) {
                // 压缩处理
                this.addHeader(HttpHeaders.ACCEPT_ENCODING, "gzip");
            }
            if (this instanceof PostExecutorImpl) {
                //如果是POST请求
                PostExecutorImpl self = (PostExecutorImpl) this;
                if (self.postParamList != null && self.postParamList.size() > 0) {
                    //添加表单参数
                    if (((HttpPost) self.request).getEntity() != null) {
                        throw new IllegalArgumentException("POST请求中的 addFormParam 和 setEntity 不能同时被设置值");
                    }
                    self.setEntity(new UrlEncodedFormEntity(self.postParamList, Consts.UTF_8));
                }
            }
            // Log日志
            LogUtils.debug(log, "HTTP request: {}", this.request.getURI().toString());
            CloseableHttpResponse response = HttpClientAutoConfiguration.getClient().execute(request);
            if (this.gzip) {
                // 解压缩处理
                Header header = response.getEntity().getContentEncoding();
                if (header != null) {
                    HeaderElement[] codecs = header.getElements();
                    for (HeaderElement codec : codecs) {
                        if ("gzip".equalsIgnoreCase(codec.getName())) {
                            response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                            break;
                        }
                    }
                }
            }
            return response;
        }


        /**
         * 不同实现类返回的类型不同
         *
         * @return 请求执行器
         */
        protected abstract T extracted();

        /**
         * 获取请求方式
         *
         * @return 请求方式
         */
        protected abstract String getMethod();
    }

    public static class GetExecutorImpl extends ExecutorImpl<HttpExecutor.GetExecutor> implements HttpExecutor.GetExecutor {

        private GetExecutorImpl(String url) {
            this.request = new HttpGet(url);
        }

        @Override
        protected HttpExecutor.GetExecutor extracted() {
            return this;
        }

        @Override
        protected String getMethod() {
            return ExecutorImpl.METHOD_GET;
        }

    }

    public static class PostExecutorImpl extends ExecutorImpl<HttpExecutor.PostExecutor> implements HttpExecutor.PostExecutor {
        protected volatile List<NameValuePair> postParamList;

        private PostExecutorImpl(String url) {
            this.request = new HttpPost(url);
        }

        @Override
        public HttpExecutor.PostExecutor addFormParam(String name, String value) {
            if (!StringUtils.hasText(name)) {
                return this;
            }
            if (postParamList == null) {
                synchronized (PostExecutorImpl.class) {
                    if (postParamList == null) {
                        postParamList = new ArrayList<>();
                    }
                }
            }
            postParamList.add(new BasicNameValuePair(name, value));
            return this;
        }

        @Override
        public HttpExecutor.PostExecutor addFormParam(Map<String, String> params) {
            if (CollectionUtils.isEmpty(params)) {
                return this;
            }
            params.forEach(this::addFormParam);
            return this;
        }

        @Override
        public HttpExecutor.PostExecutor setEntity(final HttpEntity entity) {
            ((HttpPost) this.request).setEntity(entity);
            return this;
        }

        @Override
        public HttpExecutor.PostExecutor setBodyJson(String json) {
            StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
            entity.setContentType("application/json;charset=UTF-8");
            return setEntity(entity);
        }

        @Override
        protected HttpExecutor.PostExecutor extracted() {
            return this;
        }

        @Override
        protected String getMethod() {
            return ExecutorImpl.METHOD_POST;
        }
    }
}
