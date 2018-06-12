package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.StringUtils;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.R;
import com.android.bitglobal.entity.WithdrawDetailResult;
import com.android.bitglobal.http.HttpMethods;
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
 * 2016-08-26 18:10
 * 793169940@qq.com
 *充值记录
 */
public class CurrencyWithdrawRecord extends SwipeBackActivity implements View.OnClickListener,SwipyRefreshLayout.OnRefreshListener{
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
    private String currencyType,status="-1";
    private int pageIndex=1,pageSize=20,sfjz=0;
    private List<String> list=new ArrayList<String>();
    private List<com.android.bitglobal.entity.WithdrawDetail> withdrawDetailList=new ArrayList<com.android.bitglobal.entity.WithdrawDetail>();
    private SubscriberOnNextListener searchWithdrawOnNext;
    private QuickAdapter<com.android.bitglobal.entity.WithdrawDetail> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_currency_withdraw_record);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        tv_head_title.setText(getString(R.string.asset_fbjl)+"-"+currencyType);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        mSwipyRefreshLayout.setOnRefreshListener(this);
        adapter = new QuickAdapter<com.android.bitglobal.entity.WithdrawDetail>(context, R.layout.asset_currency_withdraw_record_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, final com.android.bitglobal.entity.WithdrawDetail item) {
                helper.setText(R.id.tv_dw,currencyType);
                helper.setText(R.id.tv_sl,"- "+deFormat(item.getDoubleAmount()));
                if(item.getStatus().equals("3")){
                    helper.setVisible(R.id.tv_zt,true);
                }else{
                    helper.setVisible(R.id.tv_zt,false);
                }
                helper.setText(R.id.tv_zt,item.getShowStat());
                helper.setText(R.id.tv_dz,item.getToAddress());
                helper.setText(R.id.tv_sj, StringUtils.dateFormat5(item.getSubmitTimestamp()));
                helper.setOnClickListener(R.id.ll_asset_record, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("currencyType",currencyType);
                        bundle.putString("id",item.getId());
                        bundle.putString("commandId",item.getCommandId());
                        bundle.putString("doubleAmount",item.getDoubleAmount());
                        bundle.putString("afterAmount",item.getAfterAmount());
                        bundle.putString("toAddress",item.getToAddress());
                        bundle.putString("status",item.getStatus());
                        bundle.putString("showStat",item.getShowStat());
                        bundle.putString("fee",item.getFee());
                        bundle.putString("remark",item.getRemark());
                        bundle.putString("submitTimestamp",item.getSubmitTimestamp());
                        bundle.putString("manageTimestamp",item.getManageTimestamp());
                        UIHelper.showCurrencyWithdrawRecordDetail((Activity) context,bundle);
                    }
                });

            }
        };
        searchWithdrawOnNext = new SubscriberOnNextListener<WithdrawDetailResult>() {
            @Override
            public void onNext(WithdrawDetailResult withdrawDetailResult) {
                if(pageIndex==1){
                    withdrawDetailList.clear();
                    adapter.clear();
                }
                if(withdrawDetailResult!=null&&withdrawDetailResult.getWithdrawDetails().size()>0){
                    pageIndex++;
                    withdrawDetailList.addAll(withdrawDetailResult.getWithdrawDetails());
                    adapter.addAll(withdrawDetailResult.getWithdrawDetails());
                }
                if (withdrawDetailResult!=null && withdrawDetailResult.getWithdrawDetails().size() < 1&&withdrawDetailList.size()!=0) {
                    sfjz++;
                }
                if(withdrawDetailList.size()>0){
                    tv_no_ts.setVisibility(View.GONE);
                }else{
                    tv_no_ts.setVisibility(View.VISIBLE);
                }
            }

        };
        listview.setAdapter(adapter);
    }
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        currencyType=bundle.getString("currencyType");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;

        }

    }
    public void clearData(){
        pageIndex=1;
        sfjz=0;
        searchWithdraw();
    }
    private void searchWithdraw() {
        list.clear();
        list.add(currencyType);
        list.add(status);
        list.add(pageIndex+"");
        list.add(pageSize+"");
        HttpMethods.getInstance(3).searchWithdraw(new ProgressSubscriber(searchWithdrawOnNext, context),list);
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

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if(direction == SwipyRefreshLayoutDirection.TOP){
            clearData();
        }else{
            if(sfjz==0){
                searchWithdraw();
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
}
