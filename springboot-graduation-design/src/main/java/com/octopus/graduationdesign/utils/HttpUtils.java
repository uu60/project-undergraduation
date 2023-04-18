package com.octopus.graduationdesign.utils;

import com.octopus.graduationdesign.properties.OProperties;
import sun.net.util.IPAddressUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;

public class HttpUtils {

    private static boolean requestMappingFit(String reqMap, HashSet<String> set) {
        if (set.contains(reqMap)) {
            return true;
        }
        String[] parts = reqMap.split("/");
        StringBuilder cur = new StringBuilder();
        for (String part : parts) {
            if (part.equals("")) {
                continue;
            }
            cur.append("/");
            cur.append(part);
            if (set.contains(cur.toString() + "/*")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNoNeedLogin(HttpServletRequest request) {
        return requestMappingFit(getRequestMappingUrl(request), OProperties.URL_NO_NEED_LOGIN_SET);
    }

    public static boolean isPermitLanOnly(HttpServletRequest request) {
        return requestMappingFit(getRequestMappingUrl(request), OProperties.URL_LAN_ONLY_SET);
    }

    private static String getRequestMappingUrl(HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
    }

    private static String getRemoteIp(HttpServletRequest request) {
        return request.getRemoteHost();
    }

    public static boolean isInternalIp(HttpServletRequest request) {
        String ip = getRemoteIp(request);
        if ("0:0:0:0:0:0:0:1".equals(ip) || "127.0.0.1".equals(ip)) {
            return true;
        }
        byte[] addr = IPAddressUtil.textToNumericFormatV4(ip);
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        //10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        //172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        //192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        switch (b0) {
            case SECTION_1:
                return true;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                    return true;
                }
            case SECTION_5:
                switch (b1) {
                    case SECTION_6:
                        return true;
                }
            default:
                return false;
        }
    }
}
