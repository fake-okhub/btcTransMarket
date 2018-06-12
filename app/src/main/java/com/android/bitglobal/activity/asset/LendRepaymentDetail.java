package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.StringUtils;
import com.android.bitglobal.R;
import com.android.bitglobal.entity.PageResult;
import com.android.bitglobal.entity.RepayRecord;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
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
 * 融资融币-还款详情
 */
public class LendRepaymentDetail extends SwipeBackActivity implements View.OnClickListener,SwipyRefreshLayout.OnRefreshListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.tv_jkje)
    TextView tv_jkje;
    @BindView(R.id.tv_yhje)
    TextView tv_yhje;
    @BindView(R.id.tv_ljlx)
    TextView tv_ljlx;

    @BindView(R.id.tv_no_ts)
    TextView tv_no_ts;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.swipyrefreshlayout)
    SwipyRefreshLayout mSwipyRefreshLayout;

    private List<String> list=new ArrayList<String>();
    private SubscriberOnNextListener getRepayRecordsOnNext;
    private List<RepayRecord> list_data=new ArrayList<RepayRecord>();
    private QuickAdapter<RepayRecord> adapter;
    private String id,symbol,currencyType,debtAmount,interestAmount,hasRepay;
    private int pageIndex=1,pageSize=20,sfjz=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_lend_repayment_detail);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
        getRepayRecords();
    }

    private void initView() {
        tv_head_title.setText(getText(R.string.asset_hkxq));
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);

        tv_jkje.setText(symbol+debtAmount);
        tv_yhje.setText(symbol+hasRepay);
        tv_ljlx.setText(symbol+interestAmount);

        mSwipyRefreshLayout.setOnRefreshListener(this);

        getRepayRecordsOnNext= new SubscriberOnNextListener<PageResult>() {
            @Override
            public void onNext(PageResult pageResult) {
                if(pageIndex==1){
                    list_data.clear();
                    adapter.clear();
                }
                if(pageResult!=null&&pageResult.getRepayRecords()!=null&&pageResult.getRepayRecords().size()>0){
                    pageIndex++;
                    list_data.addAll(pageResult.getRepayRecords());
                    adapter.replaceAll(list_data);
                }
                if (pageResult!=null && pageResult.getRepayRecords()!=null&&pageResult.getRepayRecords().size() < 1&&list_data.size()!=0) {
                    sfjz++;
                }
                if(list_data.size()>0){
                    tv_no_ts.setVisibility(View.GONE);
                }else{
                    tv_no_ts.setVisibility(View.VISIBLE);
                }

            }
        };
        adapter = new QuickAdapter<RepayRecord>(context, R.layout.asset_lend_repayment_detail_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, final RepayRecord item) {
                helper.setText(R.id.tv_price,item.getPrincipalAndInterest());
                helper.setText(R.id.tv_time, StringUtils.dateFormat1(item.getRepayTime()));
            }
        };
        listview.setAdapter(adapter);
    }
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        currencyType=bundle.getString("currencyType");
        symbol=bundle.getString("symbol");
        id=bundle.getString("id");
        debtAmount=bundle.getString("debtAmount");
        interestAmount=bundle.getString("interestAmount");
        hasRepay=bundle.getString("hasRepay");
    }
    private void getRepayRecords(){
        list.clear();
        list.add(id);
        list.add(pageIndex+"");
        list.add(pageSize+"");
        HttpMethods.getInstance(4).getRepayRecords(new ProgressSubscriber(getRepayRecordsOnNext, context),list);
    }
    public void clearData(){
        pageIndex=1;
        sfjz=0;
        getRepayRecords();
    }
    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if(direction == SwipyRefreshLayoutDirection.TOP){
            clearData();
        }else{
            if(sfjz==0){
                getRepayRecords();
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
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
