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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.bitglobal.entity.ObjectResult;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.BankTrade;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.squareup.picasso.Picasso;

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
public class RmbRecharge extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.rl_xzyh)
    LinearLayout rl_xzyh;
    @BindView(R.id.img_bank)
    ImageView img_bank;
    @BindView(R.id.ed_xzyh)
    TextView ed_xzyh;
    @BindView(R.id.ed_yhkh)
    EditText ed_yhkh;
    @BindView(R.id.ed_hkr)
    EditText ed_hkr;
    @BindView(R.id.ed_czje)
    EditText ed_czje;
    @BindView(R.id.bnt_commit)
    Button bnt_commit;
    private UserInfo userInfo;
    private String type="1",bankImg="",bankNmae="",rechargeBankId="",rechargeAmount="",realName="",cardNumber="",bankReId="",remark="",randomMoney,userId;
    private SubscriberOnNextListener submitRechargeOnNext;
    private List<String> list=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_rmb_recharge);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();

    }

    private void initView() {
        tv_head_title.setText(R.string.asset_yhhk);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        rl_xzyh.setOnClickListener(this);
        ed_hkr.setText(realName);

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
            case R.id.rl_xzyh:
                bundle.putString("bankType","1");
                bundle.putString("type","1");
                UIHelper.showBankAddress(context,bundle);
                break;
            case R.id.bnt_commit:
                cardNumber=getText(ed_yhkh);
                rechargeAmount=getText(ed_czje);
                if(rechargeBankId.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.safety_xzyh_hint));
                    return;
                }
                if(cardNumber.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.safety_yhkh_hint));
                    return;
                }
                if(rechargeAmount.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.asset_recharge_czje_hint_toast));
                    return;
                }
                submitRecharge();
                break;

        }

    }
    // 通过 onActivityResult的方法获取 扫描回来的 值
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null&&resultCode == 5288) {
            Bundle bundle = data.getExtras();
            bankReId= bundle.getString("id");
            bankImg= bundle.getString("img");
            bankNmae= bundle.getString("bnankName");
            rechargeBankId= bundle.getString("bankStaticId");
            cardNumber= bundle.getString("cardNumber");
            ed_xzyh.setText(bankNmae);
            ed_yhkh.setText(cardNumber);
            try {
                Picasso.with(context).load(bankImg).into(img_bank);
            }catch (Exception e){

            }
            Editable etext = ed_yhkh.getText();
            Selection.setSelection(etext, etext.length());
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
