package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.dao.CurrencySetDao;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.CurrencySetRealm;
import com.android.bitglobal.entity.CurrencySetResult;
import com.android.bitglobal.entity.MarginUser;
import com.android.bitglobal.entity.MarginUserResult;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
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
 * 2016-08-29 13:10
 * 793169940@qq.com
 * 融资融币
 */
public class LendActivity extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.tv_leverage)
    TextView tv_leverage;
    @BindView(R.id.img_help)
    ImageView img_help;
    @BindView(R.id.ll_asset_leverage)
    LinearLayout ll_asset_leverage;


    @BindView(R.id.listview)
    ListView listView;

    private QuickAdapter adapter;
    private SubscriberOnNextListener getMarginUserDataOnNext;
    private CurrencySetResult currencySetResult;
    private UserInfo userInfo;
    private String leverageLink;
    private List<MarginUser> list=new ArrayList<MarginUser>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_lend);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();

    }

    private void initView() {
        tv_head_title.setText(R.string.asset_rzrb);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        img_help.setOnClickListener(this);
        ll_asset_leverage.setVisibility(View.GONE);
        adapter = new QuickAdapter<MarginUser>(context, R.layout.asset_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, final MarginUser item) {
                helper.setText(R.id.tv_assets_ky,getString(R.string.trans_yj)+"：");
                helper.setText(R.id.tv_assets_ky_sl,deFormat(item.getHasBorrowed()));
                helper.setText(R.id.tv_assets_dj,getString(R.string.trans_jzc)+"："+deFormat(item.getNetAssets()));
                helper.setText(R.id.tv_assets_jr,getString(R.string.trans_kj)+"："+deFormat(item.getCanBorrow()));
                helper.setText(R.id.tv_assets_cz,getString(R.string.asset_wyjk));
                helper.setText(R.id.tv_assets_tx,getString(R.string.asset_yjhk));

                helper.setImageResource(R.id.img_assets_cz,R.mipmap.ico_loanin_white);
                helper.setImageResource(R.id.img_assets_tx,R.mipmap.ico_refund_white);

                String title_1="";
                for(CurrencySetRealm csr:currencySetResult.getCurrencySets()){
                    if(csr.getCurrency().equals(item.getCurrencyType())){
                        helper.setImageUrl(R.id.img_assets_type,csr.getFinanceCoinUrl()) ;
                        if(SystemConfig.getLanguageEnv().equals("cn")){
                            title_1=csr.getName()+""+getString(R.string.trans_jd);
                            helper.setText(R.id.tv_assets_type,title_1+"（"+csr.getCurrency()+"）");
                        }else{
                            title_1=csr.getEnglishName()+" "+getString(R.string.trans_jd);
                            helper.setText(R.id.tv_assets_type,title_1+"（"+csr.getCurrency()+"）");
                        }
                    }
                }
                final String title=title_1;
                helper.setOnClickListener(R.id.ll_asset_cz, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("title",title);
                        bundle.putString("currencyType",item.getCurrencyType());
                        bundle.putString("netAssets",item.getCanBorrow());
                        bundle.putString("p2pOutRate",item.getP2pOutRate());
                        UIHelper.showLendBorrow((Activity) context,bundle);
                    }
                });
                helper.setOnClickListener(R.id.ll_asset_tx, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("currencyType",item.getCurrencyType());
                        UIHelper.showLendRepaymentQuick((Activity) context,bundle);
                    }
                });
                helper.setOnClickListener(R.id.rl_assets_jr, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("currencyType",item.getCurrencyType());
                        UIHelper.showLendRecord((Activity) context,bundle);
                    }
                });
            }
        };
        listView.setAdapter(adapter);
        getMarginUserDataOnNext= new SubscriberOnNextListener<MarginUserResult>() {
            @Override
            public void onNext(MarginUserResult marginUserResult1) {
                ll_asset_leverage.setVisibility(View.VISIBLE);
                tv_leverage.setText(marginUserResult1.getLeverage());
                leverageLink=marginUserResult1.getLeverageLink();
                adapter.clear();
                adapter.addAll(marginUserResult1.getMarginUsers());
            }
        };
    }
    private void initData()
    {
        userInfo= UserDao.getInstance().getIfon();
        if(!is_token(userInfo)){
            finish();
        }
        currencySetResult= CurrencySetDao.getInstance().getIfon();
    }
    public void getMarginUserData() {
        HttpMethods.getInstance(4).getMarginUserData(new ProgressSubscriber(getMarginUserDataOnNext, context));
    }
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.img_help:
                bundle.putString("title",getString(R.string.user_jdgz));
                bundle.putString("url", SystemConfig.getRzmbxy());
                bundle.putString("type","1");
                UIHelper.showWeb(context,bundle);
                break;

        }

    }
    @Override
    public void onResume() {
        super.onResume();
        getMarginUserData();
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
