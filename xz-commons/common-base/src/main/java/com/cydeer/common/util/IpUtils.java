package com.cydeer.common.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author song.z
 * @date 2022/1/9 5:53 下午
 */
public final class IpUtils {
    /**
     * IP地址校验
     */
    public static final String IP_PATTERN = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
    public static final String IP_PATTERN_FIRST = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])";
    public static final String IP_PATTERN_FIELD = "(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))";

    private IpUtils() {
    }

    /**
     * 从Servlet的Request对象中获取请求发起者的IP
     *
     * @param request {@link HttpServletRequest}
     * @return 返回IP地址字符串
     */
    public static String getIpFromServletRequest(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    //    /**
    //     * IP格式校验
    //     *
    //     * @param str 待校验字符串
    //     * @return 校验结果boolean值
    //     */
    //    public static boolean isIp(final String str) {
    //        return StrExtUtils.isNotEmpty(str) && str.matches(IP_PATTERN);
    //    }
    //
    //    /**
    //     * IP字符串转换为INT值，优化传输和存储，目前支持IPv4.
    //     *
    //     * @param ip IP字符串
    //     * @return IP数值
    //     */
    //    public static int toInt(String ip) {
    //        byte[] bytes = toByte(ip);
    //        if (bytes == null) {
    //            return 0;
    //        }
    //        return NumberExtUtils.toInt(bytes);
    //    }
    //
    //    /**
    //     * IP的int值还原成IP字符串.
    //     *
    //     * @param ip int值
    //     * @return IP字符串
    //     */
    //    public static String toStr(int ip) {
    //        return String.valueOf(
    //                (ip >> 24) & 0xff) + '.' + ((ip >> 16) & 0xff) + '.' + ((ip >> 8) & 0xff) + '.' + (ip & 0xff);
    //    }
    //
    //    /**
    //     * Ipv4 String 转换到 byte[]
    //     */
    //    public static byte[] toByte(String ipv4Str) {
    //        if (ipv4Str == null) {
    //            return null;
    //        }
    //
    //        List<String> it = StrExtUtils.split(ipv4Str, '.', 4);
    //        if (it.size() != 4) {
    //            return null;
    //        }
    //
    //        byte[] byteAddress = new byte[4];
    //        for (int i = 0; i < 4; i++) {
    //            int tempInt = Integer.parseInt(it.get(i));
    //            if (tempInt > 255) {
    //                return null;
    //            }
    //            byteAddress[i] = (byte) tempInt;
    //        }
    //        return byteAddress;
    //    }
}
