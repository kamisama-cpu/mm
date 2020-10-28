package com.example.demo01.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.demo01.R;
import com.example.fromwork.base.BaseActivity;
import com.example.fromwork.entity.Constants;

public class ChatActivity extends BaseActivity {

    //聊天列表
    private RecyclerView mChatView;
    //输入框
    private EditText etInputMsg;
    //发送按钮
    private Button btnSendMsg;
    //语音输入
    private LinearLayout llVoice;
    //相机
    private LinearLayout llCamera;
    //图片
    private LinearLayout llPic;
    //位置
    private LinearLayout llLocation;
    @Override
    public void initView() {
        mChatView = (RecyclerView) findViewById(R.id.mChatView);
        etInputMsg = (EditText) findViewById(R.id.et_input_msg);
        btnSendMsg = (Button) findViewById(R.id.btn_send_msg);
        llVoice = (LinearLayout) findViewById(R.id.ll_voice);
        llCamera = (LinearLayout) findViewById(R.id.ll_camera);
        llPic = (LinearLayout) findViewById(R.id.ll_pic);
        llLocation = (LinearLayout) findViewById(R.id.ll_location);
        getIntentData();
    }


    private void getIntentData(){
        Intent intent = getIntent();
        String yourUserID = intent.getStringExtra(Constants.INTENT_ID);
        String yourUserName = intent.getStringExtra(Constants.INTENT_NAME);
        String yourUserPhoto = intent.getStringExtra(Constants.INTENT_PHOTO);

    }

    @Override
    public int initLayout() {
        return R.layout.activity_chat;
    }
}
