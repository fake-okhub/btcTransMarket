package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.entity.CounterFee;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-11-11 15:10
 * 793169940@qq.com
 * 矿工费
 */
public class CounterFeeActivity extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.listview)
    ListView listview;

    private QuickAdapter<CounterFee> adapter;
    private List<CounterFee> list_data=new ArrayList<CounterFee>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_currency_withdraw_address);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
    }
    private void initData() {
        if(CurrencyWithdraw.feeInfos!=null&&CurrencyWithdraw.feeInfos.size()>0){
            list_data=CurrencyWithdraw.feeInfos;
        }else{
            if(CurrencyWithdraw.context!=null){
                CurrencyWithdraw.context.finish();
            }
            finish();
        }
    }
    private void initView() {
        tv_head_title.setText(getString(R.string.asset_kgf));
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        adapter = new QuickAdapter<CounterFee>(context, R.layout.asset_bank_address_item,list_data) {
            @Override
            protected void convert(final BaseAdapterHelper helper, final CounterFee item) {
                helper.setText(R.id.tv_bank_name,deFormat(item.getCounterFee())+" "+item.getCurrencyType());
                helper.setVisible(R.id.tv_bank_number,false);
                helper.setOnClickListener(R.id.ll_bank_address, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = getIntent();
                        Bundle bundle = new Bundle();
                        bundle.putString("counterFee", item.getCounterFee());
                        bundle.putString("feeInfoId", item.getId());
                        intent.putExtras(bundle);
                        setResult(5289, intent);
                        finish();
                    }
                });

            }
        };
        listview.setAdapter(adapter);

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
