package com.android.bitglobal.activity.trans;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.entity.TransCurrency;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.entity.EntrustOrder;
import com.android.bitglobal.entity.EntrustOrderResult;
import com.android.bitglobal.fragment.TransFragment;
import com.android.bitglobal.fragment.trans.TransTypeFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.tool.StringUtils;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-07-09 16:10
 * 793169940@qq.com
 * 委托详情
 */
public class EntrustDetail extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.tv_wtjg)
    TextView tv_wtjg;
    @BindView(R.id.tv_wtsl)
    TextView tv_wtsl;
    @BindView(R.id.tv_cjsl)
    TextView tv_cjsl;

    @BindView(R.id.tv_no_ts)
    TextView tv_no_ts;

    @BindView(R.id.ll_commit)
    LinearLayout ll_commit;
    @BindView(R.id.bnt_commit)
    Button bnt_commit;
    @BindView(R.id.listview)
    ListView listview;

    private List<EntrustOrder> list_order=new ArrayList<EntrustOrder>();
    private QuickAdapter<EntrustOrder> adapter;
    private SubscriberOnNextListener entrustDetailsOnNext,cancelOrderOnNext;
    private List<String> list=new ArrayList<String>();
    private String currencyType,exchangeType,orderId,number,unitPrice,completeNumber;
    private TransCurrency t_Currency;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_entrust_detail);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();

    }

    private void initView() {
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        bnt_commit.setOnClickListener(this);
        tv_head_title.setText(R.string.trans_wtxq);

        tv_wtsl.setText(t_Currency.getCurrencyType_symbol()+deFormat(number));
        tv_wtjg.setText(t_Currency.getExchangeType_symbol()+deFormat(unitPrice));
        tv_cjsl.setText(t_Currency.getCurrencyType_symbol()+deFormat(completeNumber));

        adapter = new QuickAdapter<EntrustOrder>(context, R.layout.trans_entrust_detail_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, final EntrustOrder item) {

                helper.setText(R.id.tv_number,t_Currency.getCurrencyType_symbol()+item.getTransactionNumber());
                helper.setText(R.id.tv_turnover,t_Currency.getExchangeType_symbol()+item.getTransactionTotalMoney());
                helper.setText(R.id.tv_time, StringUtils.dateFormat1(item.getTransactionTime()));
                helper.setText(R.id.tv_price, getString(R.string.trans_cjj)+item.getTransactionPrice());

            }
        };
        listview.setAdapter(adapter);
        entrustDetailsOnNext = new SubscriberOnNextListener<EntrustOrderResult>() {
            @Override
            public void onNext(EntrustOrderResult entrustOrderResult) {
                adapter.clear();
                list_order.clear();
                list_order.addAll(entrustOrderResult.getEntrustOrders());
                adapter.addAll(list_order);
                if(list_order.size()>0){
                    tv_no_ts.setVisibility(View.GONE);
                }else{
                    tv_no_ts.setVisibility(View.VISIBLE);
                }
            }
        };
        cancelOrderOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                ll_commit.setVisibility(View.GONE);
                UIHelper.ToastMessage(context,R.string.trans_cxcg);
                if(TransTypeFragment.ordersFragment !=null){
                    TransTypeFragment.ordersFragment.clearData();
                }
            }
        };
    }
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        orderId=bundle.getString("orderId");
        currencyType=bundle.getString("currencyType");
        exchangeType=bundle.getString("exchangeType");
        number=bundle.getString("number");
        unitPrice=bundle.getString("unitPrice");
        completeNumber=bundle.getString("completeNumber");
        t_Currency= TransFragment.fragment.getT_Currency();
    }

    private void entrustDetails() {
        list.clear();
        list.add(currencyType);
        list.add(exchangeType);
        list.add(orderId);
        HttpMethods.getInstance(1).entrustDetails(new ProgressSubscriber(entrustDetailsOnNext,context),list);
    }
    private void cancelOrder(){
        list.clear();
        list.add(currencyType);
        list.add(exchangeType);
        list.add(orderId);
        HttpMethods.getInstance(1).cancelOrder(new ProgressSubscriber(cancelOrderOnNext, context),list);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.bnt_commit:
                cancelOrder();
                break;

        }

    }
    @Override
    public void onResume() {
        super.onResume();
        entrustDetails();
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
