package com.cydeer.boot.starter.web.support;

import com.cydeer.boot.starter.constant.StarterRmsLogFormatEnum;
import com.cydeer.boot.starter.web.user.UserContext;
import com.cydeer.common.CommonException;
import com.cydeer.common.Result;
import com.cydeer.common.util.IpUtils;
import com.cydeer.common.util.LogUtils;
import com.cydeer.common.util.constant.CommonErrorCode;
import com.cydeer.common.util.log.LogRmsKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author song.z
 * @date 2022/1/10 10:19 下午
 */
@ControllerAdvice
@Slf4j
public class ExceptionAdvice {


    /**
     * 自定义异常处理
     *
     * @param request         请求参数
     * @param commonException 业务异常
     * @return 请求结果
     */
    @ResponseBody
    @ExceptionHandler(value = CommonException.class)
    public Result<Object> commonExceptionHandler(HttpServletRequest request, CommonException commonException) {
        LogUtils.error(log, LogRmsKey.of(StarterRmsLogFormatEnum.MVC_BASE_EXCEPTION, request.getRequestURI(),
                                         UserContext.getKey(), commonException.getCode(), commonException.getMsg(),
                                         IpUtils.getIpFromServletRequest(request)));
        return new Result<>(commonException.getCode(), commonException.getMsg());
    }

    /**
     * 数据绑定异常处理
     *
     * @param request       请求参数
     * @param bindException 参数绑定异常
     * @return 请求结果
     */
    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public Result<Object> bindExceptionHandler(HttpServletRequest request, BindException bindException) {
        LogUtils.error(log, LogRmsKey.of(StarterRmsLogFormatEnum.MVC_BIND_EXCEPTION, request.getRequestURI(),
                                         UserContext.getKey(), IpUtils.getIpFromServletRequest(request)),
                       bindException);
        return new Result<>(CommonErrorCode.PARAM_ERROR);
    }

    /**
     * 请求参数缺失异常处理
     *
     * @param request                                 请求参数
     * @param missingServletRequestParameterException 参数缺失异常
     * @return 请求结果
     */
    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Result<Object> missingServletRequestParameterExceptionHandler(HttpServletRequest request,
                                                                         MissingServletRequestParameterException missingServletRequestParameterException) {
        LogUtils.error(log,
                       LogRmsKey.of(StarterRmsLogFormatEnum.MVC_MISSING_PARAMETER_EXCEPTION, request.getRequestURI(),
                                    UserContext.getKey(), IpUtils.getIpFromServletRequest(request)),
                       missingServletRequestParameterException);
        return new Result<>(CommonErrorCode.PARAM_ERROR);
    }

    /**
     * 类型不匹配异常处理
     *
     * @param request               请求参数
     * @param typeMismatchException 类型不匹配异常
     * @return 请求结果
     */
    @ResponseBody
    @ExceptionHandler(value = TypeMismatchException.class)
    public Result<Object> typeMismatchExceptionHandler(HttpServletRequest request,
                                                       TypeMismatchException typeMismatchException) {
        LogUtils.error(log, LogRmsKey.of(StarterRmsLogFormatEnum.MVC_TYPE_MISMATCH_EXCEPTION, request.getRequestURI(),
                                         UserContext.getKey(), IpUtils.getIpFromServletRequest(request)),
                       typeMismatchException);
        return new Result<>(CommonErrorCode.PARAM_ERROR);
    }

    /**
     * 全局异常捕捉处理
     *
     * @param request 请求参数
     * @param ex      内部定义异常
     * @return 结果信息
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result<Object> errorHandler(HttpServletRequest request, Exception ex) {
        LogUtils.error(log, LogRmsKey.of(StarterRmsLogFormatEnum.MVC_UNKNOWN_EXCEPTION, request.getRequestURI(),
                                         UserContext.getKey(), IpUtils.getIpFromServletRequest(request)), ex);
        return new Result<>(CommonErrorCode.PARAM_ERROR);
    }
}
