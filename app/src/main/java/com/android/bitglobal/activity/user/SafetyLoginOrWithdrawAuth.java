package com.android.bitglobal.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bitglobal.entity.CurrencyWithdrawResult;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.view.SwitchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-08-06 16:20
 * 793169940@qq.com
 *登录验证
 */
public class SafetyLoginOrWithdrawAuth extends SwipeBackActivity implements View.OnClickListener{
    public static Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.tv_safet_hint)
    TextView tv_safet_hint;


    @BindView(R.id.ll_login_or_withdraw)
    LinearLayout ll_login_or_withdraw;
    @BindView(R.id.ll_trans)
    LinearLayout ll_trans;

    @BindView(R.id.ll_safety_mm4)
    LinearLayout ll_safety_mm4;
    @BindView(R.id.ll_safety_mm1)
    LinearLayout ll_safety_mm1;
    @BindView(R.id.ll_safety_mm3)
    LinearLayout ll_safety_mm3;


    @BindView(R.id.rl_safety_mm1)
    RelativeLayout rl_safety_mm1;
    @BindView(R.id.rl_safety_mm2)
    RelativeLayout rl_safety_mm2;
    @BindView(R.id.rl_safety_mm3)
    RelativeLayout rl_safety_mm3;
    @BindView(R.id.rl_safety_mm4)
    RelativeLayout rl_safety_mm4;



    @BindView(R.id.img_safety_mm1)
    ImageView img_safety_mm1;
    @BindView(R.id.img_safety_mm2)
    ImageView img_safety_mm2;
    @BindView(R.id.img_safety_mm3)
    ImageView img_safety_mm3;
    @BindView(R.id.img_safety_mm4)
    ImageView img_safety_mm4;

    @BindView(R.id.tv_safety_mm1)
    TextView tv_safety_mm1;
    @BindView(R.id.tv_safety_mm2)
    TextView tv_safety_mm2;
    @BindView(R.id.tv_safety_mm3)
    TextView tv_safety_mm3;
    @BindView(R.id.tv_safety_mm4)
    TextView tv_safety_mm4;



    //交易
    @BindView(R.id.rl_safety_yjgb)
    RelativeLayout rl_safety_yjgb;
    @BindView(R.id.rl_safety_szkq)
    RelativeLayout rl_safety_szkq;
    @BindView(R.id.rl_safety_gblss)
    RelativeLayout rl_safety_gblss;

    @BindView(R.id.img_safety_yjgb)
    ImageView img_safety_yjgb;
    @BindView(R.id.img_safety_szkq)
    ImageView img_safety_szkq;
    @BindView(R.id.img_safety_gblss)
    ImageView img_safety_gblss;

    @BindView(R.id.view_switch_jyecyz)
    SwitchView view_switch_jyecyz;

    @BindView(R.id.bnt_commit)
    Button bnt_commit;


    private UserInfo userInfo;
