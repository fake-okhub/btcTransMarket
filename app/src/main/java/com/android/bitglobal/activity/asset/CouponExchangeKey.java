package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-08-29 13:10
 * 793169940@qq.com
 * 融资融币-密钥兑换免息劵
 */
public class CouponExchangeKey extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.ed_xmjmy)
    EditText ed_xmjmy;
    @BindView(R.id.tv_zt)
    TextView tv_zt;
    @BindView(R.id.bnt_commit)
    Button bnt_commit;
    private String secret,type="1",safePwd="",pointNumber="";
    private List<String> list=new ArrayList<String>();
    private SubscriberOnNextListener exchangeInterestFreeTicketOnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_coupon_key);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        tv_head_title.setText(getString(R.string.asset_mydh));
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        bnt_commit.setOnClickListener(this);
        tv_zt.setOnClickListener(this);

        exchangeInterestFreeTicketOnNext= new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                UIHelper.ToastMessage(context,getString(R.string.user_czcg_toast));
                finish();
            }
        };
    }
    private void initData() {

    }
    private void exchangeInterestFreeTicket(){
        list.clear();
        list.add(type);
        list.add(secret);
        list.add(pointNumber);
        list.add(safePwd);
        HttpMethods.getInstance(4).exchangeInterestFreeTicket(new ProgressSubscriber(exchangeInterestFreeTicketOnNext, context),list);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.bnt_commit:
                secret=getText(ed_xmjmy);
                if(secret.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.asset_mxj_hint_toast));
                    return;
                }
                exchangeInterestFreeTicket();
                break;
            case R.id.tv_zt:
                if(!AppContext.getInstance().paste(context).equals("")){
                    secret=AppContext.getInstance().paste(context);
                    ed_xmjmy.setText(secret);
                    Editable etext = ed_xmjmy.getText();
                    Selection.setSelection(etext, etext.length());
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

    }
}
