package com.example.demo01.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.demo01.R;
import com.example.demo01.adapter.NewFriendAdapter;
import com.example.fromwork.base.BaseActivity;
import com.example.fromwork.bomb.BmobManager;
import com.example.fromwork.db.SqlHelper;
import com.example.fromwork.entity.FriendInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class NewFriendActivity extends BaseActivity {
    private ViewStub itemEmptyView;
    private RecyclerView mNewFriendView;
    private List<FriendInfo> mList = new ArrayList<>();
    private NewFriendAdapter newFriendAdapter;
    @Override
    public void initView() {
        mList.clear();
        itemEmptyView = (ViewStub) findViewById(R.id.item_empty_view);
        mNewFriendView = (RecyclerView) findViewById(R.id.mNewFriendView);
        newFriendAdapter = new NewFriendAdapter(R.layout.layout_new_friend_item,mList);
        mNewFriendView.setAdapter(newFriendAdapter);
        mNewFriendView.setLayoutManager(new LinearLayoutManager(this));
        queryNewFriend();

        newFriendAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String userID = newFriendAdapter.getData().get(position).getUserID();
                TextView textView = (TextView) newFriendAdapter.getViewByPosition(mNewFriendView, position, R.id.tv_result);
                switch (view.getId()){
                    case R.id.ll_yes:
                        Toast.makeText(NewFriendActivity.this, "同意好友请求", Toast.LENGTH_SHORT).show();
                        BmobManager.getInstance().addFriend(userID);

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("isAgree","0");

                        SqlHelper.getInstance().updateUser(contentValues);
                        break;
                    case R.id.ll_no:
                        Toast.makeText(NewFriendActivity.this, "拒绝好友请求", Toast.LENGTH_SHORT).show();

                        ContentValues contentValuess = new ContentValues();
                        contentValuess.put("isAgree","1");

                        SqlHelper.getInstance().updateUser(contentValuess);
                        break;
                }

            }
        });
    }

    @Override
    public int initLayout() {
        return R.layout.activity_new_friend;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        queryNewFriend();
    }

    /**
     * 查询新朋友
     */
    private void queryNewFriend() {
        /**
         * 从数据库中获取新朋友的信息并展示出来
         */
        mList.clear();
        List<FriendInfo> friendInfos = SqlHelper.getInstance().queryUser();
        mList.addAll(friendInfos);
        newFriendAdapter.notifyDataSetChanged();
    }

}
