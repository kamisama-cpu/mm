package com.example.demo01.ui;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.demo01.R;
import com.example.fromwork.base.BaseActivity;
import com.example.fromwork.bomb.BmobManager;
import com.example.fromwork.entity.Constants;
import com.example.fromwork.utils.SpUtils;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import java.io.File;

/**
 * 上传/填写信息页面
 */
public class ChangedUserMsgActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivUpload;
    private EditText etName;
    private Button btnAccomplish;

    @Override
    public void initView() {
        ivUpload = (ImageView) findViewById(R.id.iv_upload);
        etName = (EditText) findViewById(R.id.et_name);
        btnAccomplish = (Button) findViewById(R.id.btn_accomplish);
        Glide.with(this).load(R.drawable.a2).transform(new CircleCrop()).into(ivUpload);
        ivUpload.setOnClickListener(this);
        btnAccomplish.setOnClickListener(this);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0){
                    btnAccomplish.setBackgroundResource(R.drawable.upload_btn_style2);
                    btnAccomplish.setEnabled(true);
                }else {
                    btnAccomplish.setBackgroundResource(R.drawable.upload_btn_style1);

                    btnAccomplish.setEnabled(false);
                }
            }
        });

    }

    @Override
    public int initLayout() {
        return R.layout.activity_changed_user_msg;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_upload:
                PictureSelector.create(ChangedUserMsgActivity.this,100).selectPicture(true);
                break;
            case R.id.btn_accomplish:
                String name = String.valueOf(etName.getText());
                if (name.length() > 0){
                    SpUtils.getInstance().putString(Constants.SP_NAME,name);
                }
                finish();
                break;
        }
    }

    /**
     * 回调图片
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100){

            if (data!=null){
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                boolean cut = pictureBean.isCut();
                if (cut){
                    Glide.with(ChangedUserMsgActivity.this).load(pictureBean.getPath()).transform(new CircleCrop()).into(ivUpload);
                    String name = String.valueOf(etName.getText());
                    File file = new File(pictureBean.getPath());

                    //上传图片
                    BmobManager.getInstance().upLoadPicture(name,file);
                }
            }

        }
    }
}
