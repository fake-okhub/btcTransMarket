package com.android.bitglobal.http;

import android.app.Activity;
import android.util.Log;

import com.android.bitglobal.activity.BaseActivity;
import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.common.AppManager;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.ActivityResult;
import com.android.bitglobal.entity.AppVersionResult;
import com.android.bitglobal.entity.ChartData;
import com.android.bitglobal.entity.CoinPriceFromOther;
import com.android.bitglobal.entity.CountryResult;
import com.android.bitglobal.entity.CurrencyAddressResult;
import com.android.bitglobal.entity.CurrencyResult;
import com.android.bitglobal.entity.CurrencyWithdrawResult;
import com.android.bitglobal.entity.DepositMethodsResult;
import com.android.bitglobal.entity.EntrustTradeResult;
import com.android.bitglobal.entity.FastRepayResult;
import com.android.bitglobal.entity.FileactionResult;
import com.android.bitglobal.entity.GetCurrencyResult;
import com.android.bitglobal.entity.GoogleResult;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.IdentityAuthResult;
import com.android.bitglobal.entity.IpLegalTenderResult;
import com.android.bitglobal.entity.LoginResult;
import com.android.bitglobal.entity.MarginUserResult;
import com.android.bitglobal.entity.MarketChartDataResult;
import com.android.bitglobal.entity.MarketDataResult;
import com.android.bitglobal.entity.MarketDepth;
import com.android.bitglobal.entity.MarketRemindResult;
import com.android.bitglobal.entity.ObjectResult;
import com.android.bitglobal.entity.PageResult;
import com.android.bitglobal.entity.PlatformSetResult;
import com.android.bitglobal.entity.RechargeBankResult;
import com.android.bitglobal.entity.TickerArrayResult;
import com.android.bitglobal.entity.TradeOverviewResult;
import com.android.bitglobal.entity.TradingRatio;
import com.android.bitglobal.entity.UnReadNoticeResult;
import com.android.bitglobal.entity.UserAcountResult;
import com.android.bitglobal.entity.UserInfoResult;
import com.android.bitglobal.entity.UserVipInfo;
import com.android.bitglobal.entity.VersionResult;
import com.android.bitglobal.entity.VipInfoResult;
import com.android.bitglobal.entity.VipLevelDetailsResult;
import com.android.bitglobal.entity.WithdrawAddressResult;
import com.android.bitglobal.entity.WithdrawDetailResult;
import com.android.bitglobal.tool.L;
import com.android.bitglobal.ui.UIHelper;

import java.io.File;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * xiezuofei
 * 2016-08-02 13:20
 * 793169940@qq.com
 */
public class HttpMethods {
    public static String BASE_URL = SystemConfig.shareInstance().getApiUrl(2);

    //超时时间60s
    private static final int DEFAULT_TIMEOUT = 30;

    private static int connectFailedNum = 0;
    private static final int CONNECT_FAILED_MAX = 5;

