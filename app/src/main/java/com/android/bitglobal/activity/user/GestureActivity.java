package com.android.bitglobal.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.activity.BaseActivity;
import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.view.lockview.LockPatternUtils;
import com.android.bitglobal.view.lockview.LockPatternView;
import com.android.bitglobal.R;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * xiezuofei
 * 2016-10-08 11:10
 * 793169940@qq.com
 *
 */
public class GestureActivity extends BaseActivity implements View.OnClickListener{
    private Activity context;
    public static GestureActivity activity;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.tv_hint)
    TextView tv_hint;
    @BindView(R.id.lpv_lock)
    LockPatternView lockPatternView;
    //type=1第一次设置，2重置，3验证
    public String type;
    private boolean isFirstSet = true;
    private String user_key ="user_key",pwd_ls="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        tintManager.setStatusBarTintResource(R.color.main_login_header_bg);//通知栏所需颜色
        setContentView(R.layout.user_gesture);
        context=this;
        activity=this;
        ButterKnife.bind(this);
        initView();
        if(type.equals("3")){
            AppContext.getInstance().stopVerify();
        }
    }
    private void initView() {
        Bundle bundle = getIntent().getExtras();
        type=bundle.getString("type");
        if(type.equals("1")||type.equals("2")){
            tv_head_title.setText(R.string.user_ssmmsz);
            btn_head_back.setOnClickListener(this);
            tv_hint.setText(R.string.user_cxszmm);
            if(type.equals("2")){
                tv_head_title.setText(R.string.user_czssmm);
            }
        }else{
            btn_head_back.setVisibility(View.INVISIBLE);
            tv_hint.setText(R.string.user_wjmm);
            tv_hint.setVisibility(View.VISIBLE);
        }
        tv_hint.setOnClickListener(this);
        lockPatternView.setOnPatternListener(new LockPatternView.OnPatternListener() {
            @Override
            public void onPatternStart() {

            }

            @Override
            public void onPatternCleared() {

            }

            @Override
            public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {

            }

            @Override
            public void onPatternDetected(List<LockPatternView.Cell> pattern) {
                Animation shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake);
                if(type.equals("1")||type.equals("2")){
                    if(isFirstSet){
                        if(pattern.size()<4){
                            tv_head_title.setText(getString(R.string.user_zsljsd));
                            tv_head_title.startAnimation(shakeAnimation);
                        }else{
                            pwd_ls=LockPatternUtils.patternToString(pattern);
                            isFirstSet = false;
                            tv_head_title.setText(getString(R.string.user_qzchzmm));
                            tv_head_title.startAnimation(shakeAnimation);
                            tv_hint.setVisibility(View.VISIBLE);
                        }
                    }else{
                        int result = LockPatternUtils.getInstance(context).checkPattern2(pattern,pwd_ls);
                        if (result!= 1) {
                            if(result==0){
                                tv_head_title.setText(getString(R.string.user_yschzbyz));
                                tv_head_title.startAnimation(shakeAnimation);
                            }else{
                                tv_head_title.setText(getString(R.string.user_qszssmm));
                                tv_head_title.startAnimation(shakeAnimation);
                            }
                        } else {
                            UIHelper.ToastMessage(context,getString(R.string.user_ssmmszcg_toast));
                            LockPatternUtils.getInstance(context).saveLockPattern(pattern,user_key);
                            AppContext.getInstance().stopVerify();
                            if(type.equals("1")){
                                SharedPreferences.getInstance().putString(SharedPreferences.KEY_USER_LOCK,"1");
                            }
                            if(GestureHint.activity!=null){
                                GestureHint.activity.finish();
                            }
                            GestureActivity.activity.finish();
                        }
                    }
                }else{
                    int result = LockPatternUtils.getInstance(context).checkPattern(pattern,user_key);
                    if (result!= 1) {
                        if(result==0){
                            tv_head_title.setText(getString(R.string.user_ssmmcw));
                            tv_head_title.startAnimation(shakeAnimation);
                        }else{
                            tv_head_title.setText(getString(R.string.user_qszssmm));
                            tv_head_title.startAnimation(shakeAnimation);
                        }
                    } else {
                        GestureActivity.activity.finish();
                    }
                }
                lockPatternView.clearPattern();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.tv_hint:
                if(type.equals("1")||type.equals("2")){
                    //重置密码
                    tv_hint.setVisibility(View.INVISIBLE);
                    tv_head_title.setText(R.string.user_ssmmsz);
                    isFirstSet = true;
                    if(type.equals("2")){
                        tv_head_title.setText(R.string.user_czssmm);
                    }
                }else{
                    if(GestureHint.activity!=null){
                        GestureHint.activity.finish();
                    }
                    if(GestureSet.activity!=null){
                        GestureSet.activity.finish();
                    }
                    UserDao.getInstance().exit();
                    SharedPreferences.getInstance().putString(SharedPreferences.KEY_USER_LOCK,"0");
                    GestureActivity.activity.finish();
                }
                break;

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
        String user_lock= SharedPreferences.getInstance().getString(SharedPreferences.KEY_USER_LOCK,"");
        if(type.equals("3")&&user_lock.equals("1")){
            AppContext.getInstance().startVerify();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}