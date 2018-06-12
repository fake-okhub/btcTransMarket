package com.android.bitglobal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.LoginResult;
import com.android.bitglobal.entity.ObjectResult;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.dao.Login2LnfoDao;
import com.android.bitglobal.entity.Login2Info;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-07-05 16:50
 * 793169940@qq.com
 * 注册
 */
public class RegisterActivity extends SwipeBackActivity implements View.OnClickListener{

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ed_name)
    EditText ed_name;
    @BindView(R.id.ed_code)
    EditText ed_code;
    @BindView(R.id.ed_qh)
    TextView ed_qh;
    @BindView(R.id.btn_register)
    Button btn_register;

    @BindView(R.id.tv_forget)
    TextView tv_forget;

    @BindView(R.id.tv_login)
    TextView tv_login;

    @BindView(R.id.use_email_text)
    TextView useEmailText;

    @BindView(R.id.bnt_code)
    Button sendText;
    @BindView(R.id.rl_xzqh)
    RelativeLayout rl_xzqh;

    @BindView(R.id.img_eyes_close)
    ImageView img_eyes_close;
    @BindView(R.id.img_eyes_close2)
    ImageView img_eyes_close2;

    @BindView(R.id.ed_pwd2)
    EditText ed_pwd2;
    @BindView(R.id.ed_pwd)
    EditText ed_pwd;

    @BindView(R.id.linearLayout_pwd)
    LinearLayout mlinearLayout;

    @BindView(R.id.tv_head_title)
    TextView headTitleText;

    @BindView(R.id.btn_head_back)
    ImageView headBackImage;

    @BindView(R.id.clearEdNameImage)
    ImageView clearEdNameImage;

    String pwd,pwd2;
    public String encryptNumber="";		//用户名/手机号/邮箱
    public String mobileCode="";	    //短信验证码
    public String countryCode="+86",countryCode_name="";    //国家码，如果有值时代表是手机登录
    public String type;// //tpye=16  1：注册，2：手机认证  3 ：修改手机认证    5：谷歌认证 7：邮箱认证  8：提币 9：安全设置  10：添加接收地址16：找回登录密码 17：找回资金密码 19：重置资金密码 20：关闭资金密码  63：用户登录 65：异地登录验证 71：APP用户注册 72：APP用户登录  94：手机挂失|
    List<String> list=new ArrayList<String>();
    private TimeCount time;
    public static Activity context;
    public static RegisterActivity registerActivity;
    private SubscriberOnNextListener sendCodeOnNext;
    private SubscriberOnNextListener resetPwdOnNext;
    private SubscriberOnNextListener checkCodeOnNext;
//    private SubscriberOnNextListener validateSmsCodeOnNext;
//    private String dynamicCode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
        sendText.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        img_eyes_close2.setOnClickListener(this);
        img_eyes_close.setOnClickListener(this);
        headBackImage.setOnClickListener(this);
        useEmailText.setOnClickListener(this);
        clearEdNameImage.setOnClickListener(this);
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
        rl_xzqh.setOnClickListener(this);
        mlinearLayout.setVisibility(View.GONE);
        if(type!=null&&type.equals("16")){
            headTitleText.setText(getString(R.string.user_wjmm));
            tv_forget.setVisibility(View.GONE);
//            mlinearLayout.setVisibility(View.GONE);
            btn_register.setText(R.string.user_tjyz);
            useEmailText.setText(R.string.user_dzyjzh);
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

        checkCodeOnNext = new SubscriberOnNextListener<HttpResult>() {
            @Override
            public void onNext(HttpResult httpResult) {
                String responseCode = httpResult.getResMsg().getCode();
                    if(responseCode.equals("1000")) {
                    if(type!=null&&type.equals("16")){
                        UIHelper.showResetPwd(RegisterActivity.this);
                    } else {
                        UIHelper.showRegisterSecond(RegisterActivity.this);
                    }
                } else {
                    UIHelper.ToastMessage(RegisterActivity.this, httpResult.getResMsg().getMessage());
                }
            }
        };

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
               /* if(LoginOrRegister.context!=null){
                    LoginOrRegister.context.finish();
                }*/
                UIHelper.showMainActivity(RegisterActivity.this);
                finish();
            }
        };

