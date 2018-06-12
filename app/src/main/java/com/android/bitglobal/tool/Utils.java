package com.android.bitglobal.tool;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.dao.UserDao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by tiansj on 15/7/29.
 */
public class Utils {

    private static final String TAG = "Utils";

    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
    }


    /**
     * 设置手机网络类型，wifi，cmwap，ctwap，用于联网参数选择
     * @return
     */
    static String getNetworkType() {
        String networkType = "wifi";
        ConnectivityManager manager = (ConnectivityManager) AppContext.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();
        if (netWrokInfo == null || !netWrokInfo.isAvailable()) {
            return ""; // 当前网络不可用
        }

        String info = netWrokInfo.getExtraInfo();
        if ((info != null)
                && ((info.trim().toLowerCase().equals("cmwap"))
                || (info.trim().toLowerCase().equals("uniwap"))
                || (info.trim().toLowerCase().equals("3gwap")) || (info
                .trim().toLowerCase().equals("ctwap")))) {
            // 上网方式为wap
            if (info.trim().toLowerCase().equals("ctwap")) {
                // 电信
                networkType = "ctwap";
            } else {
                networkType = "cmwap";
            }
        }
        return networkType;
    }

    /**
     * 检测网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }




    static String getString(Context context, int resId){
        return context.getResources().getString(resId);
    }


    public static byte[] getImage(String urlImage) {
        byte[] data = null;
        try {
            //建立URL
            URL url = new URL(urlImage);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setRequestProperty("Authorization", UserDao.getInstance().getToken());
            InputStream input = conn.getInputStream();
            data = readInputStream(input);
            input.close();

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;
    }
    public static byte[] readInputStream(InputStream input) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = input.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return output.toByteArray();
    }

    public static boolean check(String s)
    {
        return s.matches("^(?![0-9]+$)(?![a-zA-Z]+$)(?!([^(0-9a-zA-Z)]|[\\(\\)])+$)([^(0-9a-zA-Z)]|[\\(\\)]|[a-zA-Z]|[0-9]){8,20}$");
        //^(?![\d]+$)(?![a-zA-Z]+$)(?![^\da-zA-Z]+$).{8,20}$
       // return s.matches("^[\\x21-\\x7E]{8,20}$");
       // return s.matches("^(?![\\d]+$)(?![a-zA-Z]+$)(?![^\\da-zA-Z]+$).{8,20}$");
    }

    //获取pakeageInfo
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 返回本币币种小数位数
     */
    public static int getPrecisionCurrency(String currencyType, String exchangeType) {
        currencyType = currencyType.toUpperCase();
        exchangeType = exchangeType.toUpperCase();
        int currencyTypePrecision = 0;
        if(exchangeType.equals(SystemConfig.BTC)) {
            switch (currencyType) {
                case SystemConfig.ETC:
                    return 3;
                case SystemConfig.ZEC:
                    return 4;
                case SystemConfig.DASH:
                    return 4;
                case SystemConfig.LTC:
                    return 3;
                case SystemConfig.ETH:
                    return 4;
                case SystemConfig.GBC:
                    return 2;
            }
        } else if(exchangeType.equals(SystemConfig.USDC)) {
            switch (currencyType) {
                case SystemConfig.LTC:
                    return 3;
                case SystemConfig.ETH:
                    return 4;
                case SystemConfig.BTC:
                    return 5;
            }
        }
        return currencyTypePrecision;
    }

    /**
     * 返回兑换币币种小数位数
     */
    public static int getPrecisionExchange(String currencyType, String exchangeType) {
        currencyType = currencyType.toUpperCase();
        exchangeType = exchangeType.toUpperCase();
        int exchangeTypePrecision = 0;
        if(exchangeType.equals(SystemConfig.BTC)) {
            switch (currencyType) {
                case SystemConfig.ETC:
                    return 7;
                case SystemConfig.ZEC:
                    return 6;
                case SystemConfig.DASH:
                    return 6;
                case SystemConfig.LTC:
                    return 6;
                case SystemConfig.ETH:
                    return 6;
                case SystemConfig.GBC:
                    return 7;
            }
            return getLegalCoinPrecision(currencyType);
        } else if(exchangeType.equals(SystemConfig.USDC)) {
            switch (currencyType) {
                case SystemConfig.LTC:
                    return 3;
                case SystemConfig.ETH:
                    return 2;
                case SystemConfig.BTC:
                    return 1;
                case SystemConfig.GBC:
                    return 5;
            }
            return getLegalCoinPrecision(currencyType);
        }
        return exchangeTypePrecision;
    }

    /**
     * 返回本币保留小数位 持有量
     * currencyType 当前币种名称
     */
    public static int getPrecisionAmount(String currencyType, String exchangeType) {
        currencyType = currencyType.toUpperCase();
        exchangeType = exchangeType.toUpperCase();
        int currencyTypePrecision = 0;
        if(exchangeType.equals(SystemConfig.BTC)) {
            switch (currencyType) {
                case SystemConfig.ETC:
                    return 3;
                case SystemConfig.ZEC:
                    return 4;
                case SystemConfig.DASH:
                    return 4;
                case SystemConfig.LTC:
                    return 3;
                case SystemConfig.ETH:
                    return 4;
                case SystemConfig.GBC:
                    return 2;
            }
            return getLegalCoinPrecision(currencyType);
        } else if(exchangeType.equals(SystemConfig.USDC)) {
            switch (currencyType) {
                case SystemConfig.LTC:
                    return 3;
                case SystemConfig.ETH:
                    return 4;
                case SystemConfig.BTC:
                    return 5;
            }
            return getLegalCoinPrecision(currencyType);
        }
        return currencyTypePrecision;
    }

    /**
     * 返回本币保留小数位 持有量
     * currencyType 当前币种名称
     */
    public static int getPrecisionAmount(String currencyType) {
        int precision = 4;
        try {
            currencyType=currencyType.toUpperCase();
            switch (currencyType){
                case SystemConfig.GBC:
                    return 2;
                case SystemConfig.LTC:
                case SystemConfig.ETC:
                    return 3;
                case SystemConfig.ETH:
                case SystemConfig.ZEC:
                case SystemConfig.DASH:
                    return 4;
                case SystemConfig.BTC:
                    return 5;
            }
            return getLegalCoinPrecision(currencyType);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return precision;
    }



    /**
     * 法币 价格显示 返回小数精度
     * @return
     */
    public static int getLegalCoinPrecision(String coinName){
        switch (coinName){
            case SystemConfig.AUD:
            case SystemConfig.USD:
            case SystemConfig.CNY:
            case SystemConfig.EUR:
            case SystemConfig.GBP:
                return 2;
            default:return 2;
        }
    }


    /**
     * 返回最小交易金额
     */
    public static float getMinAmount() {
        return 0.0001f;
    }

    //app是否挂起
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                    /*
                    BACKGROUND=400 EMPTY=500 FOREGROUND=100
                    GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                     */
//                Log.i(context.getPackageName(), "此appimportace ="
//                        + appProcess.importance
//                        + ",context.getClass().getName()="
//                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                    Log.i(context.getPackageName(), "处于后台"
//                            + appProcess.processName);
                    return true;
                } else {
//                    Log.i(context.getPackageName(), "处于前台"
//                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 获取设备联网的ip地址
     * @param context
     * @return
     */
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

}
