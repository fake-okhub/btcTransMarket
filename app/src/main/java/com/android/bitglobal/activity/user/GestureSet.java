package com.android.bitglobal.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.R;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.view.SwitchView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * xiezuofei
 * 2016-10-08 11:10
 * 793169940@qq.com
 *
 */
public class GestureSet extends SwipeBackActivity implements View.OnClickListener{
    public static GestureSet activity;
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;
    @BindView(R.id.rl_czssmm)
    RelativeLayout rl_czssmm;
    @BindView(R.id.ll_czssmm)
    LinearLayout ll_czssmm;
    @BindView(R.id.view_switch_kqssmm)
    SwitchView view_switch_kqssmm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_gesture_set);
        context=this;
        activity=this;
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_head_title.setText(R.string.user_ssmmsz);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        rl_czssmm.setOnClickListener(this);
        String user_lock= SharedPreferences.getInstance().getString(SharedPreferences.KEY_USER_LOCK,"0");
        if(user_lock.equals("0")){
            if(view_switch_kqssmm.isOpened()){
                view_switch_kqssmm.toggleSwitch(false);
                ll_czssmm.setVisibility(View.GONE);
            }
        }else{
            if(!view_switch_kqssmm.isOpened()){
                view_switch_kqssmm.toggleSwitch(true);
                ll_czssmm.setVisibility(View.VISIBLE);
            }
        }
        view_switch_kqssmm.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
                SharedPreferences.getInstance().putString(SharedPreferences.KEY_USER_LOCK,"1");
                ll_czssmm.setVisibility(View.VISIBLE);
                view_switch_kqssmm.toggleSwitch(true);

            }

            @Override
            public void toggleToOff(View view) {
                SharedPreferences.getInstance().putString(SharedPreferences.KEY_USER_LOCK,"0");
                ll_czssmm.setVisibility(View.GONE);
                view_switch_kqssmm.toggleSwitch(false);
            }
        });
    }
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.rl_czssmm:
                bundle.putString("type","2");
                UIHelper.showGestureActivity(context,bundle);
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

    }
}
