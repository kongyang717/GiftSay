package com.qianfeng.android.gifttalk.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @auther Kong Yang
 * @since 2016/7/5
 */
public class Display {

    private static DisplayMetrics displayMetrics;

    /**
     * 初始化屏幕大小数据
     * @param activity
     */
    public static void init(Activity activity) {
        if (displayMetrics == null) {
            displayMetrics = new DisplayMetrics();
            WindowManager service = activity.getWindowManager();
            service.getDefaultDisplay().getMetrics(displayMetrics);
        }
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    public static int getWidth() {
        return displayMetrics == null ? 0 : displayMetrics.widthPixels;
    }

    /**
     *
     * @param pix
     * @return
     */
    public static int getDpForPix(int pix) {
        return displayMetrics == null ? 0 : (int)(displayMetrics.density * pix);
    }
}
