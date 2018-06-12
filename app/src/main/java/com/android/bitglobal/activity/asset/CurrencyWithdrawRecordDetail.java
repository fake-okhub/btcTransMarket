package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.StringUtils;
import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.view.UserConfirm;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-08-28 11:10
 * 793169940@qq.com
 *充值记录详情
 */
public class CurrencyWithdrawRecordDetail extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.tv_assets_title)
    TextView tv_assets_title;
    @BindView(R.id.tv_assets_sl)
    TextView tv_assets_sl;
    @BindView(R.id.tv_assets_fbdz)
    TextView tv_assets_fbdz;
    @BindView(R.id.tv_assets_sjdz)
    TextView tv_assets_sjdz;
    @BindView(R.id.tv_assets_sxfy)
    TextView tv_assets_sxfy;
    @BindView(R.id.tv_assets_tjsj)
    TextView tv_assets_tjsj;
    @BindView(R.id.tv_assets_clsj)
    TextView tv_assets_clsj;
    @BindView(R.id.tv_assets_clzt)
    TextView tv_assets_clzt;

    @BindView(R.id.ll_assets_clsj)
    View ll_assets_clsj;

    @BindView(R.id.bnt_cancel)
    Button bnt_cancel;

    private UserConfirm userConfirm;
    private SubscriberOnNextListener cancelWithdrawOnNext;
    private List<String> list=new ArrayList<String>();
    private String safePwd,currencyType,id,doubleAmount,afterAmount,toAddress,status,showStat,fee,remark,submitTimestamp,manageTimestamp;
    private int commandId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_currency_withdraw_record_detail);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();

    }
    private void initData(){
        Bundle bundle = getIntent().getExtras();
        currencyType=bundle.getString("currencyType");

        id=bundle.getString("id");
        commandId=Integer.parseInt(bundle.getString("commandId"));
        doubleAmount=bundle.getString("doubleAmount");
        afterAmount=bundle.getString("afterAmount");
        toAddress=bundle.getString("toAddress");
        status=bundle.getString("status");
        showStat=bundle.getString("showStat");
        fee=bundle.getString("fee");
        remark=bundle.getString("remark");
        submitTimestamp=bundle.getString("submitTimestamp");
        manageTimestamp=bundle.getString("manageTimestamp");
    }
    private void initView() {
        userConfirm=new UserConfirm(context);
        userConfirm.tv_user_title1.setText(getString(R.string.asset_qxfb));
        userConfirm.bnt_user_commit.setOnClickListener(this);
        tv_head_title.setText(getString(R.string.asset_fbjlxq)+"-"+currencyType);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        bnt_cancel.setOnClickListener(this);
        if(remark.equals("")){
            //tv_assets_title.setVisibility(View.GONE);
            tv_assets_title.setText(getString(R.string.asset_fbjlxq));
            tv_assets_title.setVisibility(View.VISIBLE);
        }else{
            //tv_assets_title.setText(remark);
            tv_assets_title.setText(getString(R.string.asset_fbjlxq));
            tv_assets_title.setVisibility(View.VISIBLE);
        }

        tv_assets_sl.setText("- "+deFormat(doubleAmount));
        tv_assets_fbdz.setText(toAddress);
        tv_assets_sjdz.setText(deFormat(afterAmount)+" "+currencyType);
        tv_assets_sxfy.setText(deFormat(fee)+" "+currencyType);
        tv_assets_clzt.setText(showStat);
        tv_assets_tjsj.setText(StringUtils.dateFormat1(submitTimestamp));
        tv_assets_clsj.setText(StringUtils.dateFormat1(manageTimestamp));

        if(status.equals("0")&&commandId<=0){
            bnt_cancel.setVisibility(View.VISIBLE);
            ll_assets_clsj.setVisibility(View.GONE);
        }
        cancelWithdrawOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                bnt_cancel.setVisibility(View.GONE);
                tv_assets_clzt.setText(getString(R.string.user_yqx));
                MainActivity.getInstance().getfunds();
            }
        };
    }
    private void cancelWithdraw(){
        list.clear();
        list.add(currencyType);
        list.add(id);
        list.add(safePwd);
        HttpMethods.getInstance(3).cancelWithdraw(new ProgressSubscriber(cancelWithdrawOnNext, context),list);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;

            case R.id.bnt_cancel:
                userConfirm.show();
                break;
            case R.id.bnt_user_commit:
                safePwd=getText(userConfirm.ed_user_safePwd);
                if (safePwd.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.user_zjmm_hint_toast));
                    return;
                }
                userConfirm.dismiss();
                cancelWithdraw();
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
