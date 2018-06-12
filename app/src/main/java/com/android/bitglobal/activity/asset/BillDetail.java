package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-09-22 14:10
 * 793169940@qq.com
 * 账单详情
 */
public class BillDetail extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.tv_bill_title)
    TextView tv_bill_title;
    @BindView(R.id.tv_bill_number)
    TextView tv_bill_number;
    @BindView(R.id.tv_bill_type)
    TextView tv_bill_type;
    @BindView(R.id.tv_bill_tiem)
    TextView tv_bill_tiem;
    @BindView(R.id.tv_bill_balance)
    TextView tv_bill_balance;
    private String typeName,showChang,showBalance,time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_bill_detail);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        tv_head_title.setText(getString(R.string.asset_zdxq));
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);

        tv_bill_title.setText(typeName);
        tv_bill_number.setText(showChang);
        tv_bill_type.setText(typeName);
        tv_bill_balance.setText(showBalance);
        tv_bill_tiem.setText(time);
    }
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        typeName=bundle.getString("typeName");
        showChang=bundle.getString("showChang");
        showBalance=bundle.getString("showBalance");
        time=bundle.getString("time");
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
