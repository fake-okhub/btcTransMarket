package com.android.bitglobal.fragment.trans;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by joyson on 2017/9/20.
 */

public class ExchangeNewFragment extends BaseFragment {
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.view_line_usd)
    View viewLineUsd;
    @BindView(R.id.rl_select_title_usd)
    RelativeLayout rlSelectTitleUsd;
    @BindView(R.id.view_line_btc)
    View viewLineBtc;
    @BindView(R.id.rl_select_title_btc)
    RelativeLayout rlSelectTitleBtc;
    @BindView(R.id.viewpager_new_exchange)
    ViewPager viewpagerNewExchange;
    private Unbinder unbinder;
    ExchangeNewSubFragment exchangeNewUsdFragment;
    ExchangeNewSubFragment exchangeNewBtcFragment;
    List<Fragment> listFragment = new ArrayList<>();
    private boolean hidden;
    int currentPage;
    private Handler mHandler=new Handler();
    private int refreshNetTime=3000;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_exchange, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvHeadTitle.setText(getString(R.string.trans_exchange_title));
        initViewpager();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            startNetWork();
            //为了 在登出之后 刷新子fragment中的recyclerview中的数据，
            // 子fragment无自己主动刷新方法，需要此处主动调用
            if (exchangeNewUsdFragment!=null){
                exchangeNewUsdFragment.onResume();
            }
            if (exchangeNewBtcFragment!=null){
                exchangeNewBtcFragment.onResume();
            }
        } else {
            endNetWork();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            startNetWork();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        endNetWork();
    }

    private void initViewpager() {
        exchangeNewUsdFragment=new ExchangeNewSubFragment();
        exchangeNewUsdFragment.setCurrentType(SystemConfig.USDC);
        exchangeNewBtcFragment=new ExchangeNewSubFragment();
        exchangeNewBtcFragment.setCurrentType(SystemConfig.BTC);
        listFragment.add(exchangeNewUsdFragment);
        listFragment.add(exchangeNewBtcFragment);
        viewpagerNewExchange.setAdapter(new FragmentStatePagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return listFragment.get(i);
            }

            @Override
            public int getCount() {
                return listFragment.size();
            }
        });
//        viewpagerNewExchange.setAdapter(adapter);
        viewpagerNewExchange.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage=position;
                switch (position){
                    case 0:
                        setUsdIsVisible(true);
                        break;
                    case 1:
                        setUsdIsVisible(false);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void startNetWork(){
        if (getUserVisibleHint() && isVisible()){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    exchangeNewUsdFragment.initNetData();
                    exchangeNewBtcFragment.initNetData();

                    startNetWork();
                }
            },refreshNetTime);
        }
    }

    public void endNetWork(){
        if (null!=mHandler){
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 设置 标签下方横线 显示
     * @param isVisible
     */
    private void setUsdIsVisible(boolean isVisible){
        if (isVisible){
            viewLineUsd.setVisibility(View.VISIBLE);
            viewLineBtc.setVisibility(View.INVISIBLE);
            viewpagerNewExchange.setCurrentItem(0);
        }else {
            viewLineUsd.setVisibility(View.INVISIBLE);
            viewLineBtc.setVisibility(View.VISIBLE);
            viewpagerNewExchange.setCurrentItem(1);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rl_select_title_usd, R.id.rl_select_title_btc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_select_title_usd:
                setUsdIsVisible(true);
                break;
            case R.id.rl_select_title_btc:
                setUsdIsVisible(false);
                break;
        }
    }
}
