package com.android.bitglobal.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.dao.CountryDao;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.IdentityAuthResult;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.entity.Country;
import com.android.bitglobal.entity.CountryResult;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-09-06 16:20
 * 793169940@qq.com
 * 实名认证－基本信息
 */
public class SafetyBasisInfo extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.ed_zjdq)
    TextView ed_zjdq;
    @BindView(R.id.ed_zjxm)
    EditText ed_zjxm;
    @BindView(R.id.ed_zjhm)
    EditText ed_zjhm;
    @BindView(R.id.bnt_commit)
    Button bnt_commit;
    @BindView(R.id.rl_zjdq)
    View rl_zjdq;

    private String frontalImg="";//身份证正面照
    private String backImg="";//身份证背面照
    private String loadImg="";//手持身份证照
    private String proofAddressImg="";//住址证明照
    private String bankCardId="";//银行卡号
    private String bankTel="";//银行预留手机号
    private String bankId="";//银行id
    private String realName="";//真实姓名
    private String cardId="";//证件号
    private String country="+86",country_name="";//证件所属国家
    private String type="1";//操作类型 1：保存2：提交审核
    private CountryResult countryResult;
    private UserInfo userInfo;
    List<String> list=new ArrayList<String>();
    private SubscriberOnNextListener depthIdentityAuthOnNext,getIdentityAuthOnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_safety_identity_auth_basic);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
        getIdentityAuthStatus();
    }
    private void initView() {
        tv_head_title.setText(R.string.safety_grjbxx);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        bnt_commit.setOnClickListener(this);
        rl_zjdq.setOnClickListener(this);
        depthIdentityAuthOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                finish();
                UIHelper.showSafetyPicture(context);
            }
        };
        getIdentityAuthOnNext = new SubscriberOnNextListener<IdentityAuthResult>() {
            @Override
            public void onNext(IdentityAuthResult identityAuthResult) {
                frontalImg=identityAuthResult.getFrontalImg();
                backImg=identityAuthResult.getBackImg();
                loadImg=identityAuthResult.getLoadImg();
                proofAddressImg=identityAuthResult.getProofAddressImg();
                bankCardId=identityAuthResult.getBankCardId();
                bankTel=identityAuthResult.getBankTel();
                bankId=identityAuthResult.getBankId();
                realName=identityAuthResult.getRealName();
                cardId=identityAuthResult.getCardId();
                country=identityAuthResult.getCountry();
                for(Country cu:countryResult.getCountries()){
                    if(country!=null&&!cu.equals("")&&cu.getCode().equals(country)){
                        country_name=cu.getDes();
                        ed_zjdq.setText(country_name);
                    }
                }
                ed_zjxm.setText(realName);
                ed_zjhm.setText(cardId);
                Editable etext = ed_zjxm.getText();
                Selection.setSelection(etext, etext.length());
            }
        };
    }
    private void initData() {
        userInfo= UserDao.getInstance().getIfon();
        if(is_token(userInfo)){

        }else{
            finish();
        }
        countryResult= CountryDao.getInstance().getIfon();
    }
    private void depthIdentityAuth(){
        list.clear();
        list.add(frontalImg);
        list.add(backImg);
        list.add(loadImg);
        list.add(proofAddressImg);
        list.add(bankCardId);
        list.add(bankTel);
        list.add(bankId);
        list.add(realName);
        list.add(cardId);
        list.add(country);
        list.add(type);
        HttpMethods.getInstance(3).depthIdentityAuth(new ProgressSubscriber(depthIdentityAuthOnNext, this), list);
    }
    private void getIdentityAuthStatus(){
        HttpMethods.getInstance(3).getIdentityAuthStatus(new ProgressSubscriber(getIdentityAuthOnNext, this));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.rl_zjdq:
                UIHelper.showCountryActivity(context);
                break;
            case R.id.bnt_commit:
                realName=getText(ed_zjxm);
                cardId=getText(ed_zjhm);
                country_name =getText(ed_zjdq);
                for(Country cu:countryResult.getCountries()){
                    if(!country_name.equals("")&&country_name!=null&&!cu.equals("")&&cu.getDes().equals(country_name)){
                        country=cu.getCode();
                    }
                }
                if(realName.equals("")) {
                    UIHelper.ToastMessage(context,getString(R.string.safety_zjxm_hint));
                    return;
                }
                if(cardId.equals("")) {
                    UIHelper.ToastMessage(context,getString(R.string.safety_zjhm_hint));
                    return;
                }
                depthIdentityAuth();
                break;


        }
    }
    @Override
    // 通过 onActivityResult的方法获取 扫描回来的 值
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null&&resultCode == 5278) {
            Bundle bundle = data.getExtras();
            country= bundle.getString("code");
            country_name= bundle.getString("name");
            //bundle.getString("code");
            ed_zjdq.setText(country_name);
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
