package com.android.bitglobal.http;

import com.android.bitglobal.entity.ActivityResult;
import com.android.bitglobal.entity.AppVersionResult;
import com.android.bitglobal.entity.AreaDataResult;
import com.android.bitglobal.entity.ChartData;
import com.android.bitglobal.entity.CoinPriceFromOther;
import com.android.bitglobal.entity.CountryResult;
import com.android.bitglobal.entity.CurrencyAddressResult;
import com.android.bitglobal.entity.CurrencyResult;
import com.android.bitglobal.entity.CurrencyWithdrawResult;
import com.android.bitglobal.entity.DepositMethodsResult;
import com.android.bitglobal.entity.EntrustOrderResult;
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
import com.android.bitglobal.entity.ObjectResult;
import com.android.bitglobal.entity.PageResult;
import com.android.bitglobal.entity.PlatformSetResult;
import com.android.bitglobal.entity.RechargeBankResult;
import com.android.bitglobal.entity.TickerArrayResult;
import com.android.bitglobal.entity.TradeOverviewResult;
import com.android.bitglobal.entity.TradingRatio;
import com.android.bitglobal.entity.UnReadNoticeResult;
import com.android.bitglobal.entity.UnrecharedOrder;
import com.android.bitglobal.entity.UserAcountResult;
import com.android.bitglobal.entity.UserInfoResult;
import com.android.bitglobal.entity.UserVipInfo;
import com.android.bitglobal.entity.VersionResult;
import com.android.bitglobal.entity.VipInfoResult;
import com.android.bitglobal.entity.VipLevelDetailsResult;
import com.android.bitglobal.entity.WithdrawAddressResult;
import com.android.bitglobal.entity.WithdrawDetailResult;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * xiezuofei
 * 2016-08-02 13:20
 * 793169940@qq.com
 */
public interface ApiService {
    /**
     * v1.1 url前缀
     */
    public static final String V1_1="V1_1/";
    /**
     * v1.2 url前缀
     */
    public static final String V1_2="V1_2/";
    /**
     * 行情
     */
    //获取行情

    @GET(V1_1+"getTicker")
    Observable<MarketDataResult> getTicker(@QueryMap Map<String, String> config);


    @POST(V1_2+"getTickerArray")
    Observable<HttpResult<TickerArrayResult>> getTickerArray(@QueryMap Map<String, String> config);
    //获取行情盘口单子
    @POST(V1_1+"indexMarketChart")
    Observable<HttpResult<ChartData>> indexMarketChart(@QueryMap Map<String, String> config);
    @POST(V1_1+"marketDepth")
    Observable<HttpResult<MarketDepth>> marketDepth(@QueryMap Map<String, String> config);
    //获取各个平台配置信息
    @POST(V1_1+"getPlatformSet")
    Observable<HttpResult<PlatformSetResult>> getPlatformSet(@QueryMap Map<String, String> config);

    //获取实时价格提醒
    @POST(V1_1+"getMarketReminds")
    Observable<HttpResult<VersionResult>> getMarketRemind(@QueryMap Map<String, String> config);
    //设置实时价格提醒
    @POST(V1_1+"setMarketReminds")
    Observable<HttpResult> setMarketRemind(@QueryMap Map<String, String> config);
    //删除实时价格提醒
    @POST(V1_1+"delMarketReminds")
    Observable<HttpResult> deleteMarketRemind(@QueryMap Map<String, String> config);

    //获取汇率信息
    @POST(V1_1+"exchangeRate")
    Observable<HttpResult<ObjectResult>> exchangeRate(@QueryMap Map<String, String> config);

    //获取交易记录
    @POST(V1_1+"indexMarketChartTrades")
    Observable<HttpResult<MarketChartDataResult>> indexMarketChartTrades(@QueryMap Map<String, String> config);

    //获取其他网站的btc信息
    @POST(V1_1+"cointPriceFromOther")
    Observable<HttpResult<List<CoinPriceFromOther>>> getCoinPriceFromOther(@QueryMap Map<String, String> httpConfig);

    /**
     * 行情end
     */

    /**
     * 个人信息
     */

