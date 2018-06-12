package com.android.bitglobal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
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
 * 2016-09-01 16:20
 * 793169940@qq.com
 * 海外登录
 */
public class LoginAbroadActivity extends SwipeBackActivity implements View.OnClickListener{
    String userName="";		//用户名/手机号/邮箱
    String password="";       //登录密码（RSA加密）
    String mobileCode="";	    //短信验证码
    String googleCode="";     //谷歌验证码
    String countryCode="+86",countryCode_name="";    //国家码，如果有值时代表是手机登录
    List<String> list=new ArrayList<String>();
    private SubscriberOnNextListener loginOnNext;
    @BindView(R.id.ed_name)
    EditText ed_name;
    @BindView(R.id.ed_pwd)
    EditText ed_pwd;
    @BindView(R.id.ed_qh)
    TextView ed_qh;

    @BindView(R.id.btn_login)
    Button btn_login;

    @BindView(R.id.tv_forget)
    TextView tv_forget;
    @BindView(R.id.tv_abroad)
    TextView tv_abroad;

    @BindView(R.id.rl_xzqh)
    RelativeLayout rl_xzqh;

    @BindView(R.id.img_eyes_close)
    ImageView img_eyes_close;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;
    public static Activity context;
    private UserSelect userSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        tintManager.setStatusBarTintResource(R.color.main_login_header_bg);//通知栏所需颜色
        setContentView(R.layout.activity_login_abroad);
        ButterKnife.bind(this);
        context=this;
        initView();
    }
    private void initView(){
        userSelect=new UserSelect(this);
        userSelect.tv_select_title2.setVisibility(View.GONE);
        userSelect.tv_select_title1.setText(getString(R.string.user_xzzhfs));
        userSelect.bnt_select_subject_1.setText(getString(R.string.user_sjzhmm));
        userSelect.bnt_select_subject_2.setText(getString(R.string.user_yxzhmm));
        userSelect.bnt_select_subject_1.setOnClickListener(this);
        userSelect.bnt_select_subject_2.setOnClickListener(this);

        rl_xzqh.setOnClickListener(this);
        img_eyes_close.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
        tv_abroad.setOnClickListener(this);
        btn_head_back.setOnClickListener(this);
        btn_login.setOnClickListener(this);
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
                    userInfo.setIs_online(is_online);
                    userInfo.setToken(u_token);
                    if(code.equals("1000")){
                        UserDao.getInstance().login(userInfo);
                        MainActivity.getInstance().getfunds();
                        finish();
                        if(LoginActivity.context!=null){
                            LoginActivity.context.finish();
                        }
                      /*  if(LoginOrRegister.context!=null){
                            LoginOrRegister.context.finish();
                        }*/
                    }else{
                        UserDao.getInstance().login(userInfo);
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
                            finish();
                        }
                    }
                }else{
                    UIHelper.ToastMessage(context,message);
                }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.btn_login:
                userName=getText(ed_name);
                password=getText(ed_pwd);
                if(userName.equals("")){
                    UIHelper.ToastMessage(context,R.string.user_sjhm_hint_toast);
                    return;
                }
                if(password.equals("")){
                    UIHelper.ToastMessage(context,R.string.user_dlmm_hint_toast);
                    return;
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
            case R.id.tv_abroad:
                finish();
                break;
            case R.id.rl_xzqh:
                UIHelper.showCountryActivity(context);
                break;
            case R.id.tv_forget:
                userSelect.show();
                break;
            case R.id.bnt_select_subject_1:
                userSelect.dismiss();
                UIHelper.showForgetPwd(context);
                break;
            case R.id.bnt_select_subject_2:
                userSelect.dismiss();
                UIHelper.showForgetEmailPwd(context);
                break;

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
        }
    }
}
