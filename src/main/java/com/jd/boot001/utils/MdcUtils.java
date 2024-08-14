package com.jd.boot001.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * 日志用
 */
public class MdcUtils {
    private final static String INVOKE_NO = "invokeNo";

    /**
     * MDC添加invokeNo
     */
    public static void put() {
        final String invokeNo = MDC.get("invokeNo");
        if (StringUtils.isNotBlank(invokeNo)) {
            MDC.put(INVOKE_NO, invokeNo);
        }
    }

    public static void remove() {
        MDC.remove(INVOKE_NO);
    }
}
