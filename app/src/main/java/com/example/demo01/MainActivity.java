package com.example.demo01;


import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.demo01.fragment.ChatFragment;
import com.example.demo01.fragment.MeFragment;
import com.example.demo01.fragment.PlazaFragment;
import com.example.demo01.fragment.StarFragment;
import com.example.demo01.service.CloudService;
import com.example.demo01.ui.ChangedUserMsgActivity;
import com.example.fromwork.base.BaseActivity;
import com.example.fromwork.bomb.BmobManager;
import com.example.fromwork.entity.Constants;
import com.example.fromwork.entity.IMUser;
import com.example.fromwork.gson.TokenBean;
import com.example.fromwork.java.SimulationData;
import com.example.fromwork.manager.DialogManager;
import com.example.fromwork.manager.HttpManager;
import com.example.fromwork.utils.LogUtils;
import com.example.fromwork.utils.SpUtils;
import com.example.fromwork.view.DialogView;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity {
    private RadioGroup radio;
    private Fragment chatFragment,meFragment,plazaFragment,starFragment;
    private DialogView dialogView;
    private  Disposable disposable;
    @Override
    public int initLayout() {
        return R.layout.activity_main;
    }

    //初始化控件
    @Override
    public void initView() {
        checkToken();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        if (dialogView!=null){
            dialogView.dismiss();
        }
    }

    /**
     * 检查token
     */
    private void checkToken(){

        SimulationData.testData();
        String token = SpUtils.getInstance().getString(Constants.SP_TOKE, "");
        if (!(TextUtils.isEmpty(token))){
            startCloudService();
        }else {
            //创建token
            IMUser user = BmobManager.getInstance().getUser();
            String tokenNickName = user.getTokenNickName();
            String tokenPhoto = user.getTokenPhoto();
            if (TextUtils.isEmpty(tokenNickName) && TextUtils.isEmpty(tokenPhoto)){
                createDialog();
            }else {
                createToken();

            }
        }

    }

    /**
     * 创建token
     */
    private void createToken() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", BmobManager.getInstance().getUser().getObjectId());
        map.put("name", BmobManager.getInstance().getUser().getTokenNickName());
        map.put("portraitUri", BmobManager.getInstance().getUser().getTokenPhoto());

//通过OkHttp请求Token

        disposable = Observable.create(new ObservableOnSubscribe<String>() {
           @Override
           public void subscribe(ObservableEmitter<String> emitter) throws Exception {
               //执行请求过程
               String json = HttpManager.getInstance().postCloudToken(map);
               emitter.onNext(json);
               emitter.onComplete();
           }
           //线程调度
       }).subscribeOn(Schedulers.newThread())
               .subscribeOn(AndroidSchedulers.mainThread())
               .subscribe(new Consumer<String>() {
                   @Override
                   public void accept(String s) throws Exception {
                       parsingCloudToken(s);
                   }
               });


    }

    /**
     * 启动云服务去连接融云服务
     */
    private void startCloudService() {
        LogUtils.e("startCloudService");
        startService(new Intent(this, CloudService.class));
    }

    /**
     * 解析Token
     *
     * @param s
     */
    private void parsingCloudToken(String s) {
        try {
            LogUtils.e("parsingCloudToken:" + s);
            TokenBean tokenBean = new Gson().fromJson(s, TokenBean.class);
            if (tokenBean.getCode() == 200) {
                if (!TextUtils.isEmpty(tokenBean.getToken())) {
                    //保存Token
                    SpUtils.getInstance().putString(Constants.SP_TOKE, tokenBean.getToken());
                    startCloudService();
                }
            } else if (tokenBean.getCode() == 2007) {
                Toast.makeText(this, "注册人数已达上限，请替换成自己的Key", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            LogUtils.e("parsingCloudToken:" + e.toString());
        }
    }


    /**
     * 创建对话框
     */
    public void createDialog(){
        dialogView = DialogManager.getInstance().initDialog(this, R.layout.dialog_frist_upload);
        //点击外部不消失
        dialogView.setCancelable(false);
        ImageView upload = dialogView.findViewById(R.id.iv_go_upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "做梦？打你哦(￣ε(#￣)☆╰╮(￣▽￣///)", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ChangedUserMsgActivity.class);
                startActivity(intent);
            }
        });
        dialogView.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null){
            if (disposable.isDisposed()){
                disposable.dispose();
            }
        }

    }
}
