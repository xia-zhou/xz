package com.cydeer.common.util.log;

import lombok.AllArgsConstructor;

/**
 * @author song.z
 * @date 2022/1/11 8:10 下午
 */
@AllArgsConstructor
public enum RmsSampleEnum implements RmsKeyFormat {

    /**
     * 请求参数错误
     */
    REQUEST_PARAM_ERROR,
    ;

   
    @Override
    public String getFormat() {
        return name();
    }
}
