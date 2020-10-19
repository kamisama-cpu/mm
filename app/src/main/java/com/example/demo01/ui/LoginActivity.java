package com.example.demo01.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo01.MainActivity;
import com.example.demo01.R;
import com.example.fromwork.entity.Constants;
import com.example.fromwork.utils.SpUtils;
import com.example.fromwork.view.TouchPictureView;

/**
 * 登录页
 */
public class LoginActivity extends AppCompatActivity {

    private EditText btnLoginPhone;
    private EditText btnVerifyCode;
    private Button btnLogin;
    private TextView tvUserAgreement;
    private ImageView ivAgreement;
    private Button btnSend;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1){

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

    }

    //初始化控件
    private void initView() {
        btnSend = (Button) findViewById(R.id.btn_send);
        btnLoginPhone = (EditText) findViewById(R.id.btn_login_phone);
        btnVerifyCode = (EditText) findViewById(R.id.btn_verify_code);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvUserAgreement = (TextView) findViewById(R.id.tv_user_agreement);
        ivAgreement = (ImageView) findViewById(R.id.iv_agreement);

        onClick();
    }

    //点击事件
    private void onClick(){
        //发送验证码
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "发送", Toast.LENGTH_SHORT).show();
                showPopupWindow();
            }
        });

        //协议是否同意
        ivAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean agreement = SpUtils.getInstance().getBoolean(Constants.SP_AGREEMENT, false);
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
                Boolean agreement = SpUtils.getInstance().getBoolean(Constants.SP_AGREEMENT, false);
                if (agreement){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    //显示图片验证码
    private void showPopupWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.activity_picture_verify, null);
        // 设置按钮的点击事件
        TouchPictureView tpv = (TouchPictureView) contentView.findViewById(R.id.tpv);
        builder.setView(contentView);
        AlertDialog show = builder.show();
        //发送验证码
        tpv.setViewResultLisener(new TouchPictureView.OnViewResultListener() {
            @Override
            public void onResult() {
                show.dismiss();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        for (int i = 60 ; i > 0 ; i--){
                            Message message = new Message();
                            message.what = 1;
                            message.obj = i+"";
                            handler.sendMessage(message);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();

            }
        });
    }

}
