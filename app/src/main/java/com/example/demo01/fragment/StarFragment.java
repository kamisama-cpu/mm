package com.example.demo01.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.example.demo01.R;
import com.example.demo01.adapter.TagCloudAdapter;
import com.example.demo01.ui.AddFriendActivity;
import com.example.fromwork.base.BaseFragment;
import com.moxun.tagcloudlib.view.TagCloudView;

import java.util.ArrayList;
import java.util.List;

/**
 * 星球
 */
public class StarFragment extends BaseFragment implements View.OnClickListener {
    private TagCloudView tcv;
    private List<String> list = new ArrayList<>();
    private ImageView ivStarAdd;
    private ImageView ivCamera;
    @Override
    public void initView() {
        tcv = (TagCloudView) findViewById(R.id.tcv);
        ivStarAdd = (ImageView) findViewById(R.id.iv_star_add);
        ivCamera = (ImageView) findViewById(R.id.iv_camera);

        for (int i = 0 ; i < 100 ; i++){
            list.add("喵喵"+i+"号");
        }

        TagCloudAdapter tagCloudAdapter = new TagCloudAdapter(list);
        tcv.setAdapter(tagCloudAdapter);
        ivStarAdd.setOnClickListener(this);
        ivCamera.setOnClickListener(this);
    }



    @Override
    public int initLayout() {
        return R.layout.star_fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_star_add:
                Intent intent = new Intent(getContext(), AddFriendActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_camera:
                break;
        }
    }
}
