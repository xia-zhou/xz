package com.cydeer.boot.starter.constant;

import com.cydeer.common.Code;
import lombok.AllArgsConstructor;

/**
 * @author song.z
 * @date 2022/1/16 10:42 下午
 */
@AllArgsConstructor
public enum StarterErrorCodeEnum implements Code {
    /**
     * IO异常
     */
    IO_EXCEPTION("HTTP_IO_EXCEPTION", "IOException"),

    /**
     * 缓存的key不是为空
     */
    CACHE_KEY_IS_NULL("CACHE_KEY_IS_NULL", "缓存key不能为空");

    private String code;

    private String msg;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
