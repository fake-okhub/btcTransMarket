package com.android.bitglobal.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.android.bitglobal.R;
import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.dao.CountryDao;
import com.android.bitglobal.dao.CurrencySetDao;
import com.android.bitglobal.entity.Country;
import com.android.bitglobal.entity.CountryResult;
import com.android.bitglobal.entity.CurrencyResult;
import com.android.bitglobal.entity.CurrencySet;
import com.android.bitglobal.entity.CurrencySetRealm;
import com.android.bitglobal.entity.CurrencySetResult;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.IpLegalTenderResult;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.tool.SpTools;
import com.android.bitglobal.tool.Utils;
import com.android.bitglobal.ui.UIHelper;

import java.util.Collections;
import java.util.Date;

import cn.jpush.android.api.JPushInterface;
import cn.zhikaizhang.indexview.PinyinComparator;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.exceptions.RealmMigrationNeededException;
import rx.Subscriber;


/**
 * 闪屏页
 * Modified by Elbert on 17/3/22.
 */
public class SplashActivity extends FragmentActivity {
//    private static SplashActivity activity;
    // private CircleIndicator indicator;
    private CountryResult countryResult;
//    private ViewPager pager;
//    private GalleryPagerAdapter adapter;
//    private String host = "", scheme = "";
//    private int[] images = {
//            R.mipmap.newer01
//    };
//    Animation fadeIn, fadeout;
    private Subscriber<HttpResult<CurrencyResult>> getCurrencySetOnNext;
    private Subscriber<HttpResult<CountryResult>> getCountriesOnNext;
    private CurrencySetResult currencySetResult;
    private int is_currencySet = 0, is_countries = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        activity = this;
        initView();
//        initData();
//        getCountries();
//        getCurrencySet();
//        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
//        fadeout = AnimationUtils.loadAnimation(this, R.anim.fadeout);
//        final boolean firstTimeUse = SharedPreferences.getInstance().getBoolean("first-time-use", true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (firstTimeUse) {
//                    fadeout.setFillAfter(true);
//                    findViewById(R.id.guideImage).startAnimation(fadeout);
//                    initGuideGallery();
//                } else {
//                    handler.postDelayed(runnable2, 1000);
//                }
                RealmConfiguration realmConfig = new RealmConfiguration.Builder(SplashActivity.this).build();
                try {
                    AppContext.setRealm(Realm.getInstance(realmConfig));
                } catch (RealmMigrationNeededException e){
                    Realm.deleteRealm(realmConfig);
                    AppContext.setRealm(Realm.getInstance(realmConfig));
                }
                handler.postDelayed(runnable2, 1000);
            }
        }, 0);
    }

    private void initView() {
        getCurrencySetOnNext = new Subscriber<HttpResult<CurrencyResult>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                UIHelper.ToastMessage(SplashActivity.this, getString(R.string.app_network_interruption_toast));
            }

            @Override
            public void onNext(HttpResult<CurrencyResult> currencyResult) {
                CurrencySetResult csrr = new CurrencySetResult();
                RealmList<CurrencySetRealm> list_s = new RealmList<>();
                for (CurrencySet cs : currencyResult.getDatas().getCurrencySets()) {
                    CurrencySetRealm csr_s = new CurrencySetRealm(cs);
                    list_s.add(csr_s);
                }
                csrr.setCurrencySets(list_s);
                CurrencySetDao.getInstance().add(csrr);
                is_currencySet = 1;
            }
        };

        getCountriesOnNext = new Subscriber<HttpResult<CountryResult>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }


            @Override
            public void onNext(HttpResult<CountryResult> result) {
                Collections.sort(result.getDatas().getCountries(), new PinyinComparator<Country>() {
                    @Override
                    public int compare(Country s1, Country s2) {
                        return compare(s1.getDes(), s2.getDes());
                    }
                });
                CountryDao.getInstance().add(result.getDatas());
                countryResult = result.getDatas();
                is_countries = 1;
            }
        };

    }

    private void initData() {
//        Intent intent = getIntent();
//        scheme = intent.getScheme();
//        Uri uri = intent.getData();
//        if (uri != null) {
//            host = uri.getHost();
//        }
        currencySetResult = CurrencySetDao.getInstance().getIfon();

    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    private void getCurrencySet() {
        if (currencySetResult == null || (new Date().getTime() - currencySetResult.getVersion() > 60 * 1000 * 24 * 7)) {
//            HttpMethods.getInstance(2).getCurrencySet(new ProgressSubscriber(getCurrencySetOnNext, this), "");
            HttpMethods.getInstance(2).getCurrencySet(getCurrencySetOnNext, "");
        } else {
            is_currencySet = 1;
        }
    }

    private void getCountries() {
        if (countryResult == null || (new Date().getTime() - countryResult.getVersion() > 60 * 1000 * 24 * 7)) {
//            HttpMethods.getInstance(3).getCountries(new ProgressSubscriber(getCountriesOnNext, this));
            HttpMethods.getInstance(3).getCountries(getCountriesOnNext);
        } else {
            is_countries = 1;
        }

    }

    /**
     * 根据ip地址获取法币
     */
    private void getIpLegalTender(){
        String ipAddress = Utils.getIPAddress(SplashActivity.this);
        Subscriber getIpLegalTenderResult=new Subscriber<HttpResult<IpLegalTenderResult>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult<IpLegalTenderResult> ipLegalTenderResultHttpResult) {
                String legalTender=ipLegalTenderResultHttpResult.getDatas().getLegalTender();
                SpTools.setIplegalTender(false);
                SpTools.setlegalTender(legalTender);
           }
        };
        if (SpTools.getIpLegalTender()){//说明第一次进入app
            HttpMethods.getInstance(2).getIpLegalTender(getIpLegalTenderResult,ipAddress);
        }
    }

//    private void initGuideGallery() {
//
//        pager = (ViewPager) findViewById(R.id.pager);
//        pager.setVisibility(View.VISIBLE);
//        adapter = new GalleryPagerAdapter();
//        pager.setAdapter(adapter);
//    }


//    public class GalleryPagerAdapter extends PagerAdapter {
//
//        @Override
//        public int getCount() {
//            return images.length;
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            ImageView item = new ImageView(SplashActivity.this);
//            item.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            item.setImageResource(images[position]);
//            item.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    SharedPreferences.getInstance().putBoolean("first-time-use", false);
//                    finish();
//                    //UIHelper.showRegister(SplashActivity.this);
//                    UIHelper.showHome(SplashActivity.this, host);
//                }
//            });
//            container.addView(item);
//            return item;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup collection, int position, Object view) {
//            collection.removeView((View) view);
//        }
//    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            switch (is_currencySet) {
                case 1:
                    //  indicator.setVisibility(View.GONE);
                    break;
                default:
                    if (is_currencySet == 0) {
                        getCurrencySet();
                    }
                    handler.postDelayed(runnable, 3000);
                    break;
            }

        }
    };
    Runnable runnable2 = new Runnable() {
        public void run() {
            getIpLegalTender();
            if (is_currencySet == 1) {
                if (MainActivity.getInstance() != null) {
                    MainActivity.getInstance().finish();
                }
                UIHelper.showMainActivity(SplashActivity.this);

                //UIHelper.showLogin(SplashActivity.this);
                finish();
                //  UIHelper.showRegister(SplashActivity.this);

            } else {
                if (is_currencySet == 0) {
                    getCurrencySet();
                }
                if (is_countries == 0) {
                    getCountries();
                }
                handler.postDelayed(runnable2, 1000);
            }

        }
    };
}
