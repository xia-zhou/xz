package com.cydeer.boot.starter.aop;

import com.cydeer.boot.starter.constant.StarterRmsLogFormatEnum;
import com.cydeer.common.util.LogUtils;
import com.cydeer.common.util.log.LogRmsKey;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 * @author song.z
 * @date 2022/1/16 6:11 下午
 */
@Slf4j
@Component
@Aspect
@ConditionalOnClass(org.apache.ibatis.annotations.Mapper.class)
@ConditionalOnProperty(name = "xz.mybatis.mapper.cost.enable", havingValue = "true", matchIfMissing = true)
public class MybatisMapperLogInterceptor {

    @Around("@annotation(org.apache.ibatis.annotations.Mapper)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 记录请求耗时
        Object obj;
        try {
            obj = point.proceed();
        } catch (Exception exception) {
            // 记录异常日志
            exceptionLog(point, exception);
            // 不处理直接抛出
            throw exception;
        }
        return obj;
    }

    /**
     * 记录日常日志
     *
     * @param point     切点
     * @param exception 异常信息
     */
    private void exceptionLog(ProceedingJoinPoint point, Exception exception) {
        // 获得参数列表
        Object[] args = point.getArgs();
        String argStr = null;
        if (null != args && args.length != 0) {
            StringBuilder sb = new StringBuilder();
            for (Object arg : args) {
                sb.append(arg != null ? arg.toString() : "null");
                sb.append(",");
            }
            argStr = sb.toString();
        }
        // 异常记录异常的参数信息。
        if (exception instanceof DuplicateKeyException) {
            LogUtils.info(log, "SQL_EXCEPTION,clazzName:{},methodName:{},args:{}", exception.getMessage(),
                          getClassName(point), point.getSignature().getName(), argStr);
        } else {
            LogUtils.error(log, LogRmsKey.of(StarterRmsLogFormatEnum.SQL_EXCEPTION, getClassName(point),
                                             point.getSignature().getName(), argStr), exception);
        }
    }

    private String getClassName(ProceedingJoinPoint point) {
        return point.getSignature().getDeclaringType().getSimpleName();
    }
}
