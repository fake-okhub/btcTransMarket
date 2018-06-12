package com.android.bitglobal.activity.market;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.activity.BaseActivity;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.activity.trans.TransActivity;
import com.android.bitglobal.adapters.TabViewPageAdapter;
import com.android.bitglobal.entity.MarketDatasResult;
import com.android.bitglobal.fragment.TransFragment;
import com.android.bitglobal.fragment.market.AnalysisFragment;
import com.android.bitglobal.fragment.market.ChartFragment;
import com.android.bitglobal.fragment.market.OthersFragment;
import com.android.bitglobal.fragment.trans.TransBuyOrSellFragment;
import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.tool.Utils;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.view.NoScrollViewpager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 某币种行情详情
 * Created by Elbert on 17/3/15.
 */
public class MarketDetailActivity extends BaseActivity implements View.OnClickListener {

    private String currencyType = "ETH", exchangeType = "BTC";
    private String iconUrl;
    private String currencyPrice;
    private String fiatPrice;
    ArrayList<Fragment> listFragment = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();

    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.currencyPriceText)
    TextView currencyPriceText;
    @BindView(R.id.fiatPriceText)
    TextView fiatPriceText;
    @BindView(R.id.chartTitleText)
    TextView chartTitleText;
    @BindView(R.id.analysisTitleText)
    TextView analysisTitleText;
    @BindView(R.id.othersTitleText)
    TextView othersTitleText;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;
    @BindView(R.id.coinLogoImage)
    ImageView coinLogoImage;

    @BindView(R.id.viewPager)
    NoScrollViewpager viewPager;

    @BindView(R.id.btn_head_front)
    ImageView btn_head_front;

    private FragmentManager fragmentManager;
    private Unbinder unbinder;
    private ChartFragment mChartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  tintManager.setStatusBarTintResource(R.color.main_login_header_bg);//通知栏所需颜色
        setContentView(R.layout.activity_market_detail);
        fragmentManager = getSupportFragmentManager();
        Intent intent = getIntent();
        currencyType = intent.getStringExtra("currencyType");
        exchangeType = intent.getStringExtra("exchangeType");
        iconUrl = intent.getStringExtra("iconUrl");
        unbinder = ButterKnife.bind(this);
        initView();
        loadData();
    }

    private void initView() {
        tv_head_title.setText(currencyType.toUpperCase() + "/" + exchangeType.toUpperCase());
        Picasso.with(this).load(iconUrl).into(coinLogoImage);
        btn_head_front.setVisibility(View.GONE);
        btn_head_front.setOnClickListener(this);
        btn_head_back.setOnClickListener(this);
        chartTitleText.setOnClickListener(this);
        analysisTitleText.setOnClickListener(this);
        othersTitleText.setOnClickListener(this);

        mChartFragment = new ChartFragment();
        mChartFragment.setCurrencyType(currencyType);
        mChartFragment.setExchangeType(exchangeType);
        listFragment.add(mChartFragment);
//        ChartOldFragment mChartFragment = new ChartOldFragment();
//        mChartFragment.setCurrencyType(currencyType);
//        mChartFragment.setExchangeType(exchangeType);
//        listFragment.add(mChartFragment);

        final AnalysisFragment analysisFragment = new AnalysisFragment();
        analysisFragment.setCurrencyType(currencyType);
        analysisFragment.setExchangeType(exchangeType);
        listFragment.add(analysisFragment);
        final OthersFragment othersFragment = new OthersFragment();
        othersFragment.setCurrencyType(currencyType);
        othersFragment.setExchangeType(exchangeType);
        listFragment.add(othersFragment);
//        TradeFragment mTradeFragment = new TradeFragment();
//        mTradeFragment.setCurrencyType(currencyType);
//        mTradeFragment.setExchangeType(exchangeType);
//        listFragment.add(mTradeFragment);
        mTitles.add(getResources().getString(R.string.market_detail_tab_chart_title));
        mTitles.add(getResources().getString(R.string.market_detail_tab_analysis_title));
//        mTitles.add(getResources().getString(R.string.market_detail_tab_trade_title));
        mTitles.add(getResources().getString(R.string.market_detail_tab_others_title));
        //屏蔽viewpager滑动，使得chartFragment中k线图，可滑动
        viewPager.setPagerCanScroll(false);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(
                new TabViewPageAdapter(fragmentManager,
                        listFragment, mTitles));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0) {
                    setTitleSelectBackground(chartTitleText);
                } else if(position == 1) {
                    mChartFragment.dismissPopupWindow();
                    setTitleSelectBackground(analysisTitleText);
                } else {
                    mChartFragment.dismissPopupWindow();
                    setTitleSelectBackground(othersTitleText);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 初始化Tab页
//        SlidingTabLayout slidingTabLayout = (SlidingTabLayout)
//                findViewById(R.id.activity_market_detail_tab);
//        slidingTabLayout.setViewPager(viewPager);
    }

    public void initHead(List<MarketDatasResult> marketDatas) {
        currencyPrice = marketDatas.get(0).getTicker().getLast();
        fiatPrice = marketDatas.get(0).getTicker().getLegal_tender();
        currencyPriceText.setText(format(Double.valueOf(currencyPrice), Utils.getPrecisionExchange(currencyType, exchangeType)));
        fiatPriceText.setText(fiatPrice);
    }

    private void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_head_back:
                this.finish();
                break;
            case R.id.btn_head_front:
//                MainActivity.currIndex = 1;
//                SharedPreferences.getInstance().putString("trans_currencyType", currencyType);
//                SharedPreferences.getInstance().putString("trans_exchangeType", exchangeType);
//                TransFragment.isPutChangeType = true;
//                ((RadioButton) MainActivity.getInstance().group.findViewById(R.id.foot_bar_trans)).setChecked(true);
//                MainActivity.getInstance().showFragment();
//                this.finish();
                Bundle bundle = new Bundle();
                bundle.putString("currencyType", currencyType);
                bundle.putString("exchangeType", exchangeType);
                TransActivity.isPutChangeType = true;
                UIHelper.showTrans(this, bundle);
                break;
            case R.id.chartTitleText:
                viewPager.setCurrentItem(0);
                break;
            case R.id.analysisTitleText:
                viewPager.setCurrentItem(1);
                break;
            case R.id.othersTitleText:
                viewPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    public String getIconUrl() {
        return iconUrl;
    }

    private void setTitleSelectBackground(TextView selectText) {
        titleInit();
        selectText.setBackgroundColor(ContextCompat.getColor(this, R.color.market_detail_title_bg_select));
    }

    private void titleInit() {
        int titleBackground = ContextCompat.getColor(this, R.color.market_detail_title_bg);
        chartTitleText.setBackgroundColor(titleBackground);
        analysisTitleText.setBackgroundColor(titleBackground);
        othersTitleText.setBackgroundColor(titleBackground);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