    private Retrofit retrofit;
    private ApiService apiService;

    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor().addLogInterceptor());
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.sslSocketFactory(createSSLSocketFactory());
        builder.hostnameVerifier(new TrustAllHostnameVerifier());
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        Log.e("BASE_URL", BASE_URL);
        apiService = retrofit.create(ApiService.class);
    }

    private static SSLSocketFactory createSSLSocketFactory() {

        SSLSocketFactory sSLSocketFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAllManager()},
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sSLSocketFactory;
    }

    private static class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)

                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    private static class SingletonHolder1 {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    private static class SingletonHolder2 {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    private static class SingletonHolder3 {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    private static class SingletonHolder4 {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    private static class SingletonHolder5 {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    private static class SingletonHolder6 {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static HttpMethods getInstance(int type) {
        BASE_URL = SystemConfig.shareInstance().getApiUrl(type);
        if (type == 1) {
            return SingletonHolder1.INSTANCE;
        } else if (type == 2) {
            return SingletonHolder2.INSTANCE;
        } else if (type == 3) {
            return SingletonHolder3.INSTANCE;
        } else if (type == 4) {
            return SingletonHolder4.INSTANCE;
        } else if (type == 5) {
            return SingletonHolder5.INSTANCE;
        } else if (type == 6) {
            return SingletonHolder6.INSTANCE;
        } else {
            return SingletonHolder.INSTANCE;
        }
    }

    public Map<String, String> paramPut(List<String> map, List<String> list) {
        Map<String, String> param = new HashMap<String, String>();
        int select_index = 0;
        for (String str : map) {
            try {
                param.put(str, list.get(select_index));
                select_index++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return param;

    }

    /**
     * 获取各个平台配置信息
     */
    public void getPlatformSet(Subscriber<HttpResult<PlatformSetResult>> subscriber, String version) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("version", version);
        Observable observable = apiService.getPlatformSet(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取首页行情图表
     */
    public void indexMarketChart(Subscriber<HttpResult<ChartData>> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpIndexMarketChart();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.indexMarketChart(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);

    }

    /**
     * 获取行情
     */
    public void tickers(Subscriber<MarketDataResult> subscriber, String symbol) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("symbol", symbol);
        Observable observable = apiService.getTicker(SystemConfig.getHttpConfig(param));
        toSubscribe(observable, subscriber);
    }

    //    /**
//     * 批量获取行情
//     */
    public void getTickerArray(Subscriber<HttpResult<TickerArrayResult>> subscriber, String step, String size,String legal_tender) {
        Map<String, String> param = new HashMap<String, String>();
        //    param.put("exchangeType","BTC");
        param.put("exchangeType", "");
        param.put("step", step);
        param.put("size", size);
        param.put("legal_tender", legal_tender);
          Observable observable = apiService.getTickerArray(SystemConfig.getHttpConfig(param));
//        Observable observable = apiService.getTickerArray(param);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取单币种k线及最新价格行情
     */
    public void getTickerArray(Subscriber<HttpResult<TickerArrayResult>> subscriber, String exchangeType, String currencyTypes, String step, String size,String legal_tender) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("exchangeType", exchangeType);
        param.put("currencyTypes", currencyTypes);
        param.put("step", step);
        param.put("size", size);
        param.put("legal_tender", legal_tender);
          Observable observable = apiService.getTickerArray(SystemConfig.getHttpConfig(param));
//        Observable observable = apiService.getTickerArray(param);
        toSubscribe(observable, subscriber);
    }


    /**
     * 获取24小时买卖分布接口
     */
    public void getTradingRatio(Subscriber<HttpResult<TradingRatio>> subscriber, String symbol) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("symbol", symbol);
        Observable observable = apiService.getTradingRatio(SystemConfig.getHttpConfig(param));
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取交易记录
     */
    public void getIndexMarketChartTrades(Subscriber<HttpResult<MarketChartDataResult>> subscriber, String exchangeType, String currencyType) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("exchangeType", exchangeType);
        param.put("currencyType", currencyType);
        param.put("since", "0");
        param.put("size", "50");
        Observable observable = apiService.indexMarketChartTrades(param);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取其他网站的btc信息
     */
    public void getCoinPriceFromOther(Subscriber<HttpResult<List<CoinPriceFromOther>>> subscriber, String symbol,String legal_tender) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("symbol", symbol);
        param.put("legal_tender", legal_tender);
        Observable observable = apiService.getCoinPriceFromOther(SystemConfig.getHttpConfig(param));
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取盘口深度
     */
    public void marketDepth(Subscriber<HttpResult<MarketDepth>> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpMarketDepth();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.marketDepth(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc<MarketDepth>());
        toSubscribe(observable, subscriber);

    }

    /**
     * 获取个人账号信息
     */
    public void getUserInfo(Subscriber<HttpResult<UserInfoResult>> subscriber) {
        Map<String, String> param = new HashMap<String, String>();
        Observable observable = apiService.getUserInfo(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取个人等级积分
     */
    public void getUserVipInfo(Subscriber<HttpResult<UserVipInfo>> subscriber) {
        Map<String, String> param = new HashMap<String, String>();
        Observable observable = apiService.getUserVipInfo(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取等级信息规则
     */
    public void getVipRule(Subscriber<HttpResult<VipInfoResult>> subscriber) {
        Map<String, String> param = new HashMap<String, String>();
        Observable observable = apiService.getVipRule(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取用户积分明细
     */
    public void getVipLevelDetails(Subscriber<HttpResult<VipLevelDetailsResult>> subscriber, int page, int pageSize) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("page", page + "");
        param.put("pageSize", pageSize + "");
        Observable observable = apiService.getVipLevelDetails(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取个人资产信息
     */
    public void getfunds(Subscriber<HttpResult<UserAcountResult>> subscriber,String legalTender) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("legalTender", legalTender);
        Observable observable = apiService.getUserAssets(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 用户注册
     */
    public void register(Subscriber<HttpResult<LoginResult>> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpRegister();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.register(SystemConfig.getHttpConfigRSA(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 用户登录
     */
    public void login(Subscriber<HttpResult<LoginResult>> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpLogin();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.login(SystemConfig.getHttpConfigRSA(param));
        toSubscribe(observable, subscriber);
    }

    /**
     * 使用手机号修改密码
     */
    public void changePwd(Subscriber<HttpResult<LoginResult>> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpChangePwd();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.changePwd(SystemConfig.getHttpConfigRSA(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 发送验证码
     */
    public void sendCode(Subscriber<HttpResult<ObjectResult>> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpSendCode();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.sendCode(SystemConfig.getHttpConfigRSA(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 校验验证码
     */
    public void checkCodeVersion(Subscriber<HttpResult> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpCheckCode();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.checkCodeVersion(SystemConfig.getHttpCheckCode(param));
        toSubscribe(observable, subscriber);
    }

    /**
     * 验证短信验证码
     */
    public void validateSmsCode(Subscriber<HttpResult<ObjectResult>> subscriber, String mobileNumber, String dynamicCode, String countryCode) {
        Map<String, String> param = new HashMap<>();
        param.put("mobileNumber", mobileNumber);
        param.put("dynamicCode", dynamicCode);
        param.put("countryCode", countryCode);
        Observable observable = apiService.validateSmsCode(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 重新发送邮件
     */
    public void reSendEmail(Subscriber<HttpResult> subscriber, String userId) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("userId", userId);
        Observable observable = apiService.reSendEmail(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 发送验证码
     */
    public void userSendCode(Subscriber<HttpResult> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpUserSendCode();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.userSendCode(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 修改密码
     */
    public void resetPwd(Subscriber<HttpResult<LoginResult>> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpResetPwd();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.resetPwd(SystemConfig.getHttpConfigRSA(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取国家信息
     */
    public void getCountries(Subscriber<HttpResult<CountryResult>> subscriber) {
        Map<String, String> param = new HashMap<>();
        Observable observable = apiService.getCountries(SystemConfig.getHttpConfig(param));
        // Observable observable = apiService.getCountries(param)
        toSubscribe(observable, subscriber);
    }

    /**
     * 提交高级实名认证
     */
    public void depthIdentityAuth(Subscriber<HttpResult> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpDepthIdentityAuth();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.depthIdentityAuth(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取实名认证状态
     */
    public void getIdentityAuthStatus(Subscriber<HttpResult<IdentityAuthResult>> subscriber) {
        Map<String, String> param = new HashMap<>();
        Observable observable = apiService.getIdentityAuthStatus(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 设置google验证码
     */
    public void setGoogleCode(Subscriber<HttpResult> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpSetGoogleCode();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.setGoogleCode(SystemConfig.getHttpConfigRSA(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取google密钥
     */
    public void getGoogleSecret(Subscriber<HttpResult<GoogleResult>> subscriber) {
        Map<String, String> param = new HashMap<>();
        Observable observable = apiService.getGoogleSecret(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 开启/关闭Google登录/提现验证
     */
    public void changeGoogleAuth(Subscriber<HttpResult> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpChangeGoogleAuth();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.changeGoogleAuth(SystemConfig.getHttpConfigRSA(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 开启/关闭异地登录短信验证
     */
    public void changeDynamicCodeAuth(Subscriber<HttpResult> subscriber, String operation, String authType) {
        Map<String, String> param = new HashMap<>();
        param.put("operation", operation);
        param.put("authType", authType);
        Observable observable = apiService.changeDynamicCodeAuth(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 开启/关闭交易时资金密码验证
     */
    public void useOrCloseSafePwd(Subscriber<HttpResult> subscriber, String period, String safePwd) {
        Map<String, String> param = new HashMap<>();
        param.put("period", period);
        param.put("safePwd", safePwd);
        Observable observable = apiService.useOrCloseSafePwd(SystemConfig.getHttpConfigRSA(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }


    /**
     * 获取交易货币配置
     */
    public void getCurrencySet(Subscriber<HttpResult<CurrencyResult>> subscriber, String version) {
        Map<String, String> param = new HashMap<>();
        param.put("version", version);
        Observable observable = apiService.getCurrencySet(SystemConfig.getHttpConfig(param));
        toSubscribe(observable, subscriber);
    }

    /**
     * 委托下单
     */
    public void doEntrust(Subscriber<HttpResult> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpDoEntrust();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.doEntrust(SystemConfig.getHttpConfigRSA(param));
        toSubscribe(observable, subscriber);
    }

    /**
     * 当前委托
     */
    public void entrustRecord(Subscriber<HttpResult<EntrustTradeResult>> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpEntrustRecord();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.entrustRecord(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 委托详情
     */
    public void entrustDetails(Subscriber<HttpResult<EntrustTradeResult>> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpCancelOrder();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.entrustDetails(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 批量取消交易
     */
    public void cancelAllOrders(Subscriber<HttpResult> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpCancelAllOrders();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.cancelBatchEntrust(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 取消单笔交易
     */
    public void cancelOrder(Subscriber<HttpResult> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpCancelOrder();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.cancelEntrust(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取充值地址
     */
    public void getRechargeAddress(Subscriber<HttpResult<CurrencyAddressResult>> subscriber, String currencyType) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("currencyType", currencyType);
        Observable observable = apiService.getRechargeAddress(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取个人历史提现地址
     */
    public void getWithdrawAddress(Subscriber<HttpResult<WithdrawAddressResult>> subscriber, String currencyType) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("currencyType", currencyType);
        Observable observable = apiService.getWithdrawAddress(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 修改提现地址备注
     */
    public void updateWithdrawAddressMemo(Subscriber<HttpResult> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpUpdateWithdrawAddressMemo();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.updateWithdrawAddressMemo(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 提交BTC/LTC/ETH提现操作
     */
    public void withdraw(Subscriber<HttpResult<CurrencyWithdrawResult>> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpWithdraw();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.withdraw(SystemConfig.getHttpConfigRSA(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 取消BTC/LTC/ETH提现
     */
    public void cancelWithdraw(Subscriber<HttpResult> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpCancelWithdraw();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.cancelWithdraw(SystemConfig.getHttpConfigRSA(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 查询提现账单
     */
    public void searchWithdraw(Subscriber<HttpResult<WithdrawDetailResult>> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpSearchWithdraw();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.searchWithdraw(SystemConfig.getHttpConfigRSA(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取提现网络手续费
     */
    public void getCounterFee(Subscriber<HttpResult<VersionResult>> subscriber, String currencyType) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("currencyType", currencyType);
        Observable observable = apiService.getCounterFee(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }


    /**
     * 融资融币­借款
     */
    public void doLoan(Subscriber<HttpResult<MarginUserResult>> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpDoLoan();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.doLoan(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取个人融资融币信息
     */
    public void getMarginUserData(Subscriber<HttpResult<MarginUserResult>> subscriber) {
        Map<String, String> param = new HashMap<String, String>();
        Observable observable = apiService.getMarginUserData(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 兑换免息券
     */
    public void exchangeInterestFreeTicket(Subscriber<HttpResult> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpExchangeInterestFree();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.exchangeInterestFreeTicket(SystemConfig.getHttpConfigRSA(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取免息券
     */
    public void getInterestFreeTickets(Subscriber<HttpResult<PageResult>> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpInterestFree();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.getInterestFreeTickets(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 融资融币­借款记录
     */
    public void getLoanRecords(Subscriber<HttpResult<PageResult>> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpLoanRecords();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.getLoanRecords(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 融资融币­一键还款
     */
    public void fastRepay(Subscriber<HttpResult<FastRepayResult>> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpFastRepay();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.fastRepay(SystemConfig.getHttpConfigRSA(param));
        toSubscribe(observable, subscriber);
    }

    /**
     * 融资融币­还款记录
     */
    public void getRepayRecords(Subscriber<HttpResult<PageResult>> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpRepayRecords();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.getRepayRecords(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取充值银行（银行卡）
     */
    public void getRechargeBank(Subscriber<HttpResult<RechargeBankResult>> subscriber) {
        Map<String, String> param = new HashMap<String, String>();
        Observable observable = apiService.getRechargeBank(SystemConfig.getHttpConfig(param))
                // Observable observable = apiService.getRechargeBank(param)
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取用户历史充值银行卡
     */
    public void getHistoryBank(Subscriber<HttpResult<PageResult>> subscriber, String bankType, String type) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("bankType", bankType);
        param.put("type", type);
        Observable observable = apiService.getHistoryBank(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取人民币充值方式
     */
    public void getDepositMethods(Subscriber<HttpResult<DepositMethodsResult>> subscriber, String version) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("version", version);
        Observable observable = apiService.getDepositMethods(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 提交银行汇款单
     */
    public void submitRecharge(Subscriber<HttpResult<ObjectResult>> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpSubmitRecharge();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.submitRecharge(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取待充值记录
     */
    public void getUnrecharedOrder(Subscriber<HttpResult> subscriber) {
        Map<String, String> param = new HashMap<String, String>();
        Observable observable = apiService.getUnrecharedOrder(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取人民币充值/提现记录（汇款， 支付宝，在线）
     */
    public void getRechargeList(Subscriber<HttpResult<PageResult>> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpRechargeList();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.getRechargeList(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 查询账单
     */
    public void searchBill(Subscriber<HttpResult> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpSearchBill();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.searchBill(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 取消银行汇款单
     */
    public void cancelRecharge(Subscriber<HttpResult> subscriber, String serialNumber) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("serialNumber", serialNumber);
        Observable observable = apiService.cancelRecharge(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 人民币提现
     */
    public void rmbWithdraw(Subscriber<HttpResult> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpRmbWithdraw();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.rmbWithdraw(SystemConfig.getHttpConfigRSA(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 取消人民币提现
     */
    public void cancelRmbWithdraw(Subscriber<HttpResult> subscriber, String withdrawId) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("withdrawId", withdrawId);
        Observable observable = apiService.cancelRmbWithdraw(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取地区信息
     */
    public void getAreas(Subscriber<HttpResult> subscriber, String version) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("version", version);
        Observable observable = apiService.getAreas(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取汇率信息
     */
    public void exchangeRate(Subscriber<HttpResult> subscriber, String currencyA, String currencyB) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("currencyA", currencyA);
        param.put("currencyB", currencyB);
        Observable observable = apiService.exchangeRate(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 检查app更新
     */
    public void updateAppVersion(Subscriber<HttpResult<AppVersionResult>> subscriber) {
        Map<String, String> param = new HashMap<>();
        param.put("client", "android");
        Observable observable = apiService.updateAppVersion(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取当前进行中的活动
     */
    public void getActivity(Subscriber<HttpResult<ActivityResult>> subscriber, String uid) {
        Map<String, String> param = new HashMap<>();
        param.put("uid", uid);
        Observable observable = apiService.getActivity(SystemConfig.getHttpConfig(param));
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取用户的网页授权ID
     */
    public void getUserOpenId(Subscriber<HttpResult> subscriber) {
        Map<String, String> param = new HashMap<>();
        Observable observable = apiService.getUserOpenId(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }


    /**
     * 设置推送ID
     */
    public void setRegistrationID(Subscriber<HttpResult> subscriber, String registrationID) {
        Map<String, String> param = new HashMap<>();
        param.put("registrationID", registrationID);
        Observable observable = apiService.setRegistrationID(SystemConfig.getHttpConfig(param));
        toSubscribe(observable, subscriber);
    }

    /**
     * 设置 获取支持的折算货币
     */
    public void getSupportLegalTender(Subscriber<HttpResult<GetCurrencyResult>> subscriber) {
        Map<String, String> param = new HashMap<>();
        Observable observable = apiService.getSupportLegalTender(SystemConfig.getHttpConfig(param));
        toSubscribe(observable, subscriber);
    }

    /**
     * 设置折算货币
     * @param legal_tender  法币类型
     */
    public void setLegalTender(Subscriber<HttpResult> subscriber,String legal_tender) {
        Map<String, String> param = new HashMap<>();
        param.put("legal_tender", legal_tender);
        Observable observable = apiService.setLegalTender(SystemConfig.getHttpConfig(param));
        toSubscribe(observable, subscriber);
    }


    /**
     * 我要吐槽
     */
    public void complain(Subscriber<HttpResult> subscriber, String complainPoint) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("complainPoint", complainPoint);
        Observable observable = apiService.complain(SystemConfig.getHttpConfig(param));
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取行情价格提醒设置
     */
    public void getMarketRemind(Subscriber<HttpResult<VersionResult>> subscriber) {
        Map<String, String> param = new HashMap<String, String>();
        Observable observable = apiService.getMarketRemind(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 设置行情价格提醒设置
     */
    public void setMarketRemind(Subscriber<HttpResult> subscriber, MarketRemindResult marketReminds, String currencyPrice) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("currency", marketReminds.getCurrency());
        param.put("exchange", marketReminds.getExchange());
        param.put("price", marketReminds.getPrice());
        param.put("currencyPrice", currencyPrice);
        Observable observable = apiService.setMarketRemind(SystemConfig.getHttpConfig(param)).map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 删除行情价格提醒
     */
    public void deleteMarketRemind(Subscriber<HttpResult> subscriber, String marketRemindId) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("id", marketRemindId);
        Observable observable = apiService.deleteMarketRemind(SystemConfig.getHttpConfig(param));
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取推荐链接和推荐规则
     */
    public void getRecommendGuide(Subscriber<HttpResult<ObjectResult>> subscriber) {
        Map<String, String> param = new HashMap<String, String>();
        Observable observable = apiService.getRecommendGuide(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 设置系统语言
     */
    public void setSystemLanguage(Subscriber<HttpResult> subscriber, String language) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("lan", language);
        Observable observable = apiService.setSystemLanguage(SystemConfig.getHttpConfig(param))
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 扫描二维码登录网站
     */
    public void loginByQrcode(Subscriber<HttpResult<ObjectResult>> subscriber, String uid, String type, String qrcode) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("uid", uid);
        param.put("type", type);
        param.put("qrcode", qrcode);
        Observable observable = apiService.loginByQrcode(SystemConfig.getHttpConfig(param));
        toSubscribe(observable, subscriber);
    }

    /**
     * 各种安全策略
     * category	验证分类（1. 登录 2. 交易 3. 提现）
     */
    public void changeAuth(Subscriber<HttpResult<CurrencyWithdrawResult>> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpChangeAuth();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.changeAuth(SystemConfig.getHttpConfigRSA(param));
        toSubscribe(observable, subscriber);
    }

    /**
     * 各种安全策略
     * category	验证分类（1. 登录 2. 交易 3. 提现）
     */
    public void changeSafetyAuth(Subscriber<HttpResult<CurrencyWithdrawResult>> subscriber, List<String> list) {
        List<String> map = SystemConfig.getHttpChangeAuth();
        Map<String, String> param = paramPut(map, list);
        Observable observable = apiService.changeAuth(SystemConfig.getHttpConfigRSA(param)).map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取公告
     */
    public void getProclamations(Subscriber<HttpResult<VersionResult>> subscriber, int page, int pageSize, Integer type, Integer isTop) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("page", page + "");
        param.put("pageSize", pageSize + "");
        if (type != null) {
            param.put("type", type + "");
        }
        if (type != null) {
//            param.put("isTop", isTop + "");
        }
        Observable observable = apiService.getProclamations(SystemConfig.getHttpConfig(param)).map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取公告
     */
    public void getProclamations(Subscriber<HttpResult<VersionResult>> subscriber) {
        Map<String, String> param = new HashMap<String, String>();
        Observable observable = apiService.getProclamations(SystemConfig.getHttpConfig(param)).map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取未读公告
     */
    public void getUserUnReadNotice(Subscriber<HttpResult<UnReadNoticeResult>> subscriber) {
        Map<String, String> param = new HashMap<>();
        Observable observable = apiService.getUserUnReadNotice(SystemConfig.getHttpConfig(param));
        toSubscribe(observable, subscriber);
    }

    /**
     * 标记已读公告
     */
    public void readNotice(Subscriber<HttpResult> subscriber, String id) {
        Map<String, String> param = new HashMap<>();
        param.put("noticeId", id);
        Observable observable = apiService.readNotice(SystemConfig.getHttpConfig(param));
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取提示
     */
    public void getTips(Subscriber<HttpResult<ObjectResult>> subscriber, String type, String currency) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("type", type);
        param.put("currency", currency);
        Observable observable = apiService.getTips(SystemConfig.getHttpConfig(param)).map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取界面元素设置
     */
    public void getElementSettings(Subscriber<HttpResult<VersionResult>> subscriber) {
        Map<String, String> param = new HashMap<String, String>();
        Observable observable = apiService.getElementSettings(SystemConfig.getHttpConfig(param)).map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取个人账号信息
     */
    public void getUserInfo2(Subscriber<HttpResult<UserInfoResult>> subscriber, String token, String userId) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("token", token);
        param.put("userId", userId);
        Observable observable = apiService.getUserInfo(SystemConfig.getHttpConfig(param));
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取个人账号信息
     */
    public void getTradeOverview(Subscriber<HttpResult<TradeOverviewResult>> subscriber, String userId, String token) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("userId", userId);
        param.put("token", token);
        Observable observable = apiService.getTradeOverview(SystemConfig.getHttpConfig(param));
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取ip地址对应法币
     */
    public void getIpLegalTender(Subscriber<HttpResult<IpLegalTenderResult>> subscriber,String ip) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("ip", ip);
        Observable observable = apiService.getIpLegalTender(SystemConfig.getHttpConfig(param));
        toSubscribe(observable, subscriber);
    }

    /**
     * 上传图片
     */
    public void fileaction(Subscriber<FileactionResult> subscriber, File file) {
        Map<String, RequestBody> param = new HashMap<>();
        param.put("file_upload_stat", toRequestBody("1"));
        param.put("plan_task_name", toRequestBody("goods"));
        param.put("userId", toRequestBody(UserDao.getInstance().getUserId()));
        param.put("userType", toRequestBody("1"));
        param.put("savePicSize", toRequestBody("false"));
        param.put("auth", toRequestBody("true"));
        param.put("_fma.pu._0.ima\";filename=\"" + file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
        param.put("rs", toRequestBody("1"));
        Observable observable = apiService.fileaction(param);
        toSubscribe(observable, subscriber);
    }

    private RequestBody toRequestBody(String vale) {
        return RequestBody.create(MediaType.parse("text/plain"), vale);
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends T>>() {
                    @Override
                    public Observable<? extends T> call(Throwable throwable) {
                        if ((throwable instanceof SocketTimeoutException)
                                || (throwable instanceof ConnectException)) {
                            L.d("ConnectException");
                            connectFailedNum++;
                            if (connectFailedNum > CONNECT_FAILED_MAX) {
                                connectFailedNum = 0;
//                                SystemConfig.shareInstance().changeBaseUrl();
//                                OkHttpClient.Builder builder = new OkHttpClient.Builder();
//                                builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
//                                BASE_URL = SystemConfig.shareInstance().getApiUrl(2);
//                                retrofit = new Retrofit.Builder()
//                                        .client(builder.build())
//                                        .addConverterFactory(GsonConverterFactory.create())
//                                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                                        .baseUrl(BASE_URL)
//                                        .build();
//                                Log.e("BASE_URL", BASE_URL);
                            }
                        }
                        return Observable.error(throwable);
                    }
                })
                .subscribe(s);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(final HttpResult<T> httpResult) {
            L.d(httpResult + "");
            if (httpResult != null && httpResult.getResMsg() != null && httpResult.getResMsg().getCode() != null && httpResult.getResMsg().getCode().equals("1003")) {
                if (AppManager.getAppManager().ActivityStackSize() > 0) {
                    ((Activity) BaseActivity.getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UserDao.getInstance().exit();
                            //UIHelper.ToastMessage(AppContext.getInstance(), R.string.user_manage_sqsx);
                            //AppManager.getAppManager().finishActivity();
                            UIHelper.showLogin(AppManager.getAppManager().currentActivity());
                            //   UIHelper.showLoginOrRegister(AppManager.getAppManager().currentActivity());
                        }
                    });
                }
            } else if (httpResult != null && httpResult.getResMsg() != null && httpResult.getResMsg().getCode() != null && !httpResult.getResMsg().getCode().equals("1000") && !httpResult.getResMsg().getMethod().equals("withdraw") && !httpResult.getResMsg().getMethod().equals("rmbWithdraw") && !httpResult.getResMsg().getMethod().equals("changeAuth") && !httpResult.getResMsg().getMethod().equals("setMarketReminds")) {
                throw new ApiException(httpResult.getResMsg().getMessage() + "__" + httpResult.getResMsg().getCode());
            }
            if (httpResult != null && httpResult.getDatas() == null && !httpResult.getResMsg().getCode().equals("1000") ) {
                if (httpResult != null && httpResult.getResMsg() != null && httpResult.getResMsg().getCode() != null) {
                    throw new ApiException(httpResult.getResMsg().getMessage() + "__" + httpResult.getResMsg().getCode());
                } else {
                    throw new ApiException(httpResult.getResMsg().getMessage());
                }
            } else {
                if (httpResult.getResMsg().getMethod().equals("rmbWithdraw") || httpResult.getResMsg().getMethod().equals("withdraw") || httpResult.getResMsg().getMethod().equals("userSendCode") || httpResult.getResMsg().getMethod().equals("changeAuth") || httpResult.getResMsg().getMethod().equals("sendCodeVersion2")) {
                    ((Activity) BaseActivity.getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UIHelper.ToastMessage(AppContext.getInstance(), httpResult.getResMsg().getMessage());
                        }
                    });
                }
                return httpResult.getDatas();
            }

        }
    }


}
