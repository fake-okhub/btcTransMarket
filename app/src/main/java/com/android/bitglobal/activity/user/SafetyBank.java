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

import com.android.bitglobal.R;
import com.android.bitglobal.dao.CountryDao;
import com.android.bitglobal.dao.RechargeBankDao;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.CountryResult;
import com.android.bitglobal.entity.IdentityAuthResult;
import com.android.bitglobal.entity.RechargeBank;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-09-07 16:20
 * 793169940@qq.com
 * 实名认证－银行卡信息
 */
public class SafetyBank extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.ed_khyh)
    TextView ed_khyh;
    @BindView(R.id.img_bank)
    ImageView img_bank;

    @BindView(R.id.ed_yhkh)
    EditText ed_yhkh;
    @BindView(R.id.ed_ckr)
    EditText ed_ckr;
    @BindView(R.id.ed_ylsj)
    EditText ed_ylsj;



    @BindView(R.id.bnt_commit)
    Button bnt_commit;

    @BindView(R.id.rl_khyh)
    View rl_khyh;

    private String frontalImg="";//身份证正面照
    private String backImg="";//身份证背面照
    private String loadImg="";//手持身份证照
    private String proofAddressImg="";//住址证明照
    private String bankCardId="";//银行卡号
    private String bankTel="";//银行预留手机号
    private String bankImg="";
    private String bankNmae="";
    private String bankId="";
    private String realName="";//真实姓名
    private String cardId="";//证件号
    private String country="",country_name="";//证件所属国家
    private String type="1";//操作类型 1：保存2：提交审核
    private CountryResult countryResult;
    private UserInfo userInfo;
    List<String> list=new ArrayList<String>();
    private SubscriberOnNextListener depthIdentityAuthOnNext,getIdentityAuthOnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_safety_identity_auth_bank);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
        getIdentityAuthStatus();
    }
    private void initView() {
        tv_head_title.setText(R.string.safety_yhkxx);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        bnt_commit.setOnClickListener(this);
        rl_khyh.setOnClickListener(this);
        depthIdentityAuthOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                finish();
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

                ed_yhkh.setText(bankCardId);
                ed_ckr.setText(realName);
                ed_ylsj.setText(bankTel);
                Editable etext = ed_yhkh.getText();
                Selection.setSelection(etext, etext.length());
                if(bankId!=null&&!bankId.equals("")){
                    RechargeBank rb = RechargeBankDao.getInstance().getIfon(bankId);
                    if(rb==null)
                        return;
                    bankImg=rb.getImg();
                    bankNmae=rb.getName();
                    bankId=rb.getId();
                    ed_khyh.setText(bankNmae);
                    Picasso.with(context).load(bankImg).into(img_bank);
                }

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
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.rl_khyh:
                bundle.putString("bankType","0");
                bundle.putString("type","0");
                UIHelper.showBankAddress(context,bundle);
                break;
            case R.id.bnt_commit:
                realName=getText(ed_ckr);
                bankCardId=getText(ed_yhkh);
                bankTel=getText(ed_ylsj);
                if(bankId==null||bankId.equals("")) {
                    UIHelper.ToastMessage(context,getString(R.string.safety_khyh_hint_toast));
                    return;
                }
                if(realName.equals("")) {
                    UIHelper.ToastMessage(context,getString(R.string.safety_ckr_hint_toast));
                    return;
                }
                if(bankCardId.equals("")) {
                    UIHelper.ToastMessage(context,getString(R.string.safety_yhkh_hint_toast));
                    return;
                }
                if(bankTel.equals("")) {
                    UIHelper.ToastMessage(context,getString(R.string.safety_ylsj_hint));
                    return;
                }
                depthIdentityAuth();
                break;


        }
    }
    @Override
    // 通过 onActivityResult的方法获取 扫描回来的 值
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null&&resultCode == 5288) {
            Bundle bundle = data.getExtras();
            bankImg= bundle.getString("img");
            bankNmae= bundle.getString("bnankName");
            bankId= bundle.getString("bankStaticId");
            ed_khyh.setText(bankNmae);
            Picasso.with(context).load(bankImg).into(img_bank);
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
