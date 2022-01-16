package com.cydeer.boot.starter.web.user;

import java.util.Optional;

/**
 * @author song.z
 * @date 2022/1/10 10:39 下午
 */
public class UserContext {
    /**
     * 用户线程变量
     */
    private final static ThreadLocal<IUser> USER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 初始化用户信息
     *
     * @param user 用户相关信息
     */
    public static void init(IUser user) {
        USER_THREAD_LOCAL.set(user);
    }

    /**
     * 获取用户相关信息
     *
     * @return 用户信息
     */
    public static <T extends IUser> T get() {
        return (T) USER_THREAD_LOCAL.get();
    }

    /**
     * 获取用户id字符串，上下文中没有则为null
     *
     * @return 用户唯一标识
     */
    public static String getKey() {
        return Optional.ofNullable(USER_THREAD_LOCAL.get()).map(user -> user.getKey()).orElse("");
    }

    /**
     * 清空上下文信息
     */
    public static void clear() {
        USER_THREAD_LOCAL.remove();
    }
}
