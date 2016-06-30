package com.qianfeng.android.viewbinddemo.utils;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

    public static OkHttpTAsk newInstance() {
        return new OkHttpTAsk();
    }

    public static class OkHttpTAsk extends AsyncTask<String, Integer, String> {

        private String url;

        public void url(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(String... params) {
            if(url ==null){
                throw new NullPointerException("url is empty");
            }
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    response.body().string();
                }
            });
            return null;
        }

    }
}
