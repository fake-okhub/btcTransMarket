package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.CurrencyWithdrawResult;
import com.android.bitglobal.entity.VersionResult;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.entity.CounterFee;
import com.android.bitglobal.entity.ElementSetting;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.view.SwitchView;
import com.android.bitglobal.view.UserSelect;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-08-25 16:10
 * 793169940@qq.com
 *货币提现
 */
public class CurrencyWithdraw extends SwipeBackActivity implements View.OnClickListener{
    public static   Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;
    @BindView(R.id.btn_head_front)
    ImageView btn_head_front;
    @BindView(R.id.ed_ky)
    TextView ed_ky;
    @BindView(R.id.ed_dw)
    TextView ed_dw;

    @BindView(R.id.ed_dz)
    TextView ed_dz;
    @BindView(R.id.tv_hint)
    TextView tv_hint;
    @BindView(R.id.tv_hint2)
    TextView tv_hint2;

    @BindView(R.id.rl_kyye)
    RelativeLayout rl_kyye;

    @BindView(R.id.rl_kgf)
    LinearLayout rl_kgf;
    @BindView(R.id.ed_kgf)
    TextView ed_kgf;

    @BindView(R.id.img_xzdz)
    ImageView img_xzdz;
    @BindView(R.id.ed_sl)
    EditText ed_sl;
    @BindView(R.id.ed_bz)
    EditText ed_bz;
    @BindView(R.id.bnt_commit)
    Button bnt_commit;
    @BindView(R.id.view_switch)
    SwitchView mSwitchView;
    private UserSelect userSelect;
    private String currencyType,available,receiveAddress,cashAmount,liuyan,isInnerTransfer="0",feeInfoId="";
    private double counterFee=0.0001,available_d,available_cashAmount_d=0.00;

