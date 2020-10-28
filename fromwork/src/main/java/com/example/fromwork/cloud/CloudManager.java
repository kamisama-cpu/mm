package com.example.fromwork.cloud;

import android.content.Context;

import com.example.fromwork.bomb.BmobManager;
import com.example.fromwork.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

public class CloudManager {

    private static CloudManager instance;


    //ObjectName
    public static final String MSG_TEXT_NAME = "RC:TxtMsg";
    public static final String MSG_IMAGE_NAME = "RC:ImgMsg";
    public static final String MSG_LOCATION_NAME = "RC:LBSMsg";

    //添加好友消息
    //普通消息
    public static final String TYPE_TEXT = "TYPE_TEXT";
    public static final String TYPE_ADD_FRIEND = "TYPE_ADD_FRIEND";
    public static final String TYPE_ARGEED_FRIEND = "TYPE_ARGEED_FRIEND";



    public CloudManager() {
    }

    public static CloudManager getInstance(){

        if (instance == null){
            synchronized (CloudManager.class){
                if (instance ==null){
                    instance = new CloudManager();
                }
            }
        }
        return instance;
    }


    public void initCloud(Context context){
        RongIMClient.init(context);

    }

    /**
     * 链接融云服务
     * @param token
     */
    public void connect(String token){
        RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onSuccess(String s) {
                LogUtils.e("链接融云成功"+s);
            }

            @Override
            public void onError(RongIMClient.ConnectionErrorCode connectionErrorCode) {
                LogUtils.e("链接融云失败"+connectionErrorCode);

            }

            @Override
            public void onDatabaseOpened(RongIMClient.DatabaseOpenStatus databaseOpenStatus) {
                LogUtils.e("Token error");
            }
        });
    }

    /**
     * 断开链接
     */
    public void disconnect(){
        RongIMClient.getInstance().disconnect();
    }

    /**
     * 退出
     */
    public void logout(){
        RongIMClient.getInstance().logout();

    }

    /**
     * 接收消息的监听器
     * @param listener
     */
    public void setOnReceiveMessageListener(RongIMClient.OnReceiveMessageListener listener){
        RongIMClient.setOnReceiveMessageListener(listener);
    }

    /**
     * 发送文本消息
     * @param content
     * @param id
     */
    public void sendTextManager(String content,String id){
        TextMessage messageContent = TextMessage.obtain(content);
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE,
                id,
                messageContent, null, null, new IRongCallback.ISendMessageCallback() {
                    /**
                     * 消息发送前回调, 回调时消息已存储数据库
                     * @param message 已存库的消息体
                     */
                    @Override
                    public void onAttached(Message message) {

                    }
                    /**
                     * 消息发送成功。
                     * @param message 发送成功后的消息体
                     */
                    @Override
                    public void onSuccess(Message message) {
                        LogUtils.e("发送文本消息成功"+message);
                    }

                    /**
                     * 消息发送失败
                     * @param message   发送失败的消息体
                     * @param errorCode 具体的错误
                     */
                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        LogUtils.e("发送文本消息失败"+errorCode+"::"+message);
                    }
                });
    }

    /**
     * 发送文本消息
     * @param msg
     * @param type
     * @param id
     */
    public void  sendAddFriendMessage(String msg,String type,String id){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("msg",msg);
            jsonObject.put("ImId", BmobManager.getInstance().getUser().getObjectId());
            //如果没有type就是普通消息
            jsonObject.put("type",type);
            sendTextManager(jsonObject.toString(),id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 查询本地的会话记录
     *
     * @param callback
     */
    public void getConversationList(RongIMClient.ResultCallback<List<Conversation>> callback) {
        RongIMClient.getInstance().getConversationList(callback);
    }

}
