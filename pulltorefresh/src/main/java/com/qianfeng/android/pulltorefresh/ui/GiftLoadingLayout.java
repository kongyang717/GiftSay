package com.qianfeng.android.pulltorefresh.ui;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.test.mock.MockContext;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.qianfeng.android.pulltorefresh.LoadingLayout;
import com.qianfeng.android.pulltorefresh.R;

/**
 * @auther Kong Yang
 * @since 2016/7/7
 */
public class GiftLoadingLayout extends LoadingLayout implements Animation.AnimationListener {

    private View mContainer;
    private ImageView imageTop;
    private ImageView imageBox;
    private AnimationDrawable anim;
    private Animation animTop;
    private Context mContext;
    private AnimationEndListener mAnimationEndListener;

    public GiftLoadingLayout(Context context) {
        this(context, null);
    }

    public GiftLoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GiftLoadingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        imageTop = (ImageView) findViewById(R.id.iv_anim_update_heart);
        imageBox = (ImageView) findViewById(R.id.iv_anim_update_box);
        anim = (AnimationDrawable) imageBox.getDrawable();
        animTop = AnimationUtils.loadAnimation(mContext, R.anim.pull_update_loading_top);
        animTop.setAnimationListener(this);
    }

    @Override
    protected void onRefreshing() {
        super.onRefreshing();
        anim.start();
        imageTop.setVisibility(View.VISIBLE);
        imageTop.startAnimation(animTop);
    }

    @Override
    protected void onReset() {
        super.onReset();
        anim.stop();
        imageTop.setVisibility(View.INVISIBLE);
        imageTop.clearAnimation();
    }

    @Override
    public int getContentSize() {
        if (null != mContainer) {
            return mContainer.getHeight();
        }
        return (int) (getResources().getDisplayMetrics().density * 60);
    }

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        mContainer = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_gift, null);
        return mContainer;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (mAnimationEndListener != null) {
            mAnimationEndListener.onAnimationEnd(animation);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void setAnimationEndListener(AnimationEndListener animationEndListener) {
        this.mAnimationEndListener = animationEndListener;
    }

    interface AnimationEndListener {
        void onAnimationEnd(Animation animation);
    }
}
