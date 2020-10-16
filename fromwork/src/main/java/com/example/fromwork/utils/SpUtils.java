package com.example.fromwork.utils;


import android.content.Context;
import android.content.SharedPreferences;


/*
    sp存储
 */
public class SpUtils {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private static SpUtils instance = null;
    /*
        饿汉单例
     */
    public  static SpUtils getInstance(){
        if (instance == null){
            synchronized(SpUtils.class){
                instance = new SpUtils();
            }
        }
        return instance;
    }

    public void initSp(Context mContext){
        sp = mContext.getSharedPreferences("mm",Context.MODE_PRIVATE);
        editor = sp.edit();
    }


    public void putString(String key,String values){
        editor.putString(key,values);
        editor.commit();
    }

    public String getString(String key,String defaults){
        return sp.getString(key,defaults);
    }


    public void putBoolean(String key,Boolean values){
        editor.putBoolean(key,values);
        editor.commit();
    }

    public Boolean getBoolean(String key,Boolean defaults){
        return sp.getBoolean(key,defaults);
    }

    public void putFloat(String key,Float values){
        editor.putFloat(key,values);
        editor.commit();
    }

    public Float getFloat(String key,Float defaults){
        return sp.getFloat(key,defaults);
    }

    public void putLong(String key,Long values){
        editor.putLong(key,values);
        editor.commit();
    }

    public Long getLong(String key,Long defaults){
        return sp.getLong(key,defaults);
    }

    public void putInt(String key,int values){
        editor.putInt(key,values);
        editor.commit();
    }

    public int getInt(String key,int defaults){
        return sp.getInt(key,defaults);
    }

}
