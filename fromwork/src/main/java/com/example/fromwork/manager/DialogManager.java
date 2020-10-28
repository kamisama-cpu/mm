package com.example.fromwork.manager;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import com.example.fromwork.R;
import com.example.fromwork.view.DialogView;

/**
 * 提示窗管理类
 */
public class DialogManager  {

    private static DialogManager instance;

    public DialogManager() {
    }

    public static DialogManager getInstance(){
        if (instance == null){
            synchronized (DialogManager.class){
                if (instance == null){
                    instance = new DialogManager();
                }
            }
        }
        return instance;
    }
    public DialogView initDialog(Context context,int layout){
        return new DialogView(context,layout, R.style.Theme_Dialog, Gravity.CENTER);
    }

    public DialogView initDialog(Context context,int layout,int grvaity){

        return new DialogView(context,layout, R.style.Theme_Dialog,grvaity);
    }


    public void show(DialogView dialogView){
        if (dialogView != null){
            //判断是不是已经显示
            if (!dialogView.isShowing()){
                dialogView.show();
            }
        }
    }

    public void hide(DialogView dialogView){

        if (dialogView!=null){
            //判断是不是已经显示
            if (dialogView.isShowing()){
                dialogView.dismiss();
            }
        }

    }



}
