package com.jd.boot001.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtils {
    public static final String STD_DATETIME = "yyyy-MM-dd HH:mm:ss";

    public static final String date() {
        return new SimpleDateFormat(STD_DATETIME).format(new Date());
    }

}

