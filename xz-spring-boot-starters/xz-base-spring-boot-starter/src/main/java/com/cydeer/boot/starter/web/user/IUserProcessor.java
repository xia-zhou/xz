package com.cydeer.boot.starter.web.user;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author song.z
 * @date 2022/1/11 8:33 上午
 */
public interface IUserProcessor {

    /**
     * 从请求中获取到用户信息
     *
     * @param request  请求
     * @param response 响应
     * @return 用户信息
     */
    IUser getFromRequest(HttpServletRequest request, HttpServletResponse response);

}
