package com.android.bitglobal.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.common.AppManager;
import com.android.bitglobal.R;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.UnReadNoticeResult;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.tool.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rx.Subscriber;

/**
 * xiezuofei
 * 2016-06-24 11:20
 * 793169940@qq.com
 */
public class BaseActivity extends AppCompatActivity {

    private static Context mContext;
    public String language;
//    protected SystemBarTintManager tintManager;

    private AlertDialog dialog;

    DecimalFormat df0 = new DecimalFormat("0.########");
    DecimalFormat df1 = new DecimalFormat("0.00######");
    DecimalFormat df2 = new DecimalFormat("0.00");
    DecimalFormat df3 = new DecimalFormat("0.###");

    public static synchronized Context getContext() {
        if (mContext == null) {
            mContext = new BaseActivity();
        }
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatus(true);
//            tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintResource(R.color.status_bar_bg);//通知栏所需颜色
        }
        df0.setGroupingSize(0);
        df0.setRoundingMode(RoundingMode.FLOOR);
        df1.setGroupingSize(0);
        df1.setRoundingMode(RoundingMode.FLOOR);
        df2.setGroupingSize(0);
        df2.setRoundingMode(RoundingMode.FLOOR);
        df3.setGroupingSize(0);
        df3.setRoundingMode(RoundingMode.FLOOR);

