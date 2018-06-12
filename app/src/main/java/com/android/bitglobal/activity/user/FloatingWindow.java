package com.android.bitglobal.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.R;
import com.android.bitglobal.activity.FloatService;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.view.SwitchView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-08-06 16:20
 * 793169940@qq.com
 *浮动窗口
 */
public class FloatingWindow extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.view_switch)
    SwitchView mViewSwitch;
    private boolean isOpened=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_floating_window);
        context=this;
        ButterKnife.bind(this);
        //initData();
        initView();
    }
    private void initView() {
        tv_head_title.setText(R.string.user_xfck);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        isOpened= SharedPreferences.getInstance().getBoolean("is_open_window",false);
        mViewSwitch.setOpened(isOpened);
        mViewSwitch.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
                SharedPreferences.getInstance().putBoolean("is_open_window",true);
                mViewSwitch.setOpened(true);
                Intent intent = new Intent(context,FloatService.class);
                startService(intent);
            }

            @Override
            public void toggleToOff(View view) {
                SharedPreferences.getInstance().putBoolean("is_open_window",false);
                mViewSwitch.setOpened(false);
                Intent intent = new Intent(context,FloatService.class);
                stopService(intent);
            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_head_back:
                finish();
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
