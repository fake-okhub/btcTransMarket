package com.android.bitglobal.fragment.asset;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.entity.*;
import com.android.bitglobal.entity.BillDetail;
import com.android.bitglobal.fragment.BaseFragment;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.StringUtils;
import com.android.bitglobal.R;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.tool.Utils;
import com.android.bitglobal.ui.UIHelper;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * xiezuofei
 * 2016-09-22 14:10
 * 793169940@qq.com
 * 账单
 */
public class RecordsFragment extends BaseFragment implements SwipyRefreshLayout.OnRefreshListener{

    private static final String ARG_TYPE = "type";

    private Activity context;
//    @BindView(R.id.tv_head_title)
//    TextView tv_head_title;
//    @BindView(R.id.btn_head_back)
//    ImageView btn_head_back;

    @BindView(R.id.tv_no_ts)
    TextView tv_no_ts;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.swipyrefreshlayout)
    SwipyRefreshLayout mSwipyRefreshLayout;

    private String currencyType,dataType="0",yue="";
    private int pageIndex=1,pageSize=20,sfjz=0;
    private QuickAdapter<com.android.bitglobal.entity.BillDetail> adapter;
    private SubscriberOnNextListener searchBillOnNext;
    private List<String> list=new ArrayList<String>();
    private List<BillDetail> list_data=new ArrayList<BillDetail>();
    private Unbinder unbinder;
    private boolean isSlidingTop = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_records, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        initData();
        initView();
        searchBill();
        return view;
    }

    public static RecordsFragment newInstance(String param1) {
        RecordsFragment fragment = new RecordsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    private void initView() {
//        tv_head_title.setText(getString(R.string.asset_lszd)+"-"+currencyType);
        tv_no_ts.setText(getString(R.string.asset_lszd_hint));
//        btn_head_back.setVisibility(View.VISIBLE);
//        btn_head_back.setOnClickListener(this);
        mSwipyRefreshLayout.setOnRefreshListener(this);
        adapter = new QuickAdapter<BillDetail>(context, R.layout.records_fragment_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, final BillDetail item) {
                final String show=item.getShow();
//                final String balance=item.getBalance();
//                final String showBalance=deFormat(balance)+" "+currencyType;
                String showChang_ls="";
                try{
                    showChang_ls =show.substring(0,show.indexOf("="));
                }catch (Exception e){
                    showChang_ls=show;
                }
                final String showChang=showChang_ls;
//                final String typeName=item.getTypeName();
//                final String date_time= StringUtils.dateFormat1(item.getBillDate());
             /*   if("".equals(item.getYue())){
                    helper.setVisible(R.id.tv_index,false);
                }else{
                    helper.setVisible(R.id.tv_index,true);
                }*/
//                helper.setImageUrl(R.id.img_bill_type,item.getTypeImg());
                if(item.getType().equals("1")) {
                    helper.setText(R.id.img_bill_type_text, getString(R.string.account_deposit));
                    helper.setBackgroundRes(R.id.img_bill_type_text, R.drawable.bg_text_green_radius);
                    helper.setVisible(R.id.withdraw_address_text, false);
                } else {
                    helper.setText(R.id.img_bill_type_text, getString(R.string.account_withdraw));
                    helper.setBackgroundRes(R.id.img_bill_type_text, R.drawable.bg_text_red_radius);
                    helper.setText(R.id.withdraw_address_text, item.getToAddr());
                    helper.setVisible(R.id.withdraw_address_text, true);
                }
                String status = item.getStatus();
                if("successful".equals(status)) {
                    helper.setTextColor(R.id.tv_bill_name, ContextCompat.getColor(context, R.color.asset_bill_grren));
                } else {
                    helper.setTextColor(R.id.tv_bill_name, ContextCompat.getColor(context, R.color.text_color_gray));
                }
                helper.setText(R.id.tv_bill_name,item.getStatus());
             //   helper.setText(R.id.tv_index,item.getYue());
                helper.setText(R.id.tv_bill_number,format(showChang, SystemConfig.BALANCE_PRECISION) + " " + currencyType);
//                helper.setText(R.id.tv_bill_tiem1, StringUtils.dateFormat3(item.getBillDate()));
                helper.setText(R.id.tv_bill_tiem, StringUtils.dateFormat1(item.getBillDate()));
//                helper.setText(R.id.tv_bill_tiem2,StringUtils.dateFormat6(item.getBillDate()));
//                helper.setOnClickListener(R.id.ll_asset_bill, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("typeName", typeName);
//                        bundle.putString("showChang", showChang);
//                        bundle.putString("showBalance", showBalance);
//                        bundle.putString("time", date_time);
//                        UIHelper.showBillDetail((Activity) context,bundle);
//                    }
//                });

            }
        };
        searchBillOnNext = new SubscriberOnNextListener<PageResult>() {
            @Override
            public void onNext(PageResult pageResult) {
                if(pageIndex==1&&dataType.equals("0")){
                    list_data.clear();
                    adapter.clear();
                }
                List<BillDetail> detailList=pageResult.getBillDetails();

                if(detailList!=null&&detailList.size()>0){
                    pageIndex++;
                }
                if (detailList!=null&&detailList.size()>0){
                    for(BillDetail db:detailList){
                        String yue_l=StringUtils.dateFormat7(db.getBillDate());
                      /*  if(yue.equals(yue_l)){
                            db.setYue("");
                        }else{
                            db.setYue(yue_l);
                        }*/
                        list_data.add(db);
                        yue=yue_l;
                    }
                }
                if ((detailList==null&&dataType.equals("0"))||(detailList.size()==0&&dataType.equals("0"))) {
                    dataType = "1";
                    pageIndex = 1;
                    if(!isSlidingTop) {
                        UIHelper.ToastMessage(context,R.string.trans_wgdsjl_toast);
                    }
//                    searchBill();
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
        listview.setAdapter(adapter);
    }
    private void initData() {
        Bundle bundle = context.getIntent().getExtras();
        currencyType=bundle.getString("currencyType");
    }
    public void clearData(){
        dataType="0";
        pageIndex=1;
        sfjz=0;
        searchBill();
    }
    private void searchBill() {
        list.clear();
        list.add("");
        list.add(currencyType);
        list.add(dataType);
        list.add(pageIndex+"");
        list.add(pageSize+"");
        HttpMethods.getInstance(3).searchBill(new ProgressSubscriber(searchBillOnNext, context),list);
    }
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btn_head_back:
//                finish();
//                break;
//
//        }
//
//    }
    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if(direction == SwipyRefreshLayoutDirection.TOP){
            isSlidingTop = true;
            clearData();
        }else{
            if(sfjz==0){
                isSlidingTop = false;
                searchBill();
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
        pageIndex = 1;
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
