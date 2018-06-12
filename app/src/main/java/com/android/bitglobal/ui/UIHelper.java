package com.android.bitglobal.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import com.android.bitglobal.activity.CountryActivity;
import com.android.bitglobal.activity.CustomScanActivity;
import com.android.bitglobal.activity.KDiagramActivity;
import com.android.bitglobal.activity.KDiagramNewActivity;
import com.android.bitglobal.activity.LoginAbroadActivity;
import com.android.bitglobal.activity.LoginActivity;
import com.android.bitglobal.activity.LoginConfirmActivity;
import com.android.bitglobal.activity.LoginOrPayActivity;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.activity.RegisterActivity;
import com.android.bitglobal.activity.RegisterEmailActivity;
import com.android.bitglobal.activity.RegisterEmailSuccess;
import com.android.bitglobal.activity.ResetPwdActivity;
import com.android.bitglobal.activity.SetPriceActivity;
import com.android.bitglobal.activity.UserManage;
import com.android.bitglobal.activity.WebActivity;
import com.android.bitglobal.activity.asset.AreaAddress;
import com.android.bitglobal.activity.asset.BalanceTapActivity;
import com.android.bitglobal.activity.asset.BankAddress;
import com.android.bitglobal.activity.asset.BillDetail;
import com.android.bitglobal.activity.asset.CounterFeeActivity;
import com.android.bitglobal.activity.asset.CouponActivity;
import com.android.bitglobal.activity.asset.CouponExchangeIntegral;
import com.android.bitglobal.activity.asset.CouponExchangeKey;
import com.android.bitglobal.activity.asset.CurrencyWithdrawAddress;
import com.android.bitglobal.activity.asset.CurrencyWithdrawAddressUpdate;
import com.android.bitglobal.activity.asset.CurrencyWithdrawConfirm;
import com.android.bitglobal.activity.asset.CurrencyWithdrawRecord;
import com.android.bitglobal.activity.asset.CurrencyWithdrawRecordDetail;
import com.android.bitglobal.activity.asset.LendActivity;
import com.android.bitglobal.activity.asset.LendBorrow;
import com.android.bitglobal.activity.asset.LendRecord;
import com.android.bitglobal.activity.asset.LendRecordDetail;
import com.android.bitglobal.activity.asset.LendRepaymentDetail;
import com.android.bitglobal.activity.asset.LendRepaymentQuick;
import com.android.bitglobal.activity.asset.RechargeDetail;
import com.android.bitglobal.activity.asset.RmbRecharge;
import com.android.bitglobal.activity.asset.RmbRecord;
import com.android.bitglobal.activity.asset.RmbSelectRecharge;
import com.android.bitglobal.activity.asset.RmbWithdraw;
import com.android.bitglobal.activity.asset.RmbWithdrawConfirm;
import com.android.bitglobal.activity.asset.WithDrawFromScanActivity;
import com.android.bitglobal.activity.asset.WithdrawDetail;
import com.android.bitglobal.activity.asset.ZfbRecharge;
import com.android.bitglobal.activity.market.MarketDetailActivity;
import com.android.bitglobal.activity.market.MarketSet;
import com.android.bitglobal.activity.trans.EntrustDetail;
import com.android.bitglobal.activity.trans.ExchangeOrderDetailActivity;
import com.android.bitglobal.activity.trans.TransActivity;
import com.android.bitglobal.activity.user.AddressProve;
import com.android.bitglobal.activity.user.AnnouncementDetailActivity;
import com.android.bitglobal.activity.user.AnnouncementListActivity;
import com.android.bitglobal.activity.user.Feedback;
import com.android.bitglobal.activity.user.FloatingWindow;
import com.android.bitglobal.activity.user.GestureActivity;
import com.android.bitglobal.activity.user.GestureHint;
import com.android.bitglobal.activity.user.GestureSet;
import com.android.bitglobal.activity.user.SettingActivity;
import com.android.bitglobal.activity.user.RecommendReward;
import com.android.bitglobal.activity.user.SafetyAuthCommit;
import com.android.bitglobal.activity.user.SafetyBank;
import com.android.bitglobal.activity.user.SafetyBasisInfo;
import com.android.bitglobal.activity.user.SafetyCenter;
import com.android.bitglobal.activity.user.SafetyGoogleAuth;
import com.android.bitglobal.activity.user.SafetyIdentityAuth;
import com.android.bitglobal.activity.user.SafetyLoginOrWithdrawAuth;
import com.android.bitglobal.activity.user.SafetyLoginPwd;
import com.android.bitglobal.activity.user.SafetyPayAuth;
import com.android.bitglobal.activity.user.SafetyPicture;
import com.android.bitglobal.activity.user.SafetyTransAuth;
import com.android.bitglobal.activity.user.UserVipActivity;
import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.entity.EntrustTrade;
import com.android.bitglobal.fragment.asset.RecordsFragment;
import com.android.bitglobal.fragment.asset.WithdrawFragment;
import com.android.bitglobal.fragment.asset.DepositFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 应用程序UI工具包：封装UI相关的一些操作
 */
