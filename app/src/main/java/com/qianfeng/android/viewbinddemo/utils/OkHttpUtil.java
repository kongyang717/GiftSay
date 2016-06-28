package com.qianfeng.android.viewbinddemo.utils;

import android.os.AsyncTask;

import okhttp3.OkHttpClient;
/**
 * @auther Kong Yang
 * @since 2016/6/28
 */
public class OkHttpUtil {
    private static OkHttpClient okHttpClient;

    static {
        if (okHttpClient == null) {


            okHttpClient = new OkHttpClient();
        }
    }
    public static OkHttpTAsk newInstance(){
        return new OkHttpTAsk();
    }
    static class OkHttpTAsk extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... params) {
            return null;
        }

    }
}
