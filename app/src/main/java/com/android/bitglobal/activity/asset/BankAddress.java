package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.dao.RechargeBankDao;
import com.android.bitglobal.entity.HistoryBank;
import com.android.bitglobal.entity.PageResult;
import com.android.bitglobal.entity.RechargeBank;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.R;
import com.android.bitglobal.entity.RechargeBankResult;
import com.android.bitglobal.http.HttpMethods;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-08-25 16:10
 * 793169940@qq.com
 *银行卡
 */
public class BankAddress extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.tv_no_ts)
    TextView tv_no_ts;
    @BindView(R.id.listview)
    ListView listview;
    private String bankType,type;
    private QuickAdapter<HistoryBank> adapter;
    private SubscriberOnNextListener getHistoryBankOnNext;
    private List<HistoryBank> list_data=new ArrayList<HistoryBank>();
    private List<HistoryBank> list_data_l=new ArrayList<HistoryBank>();
    int lsyh=0,yhk=0;
    RechargeBankResult rechargeBankResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_currency_withdraw_address);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
        if(!bankType.equals("2")){
            adapter.replaceAll(list_data_l);
        }else{
            tv_head_title.setText(getString(R.string.safety_zfbzh));
        }
        if(!type.equals("0")){
            getHistoryBank();
        }
    }

    private void initView() {
        tv_head_title.setText(getString(R.string.safety_xzyh));
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);

        adapter = new QuickAdapter<HistoryBank>(context, R.layout.asset_bank_address_item) {
            @Override
            protected void convert(final BaseAdapterHelper helper, final HistoryBank item) {
                helper.setText(R.id.tv_bank_name,item.getBankName());
                if(item.getImg()!=null&&item.getImg().length()>1){
                    helper.setImageUrl(R.id.img_bank_type,item.getImg());
                }
                if(item.getCardNumber()!=null){
                    helper.setVisible(R.id.tv_bank_number,true);
                    helper.setText(R.id.tv_bank_number,getString(R.string.safety_kh)+item.getCardNumber());
                }else{
                    helper.setVisible(R.id.tv_bank_number,false);
                }
                if("2".equals(item.getXstb())){
                    helper.setVisible(R.id.tv_index,true);
                    helper.setText(R.id.tv_index,getString(R.string.safety_lsyhk));
                }else if("3".equals(item.getXstb())){
                    helper.setVisible(R.id.tv_index,false);
                }
                if("0".equals(item.getXstb())){
                    helper.setVisible(R.id.tv_index,true);
                    helper.setText(R.id.tv_index,getString(R.string.trans_qb));
                }else if("1".equals(item.getXstb())){
                    helper.setVisible(R.id.tv_index,false);
                }
                if("2".equals(bankType)){
                    helper.setVisible(R.id.tv_bank_number,false);
                    helper.setText(R.id.tv_bank_name,item.getCardNumber());
                }
                helper.setOnClickListener(R.id.ll_bank_address, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = getIntent();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", item.getId());
                        bundle.putString("owner", item.getOwner());
                        bundle.putString("bankStaticId", item.getBankStaticId());
                        bundle.putString("cardNumber", item.getCardNumber());
                        bundle.putString("bnankName", item.getBankName());
                        bundle.putString("province", item.getProvince());
                        bundle.putString("city", item.getCity());
                        bundle.putString("branch", item.getBranch());
                        bundle.putString("isDefault", item.getIsDefault());
                        bundle.putString("img", item.getImg());
                        intent.putExtras(bundle);
                        setResult(5288, intent);
                        finish();
                    }
                });

            }
        };
        listview.setAdapter(adapter);

        getHistoryBankOnNext = new SubscriberOnNextListener<PageResult>() {
            @Override
            public void onNext(PageResult pageResult) {
                for(HistoryBank hb:pageResult.getHistoryBanks()){
                    if (lsyh==0) {
                        lsyh++;
                        hb.setXstb("2");
                    }else{
                        hb.setXstb("3");
                    }
                    list_data.add(hb);
                }
                if(!bankType.equals("2")){
                    list_data.addAll(list_data_l);
                }
                adapter.clear();
                adapter.replaceAll(list_data);
                if(list_data.size()>0){
                    tv_no_ts.setVisibility(View.GONE);
                }else{
                    tv_no_ts.setText(R.string.safety_mylszh);
                    tv_no_ts.setVisibility(View.VISIBLE);
                }
            }

        };

    }
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        bankType=bundle.getString("bankType");
        type=bundle.getString("type");
        rechargeBankResult= RechargeBankDao.getInstance().getIfon();
        for(RechargeBank rb:rechargeBankResult.getRechargeBanks()){
            HistoryBank hb=new HistoryBank();
            if (yhk==0) {
                yhk++;
                hb.setXstb("0");
            }else{
                hb.setXstb("1");
            }
            hb.setId("0");
            hb.setBankStaticId(rb.getId());
            hb.setBankName(rb.getName());
            hb.setImg(rb.getImg());
            hb.setIsDefault("0");
            list_data_l.add(hb);
        }
    }


    private void getHistoryBank() {
        HttpMethods.getInstance(3).getHistoryBank(new ProgressSubscriber(getHistoryBankOnNext, context),bankType,type);
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
