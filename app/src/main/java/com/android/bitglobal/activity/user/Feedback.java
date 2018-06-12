package com.android.bitglobal.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.ResMsg;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-08-12 10:20
 * 793169940@qq.com
 *意见反馈
 */
public class Feedback extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.ed_content)
    EditText mEditTextContent;

    @BindView(R.id.bnt_commit)
    Button mButton;

    private String complainPoint="";
    private SubscriberOnNextListener mTsukkomiOnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_feedback);
        context=this;
        ButterKnife.bind(this);
        //initData();
        initView();
    }
    private void initView() {
        tv_head_title.setText(R.string.user_wytc);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        mButton.setOnClickListener(this);
        mTsukkomiOnNext =new SubscriberOnNextListener<HttpResult>() {
            @Override
            public void onNext(HttpResult o) {
                ResMsg res = o.getResMsg();
                String code=res.getCode();
               if(code.equals(SystemConfig.SUCCESS))
               {
                   UIHelper.ToastMessage(Feedback.this,R.string.feed_suc_toast);
                   finish();
               }
            }
        };
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.bnt_commit:
                submit();
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

    private void submit()
    {
        complainPoint = getText(mEditTextContent);
        if(complainPoint.equals(""))
        {
            UIHelper.ToastMessage(this,R.string.feed_hint_toast);
            return;
        }
        HttpMethods.getInstance(6).complain(new ProgressSubscriber(mTsukkomiOnNext,Feedback.this),complainPoint);
    }

}
