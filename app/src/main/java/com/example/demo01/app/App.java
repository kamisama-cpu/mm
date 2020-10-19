package com.example.demo01.app;

import android.app.Application;

import cn.bmob.v3.Bmob;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "5d9766d04e0d3748bae1b749c47b1aa1");
    }
}
