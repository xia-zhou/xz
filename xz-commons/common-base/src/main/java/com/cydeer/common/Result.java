package com.cydeer.common;

import com.cydeer.common.util.LogUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author song.z
 * @date 2022/1/9 4:49 下午
 */
@Getter
@ToString
@EqualsAndHashCode
@Slf4j
public final class Result<T> {

    /**
     * 成功返回的状态码
     */
    private final static String SUCCESS_CODE = "00000";

    /**
     * 空字符串
     */
    private final static String EMPTY_STRING = "";

    /**
     * 业务处理的错误码
     */
    private final String code;

    /**
     * 业务处理的错误信息
     */
    private final String msg;

    /**
     * 业务数据
     */
    private T data;

    /**
     * 业务处理成功，封装业务数据
     *
     * @param data 业务数据
     */
    public Result(T data) {
        this.code = SUCCESS_CODE;
        this.msg = null;
        this.data = data;
    }

    /**
     * 业务处理失败，封装业务错误码
     *
     * @param code 错误吗
     * @param msg  错误描述
     */
    public Result(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 是否成功
     *
     * @return 请求是否成功
     */
    public boolean isSuccess() {
        return Objects.equals(SUCCESS_CODE, code);
    }

    /**
     * 注意会记录业务异常日志
     * <p>
     * 如果成功则获取业务数据，否则抛出业务异常
     *
     * @return 业务数据
     */
    public T fetchDataIfSuccess() {
        return fetchDataIfSuccess(true);
    }

    /**
     * 如果成功则获取业务数据，否则抛出业务异常 参数控制是否输出异常日志
     *
     * @param logRecord 是否记录日志
     * @return 业务数据
     */
    public T fetchDataIfSuccess(boolean logRecord) {
        if (isSuccess()) {
            return data;
        }
        if (logRecord) {
            LogUtils.error(log, "请求结果业务异常,code:{},msg:{}", code, msg);
        }
        throw new CommonException(code, msg);
    }
}
