package com.example.demo01.fragment.chat;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.demo01.R;
import com.example.demo01.adapter.AllFriendAdapter;
import com.example.demo01.mode.AllFriendModel;
import com.example.demo01.ui.ChatActivity;
import com.example.fromwork.base.BaseFragment;
import com.example.fromwork.bomb.BmobManager;
import com.example.fromwork.bomb.Friend;
import com.example.fromwork.entity.Constants;
import com.example.fromwork.entity.IMUser;
import com.example.fromwork.utils.CommonUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 *所有联系人
 */

public class AllFriendFragment extends BaseFragment {
    private SmartRefreshLayout smart;
    private RecyclerView mChatRecordView;
    private View item_empty_view;
    private List<AllFriendModel> friendusers = new ArrayList<>();
    private AllFriendAdapter allFriendAdapter;
    @Override
    public void initView() {
        smart = (SmartRefreshLayout) findViewById(R.id.smart);
        mChatRecordView = (RecyclerView) findViewById(R.id.mChatRecordView);
        item_empty_view = (View) findViewById(R.id.item_empty_view);

        allFriendAdapter = new AllFriendAdapter(R.layout.layout_all_friend_item,friendusers);
        mChatRecordView.setAdapter(allFriendAdapter);
        mChatRecordView.setLayoutManager(new LinearLayoutManager(getContext()));
        allFriendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra(Constants.INTENT_ID,allFriendAdapter.getData().get(position).getUserId());
                intent.putExtra(Constants.INTENT_NAME,allFriendAdapter.getData().get(position).getNickName());
                intent.putExtra(Constants.INTENT_PHOTO,allFriendAdapter.getData().get(position).getUrl());
                startActivity(intent);
            }
        });
        queryMyFriend();

    }

    /**
     * 查询所有好友
     */
    private void queryMyFriend() {
        BmobManager.getInstance().queryMyFriends(new FindListener<Friend>() {
            @Override
            public void done(List<Friend> list, BmobException e) {

                if (e == null){
                    if (friendusers.size()>0){
                        friendusers.clear();
                    }
                    if (CommonUtils.isEmpty(list)){
                        for (int i = 0 ; i < list.size() ; i++){
                            Friend friend = list.get(i);
                            String objectId = friend.getFriendUser().getObjectId();
                            BmobManager.getInstance().queryObjectIdUser(objectId, new FindListener<IMUser>() {
                                @Override
                                public void done(List<IMUser> list, BmobException e) {
                                    if (e == null){
                                        if (CommonUtils.isEmpty(list)){
                                            IMUser imUser = list.get(0);
                                            AllFriendModel allFriendModel = new AllFriendModel();
                                            allFriendModel.setUserId(imUser.getObjectId());
                                            allFriendModel.setUrl(imUser.getPhoto());
                                            allFriendModel.setDesc(imUser.getDesc());
                                            allFriendModel.setNickName(imUser.getNickName());
                                            allFriendModel.setSex(imUser.isSex());
                                            friendusers.add(allFriendModel);
                                            allFriendAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            });
                        }
                        item_empty_view.setVisibility(View.GONE);
                        mChatRecordView.setVisibility(View.VISIBLE);
                    }else {
                        item_empty_view.setVisibility(View.VISIBLE);
                        mChatRecordView.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public int initLayout() {
        return R.layout.fragment_all_friend;
    }
}
