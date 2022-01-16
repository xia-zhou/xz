package com.cydeer.boot.starter.util.http;

import com.cydeer.common.Code;
import lombok.AllArgsConstructor;

/**
 * @author song.z
 * @date 2022/1/16 10:42 下午
 */
@AllArgsConstructor
public enum HttpErrorCodeEnum implements Code {
    /**
     * IO异常
     */
    IO_EXCEPTION("HTTP_IO_EXCEPTION", "IOException");

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
