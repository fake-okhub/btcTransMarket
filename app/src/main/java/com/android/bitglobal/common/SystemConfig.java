package com.android.bitglobal.common;

import android.util.Log;

import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.tool.L;
import com.android.bitglobal.tool.MiscUtil;
import com.android.bitglobal.tool.RSAUtils;
import com.android.bitglobal.tool.Base64Utils;
import com.android.bitglobal.tool.SharedPreferences;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class SystemConfig implements Serializable {

    public final static String SUCCESS = "1000";
    //原来Locale.ENGLISH.getCountry()为"" 改为getLanguage-->en
    public static final String ENGLISH = "EN";
    public static final String SIMPLIFIED_CHINESE = "zh";
    public static SystemConfig instance;
    public static boolean deBugMode = AppContext.getDeBugMode();
    public final static String TEL = "400-888-8888";
    public static int mode = 0;
    public static String appSecret = "";
    public static String appKey = "";
    public final static String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJG14+94DEgzyd6G8+Ue+lpLKK9uIftpSZ7wvnX3jtw+6SUKldkvL1mYq9W8qIJD7w5t3YQIkVoWIlm5Eba5NcDYgfDC/QnYyr9zfDthlJECvQ8TC0wjy9cOtCC4FntewsqmGxLjTA17Zn0RJpsqXvNFjZEinR6IawvnlhPKJ/IwIDAQAB";


    public static String homeDef="";
    public static final String homeUSDC="usdc";
    public static final String homeBTC="btc";

    //法币名称
    public static final String AUD ="AUD";
    public static final String USD ="USD";
    public static final String CNY ="CNY";
    public static final String EUR ="EUR";
    public static final String GBP ="GBP";

    public static final String GBC = "GBC";
    public static final String ETC = "ETC";
    public static final String ZEC = "ZEC";
    public static final String DASH = "DASH";
    public static final String LTC = "LTC";
    public static final String ETH = "ETH";
    public static final String BTC = "BTC";
    public static final String USDC = "USDC";
    public static final int BALANCE_PRECISION = 6;



    public static String exchangeBtnType="";
    public static final String exchangeBtnTypeNormal="Normal";
    public static final String exchangeBtnTypePressed="Pressed";



    //官方公告
    public final static String getBlog() {
        String str = "";
        if (SystemConfig.getLanguageEnv().equals("CN")) {
            str = "https://www.android.com/i/blog";
        } else {
            str = "https://www.android.com/i/blog/i?lan=en";
        }
        return str;
    }

    //关于我们
    public final static String getAbout() {
        String str = "";
        if (SystemConfig.getLanguageEnv().equals("CN")) {
            str = "https://www.android.com/i";
        } else {
            str = "https://www.android.com/i?lan=en";
        }
        return str;
    }

    //中国比特币积分等级与费率标准
    public final static String getLevel() {
        String str = "";
        if (SystemConfig.getLanguageEnv().equals("CN")) {
            str = "https://www.android.com/i/document?item=11";
        } else {
            str = "https://www.android.com/i/document?item=11&lan=en";
        }
        return str;
    }

    //中国比特币融资融币业务使用协议
    public final static String getRzmbxy() {
        String str = "";
        if (SystemConfig.getLanguageEnv().equals("CN")) {
            str = "https://www.android.com/i/document?item=10";
        } else {
            str = "https://www.android.com/i/document?item=10&lan=en";
        }
        return str;
    }

    //中国比特币数字货币交易风险申明
    public final static String getJyfx() {
        String str = "";
        if (SystemConfig.getLanguageEnv().equals("CN")) {
            str = "https://www.android.com/i/document?item=7";
        } else {
            str = "https://www.android.com/i/document?item=7&lan=en";
        }
        return str;
    }

    //中国比特币用户协议
    public final static String getYhxy() {
        String str = "";
        if (SystemConfig.getLanguageEnv().equals("CN")) {
            str = "https://www.android.com/i/document?item=9";
        } else {
            str = "https://www.android.com/i/document?item=9&lan=en";
        }
        return str;
    }

    public String MARKET_API_URL = "";
    public String PRO_API_URL = "https://t.gbccoin.com/api/m/";
    public String P2P_API_URL = "";
    public String FILE_API_URL = "";
    public String TC_API_URL = "http://tt.vip.com/api/m/";
    // public String BTC123_API_URL="http://192.168.4.22:8177/api/m/";
    // public String BTC123_API_URL="http://192.168.2.31:8021/api/m/";
    public String BTC123_API_URL = "http://trans.bitglobal.com/api/m/";

    public final static SystemConfig shareInstance() {
        if (instance == null) {
            instance = new SystemConfig();
            instance.init();
        }
        return instance;
    }

    public String getApiUrl(int type) {
        String url = "";
        if (type == 1) {
            url = BTC123_API_URL;
        } else if (type == 2) {
            url = MARKET_API_URL;
        } else if (type == 3) {
            url = PRO_API_URL;
        } else if (type == 4) {
            url = P2P_API_URL;
        } else if (type == 5) {
            url = FILE_API_URL;
        } else if (type == 6) {
            url = TC_API_URL;
        }
        return url;
    }

    public void init() {
        //appKey=DeviceUtil.getDeviceId(AppContext.getInstance());
//        if(deBugMode){
//            appSecret="FHpBb7bFy2PMrceKWcMxVb1dA4GFZUUpXamPrKDl26CiWiux9RwCNA1FmtfadyHuLG6dYRxPv4JqQN4fUEzotqIk";
//            appKey="QA9pKB9pHK5XJJ6nkcb32q1gLAlU0ipBZvamYU9jNIQWjmSJkX7GALGo5sr4DZAdONFEb9ZeNqLMBE8EikO2fObe";
//
//            PRO_API_URL = "http://ww.vip.com/api/m/";
//            PRO_API_URL = "http://192.168.1.25:8080/api/m/";
//
//            MARKET_API_URL ="http://ww.vip.com/api/m/";
//            MARKET_API_URL ="http://192.168.1.25:8080/api/m/";
//
//            P2P_API_URL = "http://ww.vip.com/api/m/";
//            P2P_API_URL = "http://192.168.1.25:8080/api/m/";
//
//
//            FILE_API_URL = "http://img1.android.com/";
//            TC_API_URL = "http://ww.vip.com/api/m/";
//        }else{

        appSecret = "FHpBb7bFy2PMrceKWcMxVb1dA4GFZUUpXamPrKDl26CiWiux9RwCNA1FmtfadyHuLG6dYRxPv4JqQN4fUEzotqIk";
        appKey = "QA9pKB9pHK5XJJ6nkcb32q1gLAlU0ipBZvamYU9jNIQWjmSJkX7GALGo5sr4DZAdONFEb9ZeNqLMBE8EikO2fObe";
        //开发服务器地址
//        String Domain_URL = "http://192.168.2.35:8011";
//        BTC123_API_URL = "http://192.168.2.35:8021/api/m/";
        //测试服务器地址
        String Domain_URL = "https://www.bitstaging.com";
        BTC123_API_URL = "https://t.bitstaging.com/api/m/";
        //准生产环境
//        String Domain_URL = "https://www.gbccoin.com";
//        BTC123_API_URL = "https://t.gbccoin.com/api/m/";
        //新杰
//        String Domain_URL = "http://192.168.3.47:8080";
//        BTC123_API_URL="http://192.168.3.47:8081/api/m/";
        //正式服务器地址
//        String Domain_URL = "https://www.bitglobal.com";
//        BTC123_API_URL = "https://t.bitglobal.com/api/m/";
        //正式服务器relay地址
//        String Domain_URL = "https://relay.bitglobal.com";
//        BTC123_API_URL = "https://t.bitglobal.com/api/m/";
        //文征  before:192.168.3.121  now:192.168.1.153
//        String Domain_URL = "http://192.168.3.121:8080";
//        BTC123_API_URL = "http://192.168.3.121:8081/api/m/";
        //common服务器地址
//        String Domain_URL = "http://www.common.com";
//        BTC123_API_URL = "http://t.common.com/api/m/";
        //正式环境
        PRO_API_URL = Domain_URL + "/api/m/";
        MARKET_API_URL = Domain_URL + "/api/m/";
        P2P_API_URL = Domain_URL + "/api/m/";
        FILE_API_URL = "https://img1.android.com/";
        TC_API_URL = Domain_URL + "/api/m/";
//        }
    }

    public void changeBaseUrl() {
        switch (mode++ % 2) {
            case 0:
            case 1:
                appSecret = "FHpBb7bFy2PMrceKWcMxVb1dA4GFZUUpXamPrKDl26CiWiux9RwCNA1FmtfadyHuLG6dYRxPv4JqQN4fUEzotqIk";
                appKey = "QA9pKB9pHK5XJJ6nkcb32q1gLAlU0ipBZvamYU9jNIQWjmSJkX7GALGo5sr4DZAdONFEb9ZeNqLMBE8EikO2fObe";
                //开发服务器地址
                String Domain_URL = "http://test2.bitglobal.com";
                BTC123_API_URL = "http://trans2.bitglobal.com/api/m/";
                //新杰
                //正式环境
                PRO_API_URL = Domain_URL + "/api/m/";
                MARKET_API_URL = Domain_URL + "/api/m/";
                P2P_API_URL = Domain_URL + "/api/m/";
                FILE_API_URL = "https://img1.android.com/";
                TC_API_URL = Domain_URL + "/api/m/";
                break;
        }
        L.d(BTC123_API_URL);
//        }
    }

    /**
     * 数据请求-配置
     */
    public static Map<String, String> getHttpConfig(Map<String, String> map_ls) {
        String sign = "";
        Map<String, String> map = new TreeMap<String, String>(map_ls);
        Map<String, String> config = new TreeMap<String, String>();
        if (getLanguageEnv().equals("HK")) {
            map.put("lang", "0");
        } else if (getLanguageEnv().equals("CN")) {
            map.put("lang", "1");
        } else if ("EN".equals(getLanguageEnv())){
            map.put("lang", "2");
        }
//        map.put("lang", "2");
        map.put("osType", "2");
        map.put("appKey", appKey);
        map.put("token", UserDao.getInstance().getToken());
        if (UserDao.getInstance().getUserId() == null || UserDao.getInstance().getUserId().equals("")) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals("userId")) {
                    map.put("userId", entry.getValue());
                }
            }
        } else {
            map.put("userId", UserDao.getInstance().getUserId());
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            sign = sign + entry.getKey() + entry.getValue();
            config.put(entry.getKey(), entry.getValue());
        }
        sign = appSecret + MiscUtil.getMD5(sign.getBytes()).toLowerCase() + appKey;
        sign = MiscUtil.getMD5(sign.getBytes());
       /* sign=appSecret+MD5.toMD5(sign).toLowerCase()+appKey;
        sign=MD5.toMD5(sign);*/
        config.put("sign", sign.toLowerCase());
        if (AppContext.getDeBugMode()) {
            Log.e("config", config + "");
        }
        return config;
    }

    public static Map<String, String> getHttpConfigRSA(Map<String, String> map_ls) {
        String sign = "";
        Map<String, String> map = new TreeMap<String, String>(map_ls);
        Map<String, String> config = new TreeMap<String, String>();
        if (getLanguageEnv().equals("HK")) {
            map.put("lang", "0");
        } else if (getLanguageEnv().equals("CN")) {
            map.put("lang", "1");
        } else {
            map.put("lang", "2");
        }
//        map.put("lang", "2");
        map.put("appKey", appKey);
        map.put("osType", "2");
        map.put("token", UserDao.getInstance().getToken());
        map.put("userId", UserDao.getInstance().getUserId());
        String[] strArr = {"googleCode", "safePwd", "encryptNumber", "encryptEmail", "mobileCode", "dynamicCode", "password", "newPassword", "oldPassword", "oldSafePwd", "newSafePwd"};
        for (Map.Entry<String, String> entry : map.entrySet()) {
            config.put(entry.getKey(), entry.getValue());
            String str_v = entry.getValue();
            for (String str : strArr) {
                if (entry.getKey().equals(str) && entry.getValue() != null && entry.getValue() != "null" && entry.getValue() != "" && !entry.getValue().equals("")) {
                    try {
                        PublicKey publicKey = RSAUtils.loadPublicKey(pubKey);
                        byte[] encryptByte = RSAUtils.encryptData(entry.getValue().getBytes(), publicKey);
                        str_v = Base64Utils.encode(encryptByte);
                        config.put(entry.getKey(), str_v);
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }

            }
            sign = sign + entry.getKey() + str_v;
        }
        sign = appSecret + MiscUtil.getMD5(sign.getBytes()).toLowerCase() + appKey;
        sign = MiscUtil.getMD5(sign.getBytes());

        /*sign=appSecret+MD5.toMD5(sign).toLowerCase()+appKey;
        sign=MD5.toMD5(sign);*/
        config.put("sign", sign.toLowerCase());
        if (AppContext.getDeBugMode()) {
            Log.e("config", config + "");
        }
        return config;
    }

    public static Map<String, String> getHttpCheckCode(Map<String, String> map_ls) {
        String sign = "";
        Map<String, String> map = new TreeMap<String, String>(map_ls);
        Map<String, String> config = new TreeMap<String, String>();
        if (getLanguageEnv().equals("HK")) {
            map.put("lang", "0");
        } else if (getLanguageEnv().equals("CN")) {
            map.put("lang", "1");
        } else {
            map.put("lang", "2");
        }
//        map.put("lang", "2");
        map.put("appKey", appKey);
        map.put("osType", "2");
        map.put("token", UserDao.getInstance().getToken());
        map.put("userId", UserDao.getInstance().getUserId());
        String[] strArr = {"googleCode", "safePwd", "encryptNumber", "encryptEmail", "mobileCode", "password", "newPassword", "oldPassword", "oldSafePwd", "newSafePwd"};
        for (Map.Entry<String, String> entry : map.entrySet()) {
            config.put(entry.getKey(), entry.getValue());
            String str_v = entry.getValue();
            for (String str : strArr) {
                if (entry.getKey().equals(str) && entry.getValue() != null && entry.getValue() != "null" && entry.getValue() != "" && !entry.getValue().equals("")) {
                    try {
                        PublicKey publicKey = RSAUtils.loadPublicKey(pubKey);
                        byte[] encryptByte = RSAUtils.encryptData(entry.getValue().getBytes(), publicKey);
                        str_v = Base64Utils.encode(encryptByte);
                        config.put(entry.getKey(), str_v);
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }

            }
            sign = sign + entry.getKey() + str_v;
        }
        sign = appSecret + MiscUtil.getMD5(sign.getBytes()).toLowerCase() + appKey;
        sign = MiscUtil.getMD5(sign.getBytes());

        /*sign=appSecret+MD5.toMD5(sign).toLowerCase()+appKey;
        sign=MD5.toMD5(sign);*/
        config.put("sign", sign.toLowerCase());
        if (AppContext.getDeBugMode()) {
            Log.e("config", config + "");
        }
        return config;
    }

    /**
     * 数据请求-注册
     * <p>
     * countryCode 区号（+86）中国
     * userName 用户名
     * mobileNumber 手机号
     * dynamicCode 验证码
     * password 登录密码
     * safePwd 资金密码
     * email Email
     */

    public static List<String> getHttpRegister() {
        List<String> param = new ArrayList<String>();
        param.add("userName");
        param.add("countryCode");
        param.add("mobileNumber");
        param.add("dynamicCode");
        param.add("password");
        param.add("email");
        return param;
    }

    /**
     * 数据请求-用户登录
     * <p>
     * userName		   用户名/手机号/邮箱
     * password	       登录密码（RSA加密）
     * mobileCode	   短信验证码
     * googleCode      谷歌验证码
     * countryCode     国家码，如果有值时代表是手机登录
     */

    public static List<String> getHttpLogin() {
        List<String> param = new ArrayList<String>();
        param.add("userName");
        param.add("password");
        param.add("dynamicCode");
        param.add("googleCode");
        param.add("countryCode");
        return param;
    }

    /**
     * 数据请求-发送验证码
     * <p>
     * countryCode		   区号
     * encryptNumber	  RSA加密手机号
     * encryptEmail       RSA加密邮箱
     * type	              1:注册,2:找回密码 3 :其他 4:提现人民币 5:提 现比特币 6:提现莱特币
     */
    public static List<String> getHttpSendCode() {
        List<String> param = new ArrayList<String>();
        param.add("countryCode");
        param.add("encryptNumber");
        param.add("encryptEmail");
        param.add("type");
        param.add("currency");
        return param;
    }

    /**
     * 数据请求-发送验证码
     * <p>
     * countryCode		   区号
     * encryptNumber	  RSA加密手机号
     * encryptEmail       RSA加密邮箱
     * type	              1:注册,2:找回密码 3 :其他 4:提现人民币 5:提 现比特币 6:提现莱特币
     */
    public static List<String> getHttpCheckCode() {
        List<String> param = new ArrayList<String>();
        param.add("countryCode");
        param.add("encryptNumber");
        param.add("dynamicCode");
        param.add("type");
        param.add("encryptEmail");
        return param;
    }

    /**
     * 数据请求-用户发送验证码
     * type	 1:注册,2:找回密码 3 :其他 4:提现人民币 5:提现比特币 6:提现莱特币 7:提现以太币 8:设置谷歌验证码 9:修改资金密 码 10:异地登录短信验证 15:异地登录短信验证
     */
    public static List<String> getHttpUserSendCode() {
        List<String> param = new ArrayList<String>();
        param.add("type");
        return param;
    }


    /**
     * 数据请求-找回密码
     * <p>
     * method       找回方式 1:手机 2:邮箱
     * countryCode  区号,手机找回时必须传入
     * mobileNumber 手机号,手机找回时必须传入
     * email        邮箱,邮箱找回时必须传入
     * dynamicCode  动态(短信、邮箱)验证码 (RSA加密)
     * googleCode    谷歌验证码
     * newPassword  登录密码(RSA加密)
     */

    public static List<String> getHttpChangePwd() {
        List<String> param = new ArrayList<String>();
        param.add("method");
        param.add("countryCode");
        param.add("mobileNumber");
        param.add("email");
        param.add("dynamicCode");
        param.add("googleCode");
        param.add("newPassword");
        return param;
    }


    /**
     * 数据请求-修改资金密码
     * <p>
     * oldSafePwd	   旧资金密码
     * newPassword     登录密码
     * mobileCode	   短信验证码
     */

    public static List<String> getHttpResetSafePwd() {
        List<String> param = new ArrayList<String>();
        param.add("oldSafePwd");
        param.add("newSafePwd");
        param.add("mobileCode");
        return param;
    }

    /**
     * 数据请求-修改密码
     * type
     * oldPassword	   旧登录密码
     * newPassword     新登录密码
     * dynamicCode
     * googleCode
     */

    public static List<String> getHttpResetPwd() {
        List<String> param = new ArrayList<String>();
        param.add("type");
        param.add("oldPassword");
        param.add("newPassword");
        param.add("dynamicCode");
        param.add("googleCode");
        return param;
    }

    /**
     * 数据请求-设置谷歌验证
     * secret        密钥
     * type	         类型 1 设置/修改谷歌验证 0 关闭谷歌验证
     * mobileCode    短信验证码
     * googleCode    谷歌验证码
     */

    public static List<String> getHttpSetGoogleCode() {
        List<String> param = new ArrayList<String>();
        param.add("secret");
        param.add("type");
        param.add("dynamicCode");
        param.add("googleCode");
        return param;
    }

    /**
     * 开启/关闭Google登录/提现验证
     * operation     操作类型 0 关闭 1 开启
     * authType	     验证类型 1：登录验证2：提现验证
     * googleCode    谷歌验证码（RSA加密）关闭提现验证时需要谷歌验证码
     */

    public static List<String> getHttpChangeGoogleAuth() {
        List<String> param = new ArrayList<String>();
        param.add("operation");
        param.add("authType");
        param.add("googleCode");
        return param;
    }


    /**
     * 根据市场卖买行情数据
     * <p>
     * length	     数据长度,可传入 5,10,20,50
     * depth	     深度间距,0.1 0.3 0.5 1
     * currencyType	 货币类型
     * exchangeType  兑换货币类型
     */

    public static List<String> getHttpMarketDepth() {
        List<String> param = new ArrayList<String>();
        param.add("length");
        param.add("currencyType");
        param.add("exchangeType");
        return param;
    }

    /**
     * 委托下单数据
     * <p>
     * timeStamp	 时间戳
     * type	         类型  1:买入 0:卖出
     * currencyType  货币类型
     * exchangeType  兑换货币类型
     * isPlan        1:计划/委托交易 0:立即交易
     * unitPrice     买入/卖出单价
     * number        买入/卖出数量
     * safePwd 资金密码 (RSA加密)
     */
    public static List<String> getHttpDoEntrust() {
        List<String> param = new ArrayList<String>();
        param.add("timeStamp");
        param.add("type");
        param.add("currencyType");
        param.add("exchangeType");
        param.add("isPlan");
        param.add("unitPrice");
        param.add("number");
        param.add("safePwd");
        return param;
    }

    /**
     * 委托下单数据
     * <p>
     * type	         类型  1:买入 0:卖出 -1:不限制
     * currencyType  货币类型
     * exchangeType  兑换货币类型
     * dayIn3        3天内数据 0:否 1:是 默认1
     * status        0不限制 1 已取消成功 2 交易成功 3 交易中(未完全成交)
     * pageIndex     页码
     * pageSize      每页显示数量
     */

    public static List<String> getHttpEntrustRecord() {
        List<String> param = new ArrayList<String>();
        param.add("type");
        param.add("currencyType");
        param.add("exchangeType");
        param.add("dayIn3");
        param.add("status");
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    /**
     * 批量取消交易
     * <p>
     * type	         取消类型 1:买入 2:卖出 3:全部
     * currencyType  货币类型
     * exchangeType  兑换货币类型
     */
    public static List<String> getHttpCancelAllOrders() {
        List<String> param = new ArrayList<String>();
        param.add("type");
        param.add("currencyType");
        param.add("exchangeType");
        return param;
    }

    /**
     * 取消单笔交易
     * <p>
     * currencyType  货币类型
     * exchangeType  兑换货币类型
     * entrustId     交易id
     */
    public static List<String> getHttpCancelOrder() {
        List<String> param = new ArrayList<String>();
        param.add("currencyType");
        param.add("exchangeType");
        param.add("entrustId");
        return param;
    }

    /**
     * 根据市场卖买行情数据
     * <p>
     * currencyType	 货币类型
     * exchangeType  兑换货币类型
     * step
     * size
     */

    public static List<String> getHttpIndexMarketChart() {
        List<String> param = new ArrayList<String>();
        param.add("currencyType");
        param.add("exchangeType");
        param.add("step");
        param.add("size");
        return param;
    }

    /**
     * 修改提现地址备注
     * <p>
     * currencyType  货币类型
     * withdrawAddressId  提现地址ID
     * memo     备注
     */
    public static List<String> getHttpUpdateWithdrawAddressMemo() {
        List<String> param = new ArrayList<String>();
        param.add("currencyType");
        param.add("withdrawAddressId");
        param.add("memo");
        return param;
    }

    /**
     * 修改提现地址备注
     * <p>
     * currencyType 货币类型
     * cashAmount 提现金额
     * receiveAddress 接收地址
     * liuyan 留言
     * feeInfoId
     * isInnerTransfer
     * safePwd 资金密码 (RSA加密)
     * googleCode google验证码(RSA加密)
     * dynamicCode 动态(短信、邮件)验证码(RSA加密)
     */
    public static List<String> getHttpWithdraw() {
        List<String> param = new ArrayList<String>();
        param.add("currencyType");
        param.add("cashAmount");
        param.add("receiveAddress");
        param.add("safePwd");
        param.add("dynamicCode");
        param.add("googleCode");
        return param;
    }

    /**
     * 查询提现账单
     * <p>
     * currencyType  货币类型
     * status  状态 -1:全部 0:打币中 1:失败 2:成功 3:已取消
     * pageIndex     页码
     * pageSize     每页显示数量
     */
    public static List<String> getHttpSearchWithdraw() {
        List<String> param = new ArrayList<String>();
        param.add("currencyType");
        param.add("status");
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    /**
     * 取消BTC/LTC/ETH提现
     * <p>
     * currencyType  货币类型
     * withdrawId    提现记录ID
     * safePwd       资金密码
     */
    public static List<String> getHttpCancelWithdraw() {
        List<String> param = new ArrayList<String>();
        param.add("currencyType");
        param.add("withdrawId");
        param.add("safePwd");
        return param;
    }

    /**
     * 获取免息券
     * <p>
     * currencyType  货币类型
     * status    状态值 用”|”隔开
     * pageIndex     页码
     * pageSize     每页显示数量
     */
    public static List<String> getHttpInterestFree() {
        List<String> param = new ArrayList<String>();
        param.add("currencyType");
        param.add("status");
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    /**
     * 兑换免息券
     * <p>
     * type            兑换类型 1密钥兑换 2 积分兑换
     * secret          密钥
     * pointNumber     欲兑换积分
     * safePwd         资金密码
     */
    public static List<String> getHttpExchangeInterestFree() {
        List<String> param = new ArrayList<String>();
        param.add("type");
        param.add("secret");
        param.add("pointNumber");
        param.add("safePwd");
        return param;
    }

    /**
     * 融资融币­借款
     * <p>
     * currencyType    货币类型
     * amount          借入金额
     * ticketId        免息卷id
     * safePwd         资金密码(RSA加密)
     */
    public static List<String> getHttpDoLoan() {
        List<String> param = new ArrayList<String>();
        param.add("currencyType");
        param.add("amount");
        param.add("ticketId");
        param.add("safePwd");
        return param;
    }

    /**
     * 融资融币­借款记录
     * <p>
     * currencyType  货币类型
     * status        借款状态 1还款中 2已还款 3需要平仓 4平仓还款(不传时 表 示查询全部)
     * isIn          区分借入/借出 1 借入记录 0 借出记录
     * pageIndex     页码
     * pageSize      每页显示数量
     */
    public static List<String> getHttpLoanRecords() {
        List<String> param = new ArrayList<String>();
        param.add("currencyType");
        param.add("status");
        param.add("isIn");
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    /**
     * 融资融币­一键还款
     * <p>
     * loanRecords   json数组[{“id”:”10”,”repayAmount”:”30.0”,”isPart”:1} ,]
     * 说明:id: 借款记录id repayAmount: 本次还款本金 isPart: 1部分还款 0 全部还款
     * safePwd      资金密码(RSA加密)
     */
    public static List<String> getHttpFastRepay() {
        List<String> param = new ArrayList<String>();
        param.add("loanRecords");
        param.add("safePwd");
        return param;
    }

    /**
     * 融资融币­还款记录
     * <p>
     * loanRecordId  借款记录id
     * pageIndex     页码
     * pageSize      每页显示数量
     */
    public static List<String> getHttpRepayRecords() {
        List<String> param = new ArrayList<String>();
        param.add("loanRecordId");
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    /**
     * 融资融币­还款记录
     * <p>
     * frontalImg      身份证正面照
     * backImg         身份证背面照
     * loadImg         手持身份证照
     * proofAddressImg 住址证明照
     * bankCardId      银行卡号
     * bankTel         银行预留手机号
     * bankCardType    银行id
     * realName        真实姓名
     * cardId          证件号
     * country         证件所属国家
     * type            操作类型 1：保存2：提交审核
     */
    public static List<String> getHttpDepthIdentityAuth() {
        List<String> param = new ArrayList<String>();
        param.add("frontalImg");
        param.add("backImg");
        param.add("loadImg");
        param.add("proofAddressImg");
        param.add("bankCardId");
        param.add("bankTel");
        param.add("bankId");
        param.add("realName");
        param.add("cardId");
        param.add("country");
        param.add("type");
        return param;
    }


    /**
     * 上传图像
     * file_upload_stat	Integer	是	=1
     * plan_task_name	Integer	是	=goods
     * userId	Integer	是	用户id
     * userType	Integer	是	=1
     * savePicSize	Boolean	是	=false
     * auth	Boolean	是	=true
     * _fma.pu._0.ima	是		图片数据
     * rs	Integer	是	手机端=1
     */
    public static List<String> updateImage() {
        List<String> param = new ArrayList<String>();
        param.add("file_upload_stat");
        param.add("plan_task_name");
        param.add("userId");
        param.add("userType");
        param.add("savePicSize");
        param.add("auth");
        param.add("_fma.pu._0.ima");
        param.add("rs");
        return param;
    }

    /**
     * 查询账单
     * <p>
     * dataType 0:30天内数据 1:30天前数据
     * currencyType
     * pageIndex 页码 从1开始
     * pageSize 每页显示数量
     */
    public static List<String> getHttpSearchBill() {
        List<String> param = new ArrayList<String>();
        param.add("type");
        param.add("currencyType");
        param.add("dataType");
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    /**
     * 提交银行汇款单
     * <p>
     * type 方式 1:线下汇款 2:支付宝
     * rechargeBankId 付款银行静态ID(1=支付宝)
     * rechargeAmount 充值金额
     * realName 用户账户真实姓名
     * cardNumber 用户账户卡号(支付宝账号)
     * bankReId 用户账户的id(支付宝账户id)
     * remark 备注
     */
    public static List<String> getHttpSubmitRecharge() {
        List<String> param = new ArrayList<String>();
        param.add("type");
        param.add("rechargeBankId");
        param.add("rechargeAmount");
        param.add("realName");
        param.add("cardNumber");
        param.add("bankReId");
        param.add("remark");
        return param;
    }

    /**
     * 人民币充值/提现记录
     * <p>
     * type 1：充值 2：提现
     * status 状态值 0 充值成功 1 充值失败 2 处理中 3 提现中，等待处理 4 提现成功 5 提现失败 6 等待确认 8 已取消 9 等待汇款/确认 10 等待实名认证 其他状态值显示“–” , 获取全部记录时不传此参数
     * pageIndex 页码 从1开始
     * pageSize
     */
    public static List<String> getHttpRechargeList() {
        List<String> param = new ArrayList<String>();
        param.add("type");
        param.add("status");
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    /**
     * RMB提现操作
     * <p>
     * bankRid         历史提现银行卡id（新银行卡不传）
     * bankStaticId    银行静态id
     * province        所属省编号
     * city            所属市编号
     * branchName
     * isDefault
     * acountId        银行卡号
     * realName	       真实姓名
     * withdrawAmount  提现金额
     * safePwd         资金密码 （RSA加密）
     * dynamicCode     动态（短信、邮件）验证码（RSA加密）
     * googleCode      google验证码（RSA加密）
     */
    public static List<String> getHttpRmbWithdraw() {
        List<String> param = new ArrayList<String>();
        param.add("bankRid");
        param.add("bankStaticId");
        param.add("province");
        param.add("city");
        param.add("branchName");
        param.add("isDefault");
        param.add("acountId");
        param.add("realName");
        param.add("withdrawAmount");
        param.add("safePwd");
        param.add("dynamicCode");
        param.add("googleCode");
        return param;
    }

    /**
     * RMB提现操作
     * <p>
     * type            验证策略类型
     * category        验证分类  （1. 登录 2. 交易 3. 提现）
     * safePwd         资金密码 （RSA加密）
     * dynamicCode     动态（短信、邮件）验证码（RSA加密）
     * googleCode      google验证码（RSA加密）
     */
    public static List<String> getHttpChangeAuth() {
        List<String> param = new ArrayList<String>();
        param.add("type");
        param.add("category");
        param.add("safePwd");
        param.add("dynamicCode");
        param.add("googleCode");
        return param;
    }

    public static String getLanguageEnv() {
//        Locale l = Locale.getDefault();
//        String country = l.getCountry().toLowerCase();
//        if ("zh".equals(language)) {
//            if ("cn".equals(country)) {
//                 language = "zh-CN";
//            } else if ("tw".equals(country)) {
//                language = "zh-TW";
//            }
//            return "cn";
//        } else {
//            return "en";
//        }
        String language = SharedPreferences.getInstance().getString(SharedPreferences.APP_LANGUAGE,
                SystemConfig.ENGLISH);
        if(SIMPLIFIED_CHINESE.equals(language)) {//zh-->CN
            language = "CN";
        } else if (Locale.TRADITIONAL_CHINESE.getCountry().equals((language))) {//TW-->HK
            language = "HK";
        } else if (ENGLISH.equals(language)){//en-->EN
            language = "EN";
        }
        return language;
    }

    public static double getRandomMoney(String userId) {
        int point = 0;
        if (userId.length() >= 2) {
            userId = userId.substring(userId.length() - 2, userId.length());
        }
        point = Integer.parseInt(userId);
        double d = new BigDecimal(point).divide(new BigDecimal(100)).setScale(2).doubleValue();
        if (d <= 0) {
            d = 0.01D;
        }
        return d;
    }

}
