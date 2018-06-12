package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.dao.CurrencySetDao;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.DeviceUtil;
import com.android.bitglobal.tool.StringUtils;
import com.android.bitglobal.R;
import com.android.bitglobal.entity.CurrencySetRealm;
import com.android.bitglobal.entity.CurrencySetResult;
import com.android.bitglobal.entity.LoadRecord;
import com.android.bitglobal.entity.PageResult;
import com.android.bitglobal.entity.TransMarketDepth;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.view.AssetList;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

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
public class LendRecord extends SwipeBackActivity implements View.OnClickListener,SwipyRefreshLayout.OnRefreshListener{
    private Activity context;
    public static LendRecord activity;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.img_head_ico_r)
    ImageView img_head_ico_r;
    @BindView(R.id.ll_head_title)
    LinearLayout ll_head_title;

    @BindView(R.id.tv_no_ts)
    TextView tv_no_ts;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.swipyrefreshlayout)
    SwipyRefreshLayout mSwipyRefreshLayout;
    @BindView(R.id.ll_commit)
    LinearLayout ll_commit;
    @BindView(R.id.bnt_commit)
    Button bnt_commit;

    private AssetList assetList;
    private int pageIndex=1,pageSize=20,sfjz=0;
    private String currencyType,status,title_name,isIn="1",symbol="";
    private QuickAdapter<TransMarketDepth> adapter;
    private List<TransMarketDepth> list_data=new ArrayList<TransMarketDepth>();
    private List<String> list=new ArrayList<String>();
    private CurrencySetResult currencySetResult;
    private SubscriberOnNextListener getLoanRecordsOnNext,fastRepayOnNext;
    private List<LoadRecord> list_data2=new ArrayList<LoadRecord>();
    private QuickAdapter<LoadRecord> adapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_lend_record);
        activity=this;
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
        getLoanRecords();

    }

    private void initView() {
        tv_head_title.setText(title_name);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        //tv_head_title.setOnClickListener(this);
        bnt_commit.setOnClickListener(this);

        ll_head_title.setOnClickListener(this);
        img_head_ico_r.setVisibility(View.VISIBLE);

        mSwipyRefreshLayout.setOnRefreshListener(this);
        assetList=new AssetList(context, DeviceUtil.dp2px(context,45.0f));

        adapter= new QuickAdapter<TransMarketDepth>(context, R.layout.asset_select_list_item,list_data) {
            @Override
            protected void convert(BaseAdapterHelper helper,final TransMarketDepth item) {
                helper.setText(R.id.tv_asset_name,item.getName());
                helper.setOnClickListener(R.id.rl_asset_select, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        status=item.getId();
                        if(status.equals("0")){
                            status="";
                        }
                        title_name=item.getName();
                        tv_head_title.setText(title_name);
                        assetList.dismiss();
                        clearData();
                    }
                });
            }
        };
        assetList.listview.setAdapter(adapter);

        getLoanRecordsOnNext= new SubscriberOnNextListener<PageResult>() {
            @Override
            public void onNext(PageResult pageResult) {
                if(pageIndex==1){
                    list_data2.clear();
                    adapter2.clear();
                }
                if(pageResult!=null&&pageResult.getLoanRecords()!=null&&pageResult.getLoanRecords().size()>0){
                    pageIndex++;
                    list_data2.addAll(pageResult.getLoanRecords());
                    adapter2.replaceAll(list_data2);
                }
                if (pageResult!=null && pageResult.getLoanRecords()!=null&&pageResult.getLoanRecords().size() < 1&&list_data2.size()!=0) {
                    sfjz++;
                }
                if(list_data2.size()>0){
                    tv_no_ts.setVisibility(View.GONE);
                    ll_commit.setVisibility(View.VISIBLE);
                }else{
                    tv_no_ts.setVisibility(View.VISIBLE);
                    ll_commit.setVisibility(View.GONE);
                }

            }
        };

        adapter2 = new QuickAdapter<LoadRecord>(context, R.layout.asset_lend_record_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, final LoadRecord item) {
                final String id=item.getId();
                final String debtAmount=item.getDebtAmount();
                final String interestAmount=item.getInterestAmount();
                final String hasRepay=item.getHasRepay();
                final String amountShouldBeRepay=item.getAmountShouldBeRepay();
                final String remainingPrincipal=item.getRemainingPrincipal();
                final String principal=item.getPrincipal();



                helper.setText(R.id.tv_price,symbol+debtAmount);
                helper.setText(R.id.tv_rate,getText(R.string.asset_rll)+item.getRate()+"%");
                helper.setText(R.id.tv_day,item.getDaysWithRate());
                helper.setText(R.id.tv_accrual,interestAmount);
                helper.setText(R.id.tv_repayment_total,amountShouldBeRepay);
                helper.setText(R.id.tv_time, StringUtils.dateFormat1(item.getSubmitTime()));
                helper.setText(R.id.tv_discount, getString(R.string.asset_wsymxj));
                if(item.getIsUsedTicket().equals("1")){
                    helper.setText(R.id.tv_discount, symbol+item.getAmountWithNoRate()+getString(R.string.asset_mx)+item.getDaysWithNoRate()+getString(R.string.market_t));
                }
                for(TransMarketDepth tmd:list_data){
                    if(tmd.getId().equals(item.getStatus())){
                        helper.setText(R.id.tv_state, tmd.getName());
                    }
                }
                final String status_s=item.getStatus();
                helper.setVisible(R.id.tv_state,false);
                helper.setVisible(R.id.img_state,false);
                if(status_s.equals("2")){
                    helper.setVisible(R.id.img_state,true);
                    helper.setImageResource(R.id.img_state,R.mipmap.icon_repaid);
                }else if(status_s.equals("4")){
                    helper.setVisible(R.id.img_state,true);
                    helper.setImageResource(R.id.img_state,R.mipmap.icon_brokenassets);
                }else{
                    helper.setVisible(R.id.tv_state,true);
                }


                if(status_s.equals("1")||status_s.equals("3")){
                    helper.setText(R.id.tv_repayment_total,item.getAmountShouldBeRepay());
                    helper.setText(R.id.tv_repayment_total_ts,getString(R.string.asset_yhje));
                    helper.setText(R.id.tv_accrual_ts,getString(R.string.asset_whlx));
                }else{
                    helper.setText(R.id.tv_repayment_total,item.getHasRepay());
                    helper.setText(R.id.tv_repayment_total_ts,getString(R.string.asset_yhze));
                    helper.setText(R.id.tv_accrual_ts,getString(R.string.asset_ljlx));
                }

                View view=helper.getView();
                View ll_record=view.findViewById(R.id.ll_record);
                if (status_s.equals("1")||status_s.equals("2")){
                    ll_record.setBackgroundResource(R.drawable.bnt_click_background);
                }else{
                    ll_record.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                ll_record.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        if (status_s.equals("1")){
                            bundle.putString("id", id);
                            bundle.putString("currencyType", currencyType);
                            bundle.putString("debtAmount", debtAmount);
                            bundle.putString("interestAmount", interestAmount);
                            bundle.putString("hasRepay", hasRepay);
                            bundle.putString("amountShouldBeRepay", amountShouldBeRepay);
                            bundle.putString("remainingPrincipal", remainingPrincipal);
                            bundle.putString("principal",principal);
                            bundle.putString("symbol", symbol);
                            UIHelper.showLendRecordDetail((Activity) context,bundle);
                        }else if(status_s.equals("2")){
                            bundle.putString("id", id);
                            bundle.putString("currencyType", currencyType);
                            bundle.putString("debtAmount", debtAmount);
                            bundle.putString("interestAmount", interestAmount);
                            bundle.putString("hasRepay", hasRepay);
                            bundle.putString("symbol", symbol);
                            UIHelper.showLendRepaymentDetail((Activity) context,bundle);
                        }
                    }
                });
            }
        };
        listview.setAdapter(adapter2);
    }
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        currencyType=bundle.getString("currencyType");
        currencySetResult= CurrencySetDao.getInstance().getIfon();
        for(CurrencySetRealm csr:currencySetResult.getCurrencySets()){
            if(csr.getCurrency().equals(currencyType)){
                symbol=csr.getSymbol();
            }
        }
        String record_status[]=getResources().getStringArray(R.array.lend_record_status);
        String record_status_key[]=getResources().getStringArray(R.array.lend_record_status_key);

        int n_length=record_status.length;
        for(int n=0;n<n_length;n++){
            TransMarketDepth mdd=new TransMarketDepth();
            mdd.setId(record_status_key[n]);
            mdd.setName(record_status[n]);
            list_data.add(mdd);
        }
        status=record_status_key[0];
        if(status.equals("0")){
            status="";
        }
        title_name=record_status[0];
    }
    
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.ll_head_title:
                assetList.show();
                break;
            case R.id.bnt_commit:
                bundle.putString("currencyType",currencyType);
                UIHelper.showLendRepaymentQuick(context,bundle);
                break;


        }

    }
    public void clearData(){
        pageIndex=1;
        sfjz=0;
        getLoanRecords();
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
                                if(mSwipyRefreshLayout != null) {
                                    mSwipyRefreshLayout.setRefreshing(false);
                                }
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
        //clearData();
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
