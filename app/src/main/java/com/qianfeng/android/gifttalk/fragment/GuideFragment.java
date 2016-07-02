package com.qianfeng.android.gifttalk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.qianfeng.android.gifttalk.bean.GuideTopTabBean;
import com.qianfeng.android.gifttalk.utils.OkHttpUtil;
import com.qianfeng.android.gifttalk.utils.URLConstant;
import com.qianfeng.android.gifttalk.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 指南Fragment
 * 包含一个FragmentPager
 */
public class GuideFragment extends Fragment implements OkHttpUtil.CallBack {

    @BindView(R.id.tl_guide_tab)
    TabLayout mTabLayout;
    @BindView(R.id.vp_guide_content)
    ViewPager mViewPager;
    private List<String> mListTitles = new ArrayList<>();

    private MyFragmentPagerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new MyFragmentPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        OkHttpUtil.newInstance().start(URLConstant.GUIDE_TOP_TAB_TITLES).callback(this);
    }

    /**
     * 解析json并更新tab
     *
     * @param result 获取到的String字符串
     */
    @Override
    public void callback(String result) {
        if(result == null){
            return;
        }
        GuideTopTabBean bean = new Gson().fromJson(result, GuideTopTabBean.class);
        for (GuideTopTabBean.DataBean.ChannelsBean c : bean.getData().getChannels()) {
            mListTitles.add(c.getName());
        }
        mAdapter.notifyDataSetChanged();
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new GuideHomeFragment();
            } else {
                return new GuideOtherFragment();
            }
        }

        @Override
        public int getCount() {
            return mListTitles == null ? null : mListTitles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mListTitles == null ? null : mListTitles.get(position);
        }
    }
}
