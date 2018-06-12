package com.android.bitglobal.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.EdgeEffectCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.dao.ArticleDao;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.Article;
import com.android.bitglobal.entity.MarketAndCurrency;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.entity.VersionResult;
import com.android.bitglobal.fragment.market.MarketFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.ui.UIHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeFragment extends BaseFragment implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private Activity context;
    private EdgeEffectCompat leftEdge;
    private EdgeEffectCompat rightEdge;
    List<Fragment> listFragment = new ArrayList<>();
    private View mView;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_front)
    ImageView btn_head_front;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.img_head_ico)
    ImageView img_head_ico;
    @BindView(R.id.set_price_image)
    ImageView setPriceImage;

    @BindView(R.id.rl_select_title_usd)
    RelativeLayout rl_select_title_usd;
    @BindView(R.id.rl_select_title_btc)
    RelativeLayout rl_select_title_btc;
    @BindView(R.id.view_line_usd)
    View view_line_usd;
    @BindView(R.id.view_line_btc)
    View view_line_btc;

//    @BindView(R.id.ll_home_message)
//    FrameLayout ll_home_message;
//    @BindView(R.id.activity_home_message_close)
//    TextView activity_home_message_close;
//    @BindView(R.id.activity_home_message_title)
//    TextView activity_home_message_title;
//    @BindView(R.id.activity_home_message_time)
//    TextView activity_home_message_time;
//    @BindView(R.id.activity_home_message_summary)
//    WebView activity_home_message_summary;

    private String currencyType = "ETH";
    private String exchangeType = "BTC";

    private MarketFragment market_btc;
    private MarketFragment market_usd;
    int select_position = 0;

    private SubscriberOnNextListener getProclamationsOnNext;
    private Timer timerHome = null;  //刷新定时器
    private final int dataRefreshTime = 3 * 1000;//数据刷新间隔
    private List<Article> article_list = new ArrayList<Article>();
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, mView);
        initView();
        getProclamations();
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
//        if (getUserVisibleHint()) {
//            getProclamations();
//        }
    }

    public void setHeadTitle() {
        currencyType = SharedPreferences.getInstance().getString("currencyType", "ETH");
        exchangeType = SharedPreferences.getInstance().getString("exchangeType", "BTC");
        // tv_head_title.setText(currencyType+"/"+exchangeType);
//        tv_head_title.setText(exchangeType);
    }

    private void initView() {
//        img_head_ico.setVisibility(View.VISIBLE);
        setHeadTitle();
        btn_head_front.setOnClickListener(this);
        rl_select_title_usd.setOnClickListener(this);
        rl_select_title_btc.setOnClickListener(this);
        setPriceImage.setVisibility(View.VISIBLE);
        setPriceImage.setOnClickListener(this);

        market_usd = new MarketFragment();
        market_usd.setCurrencyType(currencyType);
        market_usd.setExchangeFlag(SystemConfig.homeUSDC);
        market_usd.setHomeContext(this);
        listFragment.add(market_usd);
        market_btc = new MarketFragment();
        market_btc.setCurrencyType(currencyType);
        market_btc.setExchangeFlag(SystemConfig.homeBTC);
        market_btc.setHomeContext(this);
        listFragment.add(market_btc);



        viewPager = (ViewPager) mView.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return listFragment.get(i);
            }

            @Override
            public int getCount() {
                return listFragment.size();
            }
        });
        viewPager.setOnPageChangeListener(this);
        initViewPager();
        getProclamationsOnNext = new SubscriberOnNextListener<VersionResult>() {
            @Override
            public void onNext(VersionResult versionResult) {
                if (versionResult != null && versionResult.getArticles() != null) {
                    List<Article> articles = versionResult.getArticles();
                    for (Article article : articles) {
                        ArticleDao.getInstance().add(article);
                    }
                }
                getArticles();

            }
        };
    }

    private void initViewPager() {
        try {
            Field leftEdgeField = viewPager.getClass().getDeclaredField("mLeftEdge");
            Field rightEdgeField = viewPager.getClass().getDeclaredField("mRightEdge");
            if (leftEdgeField != null && rightEdgeField != null) {
                leftEdgeField.setAccessible(true);
                rightEdgeField.setAccessible(true);
                leftEdge = (EdgeEffectCompat) leftEdgeField.get(viewPager);
                rightEdge = (EdgeEffectCompat) rightEdgeField.get(viewPager);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.activity_home_message_close:
////                ArticleDao.getInstance().delect();
//                ll_home_message.setVisibility(View.GONE);
//                break;
            case R.id.btn_head_front:
                AppContext.customScan(MainActivity.getInstance());
                //UIHelper.showMarketSet(context);
                break;
            case R.id.set_price_image:
                UserInfo userInfo= UserDao.getInstance().getIfon();
                if(is_token(userInfo)){
                    HashMap<String, String> btcPriceMap = new HashMap<>();
                    HashMap<String, String> usdPriceMap = new HashMap<>();
                    if (!market_btc.getAllMarketAndCurrencyList().isEmpty()){
                        for(MarketAndCurrency marketAndCurrency : market_btc.getShowMarketAndCurrencyList()) {
                            btcPriceMap.put(marketAndCurrency.getCurrencySet().getCurrency(), marketAndCurrency.getTickerData().getLast());
                        }
                        for(MarketAndCurrency marketAndCurrency : market_usd.getShowMarketAndCurrencyList()) {
                            usdPriceMap.put(marketAndCurrency.getCurrencySet().getCurrency(), marketAndCurrency.getTickerData().getLast());
                        }
                        UIHelper.showSetPrice(MainActivity.getInstance(), btcPriceMap, usdPriceMap);
                    }else {
                        UIHelper.ToastMessage(getActivity(),getString(R.string.app_network_interruption_toast));
                    }

                }else{
                    //UIHelper.showLoginOrRegister(MainActivity.getInstance());
                    UIHelper.showLogin(MainActivity.getInstance());
                }
                break;
            case R.id.rl_select_title_usd:
                showUsdShow(true);
                viewPager.setCurrentItem(0);
                break;
            case R.id.rl_select_title_btc:
                showUsdShow(false);
                viewPager.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (leftEdge != null && rightEdge != null) {
            leftEdge.finish();
            rightEdge.finish();
            leftEdge.setSize(0, 0);
            rightEdge.setSize(0, 0);
        }
    }

    private void getProclamations() {
        ProgressSubscriber progressSubscriber = new ProgressSubscriber(getProclamationsOnNext, context);
        progressSubscriber.setIs_progress_show(false);
        HttpMethods.getInstance(3).getProclamations(progressSubscriber);
    }

    private void getArticles() {

        article_list = ArticleDao.getInstance().getIfon();
//        if (article_list != null && article_list.size() > 0) {
//            Article article_info = article_list.get(0);
//            String article_time = article_info.getPublishTime();
//            String article_title = article_info.getTitle();
//            String article_link = article_info.getLink();
//
//            LayoutInflater inflater = getActivity().getLayoutInflater();
//            View root = inflater.inflate(R.layout.home_message, null);
//            final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
//            dialog.setView(root);
//            dialog.setCancelable(false);
//            TextView view = (TextView) root.findViewById(R.id.activity_home_message_title);
//            if (view != null) {
//                view.setText(article_title);
//            }
//            view = (TextView) root.findViewById(R.id.activity_home_message_time);
//            if (view != null) {
//                if(!"".equals(article_time)) {
//                    SimpleDateFormat date = new SimpleDateFormat("MM dd, yyyy", Locale.getDefault());
//                    view.setText(date.format(new Date(Long.valueOf(article_time))));
//                }
//            }
//            view = (TextView) root.findViewById(R.id.activity_home_message_close);
//            if (view != null) {
//                view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//            }
//            WebView webView = (WebView) root.findViewById(R.id.activity_home_message_summary);
//            if (webView != null) {
//                webView.loadUrl(article_link);
//                webView.getSettings().setUseWideViewPort(true);
//                webView.getSettings().setLoadWithOverviewMode(true);
//
//                webView.setWebViewClient(new WebViewClient(){
//                    @Override
//                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                        //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//                        view.loadUrl(url);
//                        return true;
//                    }
//                });
//            }
//            dialog.show();
//
//            handler.postDelayed(runnable, 5000);
//        }
//        else {
//            if (ll_home_message != null) {
//                ll_home_message.setVisibility(View.GONE);
//            }
//
//        }
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            getArticles();
        }
    };

    @Override
    public void onPageSelected(int position) {
        select_position = position;
        switch (position){
            case 0:
                showUsdShow(true);
                break;
            case 1:
                showUsdShow(false);
                break;
            default:
                showUsdShow(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 设置usd下划线是否显示or隐藏 btc下划线是够 隐藏or 显示
     */
    private void showUsdShow(boolean isShow){
        if (isShow){
            view_line_usd.setVisibility(View.VISIBLE);
            view_line_btc.setVisibility(View.INVISIBLE);
        }else {
            view_line_usd.setVisibility(View.INVISIBLE);
            view_line_btc.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private boolean hidden;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            startTimer();
        } else {
            stopTimer();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            startTimer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 定时器任务
     */
    class TransTimerTask extends TimerTask {

        private Handler mHandler;

        public TransTimerTask(Handler handler) {
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

    ;
    Handler handlerOfTrans = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                market_usd.initDataMarketlist();
                market_btc.initDataMarketlist();

            }
            super.handleMessage(msg);
        }

        ;
    };

    /**
     * 启动定时器
     */
    public void startTimer() {
        if (timerHome == null) {
            timerHome = new Timer();
            TransTimerTask task = new TransTimerTask(handlerOfTrans);
            timerHome.schedule(task, 0, dataRefreshTime);
        }
    }

    /**
     * 停止定时器
     */
    public void stopTimer() {
        if (timerHome != null) {
            timerHome.cancel();
            timerHome = null;
        }
    }

    public static void showHeaderAnimation(View view) {
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


}
