package com.example.demo01.fragment;

import com.example.demo01.R;
import com.example.demo01.adapter.TagCloudAdapter;
import com.example.fromwork.base.BaseFragment;
import com.moxun.tagcloudlib.view.TagCloudView;
import com.moxun.tagcloudlib.view.TagsAdapter;

import java.util.ArrayList;
import java.util.List;

public class StarFragment extends BaseFragment {
    private TagCloudView tcv;
    private List<String> list = new ArrayList<>();
    @Override
    public void initView() {
        tcv = (TagCloudView) findViewById(R.id.tcv);

        for (int i = 0 ; i < 50 ; i++){
            list.add("喵喵"+i+"号");
        }

        TagCloudAdapter tagCloudAdapter = new TagCloudAdapter(list);
        tcv.setAdapter(tagCloudAdapter);
    }

    @Override
    public int initLayout() {
        return R.layout.star_fragment;
    }
}