    //注册
    /*@GET("register")
    Observable<HttpResult<LoginResult>> register(@QueryMap Map<String, String> config);*/
    @FormUrlEncoded
    @POST(V1_1+"register")
    Observable<HttpResult<LoginResult>> register(@FieldMap Map<String, String> config);
    //登录
    @POST(V1_1+"login")
    Observable<HttpResult<LoginResult>> login(@QueryMap Map<String, String> config);
    //发送短信验证码
    @POST(V1_1+"sendCodeVersion2")
    Observable<HttpResult<ObjectResult>> sendCode(@QueryMap Map<String, String> config);
    //校验短信验证码
    @POST(V1_1+"checkCode")
    Observable<HttpResult> checkCodeVersion(@QueryMap Map<String, String> config);
    //验证短信验证码
    @POST(V1_1+"validatorSmsCode")
    Observable<HttpResult<ObjectResult>> validateSmsCode(@QueryMap Map<String, String> config);
    //重新发送邮件
    @POST(V1_1+"reSendEmail")
    Observable<HttpResult> reSendEmail(@QueryMap Map<String, String> config);
    //登录用户发送短信验证码
    @POST(V1_1+"userSendCode")
    Observable<HttpResult> userSendCode(@QueryMap Map<String, String> config);
    //使用手机号修改密码
    @POST(V1_1+"changePwd")
    Observable<HttpResult<LoginResult>> changePwd(@QueryMap Map<String, String> config);

    //设置密码
    @POST(V1_1+"resetPwd")
    Observable<HttpResult<LoginResult>> resetPwd(@QueryMap Map<String, String> config);
    //获取个人基本信息
    @POST(V1_1+"getUserInfo")
    Observable<HttpResult<UserInfoResult>> getUserInfo(@QueryMap Map<String, String> config);
    //获取个人等级积分
    @POST(V1_1+"getUserLevel")
    Observable<HttpResult<UserVipInfo>> getUserVipInfo(@QueryMap Map<String, String> config);
    //获取等级信息规则
    @POST(V1_1+"getVipInfoAndntegralRule")
    Observable<HttpResult<VipInfoResult>> getVipRule(@QueryMap Map<String, String> config);
    //获取用户积分明细
    @POST(V1_1+"getRateDetail")
    Observable<HttpResult<VipLevelDetailsResult>> getVipLevelDetails(@QueryMap Map<String, String> config);
    //获取个人账户资产信息
    @POST(V1_2+"getUserAssets")
    Observable<HttpResult<UserAcountResult>> getUserAssets(@QueryMap Map<String, String> config);
    //获取谷歌密钥
    @POST(V1_1+"getGoogleSecret")
    Observable<HttpResult<GoogleResult>> getGoogleSecret(@QueryMap Map<String, String> config);
    //设置/修改/关闭谷歌验证
    @POST(V1_1+"setGoogleCode")
    Observable<HttpResult> setGoogleCode(@QueryMap Map<String, String> config);

    //开启/关闭异地登录短信验证
    @POST(V1_1+"changeDynamicCodeAuth")
    Observable<HttpResult> changeDynamicCodeAuth(@QueryMap Map<String, String> config);
    //开启/关闭交易时资金密码验证
    @POST(V1_1+"useOrCloseSafePwd")
    Observable<HttpResult> useOrCloseSafePwd(@QueryMap Map<String, String> config);
    //开启/关闭Google登录/提现验证
    @POST(V1_1+"changeGoogleAuth")
    Observable<HttpResult> changeGoogleAuth(@QueryMap Map<String, String> config);


    //获取国家信息
    @POST(V1_1+"getCountries")
    Observable<HttpResult<CountryResult>> getCountries(@QueryMap Map<String, String> config);
    //获取实名认证状态
    @POST(V1_1+"getIdentityAuthStatus")
    Observable<HttpResult<IdentityAuthResult>> getIdentityAuthStatus(@QueryMap Map<String, String> config);
    //提交高级实名认证
    @FormUrlEncoded
    @POST(V1_1+"depthIdentityAuth")
    Observable<HttpResult> depthIdentityAuth(@FieldMap Map<String, String> config);
    //设置推送ID
    @POST(V1_1+"setRegistrationID")
    Observable<HttpResult> setRegistrationID(@QueryMap Map<String, String> config);
    //我要吐槽
    @POST(V1_1+"complain")
    Observable<HttpResult> complain(@QueryMap Map<String, String> config);
    //检查app更新
    @POST(V1_1+"updateAppVersion")
    Observable<HttpResult<AppVersionResult>> updateAppVersion(@QueryMap Map<String, String> config);
    //获取当前进行中的活动
    @POST(V1_1+"getActivity")
    Observable<HttpResult<ActivityResult>> getActivity(@QueryMap Map<String, String> config);
    //获取当前进行中的活动
    @POST(V1_1+"getUserOpenId")
    Observable<HttpResult<ObjectResult>> getUserOpenId(@QueryMap Map<String, String> config);


