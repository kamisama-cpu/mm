package com.example.demo01.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo01.R;
import com.example.fromwork.base.BasePagerAdapter;
import com.example.fromwork.base.BaseUiActivity;
import com.example.fromwork.entity.Constants;
import com.example.fromwork.manager.MediaPlayerManager;
import com.example.fromwork.utils.SpUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 */
public class GuideActivity extends BaseUiActivity {
    private ViewPager mViewPager;
    private ImageView ivMusicSwitch;
    private TextView tvGuideSkip;
    private ImageView ivGuidePoint1;
    private ImageView ivGuidePoint2;
    private ImageView ivGuidePoint3;
    private String[] permissiones=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
    private List<View> viewPagerData = new ArrayList<>();
    private List<String> muiscList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        getMusic();
        checkPermission();
        initData();
        initView();

    }

    private void initData(){
        SpUtils.getInstance().initSp(this);
        SpUtils.getInstance().putBoolean(Constants.SP_IS_FIRST_APP,false);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        ivMusicSwitch = (ImageView) findViewById(R.id.iv_music_switch);
        tvGuideSkip = (TextView) findViewById(R.id.tv_guide_skip);
        ivGuidePoint1 = (ImageView) findViewById(R.id.iv_guide_point_1);
        ivGuidePoint2 = (ImageView) findViewById(R.id.iv_guide_point_2);
        ivGuidePoint3 = (ImageView) findViewById(R.id.iv_guide_point_3);

        View inflate1 = View.inflate(this, R.layout.guide_star_layout, null);
        View inflate2 = View.inflate(this, R.layout.guide_night_layout, null);
        View inflate3 = View.inflate(this, R.layout.guide_smile_layout, null);
        ImageView viewById1 = inflate1.findViewById(R.id.iv_guide_star);
        ImageView viewById2 = inflate2.findViewById(R.id.iv_guide_night);
        ImageView viewById3 = inflate3.findViewById(R.id.iv_guide_smile);
        viewPagerData.add(inflate1);
        viewPagerData.add(inflate2);
        viewPagerData.add(inflate3);

        mViewPager.setOffscreenPageLimit(viewPagerData.size());

        mViewPager.setAdapter(new BasePagerAdapter(this,viewPagerData));

        //帧动画
        AnimationDrawable animationDrawable1 = (AnimationDrawable) viewById1.getDrawable();
        animationDrawable1.start();
        AnimationDrawable animationDrawable2 = (AnimationDrawable) viewById2.getDrawable();
        animationDrawable2.start();
        AnimationDrawable animationDrawable3 = (AnimationDrawable) viewById3.getDrawable();
        animationDrawable3.start();


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        ivGuidePoint1.setImageResource(R.drawable.img_guide_point_p);
                        ivGuidePoint2.setImageResource(R.drawable.img_guide_point);
                        ivGuidePoint3.setImageResource(R.drawable.img_guide_point);
                        break;
                    case 1:
                        ivGuidePoint1.setImageResource(R.drawable.img_guide_point);
                        ivGuidePoint2.setImageResource(R.drawable.img_guide_point_p);
                        ivGuidePoint3.setImageResource(R.drawable.img_guide_point);
                        break;
                    case 2:
                        ivGuidePoint1.setImageResource(R.drawable.img_guide_point);
                        ivGuidePoint2.setImageResource(R.drawable.img_guide_point);
                        ivGuidePoint3.setImageResource(R.drawable.img_guide_point_p);
                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        //关闭音乐
        ivMusicSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMusic();
            }
        });
        //跳过引导页
        tvGuideSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuideActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void getMusic(){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        while (cursor.moveToNext()){
            //字段
            String title=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            //地址
            String data=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            //歌手
            String artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            //时长
            long duration=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            long size=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            Log.e("###", "音乐: "+title+":"+data+":"+artist+":"+duration+":"+size);
            muiscList.add(data);
        }
        MediaPlayerManager mediaPlayerManager = new MediaPlayerManager();
        mediaPlayerManager.startPlay(muiscList.get(0));
    }





    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission(){
        int readCode = ActivityCompat.checkSelfPermission(this,permissiones[0]);
        int writeCode = ActivityCompat.checkSelfPermission(this,permissiones[1]);
        if (!(readCode== PackageManager.PERMISSION_GRANTED)||!(writeCode==PackageManager.PERMISSION_GRANTED)){//只要有一项没有申请到
            //主动去申请权限
            requestPermissions(permissiones,100);//要动态申请的权限;100请求状态码->判断哪次动态申请的
        }
    }

    //动态权限申请回调方法->获取动态申请结果
    //permissions->动态申请的权限
    //grantResults->动态权限申请结果->若干结果中1个权限申请失败->申请失败
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (100==requestCode){
            boolean flag = true;
            //遍历请求结果数组->若有一个权限失败->flag=false->本次申请失败
            for (int code : grantResults){
                if (code!= PackageManager.PERMISSION_GRANTED) {
                    flag = false;
                    break;
                }
            }
            if (flag)
                Toast.makeText(this,"申请成功",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this,"申请失败",Toast.LENGTH_SHORT).show();
        }
    }

}
