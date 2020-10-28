package com.example.fromwork.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Sql extends SQLiteOpenHelper {
    public Sql(Context context,String name) {
        super(context, name, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /**
         * msg 留言
         * userID 发送好友请求人的id
         * saveTime 时间
         * isAgree 是否同意
         */
        sqLiteDatabase.execSQL("create table userinfo(msg VARCHER(100),userId VARCHAR(100),saveTime VARCHAR(100),isAgree VARCHAR(100))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
