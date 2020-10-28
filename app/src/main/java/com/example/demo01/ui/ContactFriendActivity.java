package com.example.demo01.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.example.demo01.R;
import com.example.demo01.adapter.ContactFriendAdapter;
import com.example.demo01.mode.AddFriendModel;
import com.example.fromwork.base.BaseActivity;
import com.example.fromwork.bomb.BmobManager;
import com.example.fromwork.bomb.PrivateSet;
import com.example.fromwork.entity.IMUser;
import com.example.fromwork.utils.CommonUtils;
import com.example.fromwork.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ContactFriendActivity extends BaseActivity {
    private Disposable disposable;

    private RecyclerView mContactView;
    private Map<String, String> mContactMap = new HashMap<>();
    private ContactFriendAdapter  contactFriendAdapter;
    private List<AddFriendModel> mList = new ArrayList<>();
    @Override
    public void initView() {
        mContactView = (RecyclerView) findViewById(R.id.mContactView);
        contactFriendAdapter = new ContactFriendAdapter(R.layout.layout_add_friend_item,mList);
        mContactView.setAdapter(contactFriendAdapter);
        mContactView.setLayoutManager(new LinearLayoutManager(this));
        loadUser();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_contact_friend;
    }



    /**
     * 加载用户
     */
    private void loadUser() {

        disposable = Observable.create(new ObservableOnSubscribe<List<PrivateSet>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<PrivateSet>> emitter) throws Exception {
                //加载联系人
                loadContact();
                //查询我们的PrivateSet
                BmobManager.getInstance().queryPrivateSet(new FindListener<PrivateSet>() {
                    @Override
                    public void done(List<PrivateSet> list, BmobException e) {
                        if (e == null) {
                            emitter.onNext(list);
                            emitter.onComplete();
                        }
                    }
                });
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<PrivateSet>>() {
                    @Override
                    public void accept(List<PrivateSet> privateSets) throws Exception {
                        fixprivateSets(privateSets);
                        //这里判断无数据
                    }
                });
    }

    /**
     * 解析私有库的内容进行联系人过滤
     *
     * @param privateSets
     */
    private void fixprivateSets(List<PrivateSet> privateSets) {
        List<String> userListPhone = new ArrayList<>();

        if (CommonUtils.isEmpty(privateSets)) {
            for (int i = 0; i < privateSets.size(); i++) {
                PrivateSet sets = privateSets.get(i);
                String phone = sets.getPhone();
                userListPhone.add(phone);
            }
        }

        //拿到了后台所有字段的电话号码
        if (mContactMap.size() > 0) {
            for (final Map.Entry<String, String> entry : mContactMap.entrySet()) {
                //过滤：判断你当前的号码在私有库是否存在
                if (userListPhone.contains(entry.getValue())) {
                    continue;
                }
                BmobManager.getInstance().queryPhoneUser(entry.getValue(), new FindListener<IMUser>() {
                    @Override
                    public void done(List<IMUser> list, BmobException e) {
                        if (e == null) {
                            if (CommonUtils.isEmpty(list)) {
                                IMUser imUser = list.get(0);
                                addContent(imUser, entry.getKey(), entry.getValue());
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * 加载联系人
     */
    private void loadContact() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                , null, null, null, null);
        String name;
        String phone;
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phone = cursor.getString(cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.NUMBER));

            phone = phone.replace(" ", "").replace("-", "");
            mContactMap.put(name, phone);
        }
    }

    /**
     * 添加内容
     *
     * @param imUser
     */
    private void addContent(IMUser imUser, String name, String phone) {
        AddFriendModel model = new AddFriendModel();
        model.setUserId(imUser.getObjectId());
        model.setPhoto(imUser.getPhoto());
        model.setSex(imUser.isSex());
        model.setAge(imUser.getAge());
        model.setNickName(imUser.getNickName());
        model.setDesc(imUser.getDesc());

        model.setContact(true);
        model.setContactName(name);
        model.setContactPhone(phone);

        mList.add(model);
        contactFriendAdapter.notifyDataSetChanged();
    }}
