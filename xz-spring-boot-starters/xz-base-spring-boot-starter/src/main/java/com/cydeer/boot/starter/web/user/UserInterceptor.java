package com.cydeer.boot.starter.web.user;

import com.cydeer.boot.starter.constant.StarterRmsLogFormatEnum;
import com.cydeer.common.BaseException;
import com.cydeer.common.util.IpUtils;
import com.cydeer.common.util.LogUtils;
import com.cydeer.common.util.constant.CommonErrorCode;
import com.cydeer.common.util.log.LogRmsKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author song.z
 * @date 2022/1/11 8:35 上午
 */
@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    private final IUserProcessor iUserProcessor;

    public UserInterceptor(IUserProcessor iUserProcessor) {
        this.iUserProcessor = iUserProcessor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            IUser user = iUserProcessor.getFromRequest(request, response);
            UserContext.init(user);
            return true;
        } catch (BaseException baseException) {
            throw baseException;
        } catch (Exception e) {
            LogUtils.error(log, LogRmsKey.of(StarterRmsLogFormatEnum.MVC_GET_USER_FROM_REQUEST_EXCEPTION,
                                             request.getRequestURL(), IpUtils.getIpFromServletRequest(request)), e);
            throw new BaseException(CommonErrorCode.SYSTEM_ERROR);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) {
        UserContext.clear();
    }
}
