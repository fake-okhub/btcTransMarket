package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.ObjectResult;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.entity.BankTrade;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * xiezuofei
 * 2016-09-22 16:10
 * 793169940@qq.com
 *支付宝充值
 */
public class ZfbRecharge extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.ed_zfb_zh)
    EditText ed_zfb_zh;
    @BindView(R.id.ed_zfb_je)
    EditText ed_zfb_je;
    @BindView(R.id.img_zfb_r)
    ImageView img_zfb_r;
    @BindView(R.id.bnt_commit)
    Button bnt_commit;
    private UserInfo userInfo;
    private String type="2",rechargeBankId="1",rechargeAmount="",realName="",cardNumber="",bankReId="",remark="",randomMoney,userId;
    private SubscriberOnNextListener submitRechargeOnNext;
    private List<String> list=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_zfb_recharge);
        context=this;
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        tv_head_title.setText(R.string.asset_zfbcz);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        img_zfb_r.setOnClickListener(this);
        bnt_commit.setOnClickListener(this);
        submitRechargeOnNext= new SubscriberOnNextListener<ObjectResult>() {
            @Override
            public void onNext(ObjectResult objectResult) {
                RmbSelectRecharge.context.getRechargeList();
                BankTrade bankTrade=objectResult.getBankTrade();
                finish();
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
                bundle.putString("payBankLink",bankTrade.getPayBankLink());
                UIHelper.showRechargeDetail(context,bundle);
            }
        };
    }
    private void initData() {
        userInfo= UserDao.getInstance().getIfon();
        if(!is_token(userInfo)){
            finish();
        }
        realName=userInfo.getRealName();
        userId=userInfo.getUserId();
        randomMoney= SystemConfig.getRandomMoney(userId)+"";
        randomMoney=randomMoney.substring(1,randomMoney.length());
    }
    private void submitRecharge(){
        list.clear();
        list.add(type);//1:线下汇款 2:支付宝
        list.add(rechargeBankId);//付款银行静态ID(1=支付宝)
        list.add(rechargeAmount+randomMoney);//充值金额
        list.add(realName);//用户账户真实姓名
        list.add(cardNumber);//用户账户卡号(支付宝账号)
        list.add(bankReId);//用户账户的id(支付宝账户id)
        list.add(remark);//备注
        HttpMethods.getInstance(3).submitRecharge(new ProgressSubscriber(submitRechargeOnNext, context),list);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.bnt_commit:
                cardNumber=getText(ed_zfb_zh);
                rechargeAmount=getText(ed_zfb_je);
                if(cardNumber.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.asset_recharge_czzh_hint_toast));
                   return;
                }
                if(rechargeAmount.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.asset_recharge_czje_hint));
                    return;
                }
                submitRecharge();
                break;
            case R.id.img_zfb_r:
                bundle.putString("bankType","2");
                bundle.putString("type","1");
                UIHelper.showBankAddress(context,bundle);
                break;

        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }
    // 通过 onActivityResult的方法获取 扫描回来的 值
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null&&resultCode == 5288) {
            Bundle bundle = data.getExtras();
            bankReId= bundle.getString("id");
            cardNumber= bundle.getString("cardNumber");
            ed_zfb_zh.setText(cardNumber);
            Editable etext = ed_zfb_zh.getText();
            Selection.setSelection(etext, etext.length());
        }
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
