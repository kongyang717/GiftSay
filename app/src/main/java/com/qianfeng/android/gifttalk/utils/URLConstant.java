package com.qianfeng.android.gifttalk.utils;

/**
 * 封装了url接口常量
 *
 * @auther Kong Yang
 * @since 2016/6/30
 */
public class URLConstant {
    /**
     * 指南界面顶部tab标题列表
     */
    public static final String GUIDE_TOP_TAB_TITLES = "http://api.liwushuo.com/v2/channels/preset?gender=1&generation=3";
    /**
     * 指南界面头部下面的横向列表
     */
    public static final String GUIDE_HOME_HEAD_CONTENTS = "http://api.liwushuo.com/v2/secondary_banners?gender=1&generation=3";
    public static final String b = "http://api.liwushuo.com/v2/banners";
    /**
     * 指南界面内容列表
     */
    public static final String GUIDE_HOME_CONTENTS = "http://api.liwushuo.com/v2/channels/102/items_v2?limit=20&ad=2&gender=1&offset=0&generation=3";
}
