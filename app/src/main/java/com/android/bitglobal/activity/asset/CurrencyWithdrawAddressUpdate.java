package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.R;
import com.android.bitglobal.http.HttpMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-08-25 16:10
 * 793169940@qq.com
 * 修改提现地址备注
 */
public class CurrencyWithdrawAddressUpdate extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.ed_dz)
    TextView ed_dz;
    @BindView(R.id.ed_bz)
    EditText ed_bz;
    @BindView(R.id.bnt_commit)
    Button bnt_commit;
    private SubscriberOnNextListener updateWithdrawAddressMemoOnNext;
    private List<String> list=new ArrayList<String>();
    private String currencyType,address,memo,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_currency_withdraw_address_update);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();

    }

    private void initView() {
        tv_head_title.setText(getString(R.string.asset_xgdzbz)+"-"+currencyType);
        bnt_commit.setOnClickListener(this);
        btn_head_back.setOnClickListener(this);
        btn_head_back.setVisibility(View.VISIBLE);
        ed_dz.setText(address);
        ed_bz.setText(memo);
        Editable etext = ed_bz.getText();
        Selection.setSelection(etext, etext.length());

        updateWithdrawAddressMemoOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                UIHelper.ToastMessage(context,getString(R.string.user_czcg_toast));
                finish();
            }
        };
    }
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        currencyType=bundle.getString("currencyType");
        address=bundle.getString("address");
        memo=bundle.getString("memo");
        id=bundle.getString("id");

    }
    private void updateWithdrawAddressMemo(){
        list.clear();
        list.add(currencyType);
        list.add(id);
        list.add(memo);
        HttpMethods.getInstance(3).updateWithdrawAddressMemo(new ProgressSubscriber(updateWithdrawAddressMemoOnNext, context),list);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.bnt_commit:
                memo=getText(ed_bz);
                updateWithdrawAddressMemo();
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
