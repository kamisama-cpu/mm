package com.example.demo01.fragment;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.demo01.R;
import com.example.demo01.adapter.ChatFragmentAdapter;
import com.example.demo01.fragment.chat.AllFriendFragment;
import com.example.demo01.fragment.chat.CallRecordFragment;
import com.example.demo01.fragment.chat.ChatRecordFragment;
import com.example.fromwork.base.BaseFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天
 */
public class ChatFragment extends BaseFragment {
    private TabLayout tlTitle;
    private ViewPager vp;
    private List<Fragment> fragments = new ArrayList<>();
    @Override
    public void initView() {
        tlTitle = (TabLayout) findViewById(R.id.tl_title);
        vp = (ViewPager) findViewById(R.id.vp);

        AllFriendFragment allFriendFragment = new AllFriendFragment();
        CallRecordFragment callRecordFragment = new CallRecordFragment();
        ChatRecordFragment chatRecordFragment = new ChatRecordFragment();
        fragments.add(chatRecordFragment);
        fragments.add(callRecordFragment);
        fragments.add(allFriendFragment);
        ChatFragmentAdapter chatFragmentAdapter = new ChatFragmentAdapter(getFragmentManager(),getContext(),fragments);
        vp.setAdapter(chatFragmentAdapter);
        tlTitle.setupWithViewPager(vp);
    }

    @Override
    public int initLayout() {
        return R.layout.chat_fragment;
    }
}
