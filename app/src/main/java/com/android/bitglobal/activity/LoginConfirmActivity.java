package com.android.bitglobal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.dao.Login2LnfoDao;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.Login2Info;
import com.android.bitglobal.entity.LoginResult;
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
 * 2016-06-24 16:20
 * 793169940@qq.com
 * 登录验证
 */
public class LoginConfirmActivity extends SwipeBackActivity implements View.OnClickListener{

    private SubscriberOnNextListener loginOnNext;
    @BindView(R.id.ed_name)
    EditText ed_name;
    @BindView(R.id.ed_dxyzm)
    EditText ed_dxyzm;
    @BindView(R.id.ed_yxyzm)
    EditText ed_yxyzm;
    @BindView(R.id.ed_ggyzm)
    EditText ed_ggyzm;

    @BindView(R.id.bnt_dxyzm)
    Button bnt_dxyzm;
    @BindView(R.id.bnt_yxyzm)
    Button bnt_yxyzm;
    @BindView(R.id.bnt_ggyzm)
    Button bnt_ggyzm;

    @BindView(R.id.btn_login)
    Button btn_login;

    @BindView(R.id.ll_dxyzm)
    LinearLayout ll_dxyzm;
    @BindView(R.id.ll_yxyzm)
    LinearLayout ll_yxyzm;
    @BindView(R.id.ll_ggyzm)
    LinearLayout ll_ggyzm;

