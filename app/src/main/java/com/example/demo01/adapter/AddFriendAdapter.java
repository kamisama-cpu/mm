package com.example.demo01.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.example.demo01.R;
import com.example.demo01.mode.AddFriendModel;
import com.example.fromwork.utils.LogUtils;

import java.util.List;

public class AddFriendAdapter extends BaseQuickAdapter<AddFriendModel, BaseViewHolder> {

    public static final int TITLE = 0;
    public static final int CONTENT = 1;

    public AddFriendAdapter(@Nullable List<AddFriendModel> data) {
        super(data);
        setMultiTypeDelegate(new MultiTypeDelegate<AddFriendModel>() {
            @Override
            protected int getItemType(AddFriendModel addFriendModel) {
                return addFriendModel.getType();
            }
        });

        getMultiTypeDelegate().registerItemType(CONTENT,R.layout.layout_add_friend_item);
        getMultiTypeDelegate().registerItemType(TITLE,R.layout.layout_search_title_item);

    }

    @Override
    protected void convert(BaseViewHolder helper, AddFriendModel item) {
        switch (helper.getItemViewType()){
            case TITLE:
                TextView tvTitle = helper.getView(R.id.tv_title);
                tvTitle.setText(item.getTitle());
                break;
            case CONTENT:

                ImageView ivPhoto = helper.getView(R.id.iv_photo);
                ImageView ivSex = helper.getView(R.id.iv_sex);

                TextView tvNickname = helper.getView(R.id.tv_nickname);
                TextView tvAge = helper.getView(R.id.tv_age);
                TextView tvDesc = helper.getView(R.id.tv_desc);

                Glide.with(mContext).load(item.getPhoto()).transform(new CircleCrop()).into(ivPhoto);
                if (item.isSex()){
                    Glide.with(mContext).load(R.drawable.img_boy_icon).into(ivSex);
                }else {
                    Glide.with(mContext).load(R.drawable.img_girl_icon).into(ivSex);
                }
                tvNickname.setText(item.getNickName()+"");
                tvAge.setText(item.getAge()+"");
                tvDesc.setText(item.getDesc()+"");
                break;
        }


    }
}
