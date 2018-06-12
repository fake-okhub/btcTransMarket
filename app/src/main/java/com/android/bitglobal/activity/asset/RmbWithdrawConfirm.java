package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.entity.CurrencyWithdrawResult;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-09-25 16:10
 * 793169940@qq.com
 * 人民币提现确认
 */
public class RmbWithdrawConfirm extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title) TextView tv_head_title;
    @BindView(R.id.btn_head_back) ImageView btn_head_back;

    @BindView(R.id.rl_asset_zjmm)  View rl_asset_zjmm;
    @BindView(R.id.rl_asset_dxyzm) View rl_asset_dxyzm;
    @BindView(R.id.rl_asset_yxyzm) View rl_asset_yxyzm;
    @BindView(R.id.rl_asset_ggyzm) View rl_asset_ggyzm;
    @BindView(R.id.rl_asset_hint) View rl_asset_hint;

    @BindView(R.id.ed_zjmm)  EditText ed_zjmm;
    @BindView(R.id.ed_dxyzm) EditText ed_dxyzm;
    @BindView(R.id.ed_yxyzm) EditText ed_yxyzm;
    @BindView(R.id.ed_ggyzm) EditText ed_ggyzm;

    @BindView(R.id.bnt_get_dxyzm) Button bnt_get_dxyzm;
    @BindView(R.id.bnt_get_yxyzm) Button bnt_get_yxyzm;
    @BindView(R.id.bnt_get_ggyzm) Button bnt_get_ggyzm;

    @BindView(R.id.tv_assets_txje) TextView tv_assets_txje;
    @BindView(R.id.tv_assets_txje_lx) TextView tv_assets_txje_lx;
    @BindView(R.id.tv_assets_sjdz) TextView tv_assets_sjdz;
    @BindView(R.id.tv_assets_skyh) TextView tv_assets_skyh;
    @BindView(R.id.tv_assets_sl_dw) TextView tv_assets_sl_dw;
    @BindView(R.id.tv_assets_skzh) TextView tv_assets_skzh;
    @BindView(R.id.tv_assets_skr) TextView tv_assets_skr;


    @BindView(R.id.bnt_commit)
    Button bnt_commit;
    @BindView(R.id.bnt_see)
    Button bnt_see;

    private String needSafePwd,needMobileCode,needEmailCode,needGoogleCode,type;
    private String bankRid,bankStaticId="",counterFee,bankName,bankImg,address_name,province="",city="",branchName="",isDefault="",acountId="",realName="",withdrawAmount,actualAmount,safePwd="",dynamicCode="",googleCode="";

    private SubscriberOnNextListener withdrawOnNext,userSendCodeOnNext;
    private List<String> list=new ArrayList<String>();
    private TimeCount time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_rmb_withdraw_confirm);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
    }
    private void initData() {
        Bundle bundle = getIntent().getExtras();

        bankRid=bundle.getString("bankRid");
        bankStaticId=bundle.getString("bankStaticId");
        bankName=bundle.getString("bankName");
        province=bundle.getString("province");
        city=bundle.getString("city");
        branchName=bundle.getString("branchName");
        isDefault=bundle.getString("isDefault");
        acountId=bundle.getString("acountId");
        realName=bundle.getString("realName");
        counterFee=bundle.getString("counterFee");
        withdrawAmount=bundle.getString("withdrawAmount");
        actualAmount=bundle.getString("actualAmount");
        address_name=bundle.getString("address_name");

        needSafePwd=bundle.getString("needSafePwd");
        needMobileCode=bundle.getString("needMobileCode");
        needEmailCode=bundle.getString("needEmailCode");
        needGoogleCode=bundle.getString("needGoogleCode");
        type="4";

    }
    private void initView() {
        tv_head_title.setText(getString(R.string.asset_qrtx));
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        bnt_commit.setOnClickListener(this);
        bnt_see.setOnClickListener(this);

        bnt_get_ggyzm.setOnClickListener(this);
        bnt_get_dxyzm.setOnClickListener(this);
        bnt_get_yxyzm.setOnClickListener(this);

        tv_assets_txje.setText(withdrawAmount);
        tv_assets_sjdz.setText(actualAmount);
        tv_assets_skyh.setText(bankName);
        tv_assets_skzh.setText(acountId);
        tv_assets_skr.setText(realName);

        withdrawOnNext = new SubscriberOnNextListener<CurrencyWithdrawResult>() {
            @Override
            public void onNext(CurrencyWithdrawResult currencyWithdrawResult) {
                if(currencyWithdrawResult==null){
                    rl_asset_zjmm.setVisibility(View.GONE);
                    rl_asset_dxyzm.setVisibility(View.GONE);
                    rl_asset_yxyzm.setVisibility(View.GONE);
                    rl_asset_ggyzm.setVisibility(View.GONE);
                    bnt_commit.setVisibility(View.GONE);
                    rl_asset_hint.setVisibility(View.GONE);
                    tv_head_title.setText(getString(R.string.user_czcg_toast));
                    bnt_see.setVisibility(View.VISIBLE);
                    MainActivity.getInstance().getfunds();
                }

            }
        };

        if("0".equals(needSafePwd)){
            rl_asset_zjmm.setVisibility(View.GONE);
        }
        if("0".equals(needMobileCode)){
            rl_asset_dxyzm.setVisibility(View.GONE);
        }
        if("0".equals(needEmailCode)){
            rl_asset_yxyzm.setVisibility(View.GONE);
        }
        if("0".equals(needGoogleCode)){
            rl_asset_ggyzm.setVisibility(View.GONE);
        }

        time = new TimeCount(60000, 1000);
        userSendCodeOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                time.start();
            }
        };

    }
    private void rmbWithdraw(){
        list.clear();
        list.add(bankRid);
        list.add(bankStaticId);
        list.add(province);
        list.add(city);
        list.add(branchName);
        list.add(isDefault);
        list.add(acountId);
        list.add(realName);
        list.add(withdrawAmount);
        list.add(safePwd);//safePwd
        list.add(dynamicCode);//dynamicCode
        list.add(googleCode);//googleCode
        HttpMethods.getInstance(3).rmbWithdraw(new ProgressSubscriber(withdrawOnNext, context),list);
    }
    //用户发送短信
    private void userSendCode(){
        list.clear();
        list.add(type);
        HttpMethods.getInstance(3).userSendCode(new ProgressSubscriber(userSendCodeOnNext, this), list);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.bnt_see:
                bundle.putString("type","2");
                UIHelper.showRmbRecord(context,bundle);
                finish();
                break;
            case R.id.bnt_get_ggyzm:
                if(!AppContext.getInstance().paste(context).equals("")){
                    googleCode=AppContext.getInstance().paste(context);
                    ed_ggyzm.setText(googleCode);
                }
                Editable etext = ed_ggyzm.getText();
                Selection.setSelection(etext, etext.length());
                break;
            case R.id.bnt_get_dxyzm:
                userSendCode();
                break;
            case R.id.bnt_get_yxyzm:
                userSendCode();
                break;
            case R.id.bnt_commit:
                if(needSafePwd.equals("1")){
                    safePwd=getText(ed_zjmm);
                    if(safePwd.equals("")){
                        UIHelper.ToastMessage(context,getString(R.string.user_zjmm_hint_toast));
                        return;
                    }
                }
                if(needMobileCode.equals("1")){
                    dynamicCode=getText(ed_dxyzm);
                    if(dynamicCode.equals("")){
                        UIHelper.ToastMessage(context,getString(R.string.user_dtyzm_toast));
                        return;
                    }
                }
                if(needEmailCode.equals("1")){
                    dynamicCode=getText(ed_yxyzm);
                    if(dynamicCode.equals("")){
                        UIHelper.ToastMessage(context,getString(R.string.user_yxyzm_hint_toast));
                        return;
                    }
                }
                if(needGoogleCode.equals("1")){
                    googleCode=getText(ed_ggyzm);
                    if(googleCode.equals("")){
                        UIHelper.ToastMessage(context,getString(R.string.user_ggyzm_hint_toast));
                        return;
                    }
                }
                rmbWithdraw();
                break;

        }

    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 计时完毕
            if(needMobileCode.equals("1")){
                bnt_get_dxyzm.setText(R.string.user_hqyzm);
                bnt_get_dxyzm.setClickable(true);
            }
            if(needEmailCode.equals("1")){
                bnt_get_yxyzm.setText(R.string.user_hqyzm);
                bnt_get_yxyzm.setClickable(true);
            }

        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 计时过程
            if(needMobileCode.equals("1")){
                bnt_get_dxyzm.setClickable(false);//防止重复点击
                bnt_get_dxyzm.setText(millisUntilFinished / 1000 + getResources().getString(R.string.user_maio));
            }
            if(needEmailCode.equals("1")){
                bnt_get_yxyzm.setClickable(false);//防止重复点击
                bnt_get_yxyzm.setText(millisUntilFinished / 1000 + getResources().getString(R.string.user_maio));
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
