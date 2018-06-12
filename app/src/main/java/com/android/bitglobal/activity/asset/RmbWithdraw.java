package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.AreaDataResult;
import com.android.bitglobal.entity.CurrencyWithdrawResult;
import com.android.bitglobal.entity.VersionResult;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.dao.AreaDataDao;
import com.android.bitglobal.entity.CounterFee;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-07-09 16:10
 * 793169940@qq.com
 *人民币充值
 */
public class RmbWithdraw extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;
    @BindView(R.id.btn_head_front)
    ImageView btn_head_front;

    @BindView(R.id.ll_hint)
    LinearLayout ll_hint;
    @BindView(R.id.rl_xzyh)
    LinearLayout rl_xzyh;
    @BindView(R.id.rl_xzdq)
    LinearLayout rl_xzdq;
    @BindView(R.id.img_bank)
    ImageView img_bank;
    @BindView(R.id.ed_xzyh)
    TextView ed_xzyh;
    @BindView(R.id.ed_xzdq)
    TextView ed_xzdq;
    @BindView(R.id.ed_yhkh)
    EditText ed_yhkh;
    @BindView(R.id.ed_szzh)
    EditText ed_szzh;
    @BindView(R.id.ed_txje)
    EditText ed_txje;
    @BindView(R.id.ed_skr)
    EditText ed_skr;
    @BindView(R.id.tv_sjdz)
    TextView tv_sjdz;
    @BindView(R.id.bnt_commit)
    Button bnt_commit;
    private String currencyType,available;
    private double counterFee=0,feeRate=0,available_d,available_cashAmount_d=0.00,fee;

    private UserInfo userInfo;
    private AreaDataResult areaDataResult;
    private List<String> list=new ArrayList<String>();
    private SubscriberOnNextListener getAreasOnNext,withdrawOnNext,getCounterFeeOnNext;
    private String bankRid,bankStaticId="",bankNmae,bankImg,address_name,province="",city="",branchName="",isDefault="",acountId="",realName="",withdrawAmount,safePwd,dynamicCode,googleCode;
    private List<CounterFee> feeInfos=new ArrayList<CounterFee>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_rmb_withdraw);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
        getAreas();
        getCounterFee();
    }

    private void initView() {
        tv_head_title.setText(R.string.asset_rmbtx);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        btn_head_front.setImageResource(R.mipmap.icon_nav_recharge_camera);
        btn_head_front.setVisibility(View.VISIBLE);
        btn_head_front.setOnClickListener(this);
        bnt_commit.setOnClickListener(this);
        rl_xzyh.setOnClickListener(this);
        rl_xzdq.setOnClickListener(this);
        ll_hint.setOnClickListener(this);
        ed_skr.setText(realName);
        ed_txje.setHint(getString(R.string.asset_kyje)+" "+available);
        ed_txje.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    double money=Double.parseDouble(getText(ed_txje));
                    fee=(money*feeRate)< counterFee ? counterFee: (money*feeRate);
                    available_cashAmount_d=money-fee;
                    //available_cashAmount_d=Double.parseDouble(getText(ed_txje))-counterFee;
                    if(available_cashAmount_d<=0){
                        available_cashAmount_d=0.00;
                    }else{
                        available_cashAmount_d=Double.parseDouble(deFormat(available_cashAmount_d));
                    }
                }catch (Exception e){
                    available_cashAmount_d=0.00;
                }
                tv_sjdz.setText(deFormat(available_cashAmount_d));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getCounterFeeOnNext= new SubscriberOnNextListener<VersionResult>() {
            @Override
            public void onNext(VersionResult versionResult) {
                Log.e("versionResult",versionResult.getFeeInfos()+"====");
                //counterFee=Double.parseDouble(cf.getCounterFee());
                if(versionResult.getFeeInfos()!=null){
                    feeInfos=versionResult.getFeeInfos();
                    for(CounterFee cf:feeInfos){
                        counterFee=Double.parseDouble(cf.getCounterFee());
                        feeRate=Double.parseDouble(cf.getFeeRate());
                    }
                }
            }
        };
        getAreasOnNext= new SubscriberOnNextListener<AreaDataResult>() {
            @Override
            public void onNext(AreaDataResult areaDataResult) {
                AreaDataDao.getInstance().add(areaDataResult);
            }
        };
        withdrawOnNext = new SubscriberOnNextListener<CurrencyWithdrawResult>() {
            @Override
            public void onNext(CurrencyWithdrawResult currencyWithdrawResult) {
                Bundle bundle = new Bundle();
                bundle.putString("bankRid",bankRid);
                bundle.putString("bankStaticId",bankStaticId);
                bundle.putString("bankName",bankNmae);
                bundle.putString("province",province);
                bundle.putString("city",city);
                bundle.putString("branchName",branchName);
                bundle.putString("isDefault",isDefault);
                bundle.putString("acountId",acountId);
                bundle.putString("realName",realName);
                bundle.putString("counterFee",deFormat(fee));
                bundle.putString("withdrawAmount",deFormat(withdrawAmount));
                bundle.putString("actualAmount",deFormat(available_cashAmount_d));
                bundle.putString("address_name",address_name);

                bundle.putString("needSafePwd",currencyWithdrawResult.getNeedSafePwd());
                bundle.putString("needMobileCode",currencyWithdrawResult.getNeedMobileCode());
                bundle.putString("needEmailCode",currencyWithdrawResult.getNeedEmailCode());
                bundle.putString("needGoogleCode",currencyWithdrawResult.getNeedGoogleCode());
                UIHelper.showRmbWithdrawConfirm(context,bundle);
            }
        };
    }
    private void initData() {
        userInfo= UserDao.getInstance().getIfon();
        areaDataResult= AreaDataDao.getInstance().getIfon();
        if(!is_token(userInfo)){
            finish();
        }
        realName=userInfo.getRealName();
        Bundle bundle = getIntent().getExtras();
        currencyType=bundle.getString("currencyType");
        available=bundle.getString("available");
        available_d=Double.parseDouble(available);

    }
    private void getCounterFee(){
        HttpMethods.getInstance(3).getCounterFee(new ProgressSubscriber(getCounterFeeOnNext,this),currencyType);
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
        list.add("");//safePwd
        list.add("");//dynamicCode
        list.add("");//googleCode
        HttpMethods.getInstance(3).rmbWithdraw(new ProgressSubscriber(withdrawOnNext, context),list);
    }
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.ll_hint:
                bundle.putString("title",getResources().getString(R.string.user_hlzz));
                bundle.putString("url", SystemConfig.getLevel());
                bundle.putString("type","1");
                UIHelper.showWeb(context,bundle);
                break;
            case R.id.rl_xzdq:
                UIHelper.showAreaAddress(context);
                break;
            case R.id.btn_head_front:
                bundle.putString("type","2");
                UIHelper.showRmbRecord(context,bundle);
                break;
            case R.id.rl_xzyh:
                bundle.putString("bankType","1");
                bundle.putString("type","2");
                UIHelper.showBankAddress(context,bundle);
                break;
            case R.id.bnt_commit:
                acountId=getText(ed_yhkh);
                withdrawAmount=getText(ed_txje);
                branchName=getText(ed_szzh);
                if(bankStaticId.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.safety_xzyh_hint));
                    return;
                }
                if(city.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.safety_xzdq));
                    return;
                }
                if(acountId.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.safety_yhkh_hint_toast));
                    return;
                }
                if(branchName.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.safety_szzh_hint));
                    return;
                }
                if(withdrawAmount.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.asset_recharge_czje_hint_toast));
                    return;
                }
                if(Double.parseDouble(withdrawAmount)>available_d){
                    UIHelper.ToastMessage(context,getString(R.string.asset_yebz_toast));
                    return;
                }
                rmbWithdraw();
                break;
        }

    }
    // 通过 onActivityResult的方法获取 扫描回来的 值
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null&&resultCode == 5288) {
            Bundle bundle = data.getExtras();
            bankRid= bundle.getString("id");
            bankImg= bundle.getString("img");
            bankNmae= bundle.getString("bnankName");
            bankStaticId= bundle.getString("bankStaticId");
            isDefault= bundle.getString("isDefault");

            String acountId_l=bundle.getString("cardNumber");
            if(acountId_l==null){

            }else{
                acountId=acountId_l;
            }
            String city_l=bundle.getString("city");
            String province_l=bundle.getString("province");
            if(city_l==null){

            }else{
                province=province_l;
                city=city_l;
            }
            String branchName_l= bundle.getString("branch");
            if(branchName_l==null){

            }else{
                branchName=branchName_l;
            }

            if(city!=null&&!city.equals("")){
                address_name="";
                try {
                    address_name=address_name+AreaDataDao.getInstance().getAreaIfon(province).getName();
                    address_name=address_name+" "+AreaDataDao.getInstance().getAreaIfon(city).getName();
                }catch (Exception e){

                }

            }

            ed_xzdq.setText(address_name);
            ed_xzyh.setText(bankNmae);
            ed_yhkh.setText(acountId);
            ed_szzh.setText(branchName);

            Picasso.with(context).load(bankImg).into(img_bank);
            Editable etext = ed_yhkh.getText();
            Selection.setSelection(etext, etext.length());
        }
        if (data!=null&&resultCode == 5289) {
            Bundle bundle = data.getExtras();
            address_name= bundle.getString("name");
            province= bundle.getString("parentId");
            city= bundle.getString("id");
            ed_xzdq.setText(address_name);

        }
    }
    private void getAreas() {
        if(areaDataResult==null||(new Date().getTime()-areaDataResult.getVersion()>60*1000*24*17)){
            HttpMethods.getInstance(3).getAreas(new ProgressSubscriber(getAreasOnNext, this),"");
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
