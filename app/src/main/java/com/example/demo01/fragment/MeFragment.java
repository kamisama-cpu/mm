package com.example.demo01.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.demo01.R;
import com.example.demo01.ui.NewFriendActivity;
import com.example.fromwork.base.BaseFragment;
import com.example.fromwork.bomb.BmobManager;

import cn.bmob.v3.Bmob;

/**
 * 我的
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {
    private ImageView ivMePhoto;
    private TextView tvNickname;
    private TextView tvServerStatus;
    private LinearLayout llMeInfo;
    private LinearLayout llNewFriend;
    private LinearLayout llPrivateSet;
    private LinearLayout llShare;
    private LinearLayout llNotice;
    private LinearLayout llSetting;
    @Override
    public void initView() {
        ivMePhoto = (ImageView) findViewById(R.id.iv_me_photo);
        tvNickname = (TextView) findViewById(R.id.tv_nickname);
        tvServerStatus = (TextView) findViewById(R.id.tv_server_status);
        llMeInfo = (LinearLayout) findViewById(R.id.ll_me_info);
        llNewFriend = (LinearLayout) findViewById(R.id.ll_new_friend);
        llPrivateSet = (LinearLayout) findViewById(R.id.ll_private_set);
        llShare = (LinearLayout) findViewById(R.id.ll_share);
        llNotice = (LinearLayout) findViewById(R.id.ll_notice);
        llSetting = (LinearLayout) findViewById(R.id.ll_setting);
        Glide.with(this).load(BmobManager.getInstance().getUser().getPhoto()).transform(new CircleCrop()).into(ivMePhoto);
        tvNickname.setText(BmobManager.getInstance().getUser().getTokenNickName());
        llNewFriend.setOnClickListener(this);
    }

    @Override
    public int initLayout() {
        return R.layout.me_fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_new_friend:
                Intent intent = new Intent(getContext(), NewFriendActivity.class);
                startActivity(intent);
                break;

        }
    }
}
