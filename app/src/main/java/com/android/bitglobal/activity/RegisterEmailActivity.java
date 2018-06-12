package com.android.bitglobal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.ObjectResult;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.dao.Login2LnfoDao;
import com.android.bitglobal.entity.Login2Info;
import com.android.bitglobal.entity.LoginResult;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-07-05 16:50
 * 793169940@qq.com
 * 邮箱注册
 */
public class RegisterEmailActivity extends SwipeBackActivity implements View.OnClickListener{
    @BindView(R.id.ed_name)
    EditText ed_name;
    @BindView(R.id.ed_pwd)
    EditText ed_pwd;
    @BindView(R.id.ed_pwd2)
    EditText ed_pwd2;
    @BindView(R.id.tv_forget)
    TextView tv_forget;
    @BindView(R.id.btn_register)
    Button btn_register;
    @BindView(R.id.tv_head_title)
    TextView headTitleText;
    @BindView(R.id.btn_head_back)
    ImageView headBackImage;


    @BindView(R.id.img_eyes_close)
    ImageView img_eyes_close;
    @BindView(R.id.img_eyes_close2)
    ImageView img_eyes_close2;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ll_pwd)
    LinearLayout ll_pwd;
    @BindView(R.id.ll_pwd2)
    LinearLayout ll_pwd2;

    @BindView(R.id.ll_dxyzm)
    LinearLayout ll_dxyzm;
    @BindView(R.id.ed_code)
    EditText ed_code;
    @BindView(R.id.bnt_code)
    Button bnt_code;

    @BindView(R.id.clearEdNameImage)
    ImageView clearEdNameImage;

    public String userName="";		//用户名/手机号/邮箱
    public String mobileCode="";	    //短信验证码
    public String type,method="2";//type=2 是找回密码
    public String pwd="",pwd2="";
    List<String> list=new ArrayList<String>();
    private TimeCount time;
    public static Activity context;
    public static RegisterEmailActivity registerActivity;
    private SubscriberOnNextListener sendCodeOnNext,registerOnNext,changePwdOnNext,checkCodeOnNext;
//    private String dynamicCode="";
    private Pattern pattern = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);
