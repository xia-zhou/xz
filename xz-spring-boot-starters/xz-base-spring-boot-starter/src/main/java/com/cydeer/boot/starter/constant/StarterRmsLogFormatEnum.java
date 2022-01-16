package com.cydeer.boot.starter.constant;

import com.cydeer.common.util.log.RmsKeyFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author song.z
 * @date 2022/1/12 10:16 下午
 */
@Getter
@AllArgsConstructor
public enum StarterRmsLogFormatEnum implements RmsKeyFormat {

    /**
     * mvc层统一异常处理
     */
    MVC_BASE_EXCEPTION("请求统一异常处理-BaseException,requestUrl:%s,userKey:%s,code:%s,msg:%s,ip:%s"),

    /**
     * 参数绑定异常
     */
    MVC_BIND_EXCEPTION("请求统一异常处理-BindException,requestUrl:%s,userKey:%s,ip:%s"),
    /**
     * 参数缺失异常
     */
    MVC_MISSING_PARAMETER_EXCEPTION("请求统一异常处理-MissingServletRequestParameterException,requestUrl:%s,userKey:%s,ip:%s"),

    /**
     * 参数不匹配异常
     */
    MVC_TYPE_MISMATCH_EXCEPTION("请求统一异常处理-TypeMismatchException,requestUrl:%s,userKey:%s,ip:%s"),
    /**
     * 未知异常
     */
    MVC_UNKNOWN_EXCEPTION("请求统一异常处理-UnknownException,requestUrl:%s,userKey:%s,ip:%s"),

    /**
     * 用户拦截器，用户信息解析失败
     */
    MVC_GET_USER_FROM_REQUEST_EXCEPTION("用户拦截器-UnknownException,requestUrl:%s,ip:%s"),


    /**
     * SQL执行异常
     */
    SQL_EXCEPTION("SQL_EXCEPTION,clazzName:%s,methodName:%s,args:%s"),
    ;

    private String format;

    @Override
    public String getFormat() {
        return format;
    }
}
