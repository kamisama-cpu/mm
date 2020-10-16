package com.example.fromwork.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;

/**
 * 沉浸式布局
 */
public class SystemUIUtils {

    public static void SystemUi(Activity mActivity){
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.M){

            mActivity.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_LAYOUT_FLAGS
                                    |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                    |View.SYSTEM_UI_FLAG_FULLSCREEN
                    );

        }


    }

}
