package com.android.bitglobal.activity.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.entity.ObjectResult;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by bitbank on 16/9/22.
 * 推荐奖励
 */
public class RecommendReward extends SwipeBackActivity implements View.OnClickListener{
    private RecommendReward context;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;
    private SubscriberOnNextListener mGetRecommendGuideOnNextListener;
    private String url;
    private String recommendContent;
    private String shareImg;
    private String recommendTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_recommend_reward);
        context=this;
        ButterKnife.bind(this);
        initView();

    }

    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    private void initView() {
        tv_head_title.setText(R.string.user_tjjl);
        btn_head_back.setVisibility(View.VISIBLE);
        mGetRecommendGuideOnNextListener= new SubscriberOnNextListener<ObjectResult>() {
            public void onNext(ObjectResult objectResult) {

                recommendTitle = objectResult.getRecommendTitle();
                url = objectResult.getRecommendLink();
                recommendContent = objectResult.getRecommendContent();
                shareImg = objectResult.getShareImg();
                String url = objectResult.getRecommendPathWithArgs();
                webView.getSettings().setJavaScriptEnabled(true);
                webView.addJavascriptInterface(context,"androidAndroidObj");
                webView.getSettings().setUseWideViewPort(true);
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.loadUrl(url);
                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        // TODO Auto-generated method stub
                        //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                        view.loadUrl(url);
                        return true;
                    }
                });

            }
        };
        getRecommendGuide();
    }

    @OnClick({R.id.btn_head_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_head_back:
                finish();
                break;
        }
    }

    private void getRecommendGuide()
    {
        HttpMethods.getInstance(3).getRecommendGuide(new ProgressSubscriber(mGetRecommendGuideOnNextListener,context));
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    public void handleShareShow() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(recommendTitle);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(recommendContent);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        //网络图片的url：所有平台
        oks.setImageUrl(shareImg);//网络图片rul
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("https://www.android.com/");
        // 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调

        oks.addHiddenPlatform("ShortMessage");
        //oks.addHiddenPlatform("QQ");
        oks.addHiddenPlatform("QZone");
        oks.addHiddenPlatform("TencentWeibo");
        //oks.addHiddenPlatform("SinaWeibo");
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                UIHelper.ToastMessage(context,R.string.share_suc_toast);
                //TCAgent.onEvent(mContext, "推荐奖励分享", "分享成功"+platform.getName());

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
              //  TCAgent.onEvent(mContext, "推荐奖励分享", "分享失败_"+platform.getName());
                UIHelper.ToastMessage(context,R.string.share_error_toast);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                UIHelper.ToastMessage(context,R.string.share_remove_toast);
            }
        });
        // 启动分享GUI
        oks.show(this);
    }
}
