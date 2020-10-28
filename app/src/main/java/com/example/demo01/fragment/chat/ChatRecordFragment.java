package com.example.demo01.fragment.chat;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo01.R;
import com.example.demo01.adapter.ChatFragmentAdapter;
import com.example.demo01.adapter.ChatRecordAdapter;
import com.example.demo01.mode.ChatRecordModel;
import com.example.fromwork.base.BaseFragment;
import com.example.fromwork.bomb.BmobManager;
import com.example.fromwork.cloud.CloudManager;
import com.example.fromwork.db.CallRecord;
import com.example.fromwork.entity.IMUser;
import com.example.fromwork.gson.TextBean;
import com.example.fromwork.utils.CommonUtils;
import com.example.fromwork.utils.LogUtils;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

/**
 * 聊天记录
 */
public class ChatRecordFragment extends BaseFragment {
    private SmartRefreshLayout smart;
    private RecyclerView mChatRecordView;
    private List<ChatRecordModel> chatRecordModels = new ArrayList<>();
    private ChatRecordAdapter chatRecordAdapter;
    private View item_empty_view;
    @Override
    public void initView() {
        smart = (SmartRefreshLayout) findViewById(R.id.smart);
        item_empty_view = (View) findViewById(R.id.item_empty_view);
        mChatRecordView = (RecyclerView) findViewById(R.id.mChatRecordView);
        chatRecordAdapter = new ChatRecordAdapter(R.layout.layout_chat_record_item, chatRecordModels);
        mChatRecordView.setLayoutManager(new LinearLayoutManager(getContext()));
        mChatRecordView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        mChatRecordView.setAdapter(chatRecordAdapter);
        smart.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //下拉刷新
                queryFriend();
                LogUtils.e("喵喵下拉刷新");
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //上拉加载
                queryFriend();
                LogUtils.e("喵喵上拉加载");
            }
        });
    }

    private void queryFriend(){
        CloudManager.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (CommonUtils.isEmpty(conversations)){
                    if (chatRecordModels.size()>0){
                        chatRecordModels.clear();
                    }

                    for (int i = 0 ; i < conversations.size() ; i++){

                        Conversation conversation = conversations.get(i);
                        String targetId = conversation.getTargetId();
                        BmobManager.getInstance().queryObjectIdUser(targetId, new FindListener<IMUser>() {
                            @Override
                            public void done(List<IMUser> list, BmobException e) {
                                if (e == null){
                                    if (CommonUtils.isEmpty(list)){
                                        IMUser imUser = list.get(0);
                                        ChatRecordModel chatRecordModel = new ChatRecordModel();
                                        chatRecordModel.setUrl(imUser.getPhoto());
                                        chatRecordModel.setNickName(imUser.getNickName());
                                        chatRecordModel.setTime(System.currentTimeMillis()+"::当前时间(毫秒)");
                                        chatRecordModel.setUnReadSize(conversation.getUnreadMessageCount());
                                        String objectName = conversation.getObjectName();
                                        if (objectName.equals(CloudManager.MSG_TEXT_NAME)){
                                            //文字
                                            TextMessage latestMessage = (TextMessage) conversation.getLatestMessage();
                                            String content = latestMessage.getContent();
                                            Gson gson = new Gson();
                                            TextBean textBean = gson.fromJson(content, TextBean.class);
                                            if (textBean.equals(CloudManager.TYPE_TEXT)){
                                                chatRecordModel.setEndMsg(textBean.getMsg());
                                            }

                                        }else if (objectName.equals(CloudManager.MSG_IMAGE_NAME)){
                                            //图片
                                            chatRecordModel.setEndMsg("[图片]");
                                        }else if (objectName.equals(CloudManager.MSG_LOCATION_NAME)){
                                            //地址
                                            chatRecordModel.setEndMsg("[地址]");
                                        }
                                        chatRecordModels.add(chatRecordModel);
                                        chatRecordAdapter.notifyDataSetChanged();

                                        if (chatRecordModels.size()>0){
                                            item_empty_view.setVisibility(View.GONE);
                                            mChatRecordView.setVisibility(View.VISIBLE);
                                        }else {
                                            item_empty_view.setVisibility(View.VISIBLE);
                                            mChatRecordView.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            }
                        });
                    }
                    smart.finishLoadMore();
                    smart.finishRefresh();
                }else {
                    item_empty_view.setVisibility(View.VISIBLE);
                    mChatRecordView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }


    @Override
    public int initLayout() {
        return R.layout.fragment_chat_record;
    }
}
