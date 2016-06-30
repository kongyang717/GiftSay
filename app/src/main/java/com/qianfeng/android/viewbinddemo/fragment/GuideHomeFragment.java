package com.qianfeng.android.viewbinddemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qianfeng.android.viewbinddemo.R;
import com.qianfeng.android.viewbinddemo.utils.OkHttpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 指南页面主Fragment
 */
public class GuideHomeFragment extends Fragment {

    public static final int HEAD_VIEW = 0;
    public static final int ITEM_VIEW = 1;

    /**
     * fragment 主控件，包含了整个界面的内容（头部View和List列表）
     */
    @BindView(R.id.elv_guide_home)
    RecyclerView mRecyclerView;

    private List<String> mList = new ArrayList<>();
    private Context mContext;
    private MyAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getContext();
        //创建视图管理器
        LinearLayoutManager llManager = new LinearLayoutManager(mContext);
        //创建适配器
        mAdapter = new MyAdapter();
        //设置视图管理器
        mRecyclerView.setLayoutManager(llManager);
        //设置适配器
        mRecyclerView.setAdapter(mAdapter);
        //获取数据
        refreshData();
    }

    /**
     * 布局初始化完毕，开始刷新数据
     */
    private void refreshData() {
        //OkHttpUtil.newInstance().url();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        int viewType;
        @BindView(R.id.tv_guide_home_time)
        TextView mTextView;

        public MyViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType=viewType;
            if (viewType == ITEM_VIEW) {
                ButterKnife.bind(this, itemView);
            }
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater from = LayoutInflater.from(mContext);
            View view = null;
            //如果viewType是头部视图类型就返回头部视图
            if (viewType == HEAD_VIEW) {
                view = from.inflate(R.layout.head_guide, parent,false);
            } else if (viewType == ITEM_VIEW) {
                view = from.inflate(R.layout.item_guide,null);
            }
            return new MyViewHolder(view, viewType);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return HEAD_VIEW;
            } else {
                return ITEM_VIEW;
            }
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if(holder.viewType==ITEM_VIEW){
                holder.mTextView.setText("3");
            }

        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }
    }
}