        changeAppLanguage();
    }

    public void changeAppLanguage() {
        language = SharedPreferences.getInstance().getString(SharedPreferences.APP_LANGUAGE,
                "");
        // 本地语言设置
        Locale myLocale;
        if("".equals(language)) {
            if("EN".equals(getResources().getConfiguration().locale.getLanguage().toUpperCase())){
                language = "EN";
            } else {
                language = getResources().getConfiguration().locale.getCountry().toUpperCase();
            }
            if("CN".equals(language)) {
                language = SystemConfig.SIMPLIFIED_CHINESE;
            }
            myLocale = new Locale(language);
            SharedPreferences.getInstance().putString(SharedPreferences.APP_LANGUAGE, language);
        } else {
            myLocale = new Locale(language);
        }
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserUnReadNotice();
    }

    private void getUserUnReadNotice() {
        if (!is_token(UserDao.getInstance().getIfon())) {
            return;
        }
        Subscriber<HttpResult<UnReadNoticeResult>> subscriber = new Subscriber<HttpResult<UnReadNoticeResult>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult<UnReadNoticeResult> unReadNoticeResultHttpResult) {
                showNoticeDialog(unReadNoticeResultHttpResult);
            }
        };
        HttpMethods.getInstance(3).getUserUnReadNotice(subscriber);
    }

    private void showNoticeDialog(HttpResult<UnReadNoticeResult> value) {
        long article_time = value.getDatas().getPublishTime();
        String article_title = value.getDatas().getTitle();
        String article_content = value.getDatas().getSummary();

        LayoutInflater inflater = getLayoutInflater();
        View root = inflater.inflate(R.layout.home_message, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(root);
        dialog.setCancelable(false);
        TextView view = (TextView) root.findViewById(R.id.activity_home_message_title);
        if (view != null) {
            view.setText(article_title);
        }
        view = (TextView) root.findViewById(R.id.activity_home_message_time);
        if (view != null) {
            if(!"".equals(article_time)) {
                SimpleDateFormat date;
                if(language.equals("EN")) {
                    date = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                } else {
                    date = new SimpleDateFormat("MM dd, yyyy", Locale.getDefault());
                }
                view.setText(date.format(new Date(Long.valueOf(article_time))));
            }
        }
        view = (TextView) root.findViewById(R.id.activity_home_message_close);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        view = (TextView) root.findViewById(R.id.activity_home_message_content);
        if (view != null) {
            if(!"".equals(article_content)) {
                CharSequence charSequence = Html.fromHtml(article_content);
                view.setText(charSequence);
            }
        }
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
//        WindowManager.LayoutParams winParams = win.getAttributes();
//        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        if (on) {
//            winParams.flags |= bits;
//        } else {
//            winParams.flags &= ~bits;
//        }
//        win.setAttributes(winParams);
        win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        win.setStatusBarColor(getResources().getColor(R.color.status_bar_bg));
    }

    protected String getText(EditText ed) {
        String str = "";
        str = ed.getText().toString().trim();
        return str;
    }

    protected String getText(TextView ed) {
        String str = "";
        str = ed.getText().toString().trim();
        return str;
    }

    public boolean is_token(UserInfo userInfo) {
        boolean is_token;
        if (userInfo == null || userInfo.getToken() == null || userInfo.getToken().equals("null") || userInfo.getToken().equals("")) {
            is_token = false;
        } else {
            is_token = true;
        }
        return is_token;
    }


    public String deFormat(String str) {
        try {
            str = df1.format(Double.parseDouble(str));
        } catch (Exception e) {
            str = "0.00";
        }
        return str;
    }

    public String deFormat(double dou) {
        String str = "";
        try {
            str = df1.format(dou);
        } catch (Exception e) {
            str = "0.00";
        }
        return str;
    }

    public String deFormat(String str, int type) {
        try {
            if (type == 2) {
                str = df2.format(Double.parseDouble(str));
            } else if (type == 3) {
                str = df3.format(Double.parseDouble(str));
            } else if (type == 0) {
                str = df0.format(Double.parseDouble(str));
            } else {
                str = df1.format(Double.parseDouble(str));
            }

        } catch (Exception e) {
            str = "0.00";
        }
        return str;
    }

    public String deFormat(double dou, int type) {
        String str = "";
        try {
            if (type == 2) {
                str = df2.format(dou);
            } else if (type == 3) {
                str = df3.format(dou);
            } else if (type == 0) {
                str = df0.format(dou);
            } else {
                str = df1.format(dou);
            }
        } catch (Exception e) {
            str = "0.00";
        }
        return str;
    }

    public String format(String num, int type) {
        String str = "0.";
        for (int i = 0; i < type; i++) {
            str += "0";
        }
        DecimalFormat df = new DecimalFormat(str);
        df.setRoundingMode(RoundingMode.DOWN);
        try {
            str = df.format(Double.parseDouble(num));
        } catch (NumberFormatException e) {
            str = "--";
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            str = "0.00";
            e.printStackTrace();
        }
        return str;
    }

    public String format(double num, int type) {
        String str = "0.";
        for (int i = 0; i < type; i++) {
            str += "0";
        }
        DecimalFormat df = new DecimalFormat(str);
        df.setRoundingMode(RoundingMode.DOWN);
        try {
            str = df.format(num);
        } catch (NumberFormatException e) {
            str = "--";
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            str = "0.00";
            e.printStackTrace();
        }
        return str;
    }

    public PackageInfo getVersion() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            return info;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void alert(View view, boolean cancelable) {
        dialog = new AlertDialog.Builder(this).create();
        dialog.setView(view);
        dialog.setCancelable(cancelable);
        dialog.show();
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public String getVolText(String vol) {
        BigDecimal totalBtcNum = new BigDecimal(vol);
        if(totalBtcNum.doubleValue() > 0 && totalBtcNum.doubleValue() >= 10000 && totalBtcNum.doubleValue() < 10000000) {
            return format(totalBtcNum.divide(new BigDecimal(1000)).doubleValue(), 2) + getString(R.string.thousand);
        } else if(totalBtcNum.doubleValue() >    0 && totalBtcNum.doubleValue() >= 10000000) {
            return format(totalBtcNum.divide(new BigDecimal(1000000)).doubleValue(), 2) + getString(R.string.millions);
        } else {
            return format(totalBtcNum.doubleValue(), 2);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        AppContext.getInstance().setLastTouchTime();
        return super.dispatchTouchEvent(ev);
    }
}
