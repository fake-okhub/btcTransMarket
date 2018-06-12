package com.android.bitglobal.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.R;
import com.android.bitglobal.dao.Login2LnfoDao;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.Login2Info;
import com.android.bitglobal.entity.LoginResult;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.view.UserSelect;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-06-24 16:20
 * 793169940@qq.com
 * 登录
 */
public class LoginActivity extends SwipeBackActivity implements View.OnClickListener{
    String userName="";		//用户名/手机号/邮箱
    String password="";       //登录密码（RSA加密）
    String mobileCode="";	    //短信验证码
    String googleCode="";     //谷歌验证码
    String countryCode="";    //国家码，如果有值时代表是手机登录
//    String countryCodeName="";    //国家码，如果有值时代表是手机登录
    List<String> list=new ArrayList<String>();
    private SubscriberOnNextListener loginOnNext;
    @BindView(R.id.ed_name)
    EditText ed_name;
    @BindView(R.id.ed_pwd)
    EditText ed_pwd;

    @BindView(R.id.sign_up_text)
    TextView signUpText;

    @BindView(R.id.btn_login)
    Button btn_login;


    @BindView(R.id.tv_forget)
    TextView tv_forget;

//    @BindView(R.id.rl_xzqh)
//    RelativeLayout rl_xzqh;

    @BindView(R.id.img_eyes_close)
    ImageView img_eyes_close;

    @BindView(R.id.tv_head_title)
    TextView headTitleText;

    @BindView(R.id.btn_head_back)
    ImageView headBackImage;

    @BindView(R.id.clearEdNameImage)
    ImageView clearEdNameImage;

