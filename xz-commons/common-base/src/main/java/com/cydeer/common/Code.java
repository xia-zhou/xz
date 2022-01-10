package com.cydeer.common;

/**
 * @author song.z
 * @date 2022/1/9 5:00 下午
 */
public interface Code {

    /**
     * 获取业务错误吗
     *
     * @return 业务错误吗
     */
    String getCode();

    /**
     * 获取业务错误描述
     *
     * @return 业务错误描述
     */
    String getMsg();
}