    //获取提示
    @POST(V1_1+"getTips")
    Observable<HttpResult<ObjectResult>> getTips(@QueryMap Map<String, String> config);
    //获取公告
    @POST(V1_1+"getProclamations")
    Observable<HttpResult<VersionResult>> getProclamations(@QueryMap Map<String, String> config);
    //获取用户未读公告
    @POST(V1_1+"getUserUnReadNotice")
    Observable<HttpResult<UnReadNoticeResult>> getUserUnReadNotice(@QueryMap Map<String, String> config);
    //标记用户已读公告
    @POST(V1_1+"readNotice")
    Observable<HttpResult> readNotice(@QueryMap Map<String, String> config);
    //获取推荐链接和推荐规则
    @POST(V1_1+"getRecommendGuide")
    Observable<HttpResult<ObjectResult>> getRecommendGuide(@QueryMap Map<String, String> config);


    //获取地区信息
    @POST(V1_1+"getAreas")
    Observable<HttpResult<AreaDataResult>> getAreas(@QueryMap Map<String, String> config);
    //扫描二维码登录网站
    @FormUrlEncoded
    @POST(V1_1+"loginByQrcode")
    Observable<HttpResult<ObjectResult>> loginByQrcode(@FieldMap Map<String, String> config);
    //各种安全策略
    @FormUrlEncoded
    @POST(V1_1+"changeAuth")
    Observable<HttpResult<CurrencyWithdrawResult>> changeAuth(@FieldMap Map<String, String> config);

    /**
     * 个人信息end
     */

    /**
     * 财务
     */
    //获取充值地址
    @POST(V1_1+"getRechargeAddress")
    Observable<HttpResult<CurrencyAddressResult>> getRechargeAddress(@QueryMap Map<String, String> config);
    //获取个人历史提现地址
    @POST(V1_1+"getWithdrawAddress")
    Observable<HttpResult<WithdrawAddressResult>> getWithdrawAddress(@QueryMap Map<String, String> config);
    //修改提现地址备注
    @FormUrlEncoded
    @POST(V1_1+"updateWithdrawAddressMemo")
    Observable<HttpResult> updateWithdrawAddressMemo(@FieldMap Map<String, String> config);
    //提现操作
    @FormUrlEncoded
    @POST(V1_1+"withdraw")
    Observable<HttpResult<CurrencyWithdrawResult>> withdraw(@FieldMap Map<String, String> config);
    //查询提现账单
    @POST(V1_1+"searchWithdraw")
    Observable<HttpResult<WithdrawDetailResult>> searchWithdraw(@QueryMap Map<String, String> config);
    //取消BTC/LTC/ETH提现
    @POST(V1_1+"cancelWithdraw")
    Observable<HttpResult> cancelWithdraw(@QueryMap Map<String, String> config);
    //获取提现网络手续费
    @POST(V1_1+"getCounterFee")
    Observable<HttpResult<VersionResult>> getCounterFee(@QueryMap Map<String, String> config);

    //获取个人融资融币信息
    @POST(V1_1+"getMarginUserData")
    Observable<HttpResult<MarginUserResult>> getMarginUserData(@QueryMap Map<String, String> config);
    //融资融币­借款
    @POST(V1_1+"doLoan")
    Observable<HttpResult> doLoan(@QueryMap Map<String, String> config);
    //融资融币­一键还款
    @POST(V1_1+"fastRepay")
    Observable<HttpResult<FastRepayResult>> fastRepay(@QueryMap Map<String, String> config);
    //融资融币­借款记录
    @POST(V1_1+"getLoanRecords")
    Observable<HttpResult<PageResult>> getLoanRecords(@QueryMap Map<String, String> config);
    // 融资融币­还款记录
    @POST(V1_1+"getRepayRecords")
    Observable<HttpResult<PageResult>> getRepayRecords(@QueryMap Map<String, String> config);

    //获取免息券
    @POST(V1_1+"getInterestFreeTickets")
    Observable<HttpResult<PageResult>> getInterestFreeTickets(@QueryMap Map<String, String> config);
    //兑换免息券
    @POST(V1_1+"exchangeInterestFreeTicket")
    Observable<HttpResult> exchangeInterestFreeTicket(@QueryMap Map<String, String> config);