//        tintManager.setStatusBarTintResource(R.color.blue_color);//通知栏所需颜色
        context=this;
        registerActivity=this;
        ButterKnife.bind(this);
        initView();

    }
    private void initView(){
        headTitleText.setText(getString(R.string.user_sign_up));
        headBackImage.setVisibility(View.VISIBLE);
        type=getIntent().getStringExtra("type");
        btn_register.setOnClickListener(this);
        headBackImage.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
        bnt_code.setOnClickListener(this);
        img_eyes_close.setOnClickListener(this);
        img_eyes_close2.setOnClickListener(this);
        clearEdNameImage.setOnClickListener(this);
        headBackImage.setOnClickListener(this);
        img_eyes_close.setVisibility(View.GONE);
        img_eyes_close2.setVisibility(View.GONE);
        ed_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    clearEdNameImage.setVisibility(View.VISIBLE);
                } else {
                    clearEdNameImage.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(type!=null&&type.equals("2")){
            headTitleText.setText(R.string.user_wjmm);
            tv_forget.setText(R.string.user_sjzhmm);
            btn_register.setText(R.string.user_start_now_confirm);
            ll_dxyzm.setVisibility(View.VISIBLE);
            ll_pwd.setVisibility(View.GONE);
            ll_pwd2.setVisibility(View.GONE);
        }else{
            type="1";
        }
        time = new TimeCount(60000, 1000);
        sendCodeOnNext = new SubscriberOnNextListener<ObjectResult>() {
            @Override
            public void onNext(ObjectResult objectResult) {
                time.start();
//                dynamicCode=objectResult.getDynamicCode();
                Login2Info login2Lnfo=objectResult.getLogin2();
                if(login2Lnfo!=null){
                    login2Lnfo.userId=objectResult.getUserId();
                    Login2LnfoDao.getInstance().setIfon(login2Lnfo);
                }
            }
        };
        registerOnNext = new SubscriberOnNextListener<LoginResult>() {
            @Override
            public void onNext(LoginResult loginResult) {
                Bundle bundle = new Bundle();
                bundle.putString("userId",loginResult.getUserId());
                bundle.putString("email",userName);
                UIHelper.showRegisterEmailSuccess(context,bundle);
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
            }
        };
        changePwdOnNext = new SubscriberOnNextListener<LoginResult>() {
            @Override
            public void onNext(LoginResult loginResult) {
                UserInfo user_info=loginResult.getUserInfo();
                user_info.setToken(loginResult.getToken());
                user_info.setIs_online("1");
                UserDao.getInstance().setIfon(user_info);
                MainActivity.getInstance().getfunds();
                UIHelper.ToastMessage(context,R.string.user_dlcg_toast);
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
            }
        };
        checkCodeOnNext = new SubscriberOnNextListener<HttpResult>() {
            @Override
            public void onNext(HttpResult httpResult) {
                String responseCode = httpResult.getResMsg().getCode();
                if(responseCode.equals("1000")) {
                    changePwd();
                } else {
                    UIHelper.ToastMessage(RegisterEmailActivity.this, httpResult.getResMsg().getMessage());
                }
            }
        };

    }


    private void register(){
        list.clear();
        list.add("");//userName
        list.add("");//countryCode
        list.add("");//encryptNumber
        list.add("");//dynamicCode
        list.add(pwd);
        list.add(userName);//email
        HttpMethods.getInstance(3).register(new ProgressSubscriber(registerOnNext, this), list);
    }
    private void changePwd(){
        list.clear();
        list.add(method);
        list.add("");
        list.add("");
        list.add(userName);//email
        list.add(mobileCode);
        list.add("");
        list.add(pwd);
        HttpMethods.getInstance().changePwd(new ProgressSubscriber(changePwdOnNext, this), list);
    }
    private void sendCode(String type){
        list.clear();
        list.add("");
        list.add("");
        list.add(userName);
        list.add(type);
        list.add("");
        HttpMethods.getInstance(3).sendCode(new ProgressSubscriber(sendCodeOnNext, this), list);
    }
    private void checkCode(){
        list.clear();
        list.add("");
        list.add("");
        list.add(mobileCode);
        list.add("16");
        list.add(userName);
        HttpMethods.getInstance(3).checkCodeVersion(new ProgressSubscriber(checkCodeOnNext, this), list);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.tv_forget:
                if(type!=null&&type.equals("2")) {
                    UIHelper.showForgetPwd(context);
                } else {
                    UIHelper.showRegister(this);
                }
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
                userName=getText(ed_name);
                if(userName.equals("")){
                    UIHelper.ToastMessage(context,R.string.user_srndyx_toast);
                    return;
                }
                Matcher matcher=pattern.matcher(userName);
                if(!matcher.matches()){
                    UIHelper.ToastMessage(context,R.string.user_yxyzm_gs_toast);
                    return;
                }
                pwd=getText(ed_pwd);
                pwd2=getText(ed_pwd2);
                mobileCode=getText(ed_code);
                if(type!=null&&type.equals("2")){
                    if(pwd.equals("")){
                        UIHelper.ToastMessage(context,R.string.user_dlmm_hint_toast);
                        return;
                    }
                    if(pwd2.equals("")){
                        UIHelper.ToastMessage(context,R.string.user_dlmm_qr_hint_toast);
                        return;
                    }
                    if(!com.android.bitglobal.tool.Utils.check(pwd))
                    {
                        UIHelper.ToastMessage(context,R.string.user_check_pwd_toast);
                        return;
                    }
                    if(!pwd.equals(pwd2)){
                        UIHelper.ToastMessage(context,R.string.user_lcmmsrbyz_toast);
                        return;
                    }
                    if(mobileCode.equals("")){
                        UIHelper.ToastMessage(context,R.string.user_yxyzm_hint_toast);
                        return;
                    }
//                    String dynamicCode_l= MD5.toMD5(MD5.toMD5(userName) + MD5.toMD5(mobileCode));
//                    if(!dynamicCode_l.equals(dynamicCode)){
//                        UIHelper.ToastMessage(context,R.string.user_yxyzm_cw_hint_toast);
//                        return;
//                    }
//                    UIHelper.showResetEmailPwd(this);
                    checkCode();
                }else{
                    if(pwd.equals("")){
                        UIHelper.ToastMessage(context,R.string.user_dlmm_hint_toast);
                        return;
                    }
                    if(pwd2.equals("")){
                        UIHelper.ToastMessage(context,R.string.user_dlmm_qr_hint_toast);
                        return;
                    }
                    if(!com.android.bitglobal.tool.Utils.check(pwd))
                    {
                        UIHelper.ToastMessage(context,R.string.user_check_pwd_toast);
                        return;
                    }
                    if(!pwd.equals(pwd2)){
                        UIHelper.ToastMessage(context,R.string.user_lcmmsrbyz_toast);
                        return;
                    }
                    register();
                }
                break;
            case R.id.bnt_code:
                userName=ed_name.getText().toString().trim();
                    if(userName.equals("")){
                        if(type!=null&&type.equals("2")) {
                            UIHelper.ToastMessage(context, R.string.user_sremail_hint_toast);
                        }else {
                            UIHelper.ToastMessage(context, R.string.user_sjhm_hint_toast);
                        }
                        return;
                    }
                // 开始计时
                if(type!=null&&type.equals("2")) {
                    sendCode("16");
                }else {
                    sendCode("2");
                }
                break;
            case R.id.clearEdNameImage:
                ed_name.setText("");
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
//            bnt_code.setText(R.string.user_hqyzm);
            bnt_code.setText(R.string.user_send);
            bnt_code.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 计时过程
            bnt_code.setClickable(false);//防止重复点击
            bnt_code.setText(millisUntilFinished / 1000 + getResources().getString(R.string.user_maio));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
