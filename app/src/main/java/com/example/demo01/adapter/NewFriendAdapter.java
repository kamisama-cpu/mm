package com.example.demo01.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.demo01.R;
import com.example.fromwork.bomb.BmobManager;
import com.example.fromwork.bomb.Friend;
import com.example.fromwork.db.NewFriend;
import com.example.fromwork.entity.FriendInfo;
import com.example.fromwork.entity.IMUser;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class NewFriendAdapter extends BaseQuickAdapter<FriendInfo, BaseViewHolder> {
    public NewFriendAdapter(int layoutResId, @Nullable List<FriendInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendInfo item) {
        ImageView ivPhoto = helper.getView(R.id.iv_photo);
        ImageView ivSex = helper.getView(R.id.iv_sex);
        TextView tvNickname = helper.getView(R.id.tv_nickname);
        TextView tvAge = helper.getView(R.id.tv_age);
        TextView tvDesc = helper.getView(R.id.tv_desc);
        TextView tvMsg = helper.getView(R.id.tv_msg);
        LinearLayout llAgree =helper.getView(R.id.ll_agree);
        LinearLayout llYes = helper.getView(R.id.ll_yes);
        LinearLayout llNo = helper.getView(R.id.ll_no);
        TextView tvResult = helper.getView(R.id.tv_result);

        helper.addOnClickListener(R.id.ll_yes);
        helper.addOnClickListener(R.id.ll_no);



        BmobManager.getInstance().queryObjectIdUser(item.getUserID(), new FindListener<IMUser>() {
            @Override
            public void done(List<IMUser> list, BmobException e) {
                if (e==null){
                    IMUser imUser = list.get(0);
                    Glide.with(mContext).load(imUser.getPhoto()).transform(new CircleCrop()).into(ivPhoto);
                    if (imUser.isSex()){
                        Glide.with(mContext).load(R.drawable.img_boy_icon).into(ivSex);
                    }else {
                        Glide.with(mContext).load(R.drawable.img_girl_icon).into(ivSex);
                    }
                    tvNickname.setText(imUser.getNickName()+"");
                    tvAge.setText(imUser.getAge()+"");
                    tvDesc.setText(imUser.getDesc()+"");
                    if (item.getIsAgree().equals("0")){
                        llAgree.setVisibility(View.GONE);
                        tvResult.setVisibility(View.VISIBLE);
                        tvResult.setText("已同意");
                    }else if (item.getIsAgree().equals("1")){
                        llAgree.setVisibility(View.GONE);
                        tvResult.setVisibility(View.VISIBLE);
                        tvResult.setText("已拒绝");
                    }
                }
            }
        });
    }
}
