package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.R;
import com.android.bitglobal.entity.WithdrawAddress;
import com.android.bitglobal.entity.WithdrawAddressResult;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-08-25 16:10
 * 793169940@qq.com
 *获取个人历史提现地址
 */
public class CurrencyWithdrawAddress extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.tv_no_ts)
    TextView tv_no_ts;
    @BindView(R.id.listview)
    ListView listview;
    private String currencyType;
    private QuickAdapter<WithdrawAddress> adapter;
    private SubscriberOnNextListener getWithdrawAddressOnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_currency_withdraw_address);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        tv_head_title.setText(getString(R.string.asset_ljfbjl)+"-"+currencyType);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);

        adapter = new QuickAdapter<WithdrawAddress>(context, R.layout.asset_currency_withdraw_address_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, final WithdrawAddress item) {
                helper.setText(R.id.tv_assets_address,item.getAddress());
                if(item.getMemo()==null||item.getMemo().equals("")){
                    helper.setText(R.id.tv_assets_memo,getString(R.string.asset_zwbz));
                }else{
                    helper.setText(R.id.tv_assets_memo,item.getMemo());
                }
                helper.setOnClickListener(R.id.ll_asset_address, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = getIntent();
                        Bundle bundle = new Bundle();
                        bundle.putString("address", item.getAddress());
                        bundle.putString("memo", item.getMemo());
                        intent.putExtras(bundle);
                        setResult(5290, intent);
                        finish();
                    }
                });
                helper.setOnClickListener(R.id.ll_asset_memo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", item.getId());
                        bundle.putString("address", item.getAddress());
                        bundle.putString("memo", item.getMemo());
                        bundle.putString("currencyType",currencyType);
                        UIHelper.showCurrencyWithdrawAddressUpdate((Activity) context,bundle);
                    }
                });

            }
        };
        getWithdrawAddressOnNext = new SubscriberOnNextListener<WithdrawAddressResult>() {
            @Override
            public void onNext(WithdrawAddressResult withdrawAddressResult) {
                adapter.clear();
                adapter.addAll(withdrawAddressResult.getWithdrawAddrs());
                if(withdrawAddressResult.getWithdrawAddrs().size()>0){
                    tv_no_ts.setVisibility(View.GONE);
                }else{
                    tv_no_ts.setVisibility(View.VISIBLE);
                }
            }

        };
        listview.setAdapter(adapter);
    }
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        currencyType=bundle.getString("currencyType");
    }

    private void getWithdrawAddress() {
        HttpMethods.getInstance(3).getWithdrawAddress(new ProgressSubscriber(getWithdrawAddressOnNext, context),currencyType);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;

        }

    }
    @Override
    public void onResume() {
        super.onResume();
        getWithdrawAddress();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
