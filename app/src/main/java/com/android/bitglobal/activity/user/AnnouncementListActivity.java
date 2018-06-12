package com.android.bitglobal.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.bitglobal.R;
import com.android.bitglobal.entity.Article;
import com.android.bitglobal.entity.HttpResult;
import com.android.bitglobal.entity.VersionResult;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.StringUtils;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Subscriber;

public class AnnouncementListActivity extends SwipeBackActivity implements SwipyRefreshLayout.OnRefreshListener {

    private static final int PAGE_SIZE = 10;
    private static final int GET_LIST_TYPE_ANNOUNCEMENT = 1;
    private static final int GET_LIST_TYPE_NEWs = 2;

    private int page = 1;
    private boolean isReadNextPage = true;

    SwipyRefreshLayout mSwipyRefreshLayout;

    private QuickAdapter<Article> mAdapter;
    private ArrayList<Article> mArticleList = new ArrayList<>();
    private SubscriberOnNextListener getProclamationsOnNext;

    public static Intent newAct(Context context) {
        return new Intent(context, AnnouncementListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_list);
        initData();
        initView();
    }

    private void initView() {
        ListView mListView = (ListView) findViewById(R.id.activity_announcement_list);
        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        mSwipyRefreshLayout.setOnRefreshListener(this);
        mAdapter = new QuickAdapter<Article>(this, R.layout.announcement_list_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, Article item) {
                helper.setText(R.id.activity_announcement_list_item_title, item.getTitle());

                SimpleDateFormat date;
                if(language.equals("EN")) {
                    date = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                } else {
                    date = new SimpleDateFormat("MM dd, yyyy", Locale.getDefault());
                }
                if(!"".equals(item.getPublishTime())) {
                    helper.setText(R.id.activity_announcement_list_item_time,
                            date.format(new Date(Long.valueOf(item.getPublishTime()))));
                }
                String str = item.getContent();
                if (StringUtils.isEmpty(str)) {
                    str = "";
                }
                CharSequence charSequence = Html.fromHtml(str);
                helper.setText(R.id.activity_announcement_list_item_summary, item.getSummary());
                setWidgetEnable(helper,false);
            }
        };
        mListView.setAdapter(mAdapter);
        // 列表点击跳转取消了
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Bundle bundle = new Bundle();
//                String article_title = mArticleList.get(position).getTitle();
//                String publishTime = mArticleList.get(position).getPublishTime();
////                String content = mArticleList.get(position).getContent();
//                String content = mArticleList.get(position).getSummary();
//                bundle.putString("title", article_title);
//                bundle.putString("publishTime", publishTime);
//                bundle.putString("content", content);
//                UIHelper.showAnnouncementDetailList(AnnouncementListActivity.this, bundle);
////                UIHelper.showWeb(AnnouncementListActivity.this, bundle);
////                readNotice(mArticleList.get(position).getId());
//            }
//        });
        findViewById(R.id.activity_announcement_list_head_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setWidgetEnable(BaseAdapterHelper helper,boolean isEnable){
        helper.getView(R.id.activity_announcement_list_item_title).setEnabled(isEnable);
        helper.getView(R.id.activity_announcement_list_item_time).setEnabled(isEnable);
        helper.getView(R.id.activity_announcement_list_item_summary).setEnabled(isEnable);

    }

    private void readNotice(String id) {
        Subscriber<HttpResult> subscriber = new Subscriber<HttpResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult httpResult) {
                Log.i("readNotice return", httpResult.getResMsg().getMessage());
            }
        };
        HttpMethods.getInstance(3).readNotice(subscriber, id);
    }

    private void initData() {
        getProclamationsOnNext = new SubscriberOnNextListener<VersionResult>() {
            @Override
            public void onNext(VersionResult versionResult) {
                if(versionResult.getArticles().size() != 0) {
                    if (versionResult != null && versionResult.getArticles() != null) {
                        List<Article> articles = versionResult.getArticles();
                        for (Article article : articles) {
                            mArticleList.add(article);
                        }
                        mAdapter.clear();
                        mAdapter.addAll(mArticleList);
                        isReadNextPage = true;
                    }
                } else {
                    UIHelper.ToastMessage(AnnouncementListActivity.this, R.string.trans_wgdsjl_toast);
                    isReadNextPage = false;
                }
            }
        };
        getProclamationsList(page, PAGE_SIZE, GET_LIST_TYPE_ANNOUNCEMENT, null);
    }

    private void getProclamationsList(int page, int pageSize, Integer type, Integer isTop){
        ProgressSubscriber progressSubscriber = new ProgressSubscriber(getProclamationsOnNext, this);
        progressSubscriber.setIs_progress_show(false);
        HttpMethods.getInstance(3).getProclamations(progressSubscriber, page, pageSize, type, isTop);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if(direction == SwipyRefreshLayoutDirection.TOP){
            clearData();
        }else{
            if(isReadNextPage) {
                page ++;
            }
            getProclamationsList(page, PAGE_SIZE, GET_LIST_TYPE_ANNOUNCEMENT, null);
        }
        try{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Hide the refresh after 2sec
                    if (this== null) {
                        return;
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(mSwipyRefreshLayout != null) {
                                    mSwipyRefreshLayout.setRefreshing(false);
                                }
                            }
                        });
                    }

                }
            }, 2000);
        }catch (Exception e){

        }
    }

    private void clearData() {
        page = 1;
        mArticleList.clear();
        getProclamationsList(page, PAGE_SIZE, GET_LIST_TYPE_ANNOUNCEMENT, null);
    }

}
