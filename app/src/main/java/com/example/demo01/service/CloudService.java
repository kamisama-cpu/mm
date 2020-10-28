package com.example.demo01.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;

import com.example.fromwork.bomb.BmobManager;
import com.example.fromwork.cloud.CloudManager;
import com.example.fromwork.db.SqlHelper;
import com.example.fromwork.entity.Constants;
import com.example.fromwork.entity.UserEntity;
import com.example.fromwork.gson.TextBean;
import com.example.fromwork.utils.LogUtils;
import com.example.fromwork.utils.SpUtils;
import com.google.gson.Gson;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

public class CloudService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        linkCloudServer();

    }

    /**
     * 链接云服务
     */
    private void linkCloudServer() {
        String token = SpUtils.getInstance().getString(Constants.SP_TOKE, "");
        LogUtils.e("喵喵mm"+token);
        CloudManager.getInstance().connect(token);
        //接收消息
        CloudManager.getInstance().setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageWrapperListener() {
            @Override
            public boolean onReceived(Message message, int i, boolean b, boolean b1) {
                LogUtils.e("message::"+message);
                String objectName = message.getObjectName();

                if (objectName.equals(CloudManager.MSG_TEXT_NAME)){
                    //文本消息
                    TextMessage textMessage = (TextMessage) message.getContent();
                    String content = textMessage.getContent();
                    LogUtils.e("content::"+content);
                    TextBean textBean = new Gson().fromJson(content, TextBean.class);

                    if (textBean.getType().equals(CloudManager.TYPE_TEXT)){
                        //普通消息
                        LogUtils.e("普通消息::"+content);
                    }else if (textBean.getType().equals(CloudManager.TYPE_ADD_FRIEND)){
                        //添加好友消息
                        LogUtils.e("添加好友消息::"+content);
                        UserEntity userEntity = new Gson().fromJson(content, UserEntity.class);
                        boolean b2 = SqlHelper.getInstance().queryUserId(userEntity.getImId());

                        if (!b2){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("msg",userEntity.getMsg());
                            contentValues.put("isAgree","-1");
                            contentValues.put("saveTime",System.currentTimeMillis());
                            contentValues.put("userId",userEntity.getImId());
                            LogUtils.e("msg::"+userEntity.getMsg()+"saveTime::"+System.currentTimeMillis()+"userId::"+userEntity.getImId());
                            SqlHelper.getInstance().insertUser(contentValues);
                        }
                    }else if (textBean.getType().equals(CloudManager.TYPE_ARGEED_FRIEND)){
                        //同意添加好友消息
                        LogUtils.e("同意添加好友消息::"+content);
                    }
                }

                return false;
            }
        });

    }
}
