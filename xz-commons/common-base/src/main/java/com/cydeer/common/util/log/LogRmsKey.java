package com.cydeer.common.util.log;

import com.cydeer.common.util.AssertUtils;
import com.cydeer.common.util.constant.CommonErrorCode;

/**
 * @author song.z
 * @date 2022/1/11 7:59 下午
 */
public class LogRmsKey {

    /**
     * rms监控的key的格式
     */
    private RmsKeyFormat keyFormat;

    /**
     * key格式中占位符对应的参数信息
     */
    private Object[] args;

    private LogRmsKey(RmsKeyFormat keyFormat, Object[] args) {
        this.keyFormat = keyFormat;
        this.args = args;
    }

    public static LogRmsKey none() {
        return new LogRmsKey(() -> "", new Object[0]);
    }

    /**
     * 构建rmsKey
     *
     * @param keyFormat key的定义格式
     * @param args      key定义格式中所需的参数信息
     * @return 封装好的rmsKey信息
     */
    public static LogRmsKey of(RmsKeyFormat keyFormat, Object... args) {
        AssertUtils.isNotNull(keyFormat, CommonErrorCode.PARAM_ERROR);
        AssertUtils.hasText(keyFormat.getFormat(), CommonErrorCode.PARAM_ERROR);
        return new LogRmsKey(keyFormat, args);
    }

    /**
     * 生成最终监控日志的key
     *
     * @return 监控日志的key
     */
    public String getKey() {
        return String.format(keyFormat.getFormat(), args);
    }
}
