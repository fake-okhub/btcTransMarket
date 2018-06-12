package com.android.bitglobal.activity.asset;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.activity.BaseActivity;
import com.android.bitglobal.adapters.market.MarketDetailTabViewPageAdapter;
import com.android.bitglobal.fragment.asset.RecordsFragment;
import com.android.bitglobal.fragment.asset.WithdrawFragment;
import com.android.bitglobal.fragment.asset.DepositFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/3/4.
 */

public class BalanceTapActivity extends BaseActivity {

    @BindView(R.id.tv_head_title)
    TextView headTitleText;
    @BindView(R.id.btn_head_back)
    ImageView headBackImage;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager balanceViewPager;
    private DepositFragment depositFragment;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();

    private int tabIndex = 0;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balances);
        unbinder = ButterKnife.bind(this);
        initData();
        init();
        setOnListener();
    }

    private void initData() {
        tabIndex = getIntent().getExtras().getInt("tabIndex");
        mFragments.add(RecordsFragment.newInstance("RecordsFragment"));
        depositFragment = DepositFragment.newInstance("DepositFragment");
        mFragments.add(depositFragment);
        mFragments.add(WithdrawFragment.newInstance("WithdrawFragment"));
        mTitles.add(getString(R.string.account_records));
        mTitles.add(getString(R.string.account_deposit));
        mTitles.add(getString(R.string.account_withdraw));
    }

    private void init() {
        headTitleText.setText(getIntent().getExtras().getString("currencyType") +  " " + getString(R.string.account_balance));
        headBackImage.setVisibility(View.VISIBLE);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.balance_sliding_layout);
        balanceViewPager = (ViewPager) findViewById(R.id.balance_view_page);
        balanceViewPager.setAdapter(
                new MarketDetailTabViewPageAdapter(getSupportFragmentManager(),
                        mFragments, mTitles));
        slidingTabLayout.setViewPager(balanceViewPager);
        //初始充值，提现获取地址
        if(tabIndex == 1 || tabIndex == 2) {
            depositFragment.setIsGetRechargeAddress(true);
        }
        balanceViewPager.setCurrentItem(tabIndex);
        balanceViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //当点击充值，提现获取地址
                if(position == 1 || position == 2) {
                    depositFragment.setIsGetRechargeAddress(true);
                    depositFragment.getRechargeAddress();
                } else {
                    depositFragment.setIsGetRechargeAddress(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFragments.get(2).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
