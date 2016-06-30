package com.qianfeng.android.viewbinddemo.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.system.ErrnoException;
import android.util.Log;
import android.widget.RadioGroup;

import com.qianfeng.android.viewbinddemo.R;
import com.qianfeng.android.viewbinddemo.fragment.GuideFragment;
import com.qianfeng.android.viewbinddemo.utils.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private Fragment[] fragments = new Fragment[4];

    @BindView(R.id.rg_home_tab)
    RadioGroup rgHomeTab;
    private Fragment currentFragment;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        rgHomeTab.check(R.id.rb_01);
        rgHomeTab.setOnCheckedChangeListener(this);
        switchFragment(0);
    }

    private void initData() {
        fragments[0] = new GuideFragment();
        fragments[1] = new GuideFragment();
        fragments[2] = new GuideFragment();
        fragments[3] = new GuideFragment();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_01:
                switchFragment(0);
                break;
            case R.id.rb_02:
                switchFragment(1);
                break;
            case R.id.rb_03:
                switchFragment(2);
                break;
            case R.id.rb_04:
                switchFragment(3);
                break;
        }
    }

    protected void switchFragment(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment targetFragment = fragments[index];
        if (!targetFragment.isAdded()) {
            transaction.add(R.id.fl_home_content, targetFragment);
        }
        if (currentFragment == null) {
            transaction.show(targetFragment);
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        transaction.commit();
        currentFragment = targetFragment;
    }

}
