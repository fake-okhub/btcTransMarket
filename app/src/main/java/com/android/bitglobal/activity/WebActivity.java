package com.android.bitglobal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.bitglobal.R;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;


/**
 * xiezuofei
 * 2016-07-09 11:10
 * 793169940@qq.com
 *加载网页
 */
public class WebActivity extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;
    @BindView(R.id.btn_head_front)
    ImageView btn_head_front;
    private String title,url,type;
    private String shareDes="",shareImg="",shareUrl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置显示的视图
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        context=this;
        initView();
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        title=bundle.getString("title");
        url=bundle.getString("url");
        type=bundle.getString("type");
        if ("2".equals(type)) {
            shareDes=bundle.getString("shareDes");
            shareUrl=bundle.getString("shareUrl");
            shareImg=bundle.getString("shareImg");

            btn_head_front.setVisibility(View.VISIBLE);
            btn_head_front.setOnClickListener(this);
            btn_head_front.setImageResource(R.mipmap.btn_room_share);
        }

        tv_head_title.setText(title);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                onKeyDown(KeyEvent.KEYCODE_BACK, null);
                break;
            case R.id.btn_head_front:
                handleShareShow();
                break;


        }
    }
    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();//返回上一页面
                return true;
            }else{
                this.finish();
                return false;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public void handleShareShow() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(shareUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(shareDes);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        //网络图片的url：所有平台
        oks.setImageUrl(shareImg);//网络图片rul
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(shareUrl);
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
