package com.android.bitglobal.fragment.user;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.bitglobal.R;
import com.android.bitglobal.entity.VipLevelDetails;
import com.android.bitglobal.entity.VipLevelDetailsResult;
import com.android.bitglobal.fragment.BaseFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.ui.UIHelper;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/3/8.
 */

public class AccountLevelDetailsFragment  extends BaseFragment implements SwipyRefreshLayout.OnRefreshListener {

    private static final String ARG_TYPE = "type";
    private static final int PAGE_SIZE = 20;

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.swipyrefreshlayout)
    SwipyRefreshLayout mSwipyRefreshLayout;

    private int page = 1;
    private boolean isReadNextPage = true;

    private Activity context;
    private SubscriberOnNextListener vipLevelDetailsOnNext;
    private QuickAdapter<VipLevelDetails> vipLevelDetailsResultQuickAdapter;
    private List<VipLevelDetails> vipLevelDetailsList = new ArrayList<>();

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_level_details, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        mSwipyRefreshLayout.setOnRefreshListener(this);
        final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        vipLevelDetailsOnNext = new SubscriberOnNextListener<VipLevelDetailsResult>() {
            @Override
            public void onNext(VipLevelDetailsResult vipLevelDetailsResult) {
                if(vipLevelDetailsResult.getDetails().size() != 0) {
                    vipLevelDetailsResultQuickAdapter.clear();
                    vipLevelDetailsList.addAll(vipLevelDetailsResult.getDetails());
                    vipLevelDetailsResultQuickAdapter.replaceAll(vipLevelDetailsList);
                    isReadNextPage = true;
                } else {
                    isReadNextPage = false;
                    UIHelper.ToastMessage(context,R.string.trans_wgdsjl_toast);
                }
            }
        };
        vipLevelDetailsResultQuickAdapter = new QuickAdapter<VipLevelDetails>(context, R.layout.item_account_level_details) {
            @Override
            protected void convert(BaseAdapterHelper helper, VipLevelDetails item) {
                helper.setText(R.id.date_text, sdf.format(item.getAddTime().getTime()));
                helper.setText(R.id.type_text, item.getTypeValue());
                helper.setText(R.id.earn_points_text, item.getJifen().toString());
                if ((helper.getPosition() % 2) == 1) {
                    helper.setBackgroundColor(R.id.list_item_bg,
                            Color.rgb(0xf5, 0xf6, 0xf7));
                } else {
                    helper.setBackgroundColor(R.id.list_item_bg,
                            Color.rgb(0xff, 0xff, 0xff));
                }
            }
        };
        listview.setAdapter(vipLevelDetailsResultQuickAdapter);
    }

    private void getVipLevelDetailList(int page, int pageSize) {
        ProgressSubscriber progressSubscriber=new ProgressSubscriber(vipLevelDetailsOnNext, context);
        progressSubscriber.setIs_progress_show(false);
        progressSubscriber.setIs_showMessage(false);
        HttpMethods.getInstance(4).getVipLevelDetails(progressSubscriber, page, pageSize);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if(direction == SwipyRefreshLayoutDirection.TOP){
            clearData();
        }else{
            if(isReadNextPage) {
                page ++;
            }
            getVipLevelDetailList(page, PAGE_SIZE);
        }
        try{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Hide the refresh after 2sec
                    if (context== null) {
                        return;
                    }else{
                        context.runOnUiThread(new Runnable() {
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
        vipLevelDetailsList.clear();
        getVipLevelDetailList(page, PAGE_SIZE);
    }

    public void initVipLevelDetailList() {
        page = 1;
        if(vipLevelDetailsList.size() != 0) {
            vipLevelDetailsList.clear();
        }
        getVipLevelDetailList(page, PAGE_SIZE);
    }

    public static AccountLevelDetailsFragment newInstance(String param1) {
        AccountLevelDetailsFragment fragment = new AccountLevelDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
