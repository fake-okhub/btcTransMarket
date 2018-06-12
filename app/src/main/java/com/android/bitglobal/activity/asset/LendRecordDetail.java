package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.entity.AssetsBalance;
import com.android.bitglobal.entity.FastRepayResult;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.UserAcount;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
public class LendRecordDetail extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_accrual)
    TextView tv_accrual;
    @BindView(R.id.tv_repayment)
    TextView tv_repayment;
    @BindView(R.id.tv_repayment_total)
    TextView tv_repayment_total;
    @BindView(R.id.ed_ky)
    TextView ed_ky;

    @BindView(R.id.ed_hkje)
    EditText ed_hkje;
    @BindView(R.id.ed_zjmm)
    EditText ed_zjmm;

    @BindView(R.id.tv_total_price)
    TextView tv_total_price;

    @BindView(R.id.bnt_qb)
    Button bnt_qb;
    @BindView(R.id.bnt_commit)
    Button bnt_commit;

    private UserAcount userAcount;
    private UserInfo userInfo;
    private int szkd=0;
    private JSONArray loanRecords;
    private double repayAmount_d=0.00,netAssets_d=0.00,amountShouldBeRepay_d=0.00,remainingPrincipal_d=0.00,interestAmount_d=0.00;
    private String currencyType,symbol="",safePwd,id,netAssets,debtAmount,interestAmount,hasRepay,amountShouldBeRepay,remainingPrincipal,principal,repayAmount;
    private List<String> list=new ArrayList<String>();
    private SubscriberOnNextListener fastRepayOnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_lend_record_detail);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        tv_head_title.setText(getString(R.string.asset_jkxq));
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        bnt_qb.setOnClickListener(this);
        bnt_commit.setOnClickListener(this);

        //账户余额
        ed_ky.setText(symbol+deFormat(netAssets));
        //借入金额
        tv_price.setText(symbol+debtAmount);
        //利息
        tv_accrual.setText(symbol+interestAmount);
        //已还金额
        tv_repayment.setText(symbol+principal);
        //应还总金额
        tv_repayment_total.setText(symbol+amountShouldBeRepay);
        //总还金额
        tv_total_price.setText(symbol+"0.00");

        if(debtAmount.length()>=interestAmount.length()&&debtAmount.length()>=principal.length()&&debtAmount.length()>=amountShouldBeRepay.length()){

            ViewTreeObserver observer = tv_price.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    if(szkd==0){
                        tv_accrual.setWidth(tv_price.getWidth());
                        tv_repayment.setWidth(tv_price.getWidth());
                        tv_repayment_total.setWidth(tv_price.getWidth());
                        szkd++;
                    }
                    return true;
                }
            });
        }
        if(interestAmount.length()>=debtAmount.length()&&interestAmount.length()>=principal.length()&&interestAmount.length()>=amountShouldBeRepay.length()){
            ViewTreeObserver observer = tv_accrual.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    if(szkd==0){
                        tv_price.setWidth(tv_accrual.getWidth());
                        tv_repayment.setWidth(tv_accrual.getWidth());
                        tv_repayment_total.setWidth(tv_accrual.getWidth());
                        szkd++;
                    }
                    return true;
                }
            });
        }
        if(principal.length()>=interestAmount.length()&&principal.length()>=debtAmount.length()&&principal.length()>=amountShouldBeRepay.length()){
            ViewTreeObserver observer = tv_repayment.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    if(szkd==0){
                        tv_price.setWidth(tv_repayment.getWidth());
                        tv_accrual.setWidth(tv_repayment.getWidth());
                        tv_repayment_total.setWidth(tv_repayment.getWidth());
                        szkd++;
                    }
                    return true;
                }
            });
        }
        if(amountShouldBeRepay.length()>=interestAmount.length()&&amountShouldBeRepay.length()>=principal.length()&&amountShouldBeRepay.length()>=debtAmount.length()){
            ViewTreeObserver observer = tv_repayment_total.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    if(szkd==0){
                        tv_price.setWidth(tv_repayment_total.getWidth());
                        tv_accrual.setWidth(tv_repayment_total.getWidth());
                        tv_repayment.setWidth(tv_repayment_total.getWidth());
                        szkd++;
                    }
                    return true;
                }
            });
        }


        ed_hkje.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    repayAmount=getText(ed_hkje);
                    repayAmount_d=Double.parseDouble(repayAmount);
                    if(repayAmount_d>remainingPrincipal_d){
                        repayAmount_d=remainingPrincipal_d;
                        ed_hkje.setText(remainingPrincipal);
                    }
                    tv_total_price.setText(symbol+deFormat(repayAmount_d+interestAmount_d));
                    Editable etext = ed_hkje.getText();
                    Selection.setSelection(etext, etext.length());
                }catch (Exception e){
                    tv_total_price.setText(symbol+"0.00");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fastRepayOnNext= new SubscriberOnNextListener<HttpResult<FastRepayResult>>() {
            @Override
            public void onNext(HttpResult<FastRepayResult> frrhr) {
                UIHelper.ToastMessage(context,frrhr.getResMsg().getMessage());
                if(frrhr.getDatas()!=null&&frrhr.getDatas().getAvailable()!=null){
                    netAssets=frrhr.getDatas().getAvailable();
                    netAssets_d=Double.parseDouble(netAssets);
                    MainActivity.getInstance().getfunds();
                }
                if(frrhr.getResMsg().getCode().equals("1000")){
                    LendRecord.activity.clearData();
                    finish();
                }

            }
        };
    }

    private void initData() {

        Bundle bundle = getIntent().getExtras();
        currencyType=bundle.getString("currencyType");

        id=bundle.getString("id");
        debtAmount=bundle.getString("debtAmount");
        interestAmount=bundle.getString("interestAmount");
        hasRepay=bundle.getString("hasRepay");
        amountShouldBeRepay=bundle.getString("amountShouldBeRepay");
        remainingPrincipal=bundle.getString("remainingPrincipal");
        principal=bundle.getString("principal");
        symbol=bundle.getString("symbol");


        userInfo= UserDao.getInstance().getIfon();
        if(is_token(userInfo)){
            //userAcount= UserAcountDao.getInstance().getIfon();
            if(userAcount==null){
                finish();
            }
            for(AssetsBalance ab:userAcount.getBalances()){
                if(ab.getCurrencyType().equals(currencyType)){
                    netAssets=ab.getAvailable();
                }
            }
        }else{
            finish();
        }

        try {
            netAssets_d=Double.parseDouble(netAssets);
            amountShouldBeRepay_d=Double.parseDouble(amountShouldBeRepay);
            remainingPrincipal_d=Double.parseDouble(remainingPrincipal);
            interestAmount_d=Double.parseDouble(interestAmount);
        }catch (Exception e){

        }




    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.bnt_qb:
                if(remainingPrincipal_d>=amountShouldBeRepay_d){
                    ed_hkje.setText(amountShouldBeRepay);
                }else{
                    ed_hkje.setText(remainingPrincipal);
                }
                Editable etext = ed_hkje.getText();
                Selection.setSelection(etext, etext.length());
                break;
            case R.id.bnt_commit:
                repayAmount=getText(ed_hkje);
                if(repayAmount.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.asset_hkje_hint_toast));
                    return;
                }
                try{
                    repayAmount_d=Double.parseDouble(repayAmount);
                    if(repayAmount_d>netAssets_d||(repayAmount_d+interestAmount_d)>netAssets_d){
                        UIHelper.ToastMessage(context,getString(R.string.asset_yebz_toast));
                        return;
                    }
                }catch (Exception e){

                }
                safePwd=getText(ed_zjmm);
                if(safePwd.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.user_zjmm_hint_toast));
                    return;
                }
                JSONObject job=new JSONObject();
                try {
                    job.put("id",id);
                    job.put("isPart","1");
                    job.put("repayAmount",repayAmount);
                    loanRecords=new JSONArray();
                    loanRecords.put(job);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                fastRepay();
                break;
        }

    }
    private void fastRepay() {
        list.clear();
        list.add(loanRecords.toString());
        list.add(safePwd);
        HttpMethods.getInstance(4).fastRepay(new ProgressSubscriber(fastRepayOnNext, context),list);
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
