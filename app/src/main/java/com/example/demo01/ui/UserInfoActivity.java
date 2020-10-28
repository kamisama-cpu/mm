package com.example.demo01.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.demo01.R;
import com.example.demo01.adapter.UserInfoAdapter;
import com.example.demo01.mode.UserInfoModel;
import com.example.fromwork.base.BaseActivity;
import com.example.fromwork.bomb.BmobManager;
import com.example.fromwork.bomb.Friend;
import com.example.fromwork.cloud.CloudManager;
import com.example.fromwork.entity.IMUser;
import com.example.fromwork.utils.CommonUtils;
import com.example.fromwork.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout llBack;
    private ImageView ivUserPhoto;
    private TextView tvNickname;
    private TextView tvDesc;
    private RecyclerView mUserInfoView;
    private Button btnAddFriend;
    private LinearLayout llIsFriend;
    private Button btnChat;
    private Button btnAudioChat;
    private Button btnVideoChat;
    private String userID;
    private List<UserInfoModel> userInfoModels = new ArrayList<>();
    private UserInfoAdapter userInfoAdapter;
    /**
     * 跳转
     */
    public static void startActivity(Context context,String userid){
        Intent intent = new Intent(context,UserInfoActivity.class);
        intent.putExtra("userID",userid);
        context.startActivity(intent);
    }

    @Override
    public void initView() {

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        llBack = (RelativeLayout) findViewById(R.id.ll_back);
        ivUserPhoto = (ImageView) findViewById(R.id.iv_user_photo);
        tvNickname = (TextView) findViewById(R.id.tv_nickname);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        mUserInfoView = (RecyclerView) findViewById(R.id.mUserInfoView);
        btnAddFriend = (Button) findViewById(R.id.btn_add_friend);
        llIsFriend = (LinearLayout) findViewById(R.id.ll_is_friend);
        btnChat = (Button) findViewById(R.id.btn_chat);
        btnAudioChat = (Button) findViewById(R.id.btn_audio_chat);
        btnVideoChat = (Button) findViewById(R.id.btn_video_chat);

        llBack.setOnClickListener(this);
        btnAddFriend.setOnClickListener(this);
        btnChat.setOnClickListener(this);
        btnAudioChat.setOnClickListener(this);
        btnVideoChat.setOnClickListener(this);


        userInfoAdapter = new UserInfoAdapter(R.layout.layout_user_info_item,userInfoModels);
        mUserInfoView.setAdapter(userInfoAdapter);
        mUserInfoView.setLayoutManager(new GridLayoutManager(UserInfoActivity.this,3));

        queueUserFriend();

    }

    private AlertDialog show;
    private EditText etMsg;
    private TextView tvCancel;
    private TextView tvAddFriend;

    private void initAddFriendDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
        View inflate = LayoutInflater.from(UserInfoActivity.this).inflate(R.layout.dialog_send_friend, null);
        etMsg = (EditText) inflate.findViewById(R.id.et_msg);
        tvCancel = (TextView) inflate.findViewById(R.id.tv_cancel);
        tvAddFriend = (TextView) inflate.findViewById(R.id.tv_add_friend);
        builder.setTitle("号码验证");
        builder.setMessage("请输入手机号码");
        builder.setView(inflate);
        tvCancel.setOnClickListener(this);
        tvAddFriend.setOnClickListener(this);

        show = builder.show();

    }
    /**
     * 查询用户信息
     *
     */
    private void queueUserFriend() {
        if (userID!=null){
            BmobManager.getInstance().queryObjectIdUser(userID, new FindListener<IMUser>() {
                @Override
                public void done(List<IMUser> list, BmobException e) {
                    if (e == null){
                        if (CommonUtils.isEmpty(list)){
                            IMUser imUser = list.get(0);
                            upDataUserInfo(imUser);
                        }
                    }else {
                        LogUtils.e("查找用户失败"+e.toString());
                    }
                }
            });
        }
        //判断好友关系
        BmobManager.getInstance().queryMyFriends(new FindListener<Friend>() {
            @Override
            public void done(List<Friend> list, BmobException e) {
                if (e == null){
                    if (CommonUtils.isEmpty(list)){
                        //好友存在
                        for (int i = 0 ; i < list.size() ; i++){
                            Friend friend = list.get(i);
                            //判断id是否相同
                            if (friend.getFriendUser().getObjectId().equals(userID)){
                                btnAddFriend.setVisibility(View.GONE);
                                llIsFriend.setVisibility(View.VISIBLE);
                            }else {
                                btnAddFriend.setVisibility(View.VISIBLE);
                                llIsFriend.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }
        });

    }

    private void addUserInfoModel(int color,String title,String content){

        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setBgColor(color);
        userInfoModel.setTitle(title);
        userInfoModel.setContent(content);
        userInfoModels.add(userInfoModel);
        userInfoAdapter.notifyDataSetChanged();
    }

    private void upDataUserInfo(IMUser imUser) {
        Glide.with(UserInfoActivity.this).load(imUser.getPhoto()).transform(new CircleCrop()).into(ivUserPhoto);
        tvNickname.setText(imUser.getNickName());
        tvDesc.setText(imUser.getDesc());
        addUserInfoModel(Color.BLUE,"性别",imUser.isSex()?"男":"女");
        addUserInfoModel(0x88FF68F,"年龄",imUser.getAge()+"");
        addUserInfoModel(0x88FF68F,"生日",imUser.getBirthday());
        addUserInfoModel(0x88FF68F,"星座",imUser.getConstellation());
        addUserInfoModel(0x88FF68F,"爱好",imUser.getHobby());
        addUserInfoModel(0x88FF68F,"单身状态",imUser.getStatus());
    }

    @Override
    public int initLayout() {
        return R.layout.activity_user_info;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
                LogUtils.e("取消点击");
                if (show!=null){
                    show.dismiss();
                }
                break;
            case R.id.tv_add_friend:

                if (etMsg!=null) {
                    String msg = String.valueOf(etMsg.getText());
                    CloudManager.getInstance().sendAddFriendMessage(msg,CloudManager.TYPE_ADD_FRIEND,userID);
//                    CloudManager.getInstance().sendTextManager("喵喵天上第一","2a55a58d1f");
                }
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.btn_add_friend:
                initAddFriendDialog();
                break;
            case R.id.btn_chat:
                Intent intent = new Intent(UserInfoActivity.this,ChatActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_audio_chat:
                break;
            case R.id.btn_video_chat:
                break;
        }
    }
}
