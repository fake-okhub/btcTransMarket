package com.android.bitglobal.activity.asset;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.bitglobal.entity.DepositMethod;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.entity.BankTrade;
import com.android.bitglobal.entity.DepositMethodsResult;
import com.android.bitglobal.entity.PageResult;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-07-09 16:10
 * 793169940@qq.com
 *人民币充值
 */
public class RmbSelectRecharge extends SwipeBackActivity implements View.OnClickListener{
    public static RmbSelectRecharge context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;
    @BindView(R.id.btn_head_front)
    ImageView btn_head_front;

    @BindView(R.id.ll_hint)
    LinearLayout ll_hint;
    @BindView(R.id.ll_asset_zfbcz)
    LinearLayout ll_asset_zfbcz;
    @BindView(R.id.ll_asset_yhhk)
    LinearLayout ll_asset_yhhk;

    @BindView(R.id.tv_hint)
    TextView tv_hint;

    private String type="1",status="9";
    private int pageIndex=1,pageSize=1;
    private List<String> list=new ArrayList<String>();
    private SubscriberOnNextListener getRechargeListOnNext,getDepositMethodsOnNext;
    private BankTrade bankTrade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_rmb_select_recharge);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
        getRechargeList();
        getDepositMethods();
    }

    private void initView() {
        ll_asset_zfbcz.setVisibility(View.GONE);
        ll_asset_yhhk.setVisibility(View.GONE);
        tv_head_title.setText(R.string.asset_rmbcz);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        btn_head_front.setImageResource(R.mipmap.icon_nav_recharge_camera);
        btn_head_front.setVisibility(View.VISIBLE);
        btn_head_front.setOnClickListener(this);
        ll_hint.setOnClickListener(this);
        ll_asset_zfbcz.setOnClickListener(this);
        ll_asset_yhhk.setOnClickListener(this);

        getRechargeListOnNext= new SubscriberOnNextListener<PageResult>() {
            @Override
            public void onNext(PageResult pageResult) {
                List<BankTrade> detailList=pageResult.getBankTrades();
                if(detailList!=null&&detailList.size()>0){
                    for(BankTrade db:detailList){
                        bankTrade=db;
                        String amount=db.getAmount();
                        tv_hint.setText("￥ "+deFormat(amount));
                    }
                    ll_hint.setVisibility(View.VISIBLE);
                }else{
                    ll_hint.setVisibility(View.GONE);

                }

            }
        };
        getDepositMethodsOnNext= new SubscriberOnNextListener<DepositMethodsResult>() {
            @Override
            public void onNext(DepositMethodsResult dmr) {
                for (DepositMethod dm:dmr.getDepositMethods()){
                    if(dm.getType().equals("1")){
                        ll_asset_yhhk.setVisibility(View.VISIBLE);
                    }
                    if(dm.getType().equals("2")){
                        ll_asset_zfbcz.setVisibility(View.VISIBLE);
                    }


                }
            }
        };
    }
    private void initData() {

    }
    public void getRechargeList() {
        list.clear();
        list.add(type);
        list.add(status);
        list.add(pageIndex+"");
        list.add(pageSize+"");
        HttpMethods.getInstance(3).getRechargeList(new ProgressSubscriber(getRechargeListOnNext, context),list);
    }
    public void getDepositMethods() {
        HttpMethods.getInstance(3).getDepositMethods(new ProgressSubscriber(getDepositMethodsOnNext, context),"");
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.btn_head_front:
                bundle.putString("type","1");
                UIHelper.showRmbRecord(context,bundle);
                break;
            case R.id.ll_hint:
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
                bundle.putString("payBankLink",bankTrade.getPayBankLink());
                UIHelper.showRechargeDetail(context,bundle);
                break;
            case R.id.ll_asset_zfbcz:
                UIHelper.showZfbRecharge(context);
                break;
            case R.id.ll_asset_yhhk:
                UIHelper.showRmbRecharge(context);
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
