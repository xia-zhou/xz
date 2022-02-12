package com.cydeer.common.util.constant;

import com.cydeer.common.Code;
import lombok.AllArgsConstructor;

/**
 * @author song.z
 * @date 2022/1/11 8:03 下午
 */
@AllArgsConstructor
public enum CommonErrorCode implements Code {
    /**
     * 系统异常
     */
    SYSTEM_ERROR("B00001", "系统异常，请稍后重试"),

    /**
     * 参数异常
     */
    PARAM_ERROR("A00001", "参数异常，请检查参数是否正确"),
    ;

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
