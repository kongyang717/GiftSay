package com.qianfeng.android.pulltorefresh.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.animation.Animation;


import com.qianfeng.android.pulltorefresh.HeaderLoadingLayout;
import com.qianfeng.android.pulltorefresh.LoadingLayout;
import com.qianfeng.android.pulltorefresh.PullToRefreshBase;

/**
 * @auther Kong Yang
 * @since 2016/7/7
 */
public class PullToRefreshRecycleView extends PullToRefreshBase<RecyclerView> {
    private RecyclerView mRecyclerView;
    private boolean misButtom;

    public PullToRefreshRecycleView(Context context) {
        this(context, null);
    }

    public PullToRefreshRecycleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        mRecyclerView = new RecyclerView(context);
        return mRecyclerView;
    }

    @Override
    protected boolean isReadyForPullDown() {
        return isFirstItemVisible();
    }

    /**
     * 判断第一个item是否完全显示出来
     *
     * @return
     */
    private boolean isFirstItemVisible() {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter == null) {
            return true;
        }
        int mostTop = mRecyclerView.getChildCount() > 0 ?
                mRecyclerView.getChildAt(0).getTop() : 0;
        if (mostTop >= 0) {
            return true;
        }
        return false;
    }


    @Override
    protected boolean isReadyForPullUp() {
        return isLastItemVisible();
    }

    /**
     * 判断最后一个item是否完全显示出来
     *
     * @return
     */
    private boolean isLastItemVisible() {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter == null) {
            return true;
        }
        return false;
    }

    @Override
    protected LoadingLayout createHeaderLoadingLayout(Context context, AttributeSet attrs) {
        GiftLoadingLayout giftLoadingLayout= new GiftLoadingLayout(context);
        giftLoadingLayout.setAnimationEndListener(new GiftLoadingLayout.AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                onPullDownRefreshComplete();
            }
        });
        return giftLoadingLayout;
    }
}
