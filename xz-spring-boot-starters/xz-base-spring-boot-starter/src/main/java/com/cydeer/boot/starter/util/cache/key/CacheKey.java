package com.cydeer.boot.starter.util.cache.key;

import com.cydeer.boot.starter.constant.StarterErrorCodeEnum;
import com.cydeer.common.util.AssertUtils;
import org.springframework.util.StringUtils;

/**
 * @author song.z
 * @date 2022/1/16 10:54 下午
 */
public class CacheKey {
    /**
     * key的定义格式
     */
    private CacheKeyFormat cacheKeyFormat;

    /**
     * key格式中的占位符对应的参数信息
     */
    private Object[] args;

    protected CacheKey() {
    }

    private CacheKey(CacheKeyFormat cacheKeyFormat, Object[] args) {
        this.cacheKeyFormat = cacheKeyFormat;
        this.args = args;
    }

    /**
     * 构建cacheKey
     *
     * @param cacheKeyFormat key的定义格式
     * @param args           key定义格式中所需的参数信息
     * @return 封装好的cacheKey信息
     */
    public static CacheKey of(CacheKeyFormat cacheKeyFormat, Object... args) {
        // 暂不做日志记录 这里只是做保险校验，因为是枚举中的定义，很容易看到是否为空
        AssertUtils.isNotNull(cacheKeyFormat, StarterErrorCodeEnum.CACHE_KEY_IS_NULL);
        AssertUtils.hasText(cacheKeyFormat.getFormat(), StarterErrorCodeEnum.CACHE_KEY_IS_NULL);
        return new CacheKey(cacheKeyFormat, args);
    }

    /**
     * 生成最终的缓存key，格式：${applicationName}.${format}
     *
     * @return 已经封装好的key
     */
    public String getKey(String applicationName) {
        String keyPrefix = "";
        // 应用名为空则不拼接应用名
        if (StringUtils.hasText(applicationName)) {
            keyPrefix = applicationName + ".";
        }
        //如果没有参数则直接返回format
        if (args == null || args.length == 0) {
            return keyPrefix + cacheKeyFormat.getFormat();
        }
        return keyPrefix + String.format(cacheKeyFormat.getFormat(), args);
    }

    /**
     * 生成最终的缓存key，格式：${applicationName}.${format}
     *
     * @return 已经封装好的key
     */
    //    public String getKey() {
    //        return getKey(UcsGlobalContext.getAppName());
    //    }

}
