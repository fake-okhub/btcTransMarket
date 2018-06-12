package com.android.bitglobal.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.text.TextUtils;

import com.android.bitglobal.R;
import com.android.bitglobal.activity.CustomScanActivity;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.activity.user.GestureActivity;
import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.ui.UIHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.tendcloud.tenddata.TCAgent;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import io.realm.Realm;

/**
 * xiezuofei
 * 2016-06-24 11:20
 * 793169940@qq.com
 *
 */

public class AppContext extends Application {

    private static AppContext app;
    private static Boolean deBugMode=true;
    private static Realm realm;
    public static Context appContext = null;
    public AppContext() {
        app = this;
    }
    public static synchronized AppContext getInstance() {
        if (app == null) {
            app = new AppContext();
        }
        return app;
    }
    private int time_sd=1000*60*3;
    private String aClassName="";
    // 是否出于后台
    private boolean isBackground = false;
    public static Realm getRealm() {
        return realm;
    }
    public static void setRealm(Realm r) {
        realm = r;
    }
    public static Boolean getDeBugMode() {
        return deBugMode;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        initDataBase();
        appContext=getApplicationContext();
        deBugMode = !getPackageName().equals("com.android.android");
        deBugMode=false;
        //TalkingData
        TCAgent.init(app, "59D07569ED884AABAD06F3D54426F990", "android_app_v" + getVersion().versionName);
        TCAgent.setReportUncaughtExceptions(true);
        JPushInterface.setDebugMode(deBugMode); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        String user_lock= SharedPreferences.getInstance().getString(SharedPreferences.KEY_USER_LOCK,"");
        if(user_lock.equals("1")){
            handler.postDelayed(runnable, 0);
        }
       // registerUncaughtExceptionHandler();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                long currentTimeMillis = System.currentTimeMillis();
                //时间差
                long temp = currentTimeMillis - lastTimeMillis;
                if(temp > time_sd&&isBackground){
                    isBackground=false;
                    verify();
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                isBackground=true;
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private void initDataBase() {
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                RealmConfiguration realmConfig = new RealmConfiguration.Builder(AppContext.this).build();
//                try {
//                    realm= Realm.getInstance(realmConfig);
//                } catch (RealmMigrationNeededException e){
//                    Realm.deleteRealm(realmConfig);
//                    realm= Realm.getInstance(realmConfig);
//                }
//            }
//        });
    }

    // 注册App异常崩溃处理器
    private void registerUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
    }
    public PackageInfo getVersion() {
        try {
            PackageManager manager = app.getPackageManager();
            PackageInfo info = manager.getPackageInfo(app.getPackageName(), 0);
            return info;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 实现文本复制功能
     * add by wangqianzhou
     * @param content
     */
    public void copy(String content, Context context)
    {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
        UIHelper.ToastMessage(context, R.string.user_fzcg_toast);
    }
    /**
     * 实现粘贴功能
     * add by wangqianzhou
     * @param context
     * @return
     */
    public String paste(Context context)
    {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        String str="";
        if(cmb.getText()!=null){
            str=cmb.getText().toString().trim();
        }
        if(str.equals("")){
            UIHelper.ToastMessage(context, R.string.user_ztsb_toast);
        }
        return str;
    }


    private static Timer timer ;
    private static TimerTask timerTask;
    private static boolean isRunning;
    // 上一次触碰时间
    long lastTimeMillis = 0;
/*关闭检测手势*/
  public void setLastTouchTime(){
        // 最新的触碰时间
        long currentTimeMillis = System.currentTimeMillis();
        if(lastTimeMillis == 0){
            // 第一次触碰
            lastTimeMillis = currentTimeMillis;
            // 开启监听
            startVerify();
        }else{
            //时间差
            long temp = currentTimeMillis - lastTimeMillis;
            // 如果时间差小于time_sd分钟，就先停掉前一次的监听，再重新开启
            if(temp < time_sd){
                stopVerify();
                startVerify();
            }
            lastTimeMillis=currentTimeMillis;
            // else 如果大于,那么上一次的监听在运行着，time_sd分钟之后自然会锁定
        }
    }
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            if(MainActivity.getInstance()!=null){
                verify();
            }else{
                handler.postDelayed(runnable, 0);
            }
        }
    };
/*检测手势*/
private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                verify();
        }


    };
    /**
     * 开启检测界面
     *
     * @param
     * @return void
     * @throws
     */
    public void verify() {
        boolean isTopRunning = isRunningForeground(getApplicationContext());
        String user_lock= SharedPreferences.getInstance().getString(SharedPreferences.KEY_USER_LOCK,"");

        if(isTopRunning&&user_lock.equals("1")){
            // 判断检测界面是否已经运行
            if(GestureActivity.activity==null||!GestureActivity.activity.type.equals("3")||!aClassName.equals("GestureActivity")){
                if(GestureActivity.activity!=null&&!GestureActivity.activity.type.equals("3")){
                    GestureActivity.activity.finish();
                }
                Bundle bundle = new Bundle();
                bundle.putString("type","3");
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), GestureActivity.class);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

        }
    }
    /**
     * 5分钟一次弹出
     *
     * @param
     * @return void
     * @throws
     */
    public void startVerify(){
        if(timer == null){
            timer = new Timer();
        }
        if(timerTask == null){
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(0);
                }
            };
        }
        if(!isRunning){
            timer.schedule(timerTask, time_sd, time_sd);
            isRunning = true;
        }
    }
    /**
     * 停止检测
     *
     * @param
     * @return void
     * @throws
     */
    public static void stopVerify(){
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        if(timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }
        isRunning = false;
    }


    /**
     * 是否在前台运行
     *
     * @param @param context
     * @param @return
     * @return boolean
     * @throws
     */
    public boolean isRunningForeground (Context context)
    {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        aClassName=cn.getClassName();
        String currentPackageName = cn.getPackageName();
//	    ComponentInfo{com.android.systemui/com.android.systemui.recent.RecentsActivity}
//	    ComponentInfo{com.android.systemui/com.android.systemui.recent.RecentAppFxActivity}
        if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(getPackageName()))
        {
            return true ;
        }
        return false ;
    }
    public static void customScan(Activity activity){
        new IntentIntegrator(activity)
                .setOrientationLocked(false)
                .setCaptureActivity(CustomScanActivity.class) // 设置自定义的activity是CustomActivity
                .initiateScan(); // 初始化扫描
    }

    /**
     * 判断某个activity是否在前台运行
     *
     * @param @param context
     * @param @return
     * @return boolean
     * @throws
     */
    public boolean isRunningForeground (Context context,String className)
    {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false ;
    }

}

