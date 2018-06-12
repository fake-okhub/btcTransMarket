package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.dao.CurrencySetDao;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.FastRepayResult;
import com.android.bitglobal.entity.UserAcount;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.entity.AssetsBalance;
import com.android.bitglobal.entity.CurrencySetRealm;
import com.android.bitglobal.entity.CurrencySetResult;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.LoadRecord;
import com.android.bitglobal.entity.PageResult;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.view.UserConfirm;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

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
 * 融资融币-一键还款
 */
public class LendRepaymentQuick extends SwipeBackActivity implements View.OnClickListener,SwipyRefreshLayout.OnRefreshListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.tv_no_ts)
    TextView tv_no_ts;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.swipyrefreshlayout)
    SwipyRefreshLayout mSwipyRefreshLayout;

    @BindView(R.id.tv_netAssets)
    TextView tv_netAssets;
    @BindView(R.id.tv_hasRepay)
    TextView tv_hasRepay;
    @BindView(R.id.tv_available)
    TextView tv_available;
    @BindView(R.id.tv_select_all)
    TextView tv_select_all;

    @BindView(R.id.bnt_commit)
    Button bnt_commit;
    @BindView(R.id.ll_commit)
    LinearLayout ll_commit;


    //总金额 还款金额 剩余金额
    private double netAssets_d=0.0,repayment_d=0.0,surplus_d=0.0;
    private String currencyType,status="1",isIn="1",symbol="",netAssets,qbxz="0",safePwd="";
    private int pageIndex=1,pageSize=20,sfjz=0;
    private CurrencySetResult currencySetResult;
    private List<String> list=new ArrayList<String>();
    private SubscriberOnNextListener getLoanRecordsOnNext,fastRepayOnNext;
    private List<LoadRecord> list_data=new ArrayList<LoadRecord>();
    private QuickAdapter<LoadRecord> adapter;
    private JSONArray jsonArray;
    private UserAcount userAcount;
    private UserInfo userInfo;
    private UserConfirm userConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_lend_repayment_quick);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
        initTop();
        getLoanRecords();

    }
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        currencyType=bundle.getString("currencyType");
        userInfo= UserDao.getInstance().getIfon();
        if(is_token(userInfo)){
           // userAcount= UserAcountDao.getInstance().getIfon();
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
        currencySetResult= CurrencySetDao.getInstance().getIfon();
        for(CurrencySetRealm csr:currencySetResult.getCurrencySets()){
            if(csr.getCurrency().equals(currencyType)){
                symbol=csr.getSymbol();
            }
        }
    }
    private void initTop(){
        try {
            netAssets_d=Double.parseDouble(netAssets);
            surplus_d=netAssets_d-repayment_d;
        }catch (Exception e){
            finish();
        }
        tv_netAssets.setText(symbol+deFormat(netAssets_d));
        tv_hasRepay.setText(symbol+deFormat(repayment_d));
        tv_available.setText(symbol+deFormat(surplus_d));
        gtqx_qb();
    }
    private void initView() {
        userConfirm=new UserConfirm(this);
        userConfirm.bnt_user_commit.setOnClickListener(this);
        userConfirm.tv_user_title1.setText(getString(R.string.asset_qrhk));
        userConfirm.tv_user_title2.setText("");

        tv_head_title.setText(getString(R.string.asset_yjhk));
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);

        bnt_commit.setOnClickListener(this);
        tv_select_all.setOnClickListener(this);
        mSwipyRefreshLayout.setOnRefreshListener(this);
        adapter = new QuickAdapter<LoadRecord>(context, R.layout.asset_lend_repayment_quick_item) {

            @Override
            protected void convert(BaseAdapterHelper helper, final LoadRecord item) {
                helper.setText(R.id.tv_price,symbol+item.getDebtAmount());
                helper.setText(R.id.tv_hasRepay,symbol+item.getPrincipal());
                helper.setText(R.id.tv_accrual,symbol+item.getSetect_amount());
                helper.setText(R.id.tv_repayment_total,symbol+item.getAmountShouldBeRepay());

                View view=helper.getView();
                final int position_i=helper.getPosition();
                CheckBox cb_xzhk=(CheckBox)view.findViewById(R.id.cb_xzhk);

                cb_xzhk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        try{
                            String sel="0";
                            if(isChecked==true){
                                sel="1";
                            }else{
                                sel="0";
                            }
                            LoadRecord mi=item;
                            mi.setSelect_status(sel);
                            list_data.set(position_i,mi);
                            refurbish();
                        }catch (Exception e){

                        }

                    }
                });

                if(item.getSelect_status().equals("1")){
                    cb_xzhk.setChecked(true);
                }else{
                    cb_xzhk.setChecked(false);
                }

            }
        };
        listview.setAdapter(adapter);
        fastRepayOnNext= new SubscriberOnNextListener<HttpResult<FastRepayResult>>() {
            @Override
            public void onNext(HttpResult<FastRepayResult> frrhr) {
                UIHelper.ToastMessage(context,frrhr.getResMsg().getMessage());
                if(frrhr.getDatas()!=null&&frrhr.getDatas().getAvailable()!=null){
                    netAssets=frrhr.getDatas().getAvailable();
                    repayment_d=0.00;
                    clearData();
                    initTop();
                    MainActivity.getInstance().getfunds();
                }

            }
        };
        getLoanRecordsOnNext= new SubscriberOnNextListener<PageResult>() {
            @Override
            public void onNext(PageResult pageResult) {
                if(pageIndex==1){
                    list_data.clear();
                    adapter.clear();
                }
                if(pageResult!=null&&pageResult.getLoanRecords()!=null&&pageResult.getLoanRecords().size()>0){
                    pageIndex++;
                    list_data.addAll(pageResult.getLoanRecords());
                    refurbish();
                }
                if (pageResult!=null && pageResult.getLoanRecords()!=null&&pageResult.getLoanRecords().size() < 1&&list_data.size()!=0) {
                    sfjz++;
                }
                if(list_data.size()>0){
                    tv_no_ts.setVisibility(View.GONE);
                    ll_commit.setVisibility(View.VISIBLE);
                }else{
                    tv_no_ts.setVisibility(View.VISIBLE);
                    ll_commit.setVisibility(View.GONE);
                }


            }
        };
    }
    public void  refurbish(){
        repayment_d=0.0;
        initTop();
        try {
            for(int n=0;n<list_data.size();n++){
                LoadRecord lrm=list_data.get(n);
                double amount=Double.parseDouble(lrm.getAmountShouldBeRepay());
                double interestAmount=Double.parseDouble(lrm.getInterestAmount());
                double surplus=0.0;
                String ip="1";
                String ss=lrm.getSelect_status();
                String sr="0";
                if(surplus_d>0.0&&ss.equals("1")){
                    ss="1";
                    if(surplus_d>=amount){
                        repayment_d+=amount;
                        surplus=amount;
                        surplus_d-=surplus;
                        ip="0";
                    }else{
                        if(interestAmount>=surplus_d){
                            ss="0";
                            UIHelper.ToastMessage(context,R.string.asset_yebz_toast);
                        }else{
                            sr=(surplus_d-interestAmount)+"";
                            repayment_d+=surplus_d;
                            surplus=surplus_d;
                            surplus_d-=surplus;
                            ip="1";
                        }
                    }
                }else{
                    ss="0";
                }
                lrm.setSelect_remaining(sr);
                lrm.setSetect_amount(deFormat(surplus));
                lrm.setIsPart(ip);
                lrm.setSelect_status(ss);
                list_data.set(n,lrm);
            }
            adapter.replaceAll(list_data);
            initTop();
        }catch (Exception e){

        }
    }
    private void gtqx_qb(){
        if(repayment_d>0){
            qbxz="0";
            tv_select_all.setText(getString(R.string.user_qx));
        }else{
            qbxz="1";
            tv_select_all.setText(getString(R.string.trans_qb));

        }
    }
    private void getLoanRecords() {
        list.clear();
        list.add(currencyType);
        list.add(status);
        list.add(isIn);
        list.add(pageIndex+"");
        list.add(pageSize+"");
        HttpMethods.getInstance(4).getLoanRecords(new ProgressSubscriber(getLoanRecordsOnNext, context),list);
    }
    private void fastRepay() {
        list.clear();
        list.add(jsonArray.toString());
        list.add(safePwd);
        HttpMethods.getInstance(4).fastRepay(new ProgressSubscriber(fastRepayOnNext, context),list);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.bnt_user_commit:
                safePwd=getText(userConfirm.ed_user_safePwd);
                if(safePwd.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.user_zjmm_hint_toast));
                    return;
                }
                userConfirm.dismiss();
                fastRepay();
                break;
            case R.id.bnt_commit:
                try {
                    jsonArray=new JSONArray();
                    for(int n=0;n<list_data.size();n++){
                        LoadRecord lrm=list_data.get(n);
                        String ss=lrm.getSelect_status();
                        if(ss.equals("1")){
                            JSONObject job=new JSONObject();
                            job.put("id",lrm.getId());
                            if(lrm.getIsPart().equals("0")){
                                job.put("repayAmount",lrm.getRemainingPrincipal());
                            }else{
                                job.put("repayAmount",lrm.getSelect_remaining());
                            }
                            job.put("isPart",lrm.getIsPart());
                            jsonArray.put(job);
                        }
                    }
                    if(jsonArray.length()<1){
                        UIHelper.ToastMessage(context,getString(R.string.asset_yjhk_hint));
                        return;
                    }
                    userConfirm.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_select_all:
                gtqx_qb();
                for(int n=0;n<list_data.size();n++){
                    LoadRecord lrm=list_data.get(n);
                    lrm.setSelect_status(qbxz);
                    list_data.set(n,lrm);
                }
                refurbish();
                break;

        }

    }
    public void clearData(){
        pageIndex=1;
        sfjz=0;
        getLoanRecords();
    }
    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if(direction == SwipyRefreshLayoutDirection.TOP){
            clearData();
        }else{
            if(sfjz==0){
                getLoanRecords();
            }else{
                UIHelper.ToastMessage(context,R.string.trans_wgdsjl_toast);
            }
        }
        try{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Hide the refresh after 2sec
                    if (context== null) {
                        return;
                    }else{
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSwipyRefreshLayout.setRefreshing(false);
                            }
                        });
                    }

                }
            }, 2000);
        }catch (Exception e){

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
