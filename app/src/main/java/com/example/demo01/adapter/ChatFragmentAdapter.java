package com.example.demo01.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class ChatFragmentAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<Fragment> fragments;
    public ChatFragmentAdapter(@NonNull FragmentManager fm, Context context, List<Fragment> fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }



    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "聊天记录";
            case 1:
                return "通话记录";
            case 2:
                return "全部好友";
                default:
                    return "喵喵";
        }

    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
