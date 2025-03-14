package com.rune.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author avalon
 * @date 22/8/5 17:03
 * @description ip工具
 */
public class IpUtil {

    private static final String UNKNOWN = "unknown";

    /**
     * 从 request 获取 ip
     *
     * @param request /
     * @return ip
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
