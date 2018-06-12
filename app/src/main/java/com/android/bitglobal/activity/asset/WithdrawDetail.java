package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.view.UserConfirm;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-09-22 14:10
 * 793169940@qq.com
 * 提现账单详情
 */
public class WithdrawDetail extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.bnt_cancel)
    Button bnt_cancel;

    @BindView(R.id.tv_bill_title)
    TextView tv_bill_title;
    @BindView(R.id.tv_bill_hint)
    TextView tv_bill_hint;
    @BindView(R.id.tv_bill_number)
    TextView tv_bill_number;

    @BindView(R.id.img_tel)
    ImageView img_tel;
    @BindView(R.id.img_sxf)
    ImageView img_sxf;

    @BindView(R.id.tv_bill_skzh)
    TextView tv_bill_skzh;
    @BindView(R.id.tv_bill_skyh)
    TextView tv_bill_skyh;
    @BindView(R.id.tv_bill_lsbh)
    TextView tv_bill_lsbh;
    @BindView(R.id.tv_bill_sjdz)
    TextView tv_bill_sjdz;
    @BindView(R.id.tv_bill_txzt)
    TextView tv_bill_txzt;
    @BindView(R.id.tv_bill_sxfy)
    TextView tv_bill_sxfy;
    private UserConfirm userConfirm;
    private SubscriberOnNextListener cancelRmbWithdrawOnNext;
    private String serialNumber,receiveAccount,receiveBankName,amount,actualAmount,
            description,status,statusShow,withdrawType,fees,withdrawTypeShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_withdraw_detail);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {

        userConfirm=new UserConfirm(context);
        userConfirm.ed_user_safePwd.setVisibility(View.GONE);
        userConfirm.tv_user_title2.setVisibility(View.INVISIBLE);
        userConfirm.tv_user_title1.setText(getString(R.string.asset_qxtx));
        userConfirm.bnt_user_commit.setOnClickListener(this);
        tv_head_title.setText(R.string.asset_recharge_txxq);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);

        bnt_cancel.setOnClickListener(this);
        img_tel.setOnClickListener(this);
        img_sxf.setOnClickListener(this);

        if(status.equals("9")||status.equals("10")||status.equals("6")){
            bnt_cancel.setVisibility(View.VISIBLE);
        }else{
            bnt_cancel.setVisibility(View.GONE);
        }
        tv_bill_title.setText(getString(R.string.asset_rmbtx)+"：");
        tv_bill_hint.setText(withdrawTypeShow);
        tv_bill_skzh.setText(receiveAccount);
        tv_bill_skyh.setText(receiveBankName);
        tv_bill_lsbh.setText(serialNumber);
        tv_bill_txzt.setText(statusShow);
        tv_bill_number.setText("-"+deFormat(amount));
        tv_bill_sjdz.setText(deFormat(actualAmount));
        tv_bill_sxfy.setText(deFormat(fees));
        cancelRmbWithdrawOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                if(RmbRecord.context!=null){
                    RmbRecord.context.clearData();
                }
                bnt_cancel.setVisibility(View.GONE);
                tv_bill_txzt.setText(getString(R.string.user_yqx));
                MainActivity.getInstance().getfunds();
            }
        };
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        serialNumber=bundle.getString("serialNumber");
        receiveAccount=bundle.getString("receiveAccount");
        receiveBankName=bundle.getString("receiveBankName");
        amount=bundle.getString("amount");
        actualAmount=bundle.getString("actualAmount");
        description=bundle.getString("description");
        status=bundle.getString("status");
        statusShow=bundle.getString("statusShow");
        fees=bundle.getString("fees");
        withdrawType=bundle.getString("withdrawType");
        withdrawTypeShow=bundle.getString("withdrawTypeShow");
    }
    private void cancelRmbWithdraw(){
        HttpMethods.getInstance(3).cancelRmbWithdraw(new ProgressSubscriber(cancelRmbWithdrawOnNext, context),serialNumber);
    }
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.img_tel:
                MainActivity.getInstance().testCall();
                break;
            case R.id.img_sxf:
                bundle.putString("title",getResources().getString(R.string.user_hlzz));
                bundle.putString("url", SystemConfig.getLevel());
                bundle.putString("type","1");
                UIHelper.showWeb(context,bundle);
                break;
            case R.id.bnt_cancel:
                userConfirm.show();
                break;
            case R.id.bnt_user_commit:
                userConfirm.dismiss();
                cancelRmbWithdraw();
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
