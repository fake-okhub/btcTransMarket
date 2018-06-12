package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.view.UserConfirm;
import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-09-22 14:10
 * 793169940@qq.com
 * 支付宝充值账单详情
 */
public class RechargeDetail extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.bnt_commit)
    Button bnt_commit;
    @BindView(R.id.bnt_cancel)
    Button bnt_cancel;

    @BindView(R.id.ll_bill_zfb)
    LinearLayout ll_bill_zfb;
    @BindView(R.id.ll_bill_yhk)
    LinearLayout ll_bill_yhk;

    @BindView(R.id.tv_bill_title)
    TextView tv_bill_title;
    @BindView(R.id.tv_bill_number)
    TextView tv_bill_number;
    @BindView(R.id.img_tel)
    ImageView img_tel;
    @BindView(R.id.img_fz)
    ImageView img_fz;
    @BindView(R.id.tv_bill_hryh)
    TextView tv_bill_hryh;
    @BindView(R.id.tv_bill_hrzh)
    TextView tv_bill_hrzh;
    @BindView(R.id.tv_bill_skr)
    TextView tv_bill_skr;
    @BindView(R.id.tv_bill_bzxx)
    TextView tv_bill_bzxx;
    @BindView(R.id.tv_bill_fkzh)
    TextView tv_bill_fkzh;
    @BindView(R.id.tv_bill_fkf)
    TextView tv_bill_fkf;
    @BindView(R.id.tv_bill_skzh)
    TextView tv_bill_skzh;
    @BindView(R.id.tv_bill_skf)
    TextView tv_bill_skf;
    @BindView(R.id.tv_bill_hkzt)
    TextView tv_bill_hkzt;
    private UserConfirm userConfirm;
    private SubscriberOnNextListener cancelRechargeOnNext;
    private String bankId,serialNumber,receiveAccount,payAccount,receiveBankName,amount,actualAmount,
            description,receiver,payer,status,statusShow,payBankLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_recharge_detail);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        userConfirm=new UserConfirm(context);
        userConfirm.ed_user_safePwd.setVisibility(View.GONE);
        userConfirm.tv_user_title2.setVisibility(View.INVISIBLE);
        userConfirm.tv_user_title1.setText(getString(R.string.asset_qxcz));
        userConfirm.bnt_user_commit.setOnClickListener(this);
        tv_head_title.setText(R.string.asset_recharge_czxq);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        bnt_commit.setOnClickListener(this);
        bnt_cancel.setOnClickListener(this);
        img_tel.setOnClickListener(this);
        img_fz.setOnClickListener(this);

        tv_bill_hrzh.setOnClickListener(this);
        tv_bill_hryh.setOnClickListener(this);
        tv_bill_skr.setOnClickListener(this);
        tv_bill_bzxx.setOnClickListener(this);


        if("1".equals(bankId)){
            ll_bill_zfb.setVisibility(View.VISIBLE);
            ll_bill_yhk.setVisibility(View.GONE);
            tv_bill_title.setText(getString(R.string.asset_zfbcz)+"：");
            bnt_commit.setText(getString(R.string.asset_bill_rhzfbzz));
        }else{
            ll_bill_zfb.setVisibility(View.GONE);
            ll_bill_yhk.setVisibility(View.VISIBLE);
            tv_bill_title.setText(getString(R.string.asset_yhhk)+"：");
        }
        if(status.equals("1")||status.equals("8")||status.equals("10")){
            tv_bill_hkzt.setTextColor(Color.parseColor("#E70101"));
        }else{
            tv_bill_hkzt.setTextColor(Color.parseColor("#1BA905"));
        }
        if(status.equals("9")||status.equals("10")||status.equals("6")){
            bnt_cancel.setVisibility(View.VISIBLE);
            bnt_commit.setVisibility(View.VISIBLE);
        }else{
            bnt_cancel.setVisibility(View.GONE);
            bnt_commit.setVisibility(View.GONE);
        }
        tv_bill_hrzh.setText(receiveAccount);
        tv_bill_hryh.setText(receiveBankName);
        tv_bill_skr.setText(receiver);
        tv_bill_bzxx.setText(description);

        tv_bill_fkzh.setText(payAccount);
        tv_bill_fkf.setText(payer);
        tv_bill_skzh.setText(receiveAccount);
        tv_bill_skf.setText(receiver);

        tv_bill_hkzt.setText(statusShow);
        tv_bill_number.setText("+"+deFormat(amount));
        cancelRechargeOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                if(RmbRecord.context!=null){
                    RmbRecord.context.clearData();
                }
                if(RmbSelectRecharge.context!=null){
                    RmbSelectRecharge.context.getRechargeList();
                }
                bnt_cancel.setVisibility(View.GONE);
                bnt_commit.setVisibility(View.GONE);
                tv_bill_hkzt.setText(getString(R.string.user_yqx));
            }
        };
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        bankId=bundle.getString("bankId");
        serialNumber=bundle.getString("serialNumber");
        receiveAccount=bundle.getString("receiveAccount");
        payAccount=bundle.getString("payAccount");
        receiveBankName=bundle.getString("receiveBankName");
        amount=bundle.getString("amount");
        actualAmount=bundle.getString("actualAmount");
        description=bundle.getString("description");
        receiver=bundle.getString("receiver");
        payer=bundle.getString("payer");
        status=bundle.getString("status");
        statusShow=bundle.getString("statusShow");
        payBankLink=bundle.getString("payBankLink");

    }
    private void cancelRecharge(){
        HttpMethods.getInstance(3).cancelRecharge(new ProgressSubscriber(cancelRechargeOnNext, context),serialNumber);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.img_tel:
                MainActivity.getInstance().testCall();
                break;
            case R.id.img_fz:
                AppContext.getInstance().copy(receiveAccount,context);
                break;
            case R.id.bnt_cancel:
                userConfirm.show();
                break;
            case R.id.bnt_commit:
                try{
                    Uri uri = Uri.parse(payBankLink);
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(it);
                }catch (Exception e){

                }
                break;
            case R.id.bnt_user_commit:
                userConfirm.dismiss();
                cancelRecharge();
                break;
            case R.id.tv_bill_hrzh:
                AppContext.getInstance().copy(receiveAccount,context);
                break;
            case R.id.tv_bill_hryh:
                AppContext.getInstance().copy(receiveBankName,context);
                break;
            case R.id.tv_bill_skr:
                AppContext.getInstance().copy(receiver,context);
                break;
            case R.id.tv_bill_bzxx:
                String repickStr = description.replaceAll("[^0-9]", "");
                AppContext.getInstance().copy(repickStr,context);
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
