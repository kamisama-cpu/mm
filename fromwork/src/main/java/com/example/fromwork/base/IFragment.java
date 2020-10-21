package com.example.fromwork.base;

import android.view.View;

import androidx.annotation.IdRes;

public interface IFragment extends IActivity {

    <T extends View> T findViewById(@IdRes int id);

}
