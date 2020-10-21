package com.example.demo01;


import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.demo01.adapter.TagCloudAdapter;
import com.example.demo01.fragment.ChatFragment;
import com.example.demo01.fragment.MeFragment;
import com.example.demo01.fragment.PlazaFragment;
import com.example.demo01.fragment.StarFragment;
import com.example.fromwork.base.BaseActivity;
import com.example.fromwork.entity.Constants;
import com.example.fromwork.utils.LogUtils;
import com.example.fromwork.utils.SpUtils;
import com.example.fromwork.utils.SystemUIUtils;
import com.example.fromwork.view.TouchPictureView;
import com.moxun.tagcloudlib.view.TagCloudView;
import com.shuyu.gsyvideoplayer.video.GSYADVideoPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity {
    private RadioGroup radio;
    private Fragment chatFragment,meFragment,plazaFragment,starFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SpUtils.getInstance().putBoolean(Constants.SP_TOKE,true);
        initView();
    }
    //初始化控件
    private void initView() {
        radio = (RadioGroup) findViewById(R.id.radio);
        chatFragment = new ChatFragment();
        meFragment = new MeFragment();
        plazaFragment = new PlazaFragment();
        starFragment = new StarFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_fragment,plazaFragment);
        fragmentTransaction.add(R.id.fl_fragment,chatFragment);
        fragmentTransaction.add(R.id.fl_fragment,meFragment);
        fragmentTransaction.add(R.id.fl_fragment,starFragment);
        fragmentTransaction.commit();

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_star:
                        //星球
                        replaceFm(starFragment);
                        break;
                    case R.id.rb_plaza:
                        //广场
                        replaceFm(plazaFragment);
                        break;
                    case R.id.rb_chat:
                        //聊天
                        replaceFm(chatFragment);
                        break;
                    case R.id.rb_me:
                        //我的
                        replaceFm(meFragment);
                        break;
                }
            }
        });
    }
    //切换fragment
    private void replaceFm(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(meFragment);
        fragmentTransaction.hide(plazaFragment);
        fragmentTransaction.hide(chatFragment);
        fragmentTransaction.hide(starFragment);
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }
}
