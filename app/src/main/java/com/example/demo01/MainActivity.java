package com.example.demo01;


import android.os.Bundle;

import com.example.demo01.adapter.TagCloudAdapter;
import com.example.fromwork.base.BaseActivity;
import com.example.fromwork.utils.SystemUIUtils;
import com.moxun.tagcloudlib.view.TagCloudView;
import com.shuyu.gsyvideoplayer.video.GSYADVideoPlayer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private List<String> list = new ArrayList<>();
    private TagCloudView view;
    private GSYADVideoPlayer gdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (TagCloudView) findViewById(R.id.view);
        gdp = (GSYADVideoPlayer) findViewById(R.id.gdp);

        SystemUIUtils.SystemUi(MainActivity.this);

        for (int i = 0  ; i< 50 ; i++){
            list.add("喵喵"+i+"号");
        }

        TagCloudAdapter tagCloudAdapter = new TagCloudAdapter(list);
        view.setAdapter(tagCloudAdapter);

    }
}
