package com.example.demo01.adapter;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.demo01.R;
import com.example.demo01.mode.AllFriendModel;

import java.util.List;

public class AllFriendAdapter extends BaseQuickAdapter<AllFriendModel, BaseViewHolder> {
    public AllFriendAdapter(int layoutResId, @Nullable List<AllFriendModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AllFriendModel item) {
        RelativeLayout rlItemview = helper.getView(R.id.rl_itemview);
        ImageView ivPhoto = helper.getView(R.id.iv_photo);
        ImageView ivSex = helper.getView(R.id.iv_sex);
        TextView tvNickname = helper.getView(R.id.tv_nickname);
        TextView tvDesc = helper.getView(R.id.tv_desc);

        Glide.with(mContext).load(item.getUrl()).transform(new CircleCrop()).into(ivPhoto);

        if (item.isSex()){
            Glide.with(mContext).load(R.drawable.img_boy_icon).into(ivSex);
        }else {
            Glide.with(mContext).load(R.drawable.img_girl_icon).into(ivSex);
        }
        tvNickname.setText(item.getNickName());
        tvDesc.setText(item.getDesc());

    }
}
