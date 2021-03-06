package com.example.demo01.ui;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.demo01.R;
import com.example.fromwork.base.BaseActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class RegisterActivity extends BaseActivity {
    private EditText etUsername;
    private EditText etPassword;
    private Button button1;
    @Override
    public int initLayout() {
        return R.layout.activity_register;
    }
    @Override
    public void initView() {
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = String.valueOf(etUsername.getText());
                String password = String.valueOf(etPassword.getText());

                signUp(username,password);

            }
        });
    }




    /**
     * 账号密码注册
     * @param username
     * @param password
     */
    private void signUp(String username, String password) {
        BmobUser.signOrLoginByMobilePhone(username, password, new LogInListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    Log.e("注册成功", "done: " );
                } else {
                    Log.e("注册失败", "done: " +e.toString());
                }
            }
        });
    }
}
