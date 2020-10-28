package com.example.demo01.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.demo01.R;
import com.example.demo01.adapter.AddFriendAdapter;
import com.example.demo01.mode.AddFriendModel;
import com.example.fromwork.base.BaseActivity;
import com.example.fromwork.bomb.BmobManager;
import com.example.fromwork.bomb.SquareSet;
import com.example.fromwork.entity.IMUser;
import com.example.fromwork.utils.CommonUtils;
import com.example.fromwork.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class AddFriendActivity extends BaseActivity implements View.OnClickListener {
    private EditText etPhone;
    private ImageView ivSearch;
    private RecyclerView mSearchResultView;
    private List<AddFriendModel> friendModels = new ArrayList<>();
    private AddFriendAdapter addFriendAdapter;
    private View include_empty_view;
    private TextView tvContact;
    @Override
    public void initView() {
        tvContact = (TextView) findViewById(R.id.tv_contact);
        include_empty_view = findViewById(R.id.include_empty_view);
        etPhone = (EditText) findViewById(R.id.et_phone);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
        mSearchResultView = (RecyclerView) findViewById(R.id.mSearchResultView);
        etPhone.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        tvContact.setOnClickListener(this);
        addFriendAdapter = new AddFriendAdapter(friendModels);
        mSearchResultView.setAdapter(addFriendAdapter);
        mSearchResultView.setLayoutManager(new LinearLayoutManager(AddFriendActivity.this));
        addFriendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String userId = addFriendAdapter.getData().get(position).getUserId();
                LogUtils.e("当前点击用户是"+userId);
                UserInfoActivity.startActivity(AddFriendActivity.this,userId);
            }
        });

    }

    @Override
    public int initLayout() {
        return R.layout.activity_add_friend;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_contact:
                Intent intent = new Intent(AddFriendActivity.this,ContactFriendActivity.class);
                startActivity(intent);
                break;
            case R.id.et_phone:
                break;
            case R.id.iv_search:
                queryPhoneFriend();
                break;
        }
    }

    /**
     * 通过电话查询
     */
    private void queryPhoneFriend() {
        String phone = String.valueOf(etPhone.getText()).trim();
        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "未获取到电话号码", Toast.LENGTH_SHORT).show();
            return;
        }
        //2.过滤自己
        String phoneNumber = BmobManager.getInstance().getUser().getMobilePhoneNumber();
        LogUtils.e("phoneNumber:" + phoneNumber);
        if (phone.equals(phoneNumber)) {
            Toast.makeText(this, "不能查询自己", Toast.LENGTH_SHORT).show();
            return;
        }

        BmobManager.getInstance().queryPhoneUser(phone, new FindListener<IMUser>() {
            @Override
            public void done(List<IMUser> list, BmobException e) {
                if (e == null){
                    if (CommonUtils.isEmpty(list) ){
                        include_empty_view.setVisibility(View.GONE);
                        mSearchResultView.setVisibility(View.VISIBLE);

                        LogUtils.e("查询成功");
                        IMUser imUser = list.get(0);
                        list.clear();
                        addTitle("查询结果");
                        addFriend(imUser);
                        allFriend();
                        addFriendAdapter.notifyDataSetChanged();
                    }
                }else {
                    LogUtils.e("查询失败");
                    include_empty_view.setVisibility(View.VISIBLE);
                    mSearchResultView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void allFriend() {
        BmobManager.getInstance().queryAllUser(new FindListener<IMUser>() {
            @Override
            public void done(List<IMUser> list, BmobException e) {
                if (e == null) {
                    if (CommonUtils.isEmpty(list)) {
                        addTitle("推荐");
                        LogUtils.e("梦梦"+list.size());
                        int num = (list.size() <= 100) ? list.size() : 100;
                        for (int i = 0; i < num; i++) {
                            //也不能自己推荐给自己
                            String phoneNumber = BmobManager.getInstance().getUser().getMobilePhoneNumber();
                            if (list.get(i).getMobilePhoneNumber().equals(phoneNumber)){
                                continue;
                            }
                            addFriend(list.get(i));
                        }
                        addFriendAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    /**
     * 添加标题
     * @param title
     */
    private void addTitle(String title){
        AddFriendModel addFriendModel = new AddFriendModel();
        addFriendModel.setType(AddFriendAdapter.TITLE);
        addFriendModel.setTitle(title);
        friendModels.add(addFriendModel);
    }

    private void addFriend(IMUser imUser){
        AddFriendModel addFriendModel = new AddFriendModel();
        addFriendModel.setType(AddFriendAdapter.CONTENT);
        addFriendModel.setUserId(imUser.getObjectId());
        addFriendModel.setPhoto(imUser.getPhoto()+"");
        addFriendModel.setSex(imUser.isSex());
        addFriendModel.setAge(imUser.getAge());
        addFriendModel.setNickName(imUser.getNickName()+"");
        addFriendModel.setDesc(imUser.getDesc()+"");
        friendModels.add(addFriendModel);
    }
}
