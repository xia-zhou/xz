package com.cydeer.common.util;

import com.cydeer.common.util.log.LogRmsKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author song.z
 * @date 2022/1/9 5:53 下午
 */
public final class LogUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger("RMS_LOG");

    /**
     * debug 日志打印 .
     *
     * @param logger 日志对象
     * @param format 日志内容
     *               <ul>
     *               <li>日志内容为<b>纯文本格式</b></li>
     *               <li>日志内容为<b>logback</b>标准格式，如 {},{},{}</li>
     *               </ul>
     * @param args   日志内容参数
     */
    public static void debug(Logger logger, String format, Object... args) {
        debugLog(logger, format, args);
    }

    /**
     * @param logger 日志对象
     * @param format 日志内容
     *               <ul>
     *               <li>日志内容为<b>纯文本格式</b></li>
     *               <li>日志内容为<b>logback</b>标准格式，如 {},{},{}</li>
     *               </ul>
     * @param args   日志内容参数
     */
    public static void info(Logger logger, String format, Object... args) {
        infoLog(logger, format, args);
    }

    /**
     * @param logger 日志对象
     * @param format 日志内容
     *               <ul>
     *               <li>日志内容为<b>纯文本格式</b></li>
     *               <li>日志内容为<b>logback</b>标准格式，如 {},{},{}</li>
     *               </ul>
     * @param args   日志内容参数
     */
    public static void warn(Logger logger, String format, Object... args) {
        warnLog(logger, format, args);
    }


    /**
     * @param logger 日志对象
     * @param format 日志内容
     *               <ul>
     *               <li>日志内容为<b>纯文本格式</b></li>
     *               <li>日志内容为<b>logback</b>标准格式，如 {},{},{}</li>
     *               </ul>
     * @param args   日志内容参数
     */
    public static void error(Logger logger, String format, Object... args) {
        errorLog(logger, format, args);
    }

    /**
     * 错误日志
     *
     * @param logger 日志对象
     * @param rmsKey 日志监控key
     * @param e      异常原始信息
     */
    public static void error(Logger logger, LogRmsKey rmsKey, Throwable e) {
        errorLog(logger, rmsKey.getKey(), e);
    }

    /**
     * 错误日志 包含监控信息
     *
     * @param logger 日志对象
     * @param rmsKey 日志监控key
     */
    public static void error(Logger logger, LogRmsKey rmsKey) {
        errorLog(logger, rmsKey.getKey());
    }

    /**
     * 监控日志 包含监控信息
     *
     * @param rmsKey 日志监控key
     */
    public static void rms(LogRmsKey rmsKey) {
        infoLog(LOGGER, rmsKey.getKey());
    }


    /**
     * debug日志
     *
     * @param logger 日志对象
     * @param format 格式
     * @param args   参数
     */
    private static void debugLog(Logger logger, String format, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(format, args);
        }
    }

    /**
     * info日志
     *
     * @param logger 日志对象
     * @param format 格式
     * @param args   参数
     */
    private static void infoLog(Logger logger, String format, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(format, args);
        }
    }

    /**
     * 警告日志
     *
     * @param logger 日志对象
     * @param format 格式
     * @param args   参数
     */
    private static void warnLog(Logger logger, String format, Object... args) {
        if (logger.isWarnEnabled()) {
            logger.warn(format, args);
        }
    }

    /**
     * 错误日志
     *
     * @param logger 日志对象
     * @param format 格式
     * @param args   参数
     */
    private static void errorLog(Logger logger, String format, Object... args) {
        if (logger.isErrorEnabled()) {
            logger.error(format, args);
        }
    }
}
