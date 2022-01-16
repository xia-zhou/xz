package com.cydeer.boot.starter.web.user;

/**
 * @author song.z
 * @date 2022/1/10 10:37 下午
 */
public interface IUser {

    /**
     * 用户唯一标识 做日志使用，可以使用用户ID
     *
     * @return 用户唯一ID
     */
    String getKey();

}
