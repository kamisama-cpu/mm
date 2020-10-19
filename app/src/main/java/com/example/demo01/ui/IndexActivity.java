package com.example.demo01.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.demo01.MainActivity;
import com.example.demo01.R;
import com.example.fromwork.entity.Constants;
import com.example.fromwork.utils.SpUtils;


/**
 * 启动页
 */
public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        SpUtils.getInstance().initSp(this);
        startMain();
    }

    /*
        主要来判断接下来要跳转那个页面
     */

    public void startMain(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //判断是不是第一次登录
                Boolean isFirstLogin = SpUtils.getInstance().getBoolean(Constants.SP_IS_FIRST_APP, true);
                Intent intent = new Intent();
                if (isFirstLogin){
                    //跳转到引导页
                    intent.setClass(IndexActivity.this,GuideActivity.class);
                }else {
                    //不是第一次判断是否登录过
                    String token = SpUtils.getInstance().getString(Constants.SP_TOKE, "");
                    //判断是不是登录过 token有数据就是登录过 空就是没有登录过
                    if (TextUtils.isEmpty(token)){
                        //跳转到登录
                        intent.setClass(IndexActivity.this,LoginActivity.class);
                    }else {
                        //跳转到主页面
                        intent.setClass(IndexActivity.this, MainActivity.class);
                    }
                }
                startActivity(intent);
                finish();
            }
        }).start();


    }

}
