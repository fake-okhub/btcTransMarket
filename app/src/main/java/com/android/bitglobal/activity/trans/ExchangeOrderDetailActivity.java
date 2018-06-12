package com.android.bitglobal.activity.trans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.android.bitglobal.R;
import com.android.bitglobal.activity.BaseActivity;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.adapters.TabViewPageAdapter;
import com.android.bitglobal.entity.EntrustTrade;
import com.android.bitglobal.fragment.trans.ExchangeOrderDetailInfoFragment;
import com.android.bitglobal.fragment.trans.ExchangeOrderDetailTradeFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.ui.UIHelper;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

public class ExchangeOrderDetailActivity extends BaseActivity implements View.OnClickListener,
        ExchangeOrderDetailInfoFragment.OnExchangeOrderDetailCallback {

    public final static String ARG_ID = "id";
    public final static String ARG_DATE = "date";
    public final static String ARG_TYPE = "type";
    public final static String ARG_PRICE = "price";
    public final static String ARG_AVG = "avg";
    public final static String ARG_AMOUNT = "amount";
    public final static String ARG_FINISHED = "finished";
    public final static String ARG_TOTAL = "total";
    public final static String ARG_CURRENCY_TYPE = "C_TYPE";
    public final static String ARG_EXCHANGE_TYPE = "E_TYPE";

    private static OnExchangeOrderDetailCallback listener;

    ExchangeOrderDetailInfoFragment.ExchangeOrderDetailInfoData infoData
            = new ExchangeOrderDetailInfoFragment.ExchangeOrderDetailInfoData();


    public static Intent newAct(Context context, EntrustTrade info, String c_type, String e_type,
                                OnExchangeOrderDetailCallback callback) {
        Intent intent = new Intent(context, ExchangeOrderDetailActivity.class);
        intent.putExtra(ARG_ID, info.getEntrustId());
        intent.putExtra(ARG_DATE, info.getSubmitTime());
        intent.putExtra(ARG_TYPE, info.getType());
        intent.putExtra(ARG_PRICE, info.getUnitPrice());
        intent.putExtra(ARG_AMOUNT, info.getNumber());
        intent.putExtra(ARG_FINISHED, info.getCompleteNumber());
        intent.putExtra(ARG_TOTAL, info.getCompleteTotalMoney());
        intent.putExtra(ARG_AVG, info.getJunjia());
        intent.putExtra(ARG_CURRENCY_TYPE, c_type);
        intent.putExtra(ARG_EXCHANGE_TYPE, e_type);
        listener = callback;
        return intent;
    }

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_order_detail);
        initData();
        initView();
    }

    private void initView() {
        // 初始化Tab页
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout)
                findViewById(R.id.activity_exchange_order_detail_tab);
        ViewPager viewPager = (ViewPager)
                findViewById(R.id.activity_exchange_order_detail_vp);
        viewPager.setAdapter(
                new TabViewPageAdapter(getSupportFragmentManager(),
                        mFragments, mTitles));
        slidingTabLayout.setViewPager(viewPager);
        // 初始化Title
        findViewById(R.id.activity_exchange_order_detail_head_back).setOnClickListener(this);

    }

    private void initData() {
        mTitles.add(getResources().getString(R.string.trans_exchange_order_detail_tab_info_title));
        mTitles.add(getResources().getString(R.string.trans_exchange_order_detail_tab_trade_title));
        if (getIntent().hasExtra(ARG_ID)) {
            infoData.setOrderId(getIntent().getStringExtra(ARG_ID));
        }
        if (getIntent().hasExtra(ARG_DATE)) {
            infoData.setDate(getIntent().getStringExtra(ARG_DATE));
        }
        if (getIntent().hasExtra(ARG_TYPE)) {
            infoData.setType(getIntent().getStringExtra(ARG_TYPE));
        }
        if (getIntent().hasExtra(ARG_PRICE)) {
            infoData.setPrice(getIntent().getStringExtra(ARG_PRICE));
        }
        if (getIntent().hasExtra(ARG_AVG)) {
            infoData.setAvgPrice(getIntent().getDoubleExtra(ARG_AVG, 0));
        }
        if (getIntent().hasExtra(ARG_AMOUNT)) {
            infoData.setAmount(getIntent().getStringExtra(ARG_AMOUNT));
        }
        if (getIntent().hasExtra(ARG_FINISHED)) {
            infoData.setFinished(getIntent().getStringExtra(ARG_FINISHED));
        }
        if (getIntent().hasExtra(ARG_TOTAL)) {
            infoData.setTotal(getIntent().getStringExtra(ARG_TOTAL));
        }
        if (getIntent().hasExtra(ARG_CURRENCY_TYPE)) {
            infoData.setCurrencyType(getIntent().getStringExtra(ARG_CURRENCY_TYPE));
        }
        if (getIntent().hasExtra(ARG_EXCHANGE_TYPE)) {
            infoData.setExchangeType(getIntent().getStringExtra(ARG_EXCHANGE_TYPE));
        }
        if (listener == null) {
            mFragments.add(ExchangeOrderDetailInfoFragment.newFrg(infoData, false));
        } else {
            mFragments.add(ExchangeOrderDetailInfoFragment.newFrg(infoData, true));
        }
        String orderId;
        if (getIntent().hasExtra(ARG_ID)) {
            orderId = getIntent().getStringExtra(ARG_ID);
        } else {
            orderId = "";
        }
        mFragments.add(ExchangeOrderDetailTradeFragment.newFrg(orderId,
                infoData.getCurrencyType(), infoData.getExchangeType()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_exchange_order_detail_head_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCancel() {
        SubscriberOnNextListener cancelOrderOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                MainActivity.getInstance().getUserInfo();
                UIHelper.ToastMessage(ExchangeOrderDetailActivity.this,
                        R.string.trans_cxcg);
                if (listener != null) {
                    listener.onCancel();
                }
                finish();

            }
        };
        List<String> list = new ArrayList<>();
        list.clear();
        list.add(infoData.getCurrencyType());
        list.add(infoData.getExchangeType());
        list.add(infoData.getOrderId());
        HttpMethods.getInstance(1)
                .cancelOrder(new ProgressSubscriber(cancelOrderOnNext,
                        this), list);
    }

    public interface OnExchangeOrderDetailCallback {
        void onCancel();
    }
}