//    private UserConfirm userConfirm;
    private String type="",category="1",safePwd="",dynamicCode="",googleCode="";
    private SubscriberOnNextListener changeAuthOnNext;
    private List<String> list=new ArrayList<String>();
    private String safety_type_str="";
    private String safety_str1,safety_str2,safety_str3,safety_str4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_safety_login_auth);
        context=this;
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        category=bundle.getString("category");
        initData();
        initView();
    }
    private void initView() {
//        userConfirm=new UserConfirm(this);
//        userConfirm.ed_user_safePwd.setVisibility(View.GONE);
//        userConfirm.bnt_user_commit.setOnClickListener(this);
//        userConfirm.tv_user_title1.setText(getString(R.string.user_ggrzsz));
//        userConfirm.tv_user_title2.setText(getString(R.string.safety_ggrzsz));
        if(category.equals("2")){
            ll_login_or_withdraw.setVisibility(View.GONE);
            ll_trans.setVisibility(View.VISIBLE);
            tv_head_title.setText(R.string.user_jyyzsz);
            safety_type_str=getString(R.string.safety_jyyzfs);
            setVisibility2();
        }else{
            ll_login_or_withdraw.setVisibility(View.VISIBLE);
            ll_trans.setVisibility(View.GONE);
            if(category.equals("1")){
                safety_str1=getString(R.string.safety_mmyz);
                safety_str2=getString(R.string.safety_mmyz)+"+"+getString(R.string.safety_ggyz);
                safety_str3=getString(R.string.safety_mmyz)+"+"+getString(R.string.safety_ydyz);
                safety_str4=getString(R.string.safety_mmyz)+"+"+getString(R.string.safety_ydyz)+"+"+getString(R.string.safety_ggyz);
                safety_type_str=getString(R.string.safety_dlyzfs);
                tv_head_title.setText(R.string.user_dlyzsz);
            } else{
                ll_safety_mm4.setVisibility(View.GONE);
                //safety_str1=getString(R.string.safety_mmyz);
                safety_str2=getString(R.string.user_zjmm) + "+" + getString(R.string.safety_ggyz);
                safety_str4="";
                if(userInfo.getMobileNumber()==null||userInfo.getMobileNumber().equals("")){
//                    ll_safety_mm1.setVisibility(View.GONE);
//                    ll_safety_mm3.setVisibility(View.GONE);
                    //邮箱验证
                    safety_str3=getString(R.string.user_zjmm)+"+"+getString(R.string.safety_yxyz)+"+"+getString(R.string.safety_ggyz);
                    safety_str1=getString(R.string.user_zjmm)+"+"+getString(R.string.safety_yxyz);
                }else{
                    //短信验证
                    safety_str3=getString(R.string.user_zjmm)+"+"+getString(R.string.safety_dxyz)+"+"+getString(R.string.safety_ggyz);
                    safety_str1=getString(R.string.user_zjmm)+"+"+getString(R.string.safety_dxyz);
                }
                safety_type_str=getString(R.string.safety_txyzfs);
                tv_head_title.setText(R.string.user_zfyzsz);
            }
            tv_safety_mm1.setText(safety_str1);
            tv_safety_mm2.setText(safety_str2);
            tv_safety_mm3.setText(safety_str3);
            tv_safety_mm4.setText(safety_str4);
            setVisibility();
        }
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        bnt_commit.setOnClickListener(this);
        rl_safety_mm1.setOnClickListener(this);
        rl_safety_mm2.setOnClickListener(this);
        rl_safety_mm3.setOnClickListener(this);
        rl_safety_mm4.setOnClickListener(this);

        //交易
        rl_safety_yjgb.setOnClickListener(this);
        rl_safety_szkq.setOnClickListener(this);
        rl_safety_gblss.setOnClickListener(this);



        String trans_state= SharedPreferences.getInstance().getString("trans_state","0");
        if(trans_state.equals("0")){
            if(view_switch_jyecyz.isOpened()){
                view_switch_jyecyz.toggleSwitch(false);
            }
        }else{
            if(!view_switch_jyecyz.isOpened()){
                view_switch_jyecyz.toggleSwitch(true);
            }
        }
        view_switch_jyecyz.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
                if(!view_switch_jyecyz.isOpened()){
                    view_switch_jyecyz.toggleSwitch(true);
                }
                SharedPreferences.getInstance().putString("trans_state","1");
            }

            @Override
            public void toggleToOff(View view) {
                if(view_switch_jyecyz.isOpened()){
                    view_switch_jyecyz.toggleSwitch(false);
                }
                SharedPreferences.getInstance().putString("trans_state","0");
            }
        });

        changeAuthOnNext = new SubscriberOnNextListener<HttpResult<CurrencyWithdrawResult>>() {
            @Override
            public void onNext(HttpResult<CurrencyWithdrawResult> httpResult) {
                CurrencyWithdrawResult currencyWithdrawResult = httpResult.getDatas();
                if(currencyWithdrawResult==null){
                    MainActivity.getInstance().getUserInfo();
                    finish();
                    UIHelper.ToastMessage(SafetyLoginOrWithdrawAuth.this, R.string.set_successful_toast);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("type",type);
                bundle.putString("category",category);

                bundle.putString("needSafePwd",currencyWithdrawResult.getNeedSafePwd());
                bundle.putString("needMobileCode",currencyWithdrawResult.getNeedMobileCode());
                bundle.putString("needEmailCode",currencyWithdrawResult.getNeedEmailCode());
                bundle.putString("needGoogleCode",currencyWithdrawResult.getNeedGoogleCode());

                UIHelper.showSafetyAuthCommit(context,bundle);
            }
        };
    }

    private void initData() {
        userInfo= UserDao.getInstance().getIfon();
        if(is_token(userInfo)){
            if(category.equals("1")){
                type=userInfo.getLoginAuthenType();
            }else if(category.equals("2")){
                type=userInfo.getTradeAuthenType();
            }else if(category.equals("3")){
                type=userInfo.getWithdrawAuthenType();
            }
        }else{
            finish();
        }
    }