    @BindView(R.id.tv_title2)
    TextView tv_title2;


    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.tv_head_title)
    TextView headTitleText;



    String userName="";		//用户名/手机号/邮箱
    String password="";       //登录密码（RSA加密）
    String mobileCode="";	    //短信验证码
    String googleCode="";     //谷歌验证码
    String countryCode="";    //国家码，如果有值时代表是手机登录
  //  String type="10";
   String type="65";
    public static Activity context;
    private  List<String> list=new ArrayList<String>();
    private Login2Info login2Lnfo;
    private String message,mobileNumber;
    private TimeCount time;
    private SubscriberOnNextListener sendCodeOnNext;
    private UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        tintManager.setStatusBarTintResource(R.color.main_login_header_bg);//通知栏所需颜色
        setContentView(R.layout.activity_login_confirm);
        ButterKnife.bind(this);
        context=this;
        initData();
        initView();
    }
    private void initData(){
        Bundle bundle = getIntent().getExtras();
        userName=bundle.getString("userName");
        password=bundle.getString("password");
        message=bundle.getString("message");
        mobileNumber=bundle.getString("encryptNumber");
        login2Lnfo= Login2LnfoDao.getInstance().getIfon();

    }
    private void initView(){
        btn_head_back.setVisibility(View.VISIBLE);
        ed_name.setText(userName);
        tv_title2.setText(message);
        headTitleText.setText(R.string.user_aqdldcyz);
        ed_name.setInputType(InputType.TYPE_NULL);
        btn_head_back.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        bnt_dxyzm.setOnClickListener(this);
        bnt_yxyzm.setOnClickListener(this);
        bnt_ggyzm.setOnClickListener(this);

        if(login2Lnfo.getNeedDynamicCode().equals("1")&&mobileNumber!=null&&!mobileNumber.equals("")&&!mobileNumber.equals("null")){
            ll_dxyzm.setVisibility(View.VISIBLE);
        }else if(login2Lnfo.getNeedDynamicCode().equals("1")){
            ll_yxyzm.setVisibility(View.VISIBLE);
        }
        if(login2Lnfo.getNeedGoogleCode().equals("1")){
            ll_ggyzm.setVisibility(View.VISIBLE);
        }

        loginOnNext = new SubscriberOnNextListener<HttpResult<LoginResult>>() {
            @Override
            public void onNext(HttpResult<LoginResult> httpResult) {
                String message=httpResult.getResMsg().getMessage();
                String code=httpResult.getResMsg().getCode();
                LoginResult loginResult= httpResult.getDatas();
                if(loginResult!=null){
                    UserInfo userInfo=loginResult.getUserInfo();
                    String u_token=loginResult.getToken();
                    String is_online="1";
                    if(code.equals("1000")){
                        userInfo.setIs_online(is_online);
                        userInfo.setToken(u_token);
                        UserDao.getInstance().login(userInfo);
                        MainActivity mMainActivity= MainActivity.getInstance();
                        if(mMainActivity!=null)
                        {
                            mMainActivity.getfunds();
                        }

                        UIHelper.showMainActivity(LoginConfirmActivity.this);
                        UIHelper.ToastMessage(LoginConfirmActivity.this, getString(R.string.user_dlcg_toast));
                        finish();
                        if(LoginActivity.context!=null){
                            LoginActivity.context.finish();
                        }
                        if(LoginAbroadActivity.context!=null){
                            LoginAbroadActivity.context.finish();
                        }
                       /* if(LoginOrRegister.context!=null){
                            LoginOrRegister.context.finish();
                        }*/
                    }else{
                        u_token="";
                        is_online="0";
                        userInfo.setIs_online(is_online);
                        userInfo.setToken(u_token);
                        UserDao.getInstance().login(userInfo);
                        UIHelper.ToastMessage(LoginConfirmActivity.this, getString(R.string.user_dlcg_toast));
                        finish();
                    }
                }else{
                    UIHelper.ToastMessage(context,message);
                }
            }

        };
        time = new TimeCount(60000, 1000);
        sendCodeOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                time.start();
            }

        };
    }
    private void login(){
        list.clear();
        list.add(userName);
        list.add(password);
        list.add(mobileCode);
        list.add(googleCode);
        list.add(countryCode);
        HttpMethods.getInstance(3).login(new ProgressSubscriber(loginOnNext,context), list);
    }
    private void userSendCode(){
        list.clear();
        list.add(type);
        HttpMethods.getInstance(3).userSendCode(new ProgressSubscriber(sendCodeOnNext, this), list);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.bnt_dxyzm:
                userSendCode();
                break;
            case R.id.bnt_yxyzm:
                userSendCode();
                break;
            case R.id.bnt_ggyzm:
                if(!AppContext.getInstance().paste(context).equals("")){
                    googleCode=AppContext.getInstance().paste(context);
                    ed_ggyzm.setText(googleCode);
                    Editable etext = ed_ggyzm.getText();
                    Selection.setSelection(etext, etext.length());
                }
                break;
            case R.id.btn_login:
                if(login2Lnfo.getNeedDynamicCode().equals("1")&&mobileNumber!=null&&!mobileNumber.equals("")&&!mobileNumber.equals("null")){
                    mobileCode=getText(ed_dxyzm);
                    if(mobileCode.equals("")){
                        UIHelper.ToastMessage(context,getString(R.string.user_dtyzm_toast));
                        return;
                    }
                }else if(login2Lnfo.getNeedDynamicCode().equals("1")){
                    mobileCode=getText(ed_yxyzm);
                    if(mobileCode.equals("")){
                        UIHelper.ToastMessage(context,getString(R.string.user_yxyzm_hint_toast));
                        return;
                    }
                }
                if(login2Lnfo.getNeedGoogleCode().equals("1")){
                    googleCode=getText(ed_ggyzm);
                    if(googleCode.equals("")){
                        UIHelper.ToastMessage(context,getString(R.string.user_ggyzm_hint_toast));
                        return;
                    }
                }
                login();
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
            if(login2Lnfo.getNeedDynamicCode().equals("1")&&mobileNumber!=null&&!mobileNumber.equals("")&&!mobileNumber.equals("null")){
                bnt_dxyzm.setText(R.string.user_hqyzm);
                bnt_dxyzm.setClickable(true);
            }else if(login2Lnfo.getNeedDynamicCode().equals("1")){
                bnt_yxyzm.setText(R.string.user_hqyzm);
                bnt_yxyzm.setClickable(true);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if(login2Lnfo.getNeedDynamicCode().equals("1")&&mobileNumber!=null&&!mobileNumber.equals("")&&!mobileNumber.equals("null")){
                // 计时过程
                bnt_dxyzm.setClickable(false);//防止重复点击
                bnt_dxyzm.setText(millisUntilFinished / 1000 + getResources().getString(R.string.user_maio));
            }else if(login2Lnfo.getNeedDynamicCode().equals("1")){
                // 计时过程
                bnt_yxyzm.setClickable(false);//防止重复点击
                bnt_yxyzm.setText(millisUntilFinished / 1000 + getResources().getString(R.string.user_maio));
            }

        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        userInfo=UserDao.getInstance().getIfon();
        if(userInfo!=null&&userInfo.getUserName().equals("")){
            UserDao.getInstance().exit();
        }
    }
}