    //获取人民币充值方式
    @POST(V1_1+"getDepositMethods")
    Observable<HttpResult<DepositMethodsResult>> getDepositMethods(@QueryMap Map<String, String> config);
    //获取待充值记录
    @POST(V1_1+"getUnrecharedOrder")
    Observable<HttpResult<UnrecharedOrder>> getUnrecharedOrder(@QueryMap Map<String, String> config);
    //获取人民币充值/提现记录（汇款， 支付宝，在线）
    @POST(V1_1+"getRechargeList")
    Observable<HttpResult<PageResult>> getRechargeList(@QueryMap Map<String, String> config);
    //获取充值银行（银行卡）
    @POST(V1_1+"getRechargeBank")
    Observable<HttpResult<RechargeBankResult>> getRechargeBank(@QueryMap Map<String, String> config);
    //获取用户历史充值银行卡
    @POST(V1_1+"getHistoryBank")
    Observable<HttpResult<PageResult>> getHistoryBank(@QueryMap Map<String, String> config);
    //查询账单
    @POST(V1_1+"searchBill")
    Observable<HttpResult<PageResult>> searchBill(@QueryMap Map<String, String> config);
    //提交银行汇款单
    @FormUrlEncoded
    @POST(V1_1+"submitRecharge")
    Observable<HttpResult<ObjectResult>> submitRecharge(@FieldMap Map<String, String> config);
    //取消银行汇款单
    @POST(V1_1+"cancelRecharge")
    Observable<HttpResult> cancelRecharge(@QueryMap Map<String, String> config);
    //RMB提现操作
    @FormUrlEncoded
    @POST(V1_1+"rmbWithdraw")
    Observable<HttpResult<CurrencyWithdrawResult>> rmbWithdraw(@FieldMap Map<String, String> config);
    //取消人民币提现
    @POST(V1_1+"cancelRmbWithdraw")
    Observable<HttpResult> cancelRmbWithdraw(@QueryMap Map<String, String> config);
    //获取界面元素设置
    @POST(V1_1+"getElementSettings")
    Observable<HttpResult<VersionResult>> getElementSettings(@QueryMap Map<String, String> config);

    /**
     * 财务end
     */

    /**
     * 交易
    */
    //获取货币配置
    @POST(V1_1+"getCurrencySet")
    Observable<HttpResult<CurrencyResult>> getCurrencySet(@QueryMap Map<String, String> config);
    //委托下单
    /*@GET("doEntrust")
    Observable<HttpResult> doEntrust(@QueryMap Map<String, String> config);*/
    @FormUrlEncoded
    @POST(V1_1+"doEntrust")
    Observable<HttpResult> doEntrust(@FieldMap Map<String, String> config);
    //当前委托
    @POST(V1_1+"entrustRecord")
    Observable<HttpResult<EntrustTradeResult>> entrustRecord(@QueryMap Map<String, String> config);
    //当前委托
    @POST(V1_1+"entrustDetails")
    Observable<HttpResult<EntrustOrderResult>> entrustDetails(@QueryMap Map<String, String> config);

    //批量取消交易
    @POST(V1_1+"cancelBatchEntrust")
    Observable<HttpResult> cancelBatchEntrust(@QueryMap Map<String, String> config);
    //取消单笔交易
    @POST(V1_1+"cancelEntrust")
    Observable<HttpResult> cancelEntrust(@QueryMap Map<String, String> config);

    @Multipart
    @POST(V1_1+"mfileaction")
    Observable<FileactionResult> fileaction(@PartMap Map<String, RequestBody> params);

    //获取24小时买卖分布接口
    @POST(V1_1+"tradingRatio")
    Observable<HttpResult<TradingRatio>> getTradingRatio(@QueryMap Map<String, String> httpConfig);

    //设置语言
    @POST(V1_1+"setLanguage")
    Observable<HttpResult> setSystemLanguage(@QueryMap Map<String, String> httpConfig);

    /**
     * 交易end
     */

    //获取支持的折算货币
    @POST(V1_1+"getSupportLegalTender")
    Observable<HttpResult<GetCurrencyResult>> getSupportLegalTender(@QueryMap Map<String, String> httpConfig);

    //设置折算货币
    @POST(V1_1+"setLegalTender")
    Observable<HttpResult> setLegalTender(@QueryMap Map<String, String> httpConfig);

    //获取 交易总览页数据
    @POST(V1_2+"tradeOverview")
    Observable<HttpResult<TradeOverviewResult>> getTradeOverview(@QueryMap Map<String, String> httpConfig);

    @POST(V1_2+"getLegalTender")
    Observable<HttpResult<IpLegalTenderResult>> getIpLegalTender(@QueryMap Map<String, String> httpConfig);
}