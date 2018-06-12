package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.dao.CurrencySetDao;
import com.android.bitglobal.entity.CurrencySetRealm;
import com.android.bitglobal.entity.CurrencySetResult;
import com.android.bitglobal.entity.InterestFreeTicket;
import com.android.bitglobal.entity.PageResult;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.StringUtils;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.R;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.view.UserSelect;
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
public class CouponActivity extends SwipeBackActivity implements View.OnClickListener,SwipyRefreshLayout.OnRefreshListener{
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

    @BindView(R.id.bnt_commit)
    Button bnt_commit;
    @BindView(R.id.bnt_cancel)
    Button bnt_cancel;

    private String currencyType,status="2|4",symbol="";
    private int pageIndex=1,pageSize=20,sfjz=0;
    private List<String> list=new ArrayList<String>();
    private List<InterestFreeTicket> list_data=new ArrayList<InterestFreeTicket>();
    private SubscriberOnNextListener getInterestFreeTicketsOnNext;
    private CurrencySetResult currencySetResult;
    private QuickAdapter<InterestFreeTicket> adapter;
    private UserSelect userSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_coupon);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
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
    }
    private void initView() {
        userSelect=new UserSelect(this);
        userSelect.bnt_select_subject_1.setOnClickListener(this);
        userSelect.bnt_select_subject_2.setOnClickListener(this);
        userSelect.bnt_select_subject_1.setText(getString(R.string.asset_mydh));
        userSelect.bnt_select_subject_2.setText(getString(R.string.asset_jfdh));
        userSelect.ll_select_subject_2.setVisibility(View.GONE);
        userSelect.tv_select_title1.setText(getString(R.string.asset_dhmxj));
        userSelect.tv_select_title2.setText(getString(R.string.asset_xzdhfs));
        mSwipyRefreshLayout.setOnRefreshListener(this);
        bnt_commit.setOnClickListener(this);
        bnt_cancel.setOnClickListener(this);
        tv_head_title.setText(getString(R.string.asset_mxj));
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        adapter = new QuickAdapter<InterestFreeTicket>(context, R.layout.asset_coupon_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, final InterestFreeTicket item) {
                helper.setText(R.id.tv_type,item.getSource());
                helper.setText(R.id.tv_day,item.getFreePeriod());
                helper.setText(R.id.tv_price,deFormat(item.getAvailable()));
                helper.setText(R.id.tv_time, StringUtils.dateFormat2(item.getExpiryTime()));
                helper.setText(R.id.tv_hb_type,symbol);
                helper.setOnClickListener(R.id.ll_asset_coupon, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = getIntent();
                        intent.putExtra("day",item.getFreePeriod()+getString(R.string.market_t));
                        intent.putExtra("price",symbol+deFormat(item.getAvailable()));
                        intent.putExtra("ticketId",item.getId());
                        setResult(10024,intent);
                        finish();
                    }
                });

            }
        };
        listview.setAdapter(adapter);
        getInterestFreeTicketsOnNext= new SubscriberOnNextListener<PageResult>() {
            @Override
            public void onNext(PageResult pageResult) {
                if(pageIndex==1){
                    list_data.clear();
                    adapter.clear();
                }
                if(pageResult!=null&&pageResult.getTickets()!=null&&pageResult.getTickets().size()>0){
                    pageIndex++;
                    list_data.addAll(pageResult.getTickets());
                    adapter.addAll(pageResult.getTickets());
                }
                if (pageResult!=null && pageResult.getTickets()!=null&&pageResult.getTickets().size() < 1&&list_data.size()!=0) {
                    sfjz++;
                }
                if(list_data.size()>0){
                    tv_no_ts.setVisibility(View.GONE);
                }else{
                    tv_no_ts.setVisibility(View.VISIBLE);
                }

            }
        };
    }

    private void getInterestFreeTickets(){
        list.clear();
        list.add(currencyType);
        list.add(status);
        list.add(pageIndex+"");
        list.add(pageSize+"");
        HttpMethods.getInstance(4).getInterestFreeTickets(new ProgressSubscriber(getInterestFreeTicketsOnNext, context),list);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.bnt_cancel:
                Intent intent = getIntent();
                intent.putExtra("day","");
                intent.putExtra("price","");
                intent.putExtra("ticketId","0");
                setResult(10024,intent);
                finish();
                break;
            case R.id.bnt_commit:
                userSelect.show();
                break;
            case R.id.bnt_select_subject_1:
                userSelect.dismiss();
                UIHelper.showCouponExchangeKey(context);
                break;
            case R.id.bnt_select_subject_2:
                userSelect.dismiss();
                UIHelper.showCouponExchangeIntegral(context);
                break;
        }

    }
    public void clearData(){
        pageIndex=1;
        sfjz=0;
        getInterestFreeTickets();
    }
    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if(direction == SwipyRefreshLayoutDirection.TOP){
            clearData();
        }else{
            if(sfjz==0){
                getInterestFreeTickets();
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
        clearData();
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
