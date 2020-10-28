package com.example.demo01.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.demo01.R;
import com.example.demo01.mode.UserInfoModel;

import java.util.List;

public class UserInfoAdapter extends BaseQuickAdapter<UserInfoModel, BaseViewHolder> {
    public UserInfoAdapter(int layoutResId, @Nullable List<UserInfoModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserInfoModel item) {
        TextView type = helper.getView(R.id.tv_type);
        TextView content = helper.getView(R.id.tv_content);
        LinearLayout ll = helper.getView(R.id.ll_bg);
        ll.setBackgroundColor(item.getBgColor());


        type.setText(item.getTitle());
        content.setText(item.getContent());
    }
}
