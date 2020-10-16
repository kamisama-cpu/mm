package com.example.fromwork.utils;

import android.text.TextUtils;
import android.util.Log;

import org.w3c.dom.Text;

/**
 *
 * 封装的日志类
 *
 */
public class LogUtils {

    private boolean flag = true;

    public static void e(Object msg){
        if (true){
            if (!TextUtils.isEmpty((CharSequence) msg)){
                Log.e("shw", "e: "+msg );
            }
        }
    }

}
