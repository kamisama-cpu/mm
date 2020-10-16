package com.example.fromwork.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.fromwork.utils.SystemUIUtils;

public class BaseUiActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUIUtils.SystemUi(this);
    }
}
