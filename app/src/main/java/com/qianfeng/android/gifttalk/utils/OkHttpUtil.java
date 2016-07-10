package com.qianfeng.android.gifttalk.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
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

    public static OkHttpTask newInstance() {
        return new OkHttpTask();
    }

    /**
     * 数据获取完毕的回调接口
     */
    public interface CallBack {
        /**
         * 回调方法
         *
         * @param result 获取到的String字符串
         */
        void callback(final String result);
    }

    public static final class OkHttpTask extends AsyncTask<String, Integer, String> {

        /**
         * url地址正则表达式
         */
        public static final String URL_REGEX = "((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\." +
                "[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))" +
                "(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
        /**
         * 上下文
         */
        private Context context;
        /**
         * post请求提交的数据
         */
        private FormBody formBody;

        /**
         * 回调接口
         */
        private CallBack callback;

        //构造器
        public OkHttpTask() {
        }

        /**
         * 启动子线程获取网络数据
         *
         * @param url url地址
         * @return this
         */
        public OkHttpTask start(String url) {
            if (url == null) {
                throw new NullPointerException("url is empty!");
            }
            if (!url.matches(URL_REGEX)) {
                throw new IllegalArgumentException("url is error!");
            }
            this.execute(url);
            return this;
        }

        public OkHttpTask callback(CallBack callback) {
            this.callback = callback;
            return this;
        }

        /**
         * 通过post方式请求网络数据
         *
         * @return this
         */
        public OkHttpTask post(Map<String, String> params) {
            Set<String> set = params.keySet();
            FormBody.Builder builder = new FormBody.Builder();
            for (String s : set) {
                String value = params.get(s);
                builder.add(s, value);
            }
            formBody = builder.build();
            return this;
        }

        /**
         * 数据本地化
         *
         * @param context 上下文
         * @return this
         */
        public OkHttpTask local(Context context) {
            this.context = context;
            return this;
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            Request.Builder builder = new Request.Builder().url(url);
            if (formBody != null) {
                builder.post(formBody);
            }
            Request request = builder.build();
            String result = null;
            try {
                Response response = okHttpClient.newCall(request).execute();
                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (context != null) {
                try {
                    if (result == null) {
                        result = FileUtil.readByUrl(context, url);
                    } else {
                        FileUtil.saveByUrl(context, url, result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (callback != null) {
                callback.callback(s);
            }
        }
    }
}
