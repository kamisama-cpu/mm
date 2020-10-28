package com.example.demo01.ui;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo01.MainActivity;
import com.example.demo01.R;
import com.example.fromwork.base.BaseActivity;
import com.example.fromwork.entity.Constants;
import com.example.fromwork.utils.SpUtils;
import com.example.fromwork.view.TouchPictureView;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * 登录页
 */
public class LoginActivity extends BaseActivity {
    private TextView tvRegister;
    private EditText etLoginPhone;
    private EditText etVerifyCode;
    private Button btnLogin;
    private TextView tvUserAgreement;
    private ImageView ivAgreement;
    private Button btnSend;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1){
                String num = (String) msg.obj;
                btnSend.setText("等待"+"("+num+")");
            }else if(msg.what == 2){
                String num = (String) msg.obj;
                btnSend.setText(num);

            }

        }
    };


    @Override
    public int initLayout() {
        return R.layout.activity_login;
    }

    //初始化控件
    @Override
    public void initView() {
        btnSend = (Button) findViewById(R.id.btn_send);
        etLoginPhone = (EditText) findViewById(R.id.et_login_phone);
        etVerifyCode = (EditText) findViewById(R.id.et_verify_code);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvUserAgreement = (TextView) findViewById(R.id.tv_user_agreement);
        ivAgreement = (ImageView) findViewById(R.id.iv_agreement);
        tvRegister = (TextView) findViewById(R.id.tv_register);

        onClick();
    }


    //点击事件
    private void onClick(){
        //发送验证码
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = String.valueOf(etLoginPhone.getText());
                if (TextUtils.isEmpty(phone)){
                    Toast.makeText(LoginActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (SpUtils.getInstance().getBoolean(Constants.SP_VERIFY_CODE,true)){
                    showPopupWindow();
                }else {

                }

            }
        });

        //协议是否同意
        ivAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean agreement = SpUtils.getInstance().getBoolean(Constants.SP_AGREEMENT, true);
                if (agreement){
                    SpUtils.getInstance().putBoolean(Constants.SP_AGREEMENT, false);
                    ivAgreement.setImageResource(R.drawable.img_login_yes);
                }else {
                    SpUtils.getInstance().putBoolean(Constants.SP_AGREEMENT, true);
                    ivAgreement.setImageResource(R.drawable.img_no);
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    //登录
    private void login() {
        Boolean agreement = SpUtils.getInstance().getBoolean(Constants.SP_AGREEMENT, true);
        if (agreement){
            String phone = String.valueOf(etLoginPhone.getText());
            String code = String.valueOf(etVerifyCode.getText());
            if (TextUtils.isEmpty(phone)){
                Toast.makeText(LoginActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(code)){
                Toast.makeText(LoginActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                return;
            }
            //登录
            BmobUser.loginByAccount(phone, code, new LogInListener<BmobUser>() {
                @Override
                public void done(BmobUser bmobUser, BmobException e) {
                    if (e == null) {
                        Log.e("登录成功", "done: ");
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    } else {
                        Log.e("登录失败", "done: "+e.toString());
                        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                        startActivity(intent);
                    }
                }
            });
//            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//            startActivity(intent);
        }
    }

    //显示图片验证码
    private void showPopupWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_picture_verify, null);
        // 设置按钮的点击事件
        TouchPictureView tpv = (TouchPictureView) contentView.findViewById(R.id.tpv);
        builder.setView(contentView);
        AlertDialog show = builder.show();
        //发送验证码
        tpv.setViewResultLisener(new TouchPictureView.OnViewResultListener() {
            @Override
            public void onResult() {
                String phone = String.valueOf(etLoginPhone.getText());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BmobSMS.requestSMSCode(phone, "", new QueryListener<Integer>() {
                            @Override
                            public void done(Integer smsId, BmobException e) {
                                if (e == null) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            for (int i = 60 ; i > 0 ; i--){
                                                Message message = new Message();
                                                message.what = 1;
                                                message.obj = i+"";
                                                handler.sendMessage(message);
                                                SpUtils.getInstance().putBoolean(Constants.SP_VERIFY_CODE,false);
                                                try {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            SpUtils.getInstance().putBoolean(Constants.SP_VERIFY_CODE,true);
                                            Message message = new Message();
                                            message.what = 2;
                                            message.obj = "发送";
                                            handler.sendMessage(message);
                                        }
                                    }).start();
                                } else {
                                    Log.e("发送验证码失败：",""+e.getMessage());
                                }
                            }
                        });
                    }
                });

                show.dismiss();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SpUtils.getInstance().putBoolean(Constants.SP_VERIFY_CODE,true);
    }
}
