package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-08-29 13:10
 * 793169940@qq.com
 * 融资融币-借入记录
 */
public class CouponExchangeIntegral extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.tv_assets_xyjf)
    TextView tv_assets_xyjf;
    @BindView(R.id.ed_dhjf)
    EditText ed_dhjf;
    @BindView(R.id.tv_hint)
    TextView tv_hint;
    @BindView(R.id.ed_zjmm)
    EditText ed_zjmm;

    @BindView(R.id.bnt_commit)
    Button bnt_commit;
    private String symbol="￥",pointNumber,safePwd,type="2",secret="";
    private UserInfo userInfo;
    private Long totalJifen,integral;
    private List<String> list=new ArrayList<String>();
    private SubscriberOnNextListener exchangeInterestFreeTicketOnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_coupon_integral);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();

    }
    private void initData() {
        userInfo= UserDao.getInstance().getIfon();
        if(is_token(userInfo)){
            totalJifen=Long.parseLong(userInfo.getTotalJifen());
        }else{
            finish();
        }

    }
    private void initView() {
        tv_head_title.setText(R.string.asset_jfdh);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        bnt_commit.setOnClickListener(this);
        tv_assets_xyjf.setText(totalJifen+"");
        tv_hint.setText(symbol+"0.00");

        ed_dhjf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    pointNumber=getText(ed_dhjf);
                    integral=Long.parseLong(pointNumber);
                    if(integral>=1000){
                        DecimalFormat df = new DecimalFormat("0.00");
                        tv_hint.setText(symbol+df.format(integral/10.0));
                    }else{
                        tv_hint.setText(symbol+"0.00");
                    }
                }catch (Exception e){
                    tv_hint.setText(symbol+"0.00");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        exchangeInterestFreeTicketOnNext= new SubscriberOnNextListener() {

            @Override
            public void onNext(Object o) {
                MainActivity.getInstance().getUserInfo();
                UIHelper.ToastMessage(context,getString(R.string.user_czcg_toast));
                finish();
            }
        };
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
                pointNumber=getText(ed_dhjf);
                safePwd=getText(ed_zjmm);
                if(pointNumber.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.asset_dhjf_hint_toast));
                    return;
                }
                integral=Long.parseLong(pointNumber);
                if(integral<1000){
                    UIHelper.ToastMessage(context,getString(R.string.asset_yqdq_toast));
                    return;
                }
                if(integral>totalJifen){
                    UIHelper.ToastMessage(context,getString(R.string.asset_jfbz_toast));
                    return;
                }
                if(safePwd.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.user_zjmm_hint_toast));
                    return;
                }
                exchangeInterestFreeTicket();
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
