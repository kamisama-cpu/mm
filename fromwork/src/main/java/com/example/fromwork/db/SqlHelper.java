package com.example.fromwork.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fromwork.entity.FriendInfo;
import com.example.fromwork.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Profile: 本地数据库帮助类
 */
public class SqlHelper {

    private static volatile SqlHelper instnce = null;
    /**
     * 用来存储好友请求信息
     */
    private List<FriendInfo> infoList = new ArrayList<>();

    private Sql sql;
    private SQLiteDatabase writableDatabase;

    private SqlHelper() {
    }

    public static SqlHelper getInstance() {
        if (instnce == null) {
            synchronized (SqlHelper.class) {
                if (instnce == null) {
                    instnce = new SqlHelper();
                }
            }
        }
        return instnce;
    }

    public void initSql(Context context,String name){
        sql = new Sql(context,name);
        writableDatabase = sql.getWritableDatabase();
    }

    /**
     * 存储信息
     * @param contentValues
     */
    public void insertUser(ContentValues contentValues){
        if (writableDatabase!=null){
            writableDatabase.insert("userinfo",null,contentValues);
            LogUtils.e("数据库存储数据成功");
        }
    }

    /**
     * 修改信息
     * @param contentValues
     */
    public void updateUser(ContentValues contentValues){
        if (writableDatabase!=null){
            writableDatabase.update("userinfo",contentValues,"isAgree=?",new String[]{"-1"});
            LogUtils.e("数据库存储数据成功");
        }
    }

    /**
     * 查找信息
     */
    public List<FriendInfo> queryUser(){
        if (writableDatabase!=null){
            Cursor userinfo = writableDatabase.query("userinfo", null, null, null, null, null, null);

            while (userinfo.moveToNext()){

                String msg = userinfo.getString(userinfo.getColumnIndex("msg"));
                String userId = userinfo.getString(userinfo.getColumnIndex("userId"));
                String saveTime = userinfo.getString(userinfo.getColumnIndex("saveTime"));
                String isAgree = userinfo.getString(userinfo.getColumnIndex("isAgree"));
                FriendInfo friendInfo = new FriendInfo(msg,userId,saveTime,isAgree);
                infoList.add(friendInfo);
                LogUtils.e("查询到的信息::msg::"+msg+"userId::"+userId+"saveTime::"+saveTime+"isAgree::"+isAgree);

            }
            return infoList;
        }
        return null;
    }

    /**
     * 查找信息
     */
    public boolean queryUserId(String ids){
        String id = null;
        if (writableDatabase!=null){
            Cursor userinfo = writableDatabase.query("userinfo", null, null, null, null, null, null);

            while (userinfo.moveToNext()){

                id = userinfo.getString(userinfo.getColumnIndex("userId"));
                LogUtils.e("查询到的信息userId::"+id);

            }

            if (id!=null){
                if (id.equals(ids)){
                    return true;
                }
            }
            return false;
        }
        return false;
    }


}
