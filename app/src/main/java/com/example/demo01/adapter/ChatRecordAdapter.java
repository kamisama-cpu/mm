package com.example.demo01.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.demo01.R;
import com.example.demo01.mode.ChatRecordModel;

import java.util.List;

public class ChatRecordAdapter extends BaseQuickAdapter<ChatRecordModel, BaseViewHolder> {
    public ChatRecordAdapter(int layoutResId, @Nullable List<ChatRecordModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatRecordModel item) {
        ImageView ivPhoto = helper.getView(R.id.iv_photo);
        TextView tvNickname = helper.getView(R.id.tv_nickname);
        TextView tvUnRead = helper.getView(R.id.tv_un_read);
        TextView tvContent = helper.getView(R.id.tv_content);
        TextView tvTime = helper.getView(R.id.tv_time);


        Glide.with(mContext).load(item.getUrl()).transform(new CircleCrop()).into(ivPhoto);
        tvNickname.setText(item.getNickName());
        tvContent.setText(item.getEndMsg());
        tvTime.setText(item.getTime());

        if (item.getUnReadSize()==0){
            tvUnRead.setVisibility(View.GONE);
        }else {
            tvUnRead.setVisibility(View.VISIBLE);
            tvUnRead.setText(item.getUnReadSize()+"");
        }
    }
}