    public static Activity context;
    private UserSelect userSelect_forget;
    private UserSelect userSelect_register;
    //tpye=0表示弹窗口点击的是忘记密码，1点击是注册按钮
    private int type=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        tintManager.setStatusBarTintResource(R.color.blue_color);//通知栏所需颜色
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        context=this;
        initView();

    }

    private void initView(){
        headTitleText.setText(getString(R.string.user_login));
        headBackImage.setVisibility(View.VISIBLE);
        userName= SharedPreferences.getInstance().getString(SharedPreferences.KEY_LOGIN_NAME,"");
        ed_name.setText(userName);
        if(!"".equals(userName)) {
            clearEdNameImage.setVisibility(View.VISIBLE);
        }
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
        Editable etext = ed_name.getText();
        Selection.setSelection(etext, etext.length());
        initRegister();
        initForget();
        img_eyes_close.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
//        rl_xzqh.setOnClickListener(this);

        btn_login.setOnClickListener(this);
        signUpText.setOnClickListener(this);
        clearEdNameImage.setOnClickListener(this);
        loginOnNext = new SubscriberOnNextListener<HttpResult<LoginResult>>() {
            @Override
            public void onNext(HttpResult<LoginResult> httpResult) {
                String message=httpResult.getResMsg().getMessage();
                String code=httpResult.getResMsg().getCode();
//                if(!code.equals("1000")) {
//                    UIHelper.ToastMessage(context,message);
//                    return;
//                }
                LoginResult loginResult= httpResult.getDatas();
                if(code.equals("1025")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("userId",loginResult.getUserId());
                    bundle.putString("email",userName);
                    UIHelper.showRegisterEmailSuccess(context,bundle);
                    return;
                }
                if(loginResult!=null){
                    SharedPreferences.getInstance().putString(SharedPreferences.KEY_LOGIN_NAME,userName);
                    UserInfo userInfo=loginResult.getUserInfo();
                    String u_token=loginResult.getToken();
                    String is_online="1";
                    userInfo.setIs_online(is_online);
                    userInfo.setToken(u_token);
                    if(code.equals("1000")){
                        UserDao.getInstance().login(userInfo);
                        MainActivity mMainActivity= MainActivity.getInstance();
                        if(mMainActivity!=null)
                        {
                            mMainActivity.getfunds();
                        }
                        UIHelper.ToastMessage(LoginActivity.this, getString(R.string.user_dlcg_toast));
                        UIHelper.showMainActivity(LoginActivity.this);
                        finish();
                    } else {UserDao.getInstance().login(userInfo);
                        Login2Info login2Lnfo=loginResult.getLogin2();
                        if(login2Lnfo!=null){
                            login2Lnfo.userId=userInfo.getUserId();
                            Bundle bundle = new Bundle();
                            bundle.putString("userName",userName);
                            bundle.putString("password",password);
                            bundle.putString("message",message);
                            bundle.putString("encryptNumber",userInfo.getMobileNumber());
                            Login2LnfoDao.getInstance().setIfon(login2Lnfo);
                            UIHelper.showLoginConfirm(context,bundle);
                        }else{
                            UIHelper.ToastMessage(LoginActivity.this, getString(R.string.user_dlcg_toast));
                            finish();
                        }
                    }
                }else{
                    UIHelper.ToastMessage(context,message);
                }
            }

        };
        headBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
   private void initForget()
    {
        userSelect_forget=new UserSelect(this);
        userSelect_forget.tv_select_title2.setVisibility(View.GONE);
        userSelect_forget.tv_select_title1.setText(getString(R.string.user_xzzhfs));
        userSelect_forget.bnt_select_subject_1.setText(getString(R.string.user_sjzhmm));
        userSelect_forget.bnt_select_subject_2.setText(getString(R.string.user_dzyjzh));
        userSelect_forget.bnt_select_subject_1.setOnClickListener(this);
        userSelect_forget.bnt_select_subject_2.setOnClickListener(this);
    }
    private void initRegister()
    {
        userSelect_register=new UserSelect(this);
        userSelect_register.tv_select_title2.setVisibility(View.GONE);
        userSelect_register.tv_select_title1.setText(getString(R.string.user_xzzcfs));
        userSelect_register.bnt_select_subject_1.setText(getString(R.string.user_sjhzc));
        userSelect_register.bnt_select_subject_2.setText(getString(R.string.user_dzyxzc));
        userSelect_register.bnt_select_subject_1.setOnClickListener(this);
        userSelect_register.bnt_select_subject_2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_up_text:
                //tpye=0表示弹窗口点击的是忘记密码，1点击是注册按钮
                type=1;
                userSelect_register.show();
               // UIHelper.showRegister(this);
                break;
//            case R.id.rl_xzqh:
//                UIHelper.showCountryActivity(context);
//                break;
            case R.id.btn_login:
                userName=getText(ed_name);
                password=getText(ed_pwd);
                if(userName.equals("")){
                    UIHelper.ToastMessage(context,R.string.user_dlzh_hint_toast);
                    return;
                }
                if(password.equals("")){
                    UIHelper.ToastMessage(context,R.string.user_dlmm_hint);
                    return;
                }
                if(MainActivity.dialog != null && MainActivity.dialog.isShowing()) {
                    MainActivity.dialog.dismiss();
                }
                login();
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
            case R.id.tv_forget:
                type=0;
                userSelect_forget.show();
                break;
            case R.id.bnt_select_subject_1:
                if(type==0)
                {
                    userSelect_forget.dismiss();
                    UIHelper.showForgetPwd(context);
                }else
                {
                    userSelect_register.dismiss();
                    UIHelper.showRegister(context);
                }

                break;
            case R.id.bnt_select_subject_2:
                if(type==0)
                {
                    userSelect_forget.dismiss();
                    UIHelper.showForgetEmailPwd(context);
                }else
                {
                    userSelect_register.dismiss();
                    UIHelper.showRegisterEmail(context);
                }

                break;
            case R.id.clearEdNameImage:
                ed_name.setText("");
                break;

        }
    }


//    @Override
//    // 通过 onActivityResult的方法获取 扫描回来的 值
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (data!=null&&resultCode == 5278) {
//            Bundle bundle = data.getExtras();
//            // bundle.getString("id");
//            countryCodeName= bundle.getString("name");
//            countryCode=bundle.getString("code");
//            ed_qh.setText(countryCode_name+" "+countryCode);
//            ed_name.setFocusable(true);
//            ed_name.setFocusableInTouchMode(true);
//            ed_name.requestFocus();
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
