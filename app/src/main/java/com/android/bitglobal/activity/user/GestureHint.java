package com.android.bitglobal.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.bitglobal.R;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * xiezuofei
 * 2016-10-08 11:10
 * 793169940@qq.com
 *
 */
public class GestureHint extends SwipeBackActivity implements View.OnClickListener{
    public static GestureHint activity;
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;
    @BindView(R.id.bnt_commit)
    Button bnt_commit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_gesture_hint);
        activity=this;
        context=this;
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_head_title.setText(R.string.user_ssmmsz);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        bnt_commit.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.bnt_commit:
                bundle.putString("type","1");
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
