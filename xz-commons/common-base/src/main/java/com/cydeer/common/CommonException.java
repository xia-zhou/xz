package com.cydeer.common;

import lombok.Getter;

/**
 * @author song.z
 * @date 2022/1/9 4:59 下午
 */
@Getter
public final class CommonException extends RuntimeException {

    /**
     * 业务异常错误吗
     */
    private final String code;

    /**
     * 业务异常信息
     */
    private final String msg;

    /**
     * 推荐使用，业务方添加错误码枚举实现Code接口
     *
     * @param code 错误码
     */
    public CommonException(Code code) {
        super(code.getMsg());
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    /**
     * 推荐使用，业务方提供错误码枚举实现Code接口，保留异常原始信息
     *
     * @param code  错误码
     * @param cause 异常原始信息
     */
    public CommonException(Code code, Throwable cause) {
        super(code.getMsg(), cause);
        this.code = code.getCode();
        this.msg = code.getMsg();
    }


    /**
     * 适用于没有定义错误码的情况下
     *
     * @param code 错误码
     * @param msg  错误信息
     */
    public CommonException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    /**
     * 适用于没有定义错误码的情况下
     *
     * @param code  错误码
     * @param msg   错误信息
     * @param cause 异常原始信息
     */
    public CommonException(String code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
    }

}
