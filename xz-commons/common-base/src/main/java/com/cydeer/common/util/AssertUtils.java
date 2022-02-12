package com.cydeer.common.util;

import com.cydeer.common.Code;
import com.cydeer.common.CommonException;

import java.util.Collection;
import java.util.Map;

/**
 * 不满足断言都会通过抛出业务异常的的方式结束程序的执行
 *
 * @author song.z
 * @see CommonException
 */
public final class AssertUtils {

    /**
     * 对象如果为空则使用code的错误码抛出业务异常
     *
     * @param object 要断言的对象
     * @param code   对象如果为空使用的错误码
     */
    public static void isNotNull(Object object, Code code) {
        checkCode(code);
        isNotNull(object, code.getCode(), code.getMsg());
    }

    /**
     * 对象如果为空则使用code的错误码抛出业务异常
     *
     * @param object 要断言的对象
     * @param code   业务错误吗
     * @param msg    业务错误信息
     */
    public static void isNotNull(Object object, String code, String msg) {
        if (object == null) {
            throw new CommonException(code, msg);
        }
    }

    /**
     * 如果字符串为空则使用code的错误码抛出业务异常
     *
     * @param str  要断言的字符串
     * @param code 断言失败使用的错误码
     */
    public static void hasText(String str, Code code) {
        checkCode(code);
        hasText(str, code.getCode(), code.getMsg());
    }

    /**
     * 如果字符串为空则使用code的错误码抛出业务异常
     *
     * @param str  要断言的字符串
     * @param code 断言失败使用的错误码
     * @param msg  业务错误信息
     */
    public static void hasText(String str, String code, String msg) {
        if (str != null && !str.isEmpty() && containsText(str)) {
            return;
        }
        throw new CommonException(code, msg);
    }


    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }


    /**
     * 断言一个集合不为空，如果为空则抛出CommonException，使用code作为异常的错误码
     *
     * @param c    要断言的集合
     * @param code 使用的错误码
     */
    public static void isNotEmpty(Collection<?> c, Code code) {
        checkCode(code);
        isNotEmpty(c, code.getCode(), code.getMsg());
    }

    /**
     * 断言一个集合不为空，如果为空则抛出业务异常，使用code作为异常的错误码
     *
     * @param c    要断言的集合
     * @param code 业务错误吗
     * @param msg  业务错误信息
     */
    public static void isNotEmpty(Collection<?> c, String code, String msg) {
        if (c == null || c.size() == 0) {
            throw new CommonException(code, msg);
        }
    }


    /**
     * 断言一个Map不为空，如果为空则抛出CommonException，使用code作为异常的错误码
     *
     * @param m    要断言的map
     * @param code 使用的错误码
     */
    public static void isNotEmpty(Map<?, ?> m, Code code) {
        checkCode(code);
        isNotEmpty(m, code.getCode(), code.getMsg());
    }

    /**
     * 断言一个Map不为空，如果为空则抛出CommonException，使用code作为异常的错误码
     *
     * @param m    要断言的map
     * @param code 业务错误吗
     * @param msg  业务错误信息
     */
    public static void isNotEmpty(Map<?, ?> m, String code, String msg) {
        if (m == null || m.isEmpty()) {
            throw new CommonException(code, msg);
        }
    }

    /**
     * 检查错误码是否为空
     *
     * @param code 错误码
     */
    private static void checkCode(Code code) {
        if (code == null) {
            throw new IllegalArgumentException("错误码为空");
        }
    }
}
