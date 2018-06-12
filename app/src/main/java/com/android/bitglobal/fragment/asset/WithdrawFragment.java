package com.android.bitglobal.fragment.asset;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.activity.LoginOrPayActivity;
import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.dao.CurrencySetDao;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.*;
import com.android.bitglobal.entity.BillDetail;
import com.android.bitglobal.fragment.BaseFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.StringUtils;
import com.android.bitglobal.tool.Utils;
import com.android.bitglobal.ui.UIHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by bitbank on 16/12/29.
 *
 * 提币界面
 */
public class WithdrawFragment extends BaseFragment implements View.OnClickListener,SwipyRefreshLayout.OnRefreshListener{

    private static final String ARG_TYPE = "type";

    private Activity context;
//    @BindView(R.id.tv_head_title)
//    TextView tv_head_title;
//    @BindView(R.id.btn_head_back)
//    ImageView btn_head_back;
    @BindView(R.id.btn_head_front)
    ImageView btn_head_front;

//    @BindView(R.id.bnt_code)
    Button bnt_code;

    @BindView(R.id.carrycoin_address)
    TextView carrycoin_address;

    @BindView(R.id.carrycoin_account)
    EditText carrycoin_account;

//    @BindView(R.id.carrycoin_pwd)
//    EditText carrycoin_pwd;

//    @BindView(R.id.carrycoin_sms)
//    EditText carrycoin_sms;


    @BindView(R.id.bnt_commit)
    Button bnt_commit;


    @BindView(R.id.carry_fee)
    TextView carry_fee;


//    @BindView(R.id.bnt_zt)
//    Button mBntZt;


    @BindView(R.id.rl_xzyh)
    LinearLayout rl_xzyh;

    //资金密码
//    @BindView(R.id.rl_zjmm)
    LinearLayout mRl_Zjmm;

     //google验证码
//    @BindView(R.id.rl_ggyzm)
    LinearLayout mRl_Ggyzm;

    //短信验证码
//    @BindView(R.id.rl_dxyzm)
    LinearLayout mRl_dxyzm;

//    @BindView(R.id.ed_ggyzm)
//    EditText ed_ggyzm;

    @BindView(R.id.tv_no_ts)
    TextView tv_no_ts;

    @BindView(R.id.listview)
    ListView listview;

    @BindView(R.id.swipyrefreshlayout)
    SwipyRefreshLayout mSwipyRefreshLayout;

    @BindView(R.id.avaliable_balance_text)
    TextView avaliableBalanceText;

    @BindView(R.id.currency_type_text)
    TextView currencyTypeText;

    @BindView(R.id.avaliable_balance_layout)
    LinearLayout avaliableBalanceLayout;

    private String dataType="0",yue="";
    private String status="-1";//  | 状态 -1：全部  0：打币中  1：失败  2：成功  3：已取消
    private int pageIndex=1,pageSize=20,sfjz=0;
    private QuickAdapter<com.android.bitglobal.entity.WithdrawDetail> adapter;
    private SubscriberOnNextListener searchWithdrawOnNext;
    private List<BillDetail> list_data=new ArrayList<BillDetail>();
    private List<com.android.bitglobal.entity.WithdrawDetail> withdrawDetailList=new ArrayList<com.android.bitglobal.entity.WithdrawDetail>();
    private String currencyType;
    private String carrycoin_address_text;
    private String carrycoin_account_text;
    private String carrycoin_pwd_text;
    private String carrycoin_sms_text;
    private String dynamicCode;
    private String totalAmount;
    private String availableAmount;
    private String type;
    private boolean isEmailUser = false;
    private CurrencySetResult currencySetResult;

    private CurrencySetRealm mCurrencySetRealm;
    private UserInfo userInfo;
    private SubscriberOnNextListener sendCodeOnNext;
    private TimeCount time;
    private SubscriberOnNextListener withdrawOnNext;
    private List<String> list=new ArrayList<String>();
    private String googleCode="";
    private String avaliableBalanceTextValue;

