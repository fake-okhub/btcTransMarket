package com.android.bitglobal.activity.user;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.entity.IdentityAuthResult;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-08-06 16:20
 * 793169940@qq.com
 * 实名认证
 */
public class SafetyIdentityAuth extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;


    @BindView(R.id.status_show)
    TextView mStatusShow;
    @BindView(R.id.tv_hint)
    TextView mFailReason;

    @BindView(R.id.bnt_commit)
    Button mBntCommit;




    @BindView(R.id.ll_identity_grzl)
    LinearLayout ll_identity_grzl;
    @BindView(R.id.ll_identity_sfzz)
    LinearLayout ll_identity_sfzz;
    @BindView(R.id.ll_identity_yhkxx)
    LinearLayout ll_identity_yhkxx;
    @BindView(R.id.ll_identity_zzzm)
    LinearLayout ll_identity_zzzm;

    @BindView(R.id.ll_hint)
    LinearLayout ll_identity_hint;


    @BindView(R.id.img_identity_1)
    ImageView img_identity_1;
    @BindView(R.id.img_identity_2)
    ImageView img_identity_2;
    @BindView(R.id.img_identity_3)
    ImageView img_identity_3;
    @BindView(R.id.img_identity_4)
    ImageView img_identity_4;

    private String frontalImg="";//身份证正面照
    private String backImg="";//身份证背面照
    private String loadImg="";//手持身份证照
    private String proofAddressImg="";//住址证明照
    private String bankCardId="";//银行卡号
    private String bankTel="";//银行预留手机号
    private String bankId="";//银行id
    private String realName="";//真实姓名
    private String cardId="";//证件号
    private String country="";//证件所属国家
    private String type="2";//操作类型 1：保存2：提交审核
    private String failReason="";
    private String statusShow="";
    private Resources res;
    private String detailStatus[];
    private UserInfo userInfo;
    List<String> list=new ArrayList<String>();
    private SubscriberOnNextListener depthIdentityAuthOnNext,getIdentityAuthOnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_safety_identity_auth);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();

    }

    private void initView() {
        tv_head_title.setText(R.string.user_gjsmrz);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);


        ll_identity_grzl.setOnClickListener(this);
        ll_identity_sfzz.setOnClickListener(this);
        ll_identity_yhkxx.setOnClickListener(this);
        ll_identity_zzzm.setOnClickListener(this);
        mBntCommit.setOnClickListener(this);

        depthIdentityAuthOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                MainActivity.getInstance().getUserInfo();
                SafetyIdentityAuth.this.finish();
            }
        };
        getIdentityAuthOnNext = new SubscriberOnNextListener<IdentityAuthResult>() {
            @Override
            public void onNext(IdentityAuthResult identityAuthResult) {
                setView(identityAuthResult);
            }
        };
    }

    private void setViewGone(String code)
    {
        //+86表示中国
        if(code.equals("+86"))
        {
            ll_identity_yhkxx.setVisibility(View.VISIBLE);
            ll_identity_zzzm.setVisibility(View.GONE);
        }else
        {
            ll_identity_yhkxx.setVisibility(View.GONE);
            ll_identity_zzzm.setVisibility(View.VISIBLE);
        }
    }
    private void setView(IdentityAuthResult identityAuthResult)
    {
        detailStatus=identityAuthResult.getDetailStatus();
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
        setViewGone(country);
        failReason = identityAuthResult.getFailReason();
        statusShow = identityAuthResult.getStatusShow();
        if(failReason.equals(""))
        {
            ll_identity_hint.setVisibility(View.GONE);
        }else
        {
            ll_identity_hint.setVisibility(View.VISIBLE);
            mFailReason.setText(failReason);
        }
        res= this.getResources();
        setBaseInfo();
        setBasePicture();
        setBaseBank();
        setBaseAddress();
    }

    private void setBaseInfo()
    {
            //detailStatus;//[0,1,0,1]	实名认证详细状态数组,-1：未设置 0:不通过 1通过，下标：0基本资料 1证件照片 2银行卡信息 3住址证明
            if(!cardId.equals("")&&!realName.equals("")&&!country.equals(""))
            {
                if("1".equals(detailStatus[0])){
                    img_identity_1.setImageDrawable(res.getDrawable(R.mipmap.ico_auth_right));
                }
                if("0".equals(detailStatus[0])){
                    img_identity_1.setImageDrawable(res.getDrawable(R.mipmap.ico_auth_alert));
                }
            }
    }
    private void setBasePicture()
    {
        if(!frontalImg.equals("")&&!backImg.equals("")&&!loadImg.equals(""))
        {
            if("1".equals(detailStatus[1])){
                img_identity_2.setImageDrawable(res.getDrawable(R.mipmap.ico_auth_right));
            }
            if("0".equals(detailStatus[1])){
                img_identity_2.setImageDrawable(res.getDrawable(R.mipmap.ico_auth_alert));
            }
        }
    }
    private void setBaseBank()
    {
        if(!bankCardId.equals("")&&!bankTel.equals(""))
        {
            if("1".equals(detailStatus[2])){
                img_identity_3.setImageDrawable(res.getDrawable(R.mipmap.ico_auth_right));
            }
            if("0".equals(detailStatus[2])){
                img_identity_3.setImageDrawable(res.getDrawable(R.mipmap.ico_auth_alert));
            }
        }
    }
    private void setBaseAddress()
    {
        if(!proofAddressImg.equals(""))
        {
            if("1".equals(detailStatus[3])){
                img_identity_4.setImageDrawable(res.getDrawable(R.mipmap.ico_auth_right));
            }
            if("0".equals(detailStatus[3])){
                img_identity_4.setImageDrawable(res.getDrawable(R.mipmap.ico_auth_alert));
            }

        }
    }
    private void initData() {
        userInfo= UserDao.getInstance().getIfon();
        if(is_token(userInfo)){

        }else{
            finish();
        }

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
            case R.id.ll_identity_grzl:
                UIHelper.showSafetyBasisInfo(context);
                break;
            case R.id.ll_identity_sfzz:
                UIHelper.showSafetyPicture(context);
                break;
            case R.id.ll_identity_yhkxx:
                UIHelper.showSafetyBank(context);
                break;
            case R.id.ll_identity_zzzm:
                UIHelper.showAddressProve(context);
                break;
            case R.id.bnt_commit:
                if(country.equals(""))
                {
                    UIHelper.ToastMessage(context,R.string.country_toast);
                    return;
                }
                if(realName.equals(""))
                {
                    UIHelper.ToastMessage(context,R.string.realName_toast);
                    return;
                }
                if(cardId.equals(""))
                {
                    UIHelper.ToastMessage(context,R.string.cardId_toast);
                    return;
                }
                if(loadImg.equals(""))
                {
                    UIHelper.ToastMessage(context,R.string.safety_scsczjz_xz_toast);
                    return;
                }
                if(frontalImg.equals(""))
                {
                    UIHelper.ToastMessage(context,R.string.safety_sczjzmz_xz_toast);
                    return;
                }
                if(backImg.equals(""))
                {
                    UIHelper.ToastMessage(context,R.string.safety_sczjfmz_xz_toast);
                    return;
                }
                if(bankId.equals(""))
                {
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
    public void onResume() {
        super.onResume();
        getIdentityAuthStatus();
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
