package com.example.fromwork;

import android.content.Context;

import com.example.fromwork.bomb.BmobManager;
import com.example.fromwork.cloud.CloudManager;
import com.example.fromwork.db.Sql;
import com.example.fromwork.db.SqlHelper;
import com.example.fromwork.utils.SpUtils;

import org.litepal.LitePal;

public class Framework {

    private volatile static Framework mFramework;
    private Framework() {
    }

    public static Framework getFramework() {
        if (mFramework == null) {
            synchronized (Framework.class) {
                if (mFramework == null) {
                    mFramework = new Framework();
                }
            }
        }
        return mFramework;
    }

    /**
     * 初始化框架
     * @param context
     */
    public void initFramework(Context context){

        SpUtils.getInstance().initSp(context);
        BmobManager.getInstance().initBmob(context);
        CloudManager.getInstance().initCloud(context);
        SqlHelper.getInstance().initSql(context,BmobManager.getInstance().getUser().getObjectId()+"newFriendSql");

    }

}
