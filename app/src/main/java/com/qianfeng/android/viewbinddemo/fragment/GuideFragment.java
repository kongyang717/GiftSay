package com.qianfeng.android.viewbinddemo.fragment;

import android.content.Context;
import android.net.Uri;
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

import com.qianfeng.android.viewbinddemo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 指南Fragment
 * 包含一个FragmentPager
 */
public class GuideFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.tl_guide_tab)
    TabLayout mTabLayout;
    @BindView(R.id.vp_guide_content)
    ViewPager mViewPager;
    private List<String> mListTitles = new ArrayList<>();
    private List<Fragment> mListContents = new ArrayList<>();

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private MyFragmentPagerAdapter mAdapter;

    public GuideFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     */
    // TODO: Rename and change types and number of parameters
    public static GuideFragment newInstance(String param1, String param2) {
        GuideFragment fragment = new GuideFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        initData();//初始化数据源
        mAdapter = new MyFragmentPagerAdapter(getChildFragmentManager());
    }

    private void initData() {
        initTitles();
        initFragments();
    }

    private void initFragments() {
        mListContents.add(GuideHomeFragment.newInstance("", ""));
        mListContents.add(GuideHomeFragment.newInstance("", ""));
        mListContents.add(GuideHomeFragment.newInstance("", ""));
        mListContents.add(GuideHomeFragment.newInstance("", ""));
        mListContents.add(GuideHomeFragment.newInstance("", ""));
        mListContents.add(GuideHomeFragment.newInstance("", ""));
        mListContents.add(GuideHomeFragment.newInstance("", ""));
        mListContents.add(GuideHomeFragment.newInstance("", ""));
        mListContents.add(GuideHomeFragment.newInstance("", ""));
        mListContents.add(GuideHomeFragment.newInstance("", ""));
        mListContents.add(GuideHomeFragment.newInstance("", ""));
        mListContents.add(GuideHomeFragment.newInstance("", ""));
        mListContents.add(GuideHomeFragment.newInstance("", ""));
        mListContents.add(GuideHomeFragment.newInstance("", ""));
        mListContents.add(GuideHomeFragment.newInstance("", ""));
    }

    private void initTitles() {
        mListTitles.add("精选");
        mListTitles.add("数码");
        mListTitles.add("礼物");
        mListTitles.add("美食");
        mListTitles.add("送基友");
        mListTitles.add("送女票");
        mListTitles.add("送闺蜜");
        mListTitles.add("送爸妈");
        mListTitles.add("送宝贝");
        mListTitles.add("奇葩搞怪");
        mListTitles.add("创意生活");
        mListTitles.add("文艺风");
        mListTitles.add("萌萌哒");
        mListTitles.add("设计感");
        mListTitles.add("科技范");
    }

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
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mListContents == null ? null : mListContents.get(position);
        }

        @Override
        public int getCount() {
            return mListContents == null ? null : mListContents.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mListTitles == null ? null :mListTitles.get(position);
        }
    }
}