//        validateSmsCodeOnNext = new SubscriberOnNextListener<ResMsg>() {
//            @Override
//            public void onNext(ResMsg resMsg) {
//                UIHelper.showRegisterSecond(RegisterActivity.this);
//            }
//        };

    }

    private void sendCode(){
        list.clear();
        list.add(countryCode);
        list.add(encryptNumber);
        list.add("");
        list.add(type);
        HttpMethods.getInstance(3).sendCode(new ProgressSubscriber(sendCodeOnNext, this), list);
    }

    private void checkCode(){
        list.clear();
        list.add(countryCode);
        list.add(encryptNumber);
        list.add(mobileCode);
        list.add(type);
        list.add("");
        HttpMethods.getInstance(3).checkCodeVersion(new ProgressSubscriber(checkCodeOnNext, this), list);
    }

    private void register(){
        list.clear();
        list.add(encryptNumber);
        list.add(countryCode);
        list.add(encryptNumber);
        list.add(mobileCode);
        list.add(pwd);
        list.add("");//email
        HttpMethods.getInstance(3).register(new ProgressSubscriber(resetPwdOnNext, this), list);
    }

//    private void validateSmsCode(){
//        HttpMethods.getInstance(3).validateSmsCode(new ProgressSubscriber(validateSmsCodeOnNext,
//                this),encryptNumber , mobileCode, countryCode);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_login:
                UIHelper.showLogin(this);
                finish();
                break;
            case R.id.tv_forget:
                UIHelper.showRegisterEmail(this);
                finish();
                break;
            case R.id.btn_register:
                encryptNumber=getText(ed_name);
                if(encryptNumber.equals("")){
                    UIHelper.ToastMessage(context,R.string.user_sjhm_hint_toast);
                    return;
                }
                mobileCode=getText(ed_code);
                if(mobileCode.equals("")){
                    UIHelper.ToastMessage(context,R.string.user_dtyzm_toast);
                    return;
                }

//                String dynamicCode_l= MD5.toMD5(MD5.toMD5(encryptNumber) + MD5.toMD5(mobileCode));
//                if(!dynamicCode_l.equals(dynamicCode)){
//                    UIHelper.ToastMessage(context,R.string.user_dxyzm_cw_hint);
//                    return;
//                }

                checkCode();
//                validateSmsCode();

//                if(type!=null&&type.equals("16")){
//                    UIHelper.showResetPwd(this);
//                }else{
//
//                    pwd=getText(ed_pwd);
//                    pwd2=getText(ed_pwd2);
//
//
//                    if(pwd.equals(""))
//                    {
//                        UIHelper.ToastMessage(context,R.string.user_dlmm_hint);
//                        return;
//                    }
//                    if(pwd2.equals(""))
//                    {
//                        UIHelper.ToastMessage(context,R.string.user_dlmm_qr_hint);
//                        return;
//                    }
//                    if(!pwd.equals(pwd2))
//                    {
//                        UIHelper.ToastMessage(context,R.string.user_lcmmsrbyz);
//                        return;
//                    }
//
//                    if(!com.android.bitglobal.tool.Utils.check(pwd))
//                    {
//                        UIHelper.ToastMessage(context,R.string.user_check_pwd);
//                        return;
//                    }
//                    register();
//                     UIHelper.showSetPwd(this);
//                }
                break;
            case R.id.bnt_code:
                   encryptNumber=ed_name.getText().toString().trim();
                    if(encryptNumber.equals("")){
                        UIHelper.ToastMessage(context,R.string.user_sjhm_hint_toast);
                        return;
                    }

                // 开始计时
                sendCode();
                break;
            case R.id.rl_xzqh:
                UIHelper.showCountryActivity(context);
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
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.use_email_text:
                if(type!=null&&type.equals("16")) {
                    UIHelper.showForgetEmailPwd(context);
                } else {
                    UIHelper.showRegisterEmail(this);
                }
                finish();
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
//            sendText.setText(R.string.user_hqyzm);
            sendText.setText(R.string.user_send);
            sendText.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 计时过程
            sendText.setClickable(false);//防止重复点击
            sendText.setText(millisUntilFinished / 1000 + getResources().getString(R.string.user_maio));
        }
    }
    @Override
    // 通过 onActivityResult的方法获取 扫描回来的 值
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null&&resultCode == 5278) {
            Bundle bundle = data.getExtras();
            // bundle.getString("id");
            countryCode_name= bundle.getString("name");
            countryCode=bundle.getString("code");
            ed_qh.setText(countryCode_name+" "+countryCode);
            ed_name.setFocusable(true);
            ed_name.setFocusableInTouchMode(true);
            ed_name.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
