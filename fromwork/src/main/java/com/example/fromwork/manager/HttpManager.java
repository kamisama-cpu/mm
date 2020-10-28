package com.example.fromwork.manager;

import android.app.Application;

import com.example.fromwork.entity.Constants;
import com.example.fromwork.sha1.SHA1;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpManager {
    private static volatile HttpManager mInstnce = null;
    private OkHttpClient mOkHttpClient;

    private HttpManager() {
        mOkHttpClient = new OkHttpClient();
    }

    public static HttpManager getInstance() {
        if (mInstnce == null) {
            synchronized (HttpManager.class) {
                if (mInstnce == null) {
                    mInstnce = new HttpManager();
                }
            }
        }
        return mInstnce;
    }

    /**
     * 请求融云token
     * @param map
     * @return
     */
    public String postCloudToken(HashMap<String, String> map) {

        String time = String.valueOf(System.currentTimeMillis() / 1000);
        String nonce = String.valueOf(Math.floor(Math.random() * 100000));

        String Signature = SHA1.sha1(Constants.APP_SECRET+nonce+time);

        FormBody.Builder builder = new FormBody.Builder();

        for (String key:map.keySet()){
            builder.add(key,map.get(key));
        }

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(Constants.TOKEN_URL)
                .header("Timestamp",time)
                .header("App-Key",Constants.APP_KEY)
                .header("Nonce",nonce)
                .header("Signature",Signature)
                .header("Content-Type","application/x-www-form-urlencoded")
                .post(requestBody)
                .build();
        try {
            return mOkHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
