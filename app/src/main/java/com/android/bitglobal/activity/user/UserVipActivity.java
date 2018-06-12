package com.android.bitglobal.activity.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.activity.BaseActivity;
import com.android.bitglobal.adapters.market.MarketDetailTabViewPageAdapter;
import com.android.bitglobal.fragment.user.AccountLevelDetailsFragment;
import com.android.bitglobal.fragment.user.AccountLevelInfoFragment;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/3/8.
 */

public class UserVipActivity extends BaseActivity {

    @BindView(R.id.tv_head_title)
    TextView headTitleText;
    @BindView(R.id.btn_head_back)
    ImageView headBackImage;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager userVipViewPager;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_vip);
        unbinder = ButterKnife.bind(this);
        initData();
        init();
        setOnListener();
    }

    private void initData() {
        mFragments.add(AccountLevelInfoFragment.newInstance("AccountLevelInfoFragment"));
        mFragments.add(AccountLevelDetailsFragment.newInstance("AccountLevelDetailsFragment"));
        mTitles.add(getString(R.string.user_account_level_info));
        mTitles.add(getString(R.string.user_account_level_details));
    }

    private void init() {
        headTitleText.setText(getString(R.string.user_account_level));
        headBackImage.setVisibility(View.VISIBLE);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.user_vip_sliding_layout);
        userVipViewPager = (ViewPager) findViewById(R.id.user_vip_view_page);
        userVipViewPager.setAdapter(
                new MarketDetailTabViewPageAdapter(getSupportFragmentManager(),
                        mFragments, mTitles));
        slidingTabLayout.setViewPager(userVipViewPager);
        slidingTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if(position == 1) {
                    ((AccountLevelDetailsFragment)(mFragments.get(1))).initVipLevelDetailList();
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private void setOnListener() {
        headBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
