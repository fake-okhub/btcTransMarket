package com.android.bitglobal.activity;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.bitglobal.entity.BTC123MarketData;
import com.android.bitglobal.entity.MarketDataResult;
import com.android.bitglobal.R;
import com.android.bitglobal.http.HttpMethods;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rx.Subscriber;

/**
 * 悬浮窗Service 该服务会在后台一直运行一个悬浮的透明的窗体
 *
 * @author Administrator
 *
 */
public class FloatService extends Service implements View.OnTouchListener {
    private static final String TAG = "Nian";
    Context context;
    WindowManager mWindowManager;
    WindowManager.LayoutParams mWindowParams;
    View showImg;
    TextView tv_nic_price,tv_nic_type;
    long ACTION_DOWN_TIME = 0;
    long ACTION_UP_TIME = 0;
    int mWindowParams_x_old = 0;
    int mWindowParams_y_old = 0;
    int select_index=0;
    double price_btc=0.0,price_eth=0.0,price_ltc=0.0;
    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;
    //行情数组

    Timer timerOfMarketPrice = null;                 //刷新行情定时器
    private final int kMarketDataRefreshTime = 4 * 1000;                         //数据刷新间隔


    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    /**
     *判断当前应用程序处于前台还是后台
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        context=this;
        createView();

        startTimer();
        final IntentFilter filter = new IntentFilter();
        // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);
        // 屏幕解锁广播
        filter.addAction(Intent.ACTION_USER_PRESENT);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                Log.d(TAG, "onReceive");
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    startTimer();
                    Log.i(TAG, "screen on");
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    stopTimer();
                    Log.i(TAG, "screen off");
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    Log.i(TAG, "screen unlock");
                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                    Log.i(TAG, " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
                }
            }
        };
        registerReceiver(mBatInfoReceiver, filter);
    }
    String symbol_str[]={"androidbtccny","androidltccny","androidethcny","androidetccny"};
    String str = "androidbtccny";
    private void marketlist() {

            Subscriber subscriber= new Subscriber<MarketDataResult>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(MarketDataResult marketDataResult) {
                    if(marketDataResult.isSuc()){
                        BTC123MarketData btc123MarketData=marketDataResult.getDatas();
                        try {
                            double pr= Double.parseDouble(btc123MarketData.getTicker().getLast());
                            double pr_s=0.0;
                            tv_nic_price.setText("￥"+btc123MarketData.getTicker().getLast());
                            if(str.equals("androidbtccny")){
                                tv_nic_type.setText("android-BTC");
                                pr_s=price_btc;
                                price_btc=pr;
                            }else if(str.equals("androidltccny")){
                                tv_nic_type.setText("android-LTC");
                                pr_s=price_ltc;
                                price_ltc=pr;
                            }else if(str.equals("androidethcny")){
                                tv_nic_type.setText("android-ETH");
                                pr_s=price_eth;
                                price_eth=pr;
                            } else if(str.equals("androidetccny")){
                                tv_nic_type.setText("android-ETC");
                                pr_s=price_eth;
                                price_eth=pr;
                            }
                            if(pr<pr_s){
                                tv_nic_price.setTextColor(Color.parseColor("#1BA905"));
                            }else{
                                tv_nic_price.setTextColor(Color.parseColor("#E70101"));
                            }
                            showHeaderAnimation(tv_nic_price);
                         /*   if(symbol == Constants.BTC123TickerType.android_BTC_CNY){
                                MarketFragment.marketPrice("BTC",marketData.getRiseRate(),marketData.getLast());
                            }else if(symbol == Constants.BTC123TickerType.android_LTC_CNY){
                                MarketFragment.marketPrice("LTC",marketData.getRiseRate(),marketData.getLast());
                            }else if(symbol == Constants.BTC123TickerType.android_ETH_CNY){
                                MarketFragment.marketPrice("ETH",marketData.getRiseRate(),marketData.getLast());

                            }*/
                        }catch (Exception e){

                        }

                    }
                }
            };
        str = symbol_str[select_index];
            HttpMethods.getInstance(1).tickers(subscriber,str);


    }

    public void  stopTimer(){
        if (timerOfMarketPrice != null) {
            timerOfMarketPrice.cancel();
            timerOfMarketPrice = null;
        }
    }
    public void  startTimer(){
        if (timerOfMarketPrice == null) {
            timerOfMarketPrice = new Timer();
            MarketTimerTask task = new MarketTimerTask(handlerOfMarketPrice);
            timerOfMarketPrice.schedule(task, 0, kMarketDataRefreshTime);
        }
    }
    Handler handlerOfMarketPrice = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                marketlist();
                if(select_index+1<symbol_str.length){
                    select_index++;
                }else{
                    select_index=0;
                }
            }
            super.handleMessage(msg);
        };
    };
    /**
     * 定时器任务
     */
    class MarketTimerTask extends TimerTask {

        private Handler mHandler;

        public MarketTimerTask() {

        }

        public MarketTimerTask(Handler handler) {
            this.mHandler = handler;
        }

        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = 1;
            this.mHandler.sendMessage(message);
        }
    }
    private static void showHeaderAnimation(View view) {
        PropertyValuesHolder pvhX1 = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.6f);
        PropertyValuesHolder pvhX3 = PropertyValuesHolder.ofFloat("scaleX", 0.6f, 1f);
        PropertyValuesHolder pvhY1 = PropertyValuesHolder.ofFloat("rotationX", 0f, 90f);
        PropertyValuesHolder pvhY2 = PropertyValuesHolder.ofFloat("rotationX", 90f, 270f);
        PropertyValuesHolder pvhY3 = PropertyValuesHolder.ofFloat("rotationX", 270f, 360f);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofPropertyValuesHolder(view, pvhX1, pvhY1).setDuration(200);
        final ObjectAnimator objectAnimator2 = ObjectAnimator.ofPropertyValuesHolder(view, pvhY2).setDuration(0);
        final ObjectAnimator objectAnimator3 = ObjectAnimator.ofPropertyValuesHolder(view, pvhX3, pvhY3).setDuration(200);
        objectAnimator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                objectAnimator2.start();
            }
        });
        objectAnimator2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                objectAnimator3.start();
            }
        });
        objectAnimator1.start();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        destoryView();
        super.onDestroy();
    }

    /**
     * 创建悬浮窗
     */
    private void createView() {
        mWindowParams=new WindowManager.LayoutParams();
        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        // 设置LayoutParams(全局变量）相关参数
        mWindowParams.type = WindowManager.LayoutParams.TYPE_TOAST;// 该类型提供与用户交互，置于所有应用程序上方，但是在状态栏后面
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 不接受获取焦点事件
        // 以屏幕左上角为原点
        mWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
        //设置开始位置
        mWindowParams.x = 0;
        mWindowParams.y = 0;
        // 设置悬浮窗口长宽数据
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.format = PixelFormat.RGBA_8888;

        // 这个可以获取别的view
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        showImg =inflater.inflate(R.layout.activity_notification, null);
        showImg.setOnTouchListener(this);
        tv_nic_price=(TextView) showImg.findViewById(R.id.tv_nic_price);
        tv_nic_type=(TextView) showImg.findViewById(R.id.tv_nic_type);
        showImg.getBackground().setAlpha(180);//0~255透明度值
        mWindowManager.addView(showImg, mWindowParams);
        updateViewPosition(200,480);
    }

    /**
     * 销毁悬浮窗
     */
    private void destoryView() {
        mWindowManager.removeView(showImg);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        x = event.getRawX();
        // 减去状态栏的高度
        y = event.getRawY() - getStatusBarHeight();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                ACTION_DOWN_TIME = System.currentTimeMillis();
                // 获取相对View的坐标，即以此View左上角为原点
                mTouchStartX = event.getX();
                mTouchStartY = event.getY() + showImg.getHeight() / 2;
                mWindowParams_x_old = mWindowParams.x;
                mWindowParams_y_old = mWindowParams.y;
                break;

            case MotionEvent.ACTION_MOVE:
                updateViewPosition((int) (x - mTouchStartX),(int) (y - mTouchStartY));
                break;

            case MotionEvent.ACTION_UP:
                ACTION_UP_TIME = System.currentTimeMillis();
                long time = ACTION_UP_TIME - ACTION_DOWN_TIME;
                Log.i(TAG, "time == " + time);
                // 我们把down和up时间差小于120 可以看作点击事件
                if ( time < 120) {
                    // 把位置给回退到以前为位置
                    updateViewPosition(mWindowParams_x_old, mWindowParams_y_old);
                    if(!isApplicationBroughtToBackground(context)){

                    }else{
                       /* UIHelper.showMainActivity(MainActivity.getInstance());*/

                    }
                } else {
                    // 把图片给回退到边上
                   // aSideViewPosition();
                }
                mTouchStartX = mTouchStartY = 0;
                break;
        }
        return true;
    }

    private void updateViewPosition(int x, int y) {
        // 更新浮动窗口位置参数
        mWindowParams.x = x;
        mWindowParams.y = y;
        mWindowManager.updateViewLayout(showImg, mWindowParams);
    }

    /**
     * 判断一下靠近那边然后自动贴近桌面
     */
    private void aSideViewPosition() {
        if (mWindowParams.x + showImg.getWidth() / 2 > getScreenSize()[0] / 2) {
            mWindowParams.x = getScreenSize()[0] - showImg.getWidth();
        } else {
            mWindowParams.x = 0;
        }
        mWindowManager.updateViewLayout(showImg, mWindowParams);
    }

    /**
     * 获取屏幕的尺寸
     * @param context
     * @return
     */
    public int[] getScreenSize() {

        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        int[] size = { dm.widthPixels, dm.heightPixels };
        return size;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public int getStatusBarHeight() {
        int statusBarHeight = 0;
        // 这里只需要获取屏幕高度
        int screenHeight = getScreenSize()[1];

        switch (screenHeight) {
            case 240:
                statusBarHeight = 20;
                break;
            case 480:
                statusBarHeight = 25;
                break;
            case 800:
                statusBarHeight = 38;
                break;
            default:
                statusBarHeight = 25;
                break;
        }
        return statusBarHeight;
    }
}