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

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.Gson;
import com.qianfeng.android.gifttalk.R;
import com.qianfeng.android.gifttalk.bean.GuideHomeBanner;
import com.qianfeng.android.gifttalk.bean.GuideHomeContents;
import com.qianfeng.android.gifttalk.bean.GuideHomeHead;
import com.qianfeng.android.gifttalk.utils.Display;
import com.qianfeng.android.gifttalk.utils.OkHttpUtil;
import com.qianfeng.android.gifttalk.utils.URLConstant;
import com.qianfeng.android.pulltorefresh.PullToRefreshBase;
import com.qianfeng.android.pulltorefresh.ui.PullToRefreshRecycleView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 指南页面主Fragment
 */
public class GuideHomeFragment extends Fragment implements
        PullToRefreshBase.OnRefreshListener<RecyclerView> {

    public static final int HEAD_VIEW = 0;
    public static final int ITEM_VIEW = 1;

    public static final int HAS_TIME = 0;
    public static final int NO_TIME = 1;
    //判断是刷新还是加载更多
    private boolean isRefresh;
    private RecyclerView mRecyclerView;
    private RecyclerView mHeadRecyclerView;
    private View Headview;
    private ConvenientBanner mConvenientBanner;
    private PullToRefreshRecycleView mContentView;

    private List<GuideHomeContents.DataBean.ItemsBean> mListBody = new ArrayList<>();
    private List<GuideHomeHead.DataBean.SecondaryBannersBean> mListHeadContents = new ArrayList<>();
    private List<GuideHomeBanner.DataBean.BannersBean> mListHeadBanner = new ArrayList<>();
    private List<Integer> mListType = new ArrayList<>();

    private Context mContext;

    private MyAdapter mAdapter;
    private HeadAdapter mHeadAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        mContentView = new PullToRefreshRecycleView(mContext);
        mRecyclerView = mContentView.getRefreshableView();
        Headview = inflater.inflate(R.layout.head_guide, container,false);
        mHeadRecyclerView = (RecyclerView) Headview.findViewById(R.id.rv_guide_home);
        mConvenientBanner = (ConvenientBanner) Headview.findViewById(R.id.cb_guide_home_banner);
        return mContentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupBodyRecycerView();//设置主内容滑动视图
        setupHeadRecycerView();//设置头部横向滑动视图
        setupHeadConvenientBanner();//设置头部广告牌视图
        //刷新数据
        mContentView.setOnRefreshListener(this);
        if (mListHeadBanner.size() == 0) {
            doRefreshHeadBanner();
        }
        if (mListHeadContents.size() == 0) {
            doRefreshHeadLands();
        }
        if (mListBody.size() == 0) {
            doRefreshBody();
        }
    }

    private void setupHeadConvenientBanner() {
        mConvenientBanner.setPages(new CBViewHolderCreator<HeadPager>() {
            @Override
            public HeadPager createHolder() {
                return new HeadPager();
            }
        }, mListHeadBanner)
                .setPageIndicator(new int[]{R.drawable.btn_check_disabled_nightmode,
                        R.drawable.btn_check_normal});
    }

    private void setupHeadRecycerView() {
        LinearLayoutManager llManager2 = new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL, false);
        mHeadAdapter = new HeadAdapter();
        mHeadRecyclerView.setLayoutManager(llManager2);
        mHeadRecyclerView.setAdapter(mHeadAdapter);
    }

    private void setupBodyRecycerView() {
        //创建视图管理器
        LinearLayoutManager llManager = new LinearLayoutManager(mContext);
        //创建适配器
        mAdapter = new MyAdapter();
        //设置视图管理器
        mRecyclerView.setLayoutManager(llManager);
        //设置适配器
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mConvenientBanner.startTurning(2000);
    }

    @Override
    public void onPause() {
        super.onPause();
        mConvenientBanner.stopTurning();
    }

    /**
     * 去刷新头部广告视图
     */
    private void doRefreshHeadBanner() {
        OkHttpUtil.newInstance()
                .local(mContext)
                .start(URLConstant.GUIDE_HOME_HEAD_BANNER)
                .callback(new OkHttpUtil.CallBack() {
                    @Override
                    public void callback(String result) {
                        if (result != null) {
                            refreshBanner(result);
                        }
                    }
                });
    }

    /**
     * 去刷新头部横向list视图
     */
    private void doRefreshHeadLands() {
        OkHttpUtil.newInstance()
                .local(mContext)
                .start(URLConstant.GUIDE_HOME_HEAD_CONTENTS)
                .callback(new OkHttpUtil.CallBack() {
                    @Override
                    public void callback(String result) {
                        if (result != null) {
                            refreshHead(result);
                        }
                    }
                });
    }

    /**
     * 去刷新主要内容视图
     */
    private void doRefreshBody() {
        OkHttpUtil.newInstance()
                .local(mContext)
                .start(URLConstant.GUIDE_HOME_CONTENTS)
                .callback(new OkHttpUtil.CallBack() {
                    @Override
                    public void callback(String result) {
                        if (result != null) {
                            refreshBody(result);
                        }
                    }
                });
    }

    private void refreshBanner(String result) {
        GuideHomeBanner bean = new Gson().fromJson(result, GuideHomeBanner.class);
        if (isRefresh) {
            mListHeadBanner.clear();
        }
        mListHeadBanner.addAll(bean.getData().getBanners());
        mConvenientBanner.notifyDataSetChanged();
    }

    /**
     * 刷新头部横向滑动view
     */
    private void refreshHead(String result) {
        GuideHomeHead bean = new Gson().fromJson(result, GuideHomeHead.class);
        //刷新适配器
        if (isRefresh) {
            mListHeadContents.clear();
        }
        mListHeadContents.addAll(bean.getData().getSecondary_banners());
        mHeadAdapter.notifyDataSetChanged();
    }

    /**
     * 刷新主界面滑动view
     */
    public void refreshBody(String result) {
        GuideHomeContents bean = new Gson().fromJson(result, GuideHomeContents.class);
        List<GuideHomeContents.DataBean.ItemsBean> list = bean.getData().getItems();
        groupItem(list);
        //刷新适配器
        if (isRefresh) {
            mListBody.clear();
        }
        mListBody.addAll(list);
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
     * @return 格式化的时间字符串
     */
    private String getTimeString(long l) {
        SimpleDateFormat sdf = new SimpleDateFormat("M月d日 E", Locale.getDefault());
        Date date = new Date(l * 1000);
        return sdf.format(date);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        isRefresh = true;
        doRefreshBody();
        doRefreshHeadLands();
        doRefreshHeadBanner();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        isRefresh = false;
    }

    class HeadPager implements Holder<GuideHomeBanner.DataBean.BannersBean> {

        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, GuideHomeBanner.DataBean.BannersBean data) {
            Picasso.with(context)
                    .load(data.getImage_url()).into(imageView);
        }
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
        @BindView(R.id.tv_guide_next_update_time)
        TextView mTvNextTime;

        public BodyViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == ITEM_VIEW) {
                ButterKnife.bind(this, itemView);
            }
        }
    }

    class MyAdapter extends RecyclerView.Adapter<BodyViewHolder> {

        int[] time = new int[]{8, 11, 17, 20};

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
            GuideHomeContents.DataBean.ItemsBean info = mListBody.get(position);
            //如果类型为有时间就显示时间布局
            if (position > 0) {
                if (mListType.get(position - 1) == HAS_TIME) {
                    if (position == 1) {
                        holder.mTvNextTime.setVisibility(View.VISIBLE);
                        String string = "下次更新 " + getNextTime();
                        holder.mTvNextTime.setText(string);
                    }
                    holder.mLlTime.setVisibility(View.VISIBLE);
                    long l = mListBody.get(position - 1).getCreated_at();
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

        /**
         * 获取下一次更新数据时间
         *
         * @return 下次更新数据时间
         */
        private String getNextTime() {
            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int curTime = calendar.get(Calendar.HOUR_OF_DAY);
            int index = 0;
            int length = time.length;
            for (int i = length - 1; i >= 0; i--) {
                if (curTime >= time[i]) {
                    index = ++i;
                    break;
                }
            }
            if (index >= length) {
                index -= length;
            }
            return time[index] + ":00";
        }

        @Override
        public int getItemCount() {
            return mListBody == null ? 0 : mListBody.size();
        }
    }
}
