package com.qianfeng.android.gifttalk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qianfeng.android.gifttalk.R;
import com.qianfeng.android.gifttalk.bean.GuideHomeContents;
import com.qianfeng.android.gifttalk.bean.GuideHomeHead;
import com.qianfeng.android.gifttalk.utils.Display;
import com.qianfeng.android.gifttalk.utils.OkHttpUtil;
import com.qianfeng.android.gifttalk.utils.URLConstant;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 指南页面主Fragment
 */
public class GuideHomeFragment extends Fragment implements OkHttpUtil.CallBack {

    public static final int HEAD_VIEW = 0;
    public static final int ITEM_VIEW = 1;

    public static final int HAS_TIME = 0;
    public static final int NO_TIME = 1;

    /**
     * fragment 主控件，包含了整个界面的内容（头部View和List列表）
     */
    @BindView(R.id.elv_guide_home)
    RecyclerView mRecyclerView;
    RecyclerView mHeadRecyclerView;

    private List<GuideHomeContents.DataBean.ItemsBean> mListContents = new ArrayList<>();
    private List<GuideHomeHead.DataBean.SecondaryBannersBean> mListHeadContents = new ArrayList<>();
    private List<Integer> mListType = new ArrayList<>();
    private Context mContext;
    private MyAdapter mAdapter;
    private HeadAdapter mHeadAdapter;
    private View Headview;

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
        LinearLayoutManager llManager2 = new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL, false);
        //创建适配器
        mAdapter = new MyAdapter();
        mHeadAdapter = new HeadAdapter();
        //设置视图管理器
        mRecyclerView.setLayoutManager(llManager);
        Headview = LayoutInflater.from(mContext).inflate(R.layout.head_guide, null);
        mHeadRecyclerView = (RecyclerView) Headview.findViewById(R.id.rv_guide_home);
        mHeadRecyclerView.setLayoutManager(llManager2);
        //设置适配器
        mRecyclerView.setAdapter(mAdapter);
        mHeadRecyclerView.setAdapter(mHeadAdapter);
        //获取数据
        refreshData();
    }

    /**
     * 布局初始化完毕，开始刷新数据
     */
    private void refreshData() {
        OkHttpUtil.newInstance().start(URLConstant.GUIDE_HOME_CONTENTS).callback(this);
        OkHttpUtil.newInstance().start(URLConstant.GUIDE_HOME_HEAD_CONTENTS).callback(new OkHttpUtil.CallBack() {
            @Override
            public void callback(String result) {
                if (result == null) {
                    return;
                }
                refreshHead(result);
            }
        });
    }

    /**
     * 刷新头部横向滑动view
     *
     * @param result
     */
    private void refreshHead(String result) {
        GuideHomeHead bean = new Gson().fromJson(result, GuideHomeHead.class);
        List<GuideHomeHead.DataBean.SecondaryBannersBean> list = bean.getData().getSecondary_banners();
        //刷新适配器
        mListHeadContents.addAll(list);
        mHeadAdapter.notifyDataSetChanged();
    }

    @Override
    public void callback(String result) {
        GuideHomeContents bean = new Gson().fromJson(result, GuideHomeContents.class);
        List<GuideHomeContents.DataBean.ItemsBean> list = bean.getData().getItems();
        groupItem(list);
        //刷新适配器
        mListContents.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 根据时间为每个item数据分组
     *
     * @param list 数据列表
     */
    private void groupItem(List<GuideHomeContents.DataBean.ItemsBean> list) {
        String currentTimeStr = "";
        mListType.clear();
        for (GuideHomeContents.DataBean.ItemsBean b : list) {
            String time = getTimeString(b.getCreated_at());
            if (time.equals(currentTimeStr)) {
                mListType.add(NO_TIME);
            } else {
                mListType.add(HAS_TIME);
            }
            currentTimeStr = time;
        }

    }

    /**
     * 获取时间
     *
     * @param l long类型时间参数（单位：毫秒）
     * @return
     */
    private String getTimeString(long l) {
        SimpleDateFormat sdf = new SimpleDateFormat("M月d日 E");
        Date date = new Date(l * 1000);
        return sdf.format(date);
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        public HeadViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView;
        }
    }

    class HeadAdapter extends RecyclerView.Adapter<HeadViewHolder> {

        @Override
        public HeadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ImageView imageView = new ImageView(mContext);
            int size = Display.getWidth() / 5;
            RecyclerView.LayoutParams params =
                    new RecyclerView.LayoutParams(size, size);
            params.rightMargin = Display.getDpForPix(8);
            imageView.setLayoutParams(params);
            return new HeadViewHolder(imageView);
        }

        @Override
        public void onBindViewHolder(HeadViewHolder holder, int position) {
            Picasso.with(mContext)
                    .load(mListHeadContents.get(position).getImage_url())
                    .into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mListHeadContents == null ? 0 : mListHeadContents.size();
        }
    }

    class BodyViewHolder extends RecyclerView.ViewHolder {

        int viewType;
        @BindView(R.id.iv_guide_item_cover)
        ImageView mImageView;
        @BindView(R.id.ll_guide_item_time)
        LinearLayout mLlTime;
        @BindView(R.id.tv_guide_update_time)
        TextView mTvTime;

        public BodyViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == ITEM_VIEW) {
                ButterKnife.bind(this, itemView);
            }
        }
    }

    class MyAdapter extends RecyclerView.Adapter<BodyViewHolder> {

        /**
         * 此方法只会在第一次加载item的时候调用，后面会复用该Holder
         *
         * @param parent   父视图
         * @param viewType view类型，RecyclerView会通过此类型判断新出现的item是否重用此view
         * @return viewholder
         */
        @Override
        public BodyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater from = LayoutInflater.from(mContext);
            View view = null;
            //如果viewType是头部视图类型就返回头部视图
            if (viewType == HEAD_VIEW) {
                view = Headview;
            } else if (viewType == ITEM_VIEW) {
                view = from.inflate(R.layout.item_guide, parent, false);
            }
            return new BodyViewHolder(view, viewType);
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
        public void onBindViewHolder(BodyViewHolder holder, int position) {
            GuideHomeContents.DataBean.ItemsBean info = mListContents.get(position);
            //如果类型为有时间就显示时间布局
            if (position > 0) {
                if (mListType.get(position - 1) == HAS_TIME) {
                    holder.mLlTime.setVisibility(View.VISIBLE);
                    long l = mListContents.get(position - 1).getCreated_at();
                    holder.mTvTime.setText(getTimeString(l));
                } else {
                    holder.mLlTime.setVisibility(View.GONE);
                }
            }
            if (holder.viewType == ITEM_VIEW) {
                Picasso.with(mContext)
                        .load(info.getCover_image_url())
                        .into(holder.mImageView);
            }
        }

        @Override
        public int getItemCount() {
            return mListContents == null ? 0 : mListContents.size();
        }
    }
}