public class UIHelper {

	public final static String TAG = "UIHelper";

	public final static int RESULT_OK = 0x00;
	public final static int REQUEST_CODE = 0x01;
	public final static int BALANCE_RECHARGE = 1;
	public final static int BALANCE_WITHDRAW = 2;
    private static Toast toast;
	public static void ToastMessage(Context cont, String msg) {
        if(cont == null || msg == null) {
            return;
        }
        if (toast == null) {
            toast= Toast.makeText(cont, msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);//如果不为空，则直接改变当前toast的文本
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
//		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ToastMessage(Context cont, int msg) {
        if(cont == null || msg <= 0) {
            return;
        }
        if (toast == null) {
            toast= Toast.makeText(cont, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(cont.getString(msg));
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
//		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ToastMessage(Context cont, String msg, int time) {
        if(cont == null || msg == null) {
            return;
        }
        if (toast == null) {
            toast= Toast.makeText(cont, msg, time);
        } else {
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
//		Toast.makeText(cont, msg, time).show();
	}

	//子线程调用toast
    public static void showToastInThead( final String str) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                ToastMessage(AppContext.appContext,str);
            }
        });
    }

    public static  void showMainActivity(Activity context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
    public static void showWeb(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showCustomScanActivity(Activity context){
        Intent intent = new Intent(context, CustomScanActivity.class);
        context.startActivity(intent);
    }

    public static void showHome(Activity context,String host){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("host",host);
        context.startActivity(intent);
    }

 /*   public static void showLoginOrRegister(Activity context){
        Intent intent = new Intent(context, LoginOrRegister.class);
        context.startActivity(intent);
    }*/
    public static void showLogin(Activity context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
    public static void showLoginAbroad(Activity context){
        Intent intent = new Intent(context, LoginAbroadActivity.class);
        context.startActivity(intent);
    }
    public static void showLoginConfirm(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, LoginConfirmActivity.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showRegister(Activity context){
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }
    public static void showRegisterEmail(Activity context){
        Intent intent = new Intent(context, RegisterEmailActivity.class);
        context.startActivity(intent);
    }
    public static void showRegisterEmailSuccess(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, RegisterEmailSuccess.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }

    //tpye=16  1：注册，2：手机认证  3 ：修改手机认证    5：谷歌认证 7：邮箱认证  8：提币 9：安全设置  10：添加接收地址16：找回登录密码 17：找回资金密码 19：重置资金密码 20：关闭资金密码  63：用户登录 65：异地登录验证 71：APP用户注册 72：APP用户登录  94：手机挂失|
    public static void showForgetPwd(Activity context){
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.putExtra("type","16");
        context.startActivity(intent);
    }
    public static void showForgetEmailPwd(Activity context){
        Intent intent = new Intent(context, RegisterEmailActivity.class);
        intent.putExtra("type","2");
        context.startActivity(intent);
    }
    public static void showResetEmailPwd(Activity context){
        Intent intent = new Intent(context, ResetPwdActivity.class);
        intent.putExtra("type","3");
        context.startActivity(intent);
    }
    public static void showResetPwd(Activity context){
        Intent intent = new Intent(context, ResetPwdActivity.class);
        intent.putExtra("type","2");
        context.startActivity(intent);
    }
    public static void showRegisterSecond(Activity context){
        Intent intent = new Intent(context, ResetPwdActivity.class);
        intent.putExtra("type","1");
        context.startActivity(intent);
    }
    public static void showSetPwd(Activity context){
        Intent intent = new Intent(context, ResetPwdActivity.class);
        context.startActivity(intent);
    }
    public static void showSafetyCenter(Activity context){
        Intent intent = new Intent(context, SafetyCenter.class);
        context.startActivity(intent);
    }
    public static void showSetting(Activity context){
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }
    public static void showFloatingWindow(Activity context){
        Intent intent = new Intent(context, FloatingWindow.class);
        context.startActivity(intent);
    }
    public static void showFeedback(Activity context){
        Intent intent = new Intent(context, Feedback.class);
        context.startActivity(intent);
    }

    public static void showSafetyLoginPwd(Activity context){
        Intent intent = new Intent(context, SafetyLoginPwd.class);
        context.startActivity(intent);
    }
    public static void showSafetySafePwd(Activity context){
        Intent intent = new Intent(context, SafetyLoginPwd.class);
        intent.putExtra("type","2");
        context.startActivity(intent);
    }
    public static void showGoogleAuth(Activity context,String type){
        Intent intent = new Intent(context, SafetyGoogleAuth.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }
    public static void SafetyLoginOrWithdrawAuth(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, SafetyLoginOrWithdrawAuth.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showSafetyPayAuth(Activity context){
        Intent intent = new Intent(context, SafetyPayAuth.class);
        context.startActivity(intent);
    }
    public static void showSafetyTransAuth(Activity context){
        Intent intent = new Intent(context, SafetyTransAuth.class);
        context.startActivity(intent);
    }
    public static void showTrans(Activity context, Bundle bundle){
        Intent intent = new Intent(context, TransActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    public static void showSafetyIdentityAuth(Activity context){
        Intent intent = new Intent(context, SafetyIdentityAuth.class);
        context.startActivity(intent);
    }
    public static void showSafetyBasisInfo(Activity context){
        Intent intent = new Intent(context, SafetyBasisInfo.class);
        context.startActivity(intent);
    }
    public static void showSafetyPicture(Activity context){
        Intent intent = new Intent(context, SafetyPicture.class);
        context.startActivity(intent);
    }
    public static void showSafetyBank(Activity context){
        Intent intent = new Intent(context, SafetyBank.class);
        context.startActivity(intent);
    }
    public static void showAddressProve(Activity context){
        Intent intent = new Intent(context, AddressProve.class);
        context.startActivity(intent);
    }

    public static void showBankAddress(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, BankAddress.class);
        intent.putExtras(mBundle);
        context.startActivityForResult(intent,5257);
    }
    public static  void showAreaAddress(Activity context){
        Intent intent = new Intent(context, AreaAddress.class);
        context.startActivityForResult(intent,5267);
    }
    public static void showCountryActivity(Activity context){
        Intent intent = new Intent(context, CountryActivity.class);
        context.startActivityForResult(intent,5277);
    }
    public static void showCurrencyRecharge(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, DepositFragment.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showCurrencyWithdraw(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, WithdrawFragment.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);

        /*Intent intent = new Intent(context, CurrencyWithdraw.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);*/
    }
    public static void showCurrencyWithdrawFromScan(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, WithDrawFromScanActivity.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showCurrencyWithdrawConfirm(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, CurrencyWithdrawConfirm.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showCurrencyWithdrawRecord(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, CurrencyWithdrawRecord.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showCurrencyWithdrawRecordDetail(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, CurrencyWithdrawRecordDetail.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }


    public static void showCurrencyWithdrawAddress(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, CurrencyWithdrawAddress.class);
        intent.putExtras(mBundle);
        context.startActivityForResult(intent,5287);
    }
    public static void showCurrencyWithdrawAddressUpdate(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, CurrencyWithdrawAddressUpdate.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }

    public static void showBillActivity(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, RecordsFragment.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showBillDetail(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, BillDetail.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showBalance(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, BalanceTapActivity.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showUserVip(Activity context){
        Intent intent = new Intent(context, UserVipActivity.class);
        context.startActivity(intent);
    }
    public static void showRmbRecharge(Activity context){
        Intent intent = new Intent(context, RmbRecharge.class);
        context.startActivity(intent);
    }
    public static void showZfbRecharge(Activity context){
        Intent intent = new Intent(context, ZfbRecharge.class);
        context.startActivity(intent);
    }
    public static void showRmbSelectRecharge(Activity context){
        Intent intent = new Intent(context, RmbSelectRecharge.class);
        context.startActivity(intent);
    }
    public static void showRmbWithdraw(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, RmbWithdraw.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showRmbWithdrawConfirm(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, RmbWithdrawConfirm.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }

    public static void showRechargeDetail(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, RechargeDetail.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showWithdrawDetail(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, WithdrawDetail.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }

    public static void showRmbRecord(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, RmbRecord.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showEntrustDetail(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, EntrustDetail.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }

    public static void showLendActivity(Activity context){
        Intent intent = new Intent(context, LendActivity.class);
        context.startActivity(intent);
    }
    public static void showLendBorrow(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, LendBorrow.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showLendRecord(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, LendRecord.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showLendRecordDetail(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, LendRecordDetail.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showLendRepaymentQuick(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, LendRepaymentQuick.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showLendRepaymentDetail(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, LendRepaymentDetail.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showCouponActivity(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, CouponActivity.class);
        intent.putExtras(mBundle);
        context.startActivityForResult(intent,10023);
    }
    public static void showCouponExchangeKey(Activity context){
        Intent intent = new Intent(context, CouponExchangeKey.class);
        context.startActivity(intent);
    }
    public static void showCouponExchangeIntegral(Activity context){
        Intent intent = new Intent(context, CouponExchangeIntegral.class);
        context.startActivity(intent);
    }

    public static void showKDiagramNewActivity(Activity context,String currencyType,String exchangeType, String iconUrl){
        Intent intent = new Intent();
        intent.setClass(context, KDiagramNewActivity.class);
        intent.putExtra("currencyType", currencyType);
        intent.putExtra("exchangeType", exchangeType);
        intent.putExtra("iconUrl", iconUrl);
        context.startActivity(intent);
    }

    public static void showKDiagramActivity(Activity context,String currencyType,String exchangeType){
        Intent intent = new Intent();
        intent.setClass(context, KDiagramActivity.class);
        intent.putExtra("currencyType", currencyType);
        intent.putExtra("exchangeType", exchangeType);
        context.startActivity(intent);
    }

    public static void showMarketDetailActivity(Activity context,String currencyType,
                                                String exchangeType, String iconUrl){
        Intent intent = new Intent();
        intent.setClass(context, MarketDetailActivity.class);
        intent.putExtra("currencyType", currencyType);
        intent.putExtra("exchangeType", exchangeType);
        intent.putExtra("iconUrl", iconUrl);
        context.startActivity(intent);
    }

    public static void showMarketDetailActivity(Activity context) {
        Intent intent = new Intent();
        intent.putExtra("isInitIntentData", false);
        intent.setClass(context, MarketDetailActivity.class);
        context.startActivity(intent);
    }
    public static void showMarketSet(Activity context){
        Intent intent = new Intent(context, MarketSet.class);
        context.startActivity(intent);
    }

    public static void showSetPrice(Activity context, HashMap<String, String> currencyBtcPriceMap, HashMap<String, String> currencyUsdPriceMap){
        Intent intent = new Intent(context, SetPriceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("currencyBtcPriceMap", currencyBtcPriceMap);
        bundle.putSerializable("currencyUsdPriceMap", currencyUsdPriceMap);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void showRecommendReward(Activity context){
        Intent intent = new Intent(context, RecommendReward.class);
        context.startActivity(intent);
    }
    public static void showGestureSet(Activity context){
        Intent intent = new Intent(context, GestureSet.class);
        context.startActivity(intent);
    }
    public static void showGestureHint(Activity context){
        Intent intent = new Intent(context, GestureHint.class);
        context.startActivity(intent);
    }
    public static void showGestureActivity(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, GestureActivity.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showLoginOrPay(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, LoginOrPayActivity.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showSafetyAuthCommit(Activity context,Bundle mBundle){
        Intent intent = new Intent(context, SafetyAuthCommit.class);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }
    public static void showCounterFee(Activity context){
        Intent intent = new Intent(context, CounterFeeActivity.class);
        context.startActivityForResult(intent,5287);
    }
    public static void showUserManage(Activity context){
        Intent intent = new Intent(context, UserManage.class);
        context.startActivity(intent);
    }

    public static void showExchangeOrderDetail(Activity context,
                                               EntrustTrade info, String c_type, String e_type,
                                               ExchangeOrderDetailActivity.OnExchangeOrderDetailCallback callback) {
        context.startActivity(ExchangeOrderDetailActivity.newAct(context, info, c_type, e_type, callback));
    }

    public static void showAnnouncementList(Activity context) {
        context.startActivity(AnnouncementListActivity.newAct(context));
    }

    public static void showAnnouncementDetailList(Activity context, Bundle bundle) {
        Intent intent = new Intent(context, AnnouncementDetailActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
