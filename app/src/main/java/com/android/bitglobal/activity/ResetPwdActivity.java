package com.android.bitglobal.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.tool.Utils;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.R;
import com.android.bitglobal.dao.Login2LnfoDao;
import com.android.bitglobal.entity.Login2Info;
import com.android.bitglobal.entity.LoginResult;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-06-24 16:20
 * 793169940@qq.com
 * 设置密码
 */
public class ResetPwdActivity extends SwipeBackActivity implements View.OnClickListener{

    @BindView(R.id.ed_pwd)
    EditText ed_pwd;
    @BindView(R.id.ed_pwd2)
    EditText ed_pwd2;
    @BindView(R.id.btn_register)
    Button btn_register;

    @BindView(R.id.img_eyes_close)
    ImageView img_eyes_close;
    @BindView(R.id.img_eyes_close2)
    ImageView img_eyes_close2;
    @BindView(R.id.tv_head_title)
    TextView headTitleText;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.ll_ggyzm)
    LinearLayout ll_ggyzm;
    @BindView(R.id.ed_ggyzm)
    EditText ed_ggyzm;
    @BindView(R.id.bnt_ggyzm)
    Button bnt_ggyzm;

    String countryCode="", encryptNumber ="",mobileCode="",password="",googleCode="",email="";
    List<String> list=new ArrayList<String>();
    String type,method="1";//type=2 是找回密码,method=1手机找回
    String pwd,pwd2;
    Context mContext;
    private Login2Info login2Lnfo;
    private SubscriberOnNextListener resetPwdOnNext;
    private int sfxsgg=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        tintManager.setStatusBarTintResource(R.color.blue_color);//通知栏所需颜色
        setContentView(R.layout.activity_reset_pwd);
        mContext=this;
        ButterKnife.bind(this);
        initView();

    }
    private void initView(){
        headTitleText.setText(getString(R.string.user_wjdlmm));
        btn_head_back.setVisibility(View.VISIBLE);
        login2Lnfo= Login2LnfoDao.getInstance().getIfon();
        bnt_ggyzm.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_head_back.setOnClickListener(this);
        img_eyes_close.setOnClickListener(this);
        img_eyes_close2.setOnClickListener(this);
        img_eyes_close.setVisibility(View.GONE);
        img_eyes_close2.setVisibility(View.GONE);
        type=getIntent().getStringExtra("type");
        if(type==null||type.equals("2")){
            countryCode=RegisterActivity.registerActivity.countryCode;
            mobileCode=RegisterActivity.registerActivity.mobileCode;
            encryptNumber =RegisterActivity.registerActivity.encryptNumber;
            method="1";
        }else if(type==null||type.equals("1")){
            headTitleText.setText(R.string.user_dlmmsz);
            btn_register.setText(R.string.user_qr);
            countryCode=RegisterActivity.registerActivity.countryCode;
            mobileCode=RegisterActivity.registerActivity.mobileCode;
            encryptNumber =RegisterActivity.registerActivity.encryptNumber;
        }else{
            email =RegisterEmailActivity.registerActivity.userName;
            mobileCode =RegisterEmailActivity.registerActivity.mobileCode;
            method="2";
        }
        if(type!=null&&(type.equals("2")||type.equals("3"))){
            btn_register.setText(R.string.user_czmm);
            if(login2Lnfo.getNeedGoogleCode().equals("1")){
                ll_ggyzm.setVisibility(View.VISIBLE);
                sfxsgg=1;
            }
            type="2";
        }else{
            type="1";
        }
        resetPwdOnNext = new SubscriberOnNextListener<LoginResult>() {
            @Override
            public void onNext(LoginResult loginResult) {
                UserInfo user_info=loginResult.getUserInfo();
                user_info.setToken(loginResult.getToken());
                user_info.setIs_online("1");
                UserDao.getInstance().setIfon(user_info);

                MainActivity mMainActivity= MainActivity.getInstance();
                if(mMainActivity!=null)
                {
                    mMainActivity.getfunds();
                }

                UIHelper.showMainActivity(ResetPwdActivity.this);
                if(type==null||type.equals("2")){
                    UIHelper.ToastMessage(mContext,R.string.user_xgcg);
                } else {
                    SharedPreferences.getInstance().putString(SharedPreferences.KEY_LOGIN_NAME, encryptNumber);
                    UIHelper.ToastMessage(mContext,R.string.user_dlcg_toast);
                }

                if(RegisterActivity.context!=null){
                    RegisterActivity.context.finish();
                }
                if(RegisterEmailActivity.context!=null){
                    RegisterEmailActivity.context.finish();
                }
                if(LoginActivity.context!=null){
                    LoginActivity.context.finish();
                }
                if(LoginAbroadActivity.context!=null){
                    LoginAbroadActivity.context.finish();
                }
            /*    if(LoginOrRegister.context!=null){
                    LoginOrRegister.context.finish();
                }*/
                finish();
            }
        };

    }
    private void register(){
        list.clear();
        list.add(encryptNumber);
        list.add(countryCode);
        list.add(encryptNumber);
        list.add(mobileCode);
        list.add(password);
        list.add("");//email
        HttpMethods.getInstance().register(new ProgressSubscriber(resetPwdOnNext, this), list);
    }
    private void changePwd(){
        list.clear();
        list.add(method);
        list.add(countryCode);
        list.add(encryptNumber);
        list.add(email);//email
        list.add(mobileCode);
        list.add(googleCode);
        list.add(password);
        HttpMethods.getInstance().changePwd(new ProgressSubscriber(resetPwdOnNext, this), list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.img_eyes_close:
                if(ed_pwd.getInputType()==129){
                    ed_pwd.setInputType(1);
                    img_eyes_close.setImageResource(R.mipmap.ico_eyes_open);
                }else{
                    ed_pwd.setInputType(129);
                    img_eyes_close.setImageResource(R.mipmap.ico_eyes_close);
                }
                Editable etext = ed_pwd.getText();
                Selection.setSelection(etext, etext.length());
                break;
            case R.id.img_eyes_close2:
                if(ed_pwd2.getInputType()==129){
                    ed_pwd2.setInputType(1);
                    img_eyes_close2.setImageResource(R.mipmap.ico_eyes_open);
                }else{
                    ed_pwd2.setInputType(129);
                    img_eyes_close2.setImageResource(R.mipmap.ico_eyes_close);
                }
                Editable etext2 = ed_pwd2.getText();
                Selection.setSelection(etext2, etext2.length());
                break;
            case R.id.btn_register:
                pwd=getText(ed_pwd);
                pwd2=getText(ed_pwd2);
                googleCode=getText(ed_ggyzm);
                if(pwd.equals("")){
//                    if(type!=null&&(type.equals("2")||type.equals("3"))){
                    if(type!=null&&(!type.equals("2")&&!type.equals("3"))){
                        UIHelper.ToastMessage(mContext,R.string.user_dlmm_hint_toast);
                    }else{
                        UIHelper.ToastMessage(mContext,R.string.user_qsrxdmm);
                    }
                    return;
                }
                if(pwd2.equals("")){
                    UIHelper.ToastMessage(mContext,R.string.user_dlmm_hint_toast);
                    return;
                }
                if(!Utils.check(pwd)) {
                    UIHelper.ToastMessage(this, R.string.user_check_pwd_toast);
                    return;
                }
                if(!pwd.equals(pwd2)){
                    UIHelper.ToastMessage(mContext,R.string.user_lcmmsrbyz_toast);
                    return;
                }
                if(sfxsgg==1&&googleCode.equals("")){
                    UIHelper.ToastMessage(mContext,R.string.user_ggyzm_hint_toast);
                    return;
                }
                password=pwd;
                if(type!=null&&(type.equals("2")||type.equals("3"))){
                    changePwd();
                } else{
                    register();
                }
                break;
            case R.id.bnt_ggyzm:
                if(!AppContext.getInstance().paste(mContext).equals("")){
                    googleCode=AppContext.getInstance().paste(mContext);
                    ed_ggyzm.setText(googleCode);
                    Editable etext3 = ed_ggyzm.getText();
                    Selection.setSelection(etext3, etext3.length());
                }
                break;
        }
    }

}