//    private void alertUserConfirmDialog() {
//        View authentificationView = View.inflate(context, R.layout.dialog_google_authentification_confirm, null);
//        TextView titleText = (TextView) authentificationView.findViewById(R.id.title_text);
//        TextView infoText = (TextView) authentificationView.findViewById(R.id.info_text);
//        TextView cancelText = (TextView) authentificationView.findViewById(R.id.cancel_text);
//        TextView confirmText = (TextView) authentificationView.findViewById(R.id.confirm_text);
//
//        titleText.setText(getString(R.string.user_ggrzsz));
//        infoText.setText(getString(R.string.safety_ggrzsz));
//
//        confirmText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        cancelText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        alert(authentificationView, true);
//    }

    private void changeAuth(){
        list.clear();
        list.add(type);
        list.add(category);
        list.add(safePwd);//safePwd
        list.add(dynamicCode);//dynamicCode
        list.add(googleCode);//googleCode
        HttpMethods.getInstance(3).changeAuth(new ProgressSubscriber(changeAuthOnNext,this),list);
    }
    @Override
    public void onClick(View v) {
        userInfo= UserDao.getInstance().getIfon();
        String googleAuth=userInfo.getGoogleAuth();
        switch (v.getId()) {
            case R.id.btn_head_back:
                finish();
                break;
//            case R.id.bnt_user_commit:
//                userConfirm.dismiss();
//                UIHelper.showGoogleAuth(context,"1");
//                break;
            case R.id.rl_safety_mm1:
                type="1";
                setVisibility();
                break;
            case R.id.rl_safety_mm3:
                if(category.equals("3")){
                    if(!"1".equals(googleAuth)){
//                        userConfirm.show();
                        UIHelper.ToastMessage(this, R.string.safety_ggrzsz);
                        return;
                    }
                }
                type="3";
                setVisibility();
                break;
            case R.id.rl_safety_mm2:
                if(!"1".equals(googleAuth)){
//                    userConfirm.show();
                    UIHelper.ToastMessage(this, R.string.safety_ggrzsz);
                    return;
                }
                type="2";
                setVisibility();
                break;
            case R.id.rl_safety_mm4:
                if(!"1".equals(googleAuth)){
//                    userConfirm.show();
                    UIHelper.ToastMessage(this, R.string.safety_ggrzsz);
                    return;
                }
                type="4";
                setVisibility();
                break;
            case R.id.rl_safety_yjgb:
                type="1";
                setVisibility2();
                break;
            case R.id.rl_safety_gblss:
                type="2";
                setVisibility2();
                break;
            case R.id.rl_safety_szkq:
                type="3";
                setVisibility2();
                break;
            case R.id.bnt_commit:
                if(type==null||type.equals("")||type.equals("0")){
                    return;
                }
                changeAuth();
                break;

        }
    }
    private void setVisibility(){
        img_safety_mm1.setVisibility(View.INVISIBLE);
        img_safety_mm2.setVisibility(View.INVISIBLE);
        img_safety_mm3.setVisibility(View.INVISIBLE);
        img_safety_mm4.setVisibility(View.INVISIBLE);
        String safety_str="";

        if("1".equals(type)){
            safety_str=safety_str1;
            img_safety_mm1.setVisibility(View.VISIBLE);
        }
        if("2".equals(type)){
            safety_str=safety_str2;
            img_safety_mm2.setVisibility(View.VISIBLE);
        }
        if("3".equals(type)){
            safety_str=safety_str3;
            img_safety_mm3.setVisibility(View.VISIBLE);
        }
        if("4".equals(type)){
            safety_str=safety_str4;
            img_safety_mm4.setVisibility(View.VISIBLE);
        }
        tv_safet_hint.setText(safety_type_str + safety_str.toUpperCase());
    }
    private void setVisibility2(){
        img_safety_yjgb.setVisibility(View.INVISIBLE);
        img_safety_gblss.setVisibility(View.INVISIBLE);
        img_safety_szkq.setVisibility(View.INVISIBLE);
        String safety_str="";
        if("1".equals(type)){
            safety_str=getString(R.string.safety_yjgb);
            img_safety_yjgb.setVisibility(View.VISIBLE);
        }
        if("2".equals(type)){
            safety_str=getString(R.string.safety_gblss);
            img_safety_gblss.setVisibility(View.VISIBLE);
        }
        if("3".equals(type)){
            safety_str=getString(R.string.safety_szkq);
            img_safety_szkq.setVisibility(View.VISIBLE);
        }
        tv_safet_hint.setText(safety_type_str + safety_str.toUpperCase());
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