    private String withdrawAuthenType="0";////提现验证策略：0.未选择1.资金密码+短信/邮件验证码2.资金密码+Google验证码3.资金密码+短信/邮件验证码+Google验证码
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_withdraw, container, false);
        currencySetResult= CurrencySetDao.getInstance().getIfon();
        context = getActivity();
        Bundle bundle = context.getIntent().getExtras();
        currencyType=bundle.getString("currencyType");
        totalAmount = bundle.getString("totalAmount");
        availableAmount = bundle.getString("availableAmount");
        type = bundle.getString(LoginOrPayActivity.TYPE_NO_SCAN_PASTE);
        if(currencySetResult!=null)
        {
            for (int i=0;i<currencySetResult.getCurrencySets().size();i++)
            {
                mCurrencySetRealm =currencySetResult.getCurrencySets().get(i);

                String e=mCurrencySetRealm.getCurrency();
                if(currencySetResult.getCurrencySets().get(i).getCurrency().equals(currencyType))
                {
                    mCurrencySetRealm =currencySetResult.getCurrencySets().get(i);
                    break;
                }else {
                    mCurrencySetRealm = null;
                }
            }

        }
        unbinder = ButterKnife.bind(this, view);
        initData();
        initView();
        searchWithdraw();
        return view;
    }

    public static WithdrawFragment newInstance(String param1) {
        WithdrawFragment fragment = new WithdrawFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    private void initView() {
//        tv_head_title.setText(currencyType+getString(R.string.carry_coin));
//        btn_head_back.setVisibility(View.VISIBLE);
//        btn_head_back.setOnClickListener(this);
        btn_head_front.setImageResource(R.mipmap.scan);
        btn_head_front.setVisibility(View.VISIBLE);
        btn_head_front.setOnClickListener(this);
        bnt_commit.setOnClickListener(this);
//        mBntZt.setOnClickListener(this);
        rl_xzyh.setOnClickListener(this);
//        bnt_code.setOnClickListener(this);
        avaliableBalanceLayout.setOnClickListener(this);

        carrycoin_account.setHint(getString(R.string.account_currency_amount_in).replaceAll("#", currencyType));
        carrycoin_address.setHint(currencyType + getString(R.string.account_carry_coin_alert_address));
        carry_fee.setText(mCurrencySetRealm.getMinFees() + " " + currencyType);

        withdrawOnNext = new SubscriberOnNextListener<CurrencyWithdrawResult>() {
            @Override
            public void onNext(CurrencyWithdrawResult currencyWithdrawResult) {
                dismiss();
//                UIHelper.ToastMessage(context, R.string.carry_coin_successful);
               // CurrencyWithdrawResult mcurrencyWit hdrawResult = currencyWithdrawResult;
                //UIHelper.showRmbWithdrawConfirm(context,bundle);
            }
        };


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

//        carrycoin_account.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                /**
//                 * 限制输入金额最多为 limit 位小数
//                 */
//                int limit = Utils.getPrecisionAmount();
//                if (s.toString().contains(".")) {
//                    if (s.length() - 1 - s.toString().indexOf(".") > limit) {
//                        s = s.toString().subSequence(0,
//                                s.toString().indexOf(".") + limit + 1);
//                        carrycoin_account.setText(s);
//                        carrycoin_account.setSelection(s.length());
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }

    private void initWithdrawDialogView() {
        //提现验证策略：0.未选择1.资金密码+短信/邮件验证码2.资金密码+Google验证码3.资金密码+短信/邮件验证码+Google验证码
        switch (withdrawAuthenType)
        {
            case "1":
                mRl_Zjmm.setVisibility(View.VISIBLE);
                mRl_dxyzm.setVisibility(View.VISIBLE);
                mRl_Ggyzm.setVisibility(View.GONE);
                break;
            case "2":
                mRl_Zjmm.setVisibility(View.VISIBLE);
                mRl_Ggyzm.setVisibility(View.VISIBLE);
                mRl_dxyzm.setVisibility(View.GONE);
                break;
            case "3":
                mRl_Zjmm.setVisibility(View.VISIBLE);
                mRl_Ggyzm.setVisibility(View.VISIBLE);
                mRl_dxyzm.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void sendCode(){
        list.clear();
        if(isEmailUser) {
            list.clear();
            list.add("");
            list.add("");
            list.add(userInfo.getEmail());
            list.add("8");
        } else {
            list.add("+86");
            list.add(userInfo.getMobileNumber());
            list.add("");
            //提币申请type8：提币
            list.add("8");
        }
        list.add(currencyType);
        HttpMethods.getInstance(3).sendCode(new ProgressSubscriber(sendCodeOnNext, context), list);
    }

    private void initData() {
        userInfo= UserDao.getInstance().getIfon();
        if(!is_token(userInfo)){
            context.finish();
        }
        if("".equals(userInfo.getMobileNumber())) {
            isEmailUser = true;
        }
        time = new TimeCount(60000, 1000);
        sendCodeOnNext = new SubscriberOnNextListener<ObjectResult>() {
            @Override
            public void onNext(ObjectResult objectResult) {
                time.start();
//                dynamicCode=objectResult.getDynamicCode();
              /*  Login2Info login2Lnfo=objectResult.getLogin2();
                if(login2Lnfo!=null){
                    login2Lnfo.userId=objectResult.getUserId();
                    Login2LnfoDao.getInstance().setIfon(login2Lnfo);
                }*/

            }
        };
        if(availableAmount == null) {
            avaliableBalanceText.setText(getString(R.string.account_avaliable_balance) + "0" + " " + currencyType);
        } else {
            int limit = SystemConfig.BALANCE_PRECISION;
            avaliableBalanceTextValue = format(availableAmount, limit);
            avaliableBalanceText.setText(getString(R.string.account_avaliable_balance) + avaliableBalanceTextValue + " " + currencyType);
        }
        currencyTypeText.setText(currencyType);
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
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()){
//            case R.id.btn_head_back:
//                finish();
//                break;
            case R.id.btn_head_front:
                AppContext.customScan(context);
                break;
            case R.id.rl_xzyh:
                if(type == null) {
                    alertSelectModelView();
                }
//                老版本点开查询记录
//                bundle.putString("currencyType",currencyType);
//                UIHelper.showCurrencyWithdrawAddress(context,bundle);
                break;
            case R.id.bnt_commit:
                carrycoin_address_text=getText(carrycoin_address);
                carrycoin_account_text=getText(carrycoin_account);
                if(carrycoin_address_text.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.safety_address_hint));
                    return;
                }
                if(carrycoin_account_text.equals("")){
                    UIHelper.ToastMessage(context,getString(R.string.safety_num_hint));
                    return;
                }
                if(new BigDecimal(carrycoin_account_text).doubleValue() > new BigDecimal(availableAmount).doubleValue()){
                    UIHelper.ToastMessage(context,getString(R.string.safety_num_error_hint));
                    return;
                }
                if(new BigDecimal(carrycoin_account_text).doubleValue() <= new BigDecimal(mCurrencySetRealm.getMinFees()).doubleValue()){
                    UIHelper.ToastMessage(context,getString(R.string.safety_num_error_1_hint).
                            replaceAll("#", mCurrencySetRealm.getMinFees()).replaceAll("@", currencyType));
                    return;
                }
                if(new BigDecimal(carrycoin_account_text).doubleValue() > new BigDecimal(mCurrencySetRealm.getDayCash()).doubleValue()){
                    UIHelper.ToastMessage(context,getString(R.string.safety_num_error_2_hint));
                    return;
                }

                alertWithDrawDialog();

                break;
            case R.id.avaliable_balance_layout:
                carrycoin_account.setText(avaliableBalanceTextValue);
                break;
        }

    }

    private void alertSelectModelView() {
        View selectModelView = View.inflate(context, R.layout.alert_view_carry_coin, null);
        TextView scanText = (TextView) selectModelView.findViewById(R.id.scan_text);
        TextView pasteText = (TextView) selectModelView.findViewById(R.id.paste_text);
        scanText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                AppContext.customScan(context);
            }
        });
        pasteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                String text = "";
                if (clipboardManager.hasPrimaryClip()){
                    text = clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();  // 获取内容
                }
                carrycoin_address.setText(text);
            }
        });
        alert(selectModelView, true);
    }

    private void alertWithDrawDialog() {
        withdrawAuthenType = userInfo.getWithdrawAuthenType();
        if(withdrawAuthenType==null || "0".equals(withdrawAuthenType))
        {
            withdrawAuthenType="0";
            UIHelper.ToastMessage(context, getString(R.string.asset_wszzjmm_toast));
        } else {
            final View withDrawView = View.inflate(context, R.layout.dialog_withdraw, null);
            final EditText ed_ggyzm = (EditText) withDrawView.findViewById(R.id.ed_ggyzm);
            bnt_code = (Button) withDrawView.findViewById(R.id.bnt_code);
            Button pasteBtn = (Button) withDrawView.findViewById(R.id.bnt_zt);
            TextView withdrawText = (TextView) withDrawView.findViewById(R.id.withdraw_text);
            TextView cancelText = (TextView) withDrawView.findViewById(R.id.cancel_text);
            TextView withdrawTitleText = (TextView) withDrawView.findViewById(R.id.withdraw_title_text);
            final EditText carrycoinPwdEdit = (EditText) withDrawView.findViewById(R.id.carrycoin_pwd);
            final EditText carrycoinSmsEdit = (EditText) withDrawView.findViewById(R.id.carrycoin_sms);
            if (isEmailUser) {
                carrycoinSmsEdit.setHint(R.string.user_yxyzm);
            } else {
                carrycoinSmsEdit.setHint(R.string.user_dxyzm);
            }
//                010  011 012 013 014  015 020  021 HFD090  HFD108 lid007 hfd125
//            withdrawTitleText.setText(getString(R.string.account_withdraw_title).replaceAll("###",
//                    carrycoin_account.getText().toString() + " " + currencyType).replaceAll("@@@", mCurrencySetRealm.getMinFees() + " " + currencyType));
            withdrawTitleText.setText(getString(R.string.account_withdraw_title).replaceAll("###",
                    carrycoin_account.getText().toString() + " " + currencyType));
            mRl_Zjmm = (LinearLayout) withDrawView.findViewById(R.id.rl_zjmm);
            mRl_Ggyzm = (LinearLayout) withDrawView.findViewById(R.id.rl_ggyzm);
            mRl_dxyzm = (LinearLayout) withDrawView.findViewById(R.id.rl_dxyzm);
            initWithdrawDialogView();
            bnt_code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(is_token(userInfo)) {
                        // 开始计时
                        sendCode();
                    }
                }
            });
            pasteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    googleCode = AppContext.getInstance().paste(context);
                    ed_ggyzm.setText(googleCode);
                    Editable etext = ed_ggyzm.getText();
                    Selection.setSelection(etext, etext.length());
                }
            });
            withdrawText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //提现验证策略：0.未选择1.资金密码+短信/邮件验证码2.资金密码+Google验证码3.资金密码+短信/邮件验证码+Google验证码
                    carrycoin_pwd_text = carrycoinPwdEdit.getText().toString().trim();
                    carrycoin_sms_text = carrycoinSmsEdit.getText().toString().trim();
                    dynamicCode = carrycoinSmsEdit.getText().toString().trim();
                    googleCode = ed_ggyzm.getText().toString();
                    if(carrycoin_pwd_text.equals("")){
                        UIHelper.ToastMessage(context,getString(R.string.user_zjmm_hint_toast));
                        return;
                    }
                    switch (withdrawAuthenType)
                    {
                        case "1":
                            if(carrycoin_sms_text.equals("")){
                                UIHelper.ToastMessage(context,getString(R.string.safety_sms_hint_toast));
                                return;
                            }
                            if(dynamicCode==null||dynamicCode.equals("")){
                                UIHelper.ToastMessage(context,getString(R.string.carry_sms_get));
                                return;
                            }
                            break;
                        case "2":
                            if(googleCode.equals(""))
                            {
                                UIHelper.ToastMessage(context,getString(R.string.user_ggyzm_hint_toast));
                                return;
                            }
                            break;
                        case "3":
                            if(carrycoin_sms_text.equals("")){
                                UIHelper.ToastMessage(context,getString(R.string.safety_sms_hint_toast));
                                return;
                            }
                            if(dynamicCode==null||dynamicCode.equals("")){
                                UIHelper.ToastMessage(context,getString(R.string.carry_sms_get));
                                return;
                            }
                            googleCode= ed_ggyzm.getText().toString();
                            if(googleCode.equals("")) {
                                UIHelper.ToastMessage(context, getString(R.string.user_ggyzm_hint_toast));
                                return;
                            }
                            break;
                    }
                    withdraw();

                }
            });
            cancelText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            alert(withDrawView, false);
        }
    }

    private void withdraw() {
        list.clear();
        list.add(currencyType);
        list.add(carrycoin_account_text);
        list.add(carrycoin_address_text);
        list.add(carrycoin_pwd_text);
        list.add(carrycoin_sms_text);
        list.add(googleCode);
        HttpMethods.getInstance(2).withdraw(new ProgressSubscriber(withdrawOnNext, context),list);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 通过 onActivityResult的方法获取 扫描回来的 值
        if (data!=null&&resultCode == 5290) {
            Bundle bundle = data.getExtras();
            String receiveAddress= bundle.getString("address");
            carrycoin_address.setText(receiveAddress);
        }
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult != null) {
            if(intentResult.getContents() != null) {
                String content=intentResult.getContents();
                carrycoin_address.setText(content);
            }
        } else {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 计时完毕
            bnt_code.setText(R.string.user_hqyzm);
            bnt_code.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 计时过程
            bnt_code.setClickable(false);//防止重复点击
            bnt_code.setText(millisUntilFinished / 1000 + getResources().getString(R.string.user_maio));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        time.cancel();
        unbinder.unbind();
    }

    public void setCarrycoinAddressText(String text) {
        if (carrycoin_address != null && !StringUtils.isEmpty(text)) {
            carrycoin_address.setText(text);
        }
    }
}
