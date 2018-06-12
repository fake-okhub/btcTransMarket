package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.view.UserConfirm;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-08-29 13:10
 * 793169940@qq.com
 * 融资融币－借入
 */
public class LendBorrow extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.ed_ky)
    TextView ed_ky;
    @BindView(R.id.ed_dw)
    TextView ed_dw;
    @BindView(R.id.ed_mrhl)
    TextView ed_mrhl;
    @BindView(R.id.ed_mrhl_hint)
    TextView ed_mrhl_hint;
    @BindView(R.id.ed_sl)
    EditText ed_sl;

    @BindView(R.id.tv_hint)
    TextView tv_hint;

    @BindView(R.id.bnt_commit)
    Button bnt_commit;
    @BindView(R.id.rl_asset_mrhl)
    View rl_asset_mrhl;
    @BindView(R.id.ll_asset_yhj)
    View ll_asset_yhj;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.tv_price)
    TextView tv_price;

    private List<String> list=new ArrayList<String>();
    private SubscriberOnNextListener doLoanOnNext;
    private UserConfirm userConfirm;
    private String title,currencyType,netAssets,p2pOutRate,amount,ticketId="0",safePwd="",day,price;
    private double netAssets_d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_lend_borrow);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();

    }
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        title=bundle.getString("title");
        currencyType=bundle.getString("currencyType");
        netAssets=bundle.getString("netAssets");
        p2pOutRate=bundle.getString("p2pOutRate");
        netAssets_d=Double.parseDouble(netAssets);
    }
    private void initView() {
        userConfirm=new UserConfirm(this);
        userConfirm.ed_user_safePwd.setVisibility(View.GONE);
        userConfirm.bnt_user_commit.setOnClickListener(this);
        userConfirm.tv_user_title1.setText(getString(R.string.asset_qrjxjk));
        userConfirm.tv_user_title2.setText("");
        tv_head_title.setText(title);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);

        rl_asset_mrhl.setOnClickListener(this);
        tv_hint.setOnClickListener(this);
        bnt_commit.setOnClickListener(this);
        ed_dw.setText(currencyType);
        ed_ky.setText(deFormat(netAssets));
        ed_mrhl.setText(p2pOutRate+"%");

        doLoanOnNext= new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                MainActivity.getInstance().getfunds();
                UIHelper.ToastMessage(context,getString(R.string.user_czcg_toast));
                finish();
            }
        };
    }
    
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.bnt_commit:
                amount=getText(ed_sl);
                if(amount.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.asset_jr_hint));
                    return;
                }
                userConfirm.show();
                break;
            case R.id.bnt_user_commit:
                userConfirm.dismiss();
                doLoan();
                break;
            case R.id.tv_hint:
                bundle.putString("title",getString(R.string.asset_syxy));
                bundle.putString("url", SystemConfig.getRzmbxy());
                bundle.putString("type","1");
                UIHelper.showWeb(context,bundle);
                break;
            case R.id.rl_asset_mrhl:
                bundle.putString("currencyType",currencyType);
                UIHelper.showCouponActivity(context,bundle);
                break;
        }

    }
    private void doLoan(){
        list.clear();
        list.add(currencyType);
        list.add(amount);
        list.add(ticketId);
        list.add(safePwd);
        HttpMethods.getInstance(4).doLoan(new ProgressSubscriber(doLoanOnNext, context),list);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10023 && resultCode == 10024) {
            Bundle bundle = data.getExtras();
            day= bundle.getString("day");
            price= bundle.getString("price");
            ticketId= bundle.getString("ticketId");
            tv_number.setText(day);
            tv_price.setText(price);
            if(!ticketId.equals("0")){
                ed_mrhl_hint.setVisibility(View.GONE);
                ll_asset_yhj.setVisibility(View.VISIBLE);
            }else{
                ed_mrhl_hint.setVisibility(View.VISIBLE);
                ll_asset_yhj.setVisibility(View.GONE);
            }
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
