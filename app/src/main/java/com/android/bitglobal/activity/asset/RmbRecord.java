package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.entity.BankTrade;
import com.android.bitglobal.entity.PageResult;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.StringUtils;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.R;
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
 * 2016-07-09 16:10
 * 793169940@qq.com
 *人民币充值提现账单
 */
public class RmbRecord extends SwipeBackActivity implements View.OnClickListener,SwipyRefreshLayout.OnRefreshListener{
    public static RmbRecord context;
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

    private String type,status="",yue="";
    private int pageIndex=1,pageSize=20,sfjz=0;
    private List<String> list=new ArrayList<String>();
    private List<BankTrade> list_data=new ArrayList<BankTrade>();
    private SubscriberOnNextListener getRechargeListOnNext;
    private QuickAdapter<BankTrade> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_records);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
        getRechargeList();
    }

    private void initView() {
        if(type.equals("1")){
            tv_head_title.setText(R.string.asset_recharge_czjl);
        }else{
            tv_head_title.setText(R.string.asset_recharge_txjl);
        }

        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        mSwipyRefreshLayout.setOnRefreshListener(this);

        getRechargeListOnNext= new SubscriberOnNextListener<PageResult>() {
            @Override
            public void onNext(PageResult pageResult) {
                if(pageIndex==1){
                    list_data.clear();
                    adapter.clear();
                }
                List<BankTrade> detailList=pageResult.getBankTrades();
                if(detailList!=null&&detailList.size()>0){
                    pageIndex++;
                }
                if (detailList!=null&&detailList.size()>0){
                    for(BankTrade db:detailList){
                        String yue_l= StringUtils.dateFormat7(db.getDate());
                        if(yue.equals(yue_l)){
                            db.setYue("");
                        }else{
                            db.setYue(yue_l);
                        }
                        list_data.add(db);
                        yue=yue_l;
                    }
                }
                if (detailList!=null && detailList.size() < 1&&list_data.size()!=0) {
                    sfjz++;
                }
                if(list_data.size()>0){
                    tv_no_ts.setVisibility(View.GONE);
                }else{
                    tv_no_ts.setVisibility(View.VISIBLE);
                }
                adapter.replaceAll(list_data);
            }
        };
        adapter = new QuickAdapter<BankTrade>(context, R.layout.asset_record_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, final BankTrade bankTrade) {
                String status_s=bankTrade.getStatus();
                if(status_s.equals("1")||status_s.equals("8")||status_s.equals("10")){
                    helper.setTextColor(R.id.tv_record_zt,Color.parseColor("#E70101"));
                }else{
                    helper.setTextColor(R.id.tv_record_zt,Color.parseColor("#1BA905"));
                }
                if(status_s.equals("0")){
                    helper.setVisible(R.id.tv_record_zt,false);
                }else{
                    helper.setVisible(R.id.tv_record_zt,true);
                }
                if (bankTrade.getType().equals("1")) {
                    helper.setText(R.id.tv_record_sl,"+"+deFormat(bankTrade.getAmount()));
                    helper.setText(R.id.tv_record_yh,bankTrade.getPayBankName()+" ("+bankTrade.getPayBankTailNumber()+")");
                }else{
                    helper.setText(R.id.tv_record_sl,"-"+deFormat(bankTrade.getAmount()));
                    helper.setText(R.id.tv_record_yh,bankTrade.getReceiveBankName()+" ("+bankTrade.getReceiveBankTailNumber()+")");
                }

                helper.setText(R.id.tv_record_zt,bankTrade.getStatusShow());
                helper.setText(R.id.tv_record_sj,StringUtils.dateFormat5(bankTrade.getDate()));
                helper.setText(R.id.tv_record_lsh,"No."+bankTrade.getSerialNumber());
                helper.setOnClickListener(R.id.ll_asset_record, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("bankId",bankTrade.getBankId());
                        bundle.putString("serialNumber",bankTrade.getSerialNumber());
                        bundle.putString("receiveAccount",bankTrade.getReceiveAccount());
                        bundle.putString("payAccount",bankTrade.getPayAccount());
                        bundle.putString("receiveBankName",bankTrade.getReceiveBankName());
                        bundle.putString("amount",bankTrade.getAmount());
                        bundle.putString("actualAmount",bankTrade.getActualAmount());
                        bundle.putString("description",bankTrade.getDescription());
                        bundle.putString("receiver",bankTrade.getReceiver());
                        bundle.putString("payer",bankTrade.getPayer());
                        bundle.putString("status",bankTrade.getStatus());
                        bundle.putString("statusShow",bankTrade.getStatusShow());
                        bundle.putString("fees",bankTrade.getFees());
                        bundle.putString("withdrawType",bankTrade.getWithdrawType());
                        bundle.putString("withdrawTypeShow",bankTrade.getWithdrawTypeShow());
                        bundle.putString("payBankLink",bankTrade.getPayBankLink());
                        if("1".equals(bankTrade.getType())){
                            UIHelper.showRechargeDetail((Activity) context,bundle);
                        }else{
                            UIHelper.showWithdrawDetail((Activity) context,bundle);
                        }

                    }
                });
            }
        };
        listview.setAdapter(adapter);
    }
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        type=bundle.getString("type");
    }
    public void clearData(){
        pageIndex=1;
        sfjz=0;
        getRechargeList();
    }
    public void getRechargeList() {
        list.clear();
        list.add(type);
        list.add(status);
        list.add(pageIndex+"");
        list.add(pageSize+"");
        HttpMethods.getInstance(3).getRechargeList(new ProgressSubscriber(getRechargeListOnNext, context),list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;

        }

    }
    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if(direction == SwipyRefreshLayoutDirection.TOP){
            clearData();
        }else{
            if(sfjz==0){
                getRechargeList();
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