    private SubscriberOnNextListener withdrawOnNext,getCounterFeeOnNext,getElementSettingsOnNext;
    private List<String> list=new ArrayList<String>();
    public static List<CounterFee> feeInfos=new ArrayList<CounterFee>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_currency_withdraw);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
        getCounterFee();
        getElementSettings();
    }
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        currencyType=bundle.getString("currencyType");
        receiveAddress=bundle.getString("receiveAddress");
        if(receiveAddress!=null&&!receiveAddress.equals("")){
            ed_dz.setText(receiveAddress);
        }else{
            receiveAddress="";
        }
       // AssetsBalance assetsBalance=UserAcountDao.getInstance().getAssets(currencyType);
      //  available=deFormat(assetsBalance.getAvailable());
        available_d=Double.parseDouble(available);
    }
    private void initView() {
        userSelect=new UserSelect(context);
        userSelect.tv_select_title1.setText(getString(R.string.asset_fbdz));
        userSelect.tv_select_title2.setText(getString(R.string.asset_xzczfs));
        userSelect.bnt_select_subject_1.setText(getString(R.string.asset_smewm));
        userSelect.bnt_select_subject_2.setText(getString(R.string.asset_cjtbjt));
        tv_head_title.setText(getString(R.string.trans_fb)+"-"+currencyType);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        btn_head_front.setVisibility(View.VISIBLE);
        btn_head_front.setOnClickListener(this);
        rl_kyye.setOnClickListener(this);
        rl_kgf.setOnClickListener(this);
        btn_head_front.setImageResource(R.mipmap.icon_nav_recharge_camera);
        bnt_commit.setOnClickListener(this);
        userSelect.bnt_select_subject_1.setOnClickListener(this);
        userSelect.bnt_select_subject_2.setOnClickListener(this);
        ed_dz.setOnClickListener(this);
        img_xzdz.setOnClickListener(this);
        ed_dw.setText(currencyType);
        ed_ky.setText(deFormat(available));


        mSwitchView.setOpened(false);
        mSwitchView.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
                isInnerTransfer="1";
                mSwitchView.toggleSwitch(true);
            }

            @Override
            public void toggleToOff(View view) {
                isInnerTransfer="0";
                mSwitchView.toggleSwitch(false);
            }
        });


        ed_sl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    available_cashAmount_d=Double.parseDouble(getText(ed_sl))-counterFee;
                    if(available_cashAmount_d<=0){
                        available_cashAmount_d=0.00;
                    }else{
                        available_cashAmount_d=Double.parseDouble(deFormat(available_cashAmount_d));
                    }
                }catch (Exception e){
                    available_cashAmount_d=0.00;
                }
                //initView2();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getElementSettingsOnNext= new SubscriberOnNextListener<VersionResult>() {
            @Override
            public void onNext(VersionResult versionResult) {
                if(versionResult!=null&&versionResult.getElementSettings()!=null){
                    for(ElementSetting es:versionResult.getElementSettings()){
                        if("WITHDRAW_BTC_fees".equals(es.getElement())&&currencyType.equals("BTC")){
                            tv_hint.setText(es.getTips());
                        }
                        if("WITHDRAW_ETC_fees".equals(es.getElement())&&currencyType.equals("ETC")){
                            tv_hint.setText(es.getTips());
                        }
                        if("WITHDRAW_LTC_fees".equals(es.getElement())&&currencyType.equals("LTC")){
                            tv_hint.setText(es.getTips());
                        }
                        if("WITHDRAW_ETH_fees".equals(es.getElement())&&currencyType.equals("ETH")){
                            tv_hint.setText(es.getTips());
                        }


                        if("WITHDRAW_BTC_isInnerTransfer".equals(es.getElement())&&currencyType.equals("BTC")){
                            tv_hint2.setText(es.getTips());
                        }
                        if("WITHDRAW_ETC_isInnerTransfer".equals(es.getElement())&&currencyType.equals("ETC")){
                            tv_hint2.setText(es.getTips());
                        }
                        if("WITHDRAW_LTC_isInnerTransfer".equals(es.getElement())&&currencyType.equals("LTC")){
                            tv_hint2.setText(es.getTips());
                        }
                        if("WITHDRAW_ETH_isInnerTransfer".equals(es.getElement())&&currencyType.equals("ETH")){
                            tv_hint2.setText(es.getTips());
                        }


                    }
                }
            }
        };
        getCounterFeeOnNext= new SubscriberOnNextListener<VersionResult>() {
            @Override
            public void onNext(VersionResult versionResult) {
                //counterFee=Double.parseDouble(cf.getCounterFee());
                if(versionResult.getFeeInfos()!=null){
                    feeInfos=versionResult.getFeeInfos();
                    for(CounterFee cf:feeInfos){
                        if("1".equals(cf.getIsDefault())){
                            counterFee=Double.parseDouble(cf.getCounterFee());
                            feeInfoId=cf.getId();
                            initView2();
                        }
                    }
                    if(feeInfoId.equals("")&&feeInfos.size()>0){
                        counterFee=Double.parseDouble(feeInfos.get(0).getCounterFee());
                        feeInfoId=feeInfos.get(0).getId();
                        initView2();
                    }
                }
            }
        };
        withdrawOnNext = new SubscriberOnNextListener<CurrencyWithdrawResult>() {
            @Override
            public void onNext(CurrencyWithdrawResult currencyWithdrawResult) {
                Bundle bundle = new Bundle();
                bundle.putString("currencyType",currencyType);
                bundle.putString("receiveAddress",receiveAddress);
                bundle.putString("cashAmount",cashAmount);
                bundle.putString("counterFee",deFormat(counterFee));
                bundle.putString("available_cashAmount",deFormat(available_cashAmount_d));
                bundle.putString("liuyan",liuyan);
                bundle.putString("feeInfoId",feeInfoId);
                bundle.putString("isInnerTransfer",isInnerTransfer);
                bundle.putString("needSafePwd",currencyWithdrawResult.getNeedSafePwd());
                bundle.putString("needMobileCode",currencyWithdrawResult.getNeedMobileCode());
                bundle.putString("needEmailCode",currencyWithdrawResult.getNeedEmailCode());
                bundle.putString("needGoogleCode",currencyWithdrawResult.getNeedGoogleCode());

                UIHelper.showCurrencyWithdrawConfirm(context,bundle);
            }
        };
    }
    private void initView2(){
        //tv_hint.setText(getString(R.string.asset_sjdz)+deFormat(available_cashAmount_d)+" "+currencyType+"，"+getString(R.string.asset_kgf)+"："+deFormat(counterFee)+" "+currencyType);
        ed_kgf.setText(deFormat(counterFee)+" "+currencyType);
    }
    private void getCounterFee(){
        HttpMethods.getInstance(3).getCounterFee(new ProgressSubscriber(getCounterFeeOnNext,this),currencyType);
    }

    private void getElementSettings(){
        HttpMethods.getInstance(3).getElementSettings(new ProgressSubscriber(getElementSettingsOnNext,this));
    }
    private void withdraw(){
        list.clear();
        list.add(currencyType);
        list.add(cashAmount);
        list.add(receiveAddress);
        list.add(liuyan);
        list.add(feeInfoId);
        list.add(isInnerTransfer);
        list.add("");//safePwd
        list.add("");//googleCode
        list.add("");//dynamicCode
        HttpMethods.getInstance(3).withdraw(new ProgressSubscriber(withdrawOnNext, context),list);
    }


    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.btn_head_front:
                bundle.putString("currencyType",currencyType);
                UIHelper.showCurrencyWithdrawRecord(context,bundle);
                break;
            case R.id.rl_kyye:
                ed_sl.setText(deFormat(available));
                Editable etext = ed_sl.getText();
                Selection.setSelection(etext, etext.length());
                break;
            case R.id.rl_kgf:
                UIHelper.showCounterFee(context);
                break;
            case R.id.ed_dz:
                userSelect.show();
                break;
            case R.id.bnt_select_subject_1:
                userSelect.dismiss();
                AppContext.customScan(context);
                break;
            case R.id.img_xzdz:
                bundle.putString("currencyType",currencyType);
                UIHelper.showCurrencyWithdrawAddress(context,bundle);
                break;
            case R.id.bnt_select_subject_2:
                userSelect.dismiss();
                if(!AppContext.getInstance().paste(context).equals("")){
                    receiveAddress=AppContext.getInstance().paste(context);
                    ed_dz.setText(receiveAddress);
                }
                break;
            case R.id.bnt_commit:
                receiveAddress=getText(ed_dz);
                cashAmount=getText(ed_sl);
                liuyan=getText(ed_bz);
                if(receiveAddress.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.asset_dz_hint_toast));
                    return;
                }
                if(cashAmount.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.asset_sl_hint_toast));
                    return;
                }
                cashAmount=deFormat(cashAmount);
                /*if(Double.parseDouble(cashAmount)<=counterFee){
                    UIHelper.ToastMessage(context,getString(R.string.asset_fbslbndykgf));
                    return;
                }*/
                if(Double.parseDouble(cashAmount)>available_d){
                    UIHelper.ToastMessage(context,getString(R.string.asset_yebz_toast));
                    return;
                }
                withdraw();
                break;

        }

    }
    @Override
    public void onResume() {
        super.onResume();
        UserInfo userInfo= UserDao.getInstance().getIfon();
        /*if(is_token(userInfo)){
          UserAcount userAcount= UserAcountDao.getInstance().getIfon(userInfo.getToken());
            for(AssetsBalance ab:userAcount.getBalances()){
                if(ab.getCurrencyType().equals(currencyType)){
                    available=ab.getAvailable();
                    available_d=Double.parseDouble(available);
                    ed_ky.setText(deFormat(available));
                }
            }
        }*/
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    @Override
    // 通过 onActivityResult的方法获取 扫描回来的 值
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null&&resultCode == 5290) {
            Bundle bundle = data.getExtras();
            receiveAddress= bundle.getString("address");
            ed_dz.setText(receiveAddress);

        }else if (data!=null&&resultCode == 5289) {
            Bundle bundle = data.getExtras();
            counterFee= Double.parseDouble(bundle.getString("counterFee"));
            feeInfoId=bundle.getString("feeInfoId");
            ed_sl.setText(getText(ed_sl));
            Editable etext = ed_sl.getText();
            Selection.setSelection(etext, etext.length());
            initView2();
        }else{
            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
            if(intentResult != null) {
                if(intentResult.getContents() == null) {
                    //UIHelper.ToastMessage(context,getString(R.string.asset_nrwk));
                } else {
                    UIHelper.ToastMessage(context,getString(R.string.asset_smcg_toast));
                    receiveAddress = intentResult.getContents();
                    ed_dz.setText(receiveAddress);
                }
            } else {
                super.onActivityResult(requestCode,resultCode,data);
            }
        }


    }
}
