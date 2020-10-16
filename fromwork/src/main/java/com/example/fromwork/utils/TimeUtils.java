package com.example.fromwork.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间转换类
 */
public class TimeUtils {

    /*
        参数 ms : 当前时间(毫秒)
        return: 返回计算出的时间
     */

    public static String formatDuring(long ms){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);

        String format = simpleDateFormat.format(new Date(ms));


        return format;
    }



}
