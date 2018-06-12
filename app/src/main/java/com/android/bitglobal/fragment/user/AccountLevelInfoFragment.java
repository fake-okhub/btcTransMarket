package com.android.bitglobal.fragment.user;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.bitglobal.R;
import com.android.bitglobal.activity.user.UserVipActivity;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.BillDetail;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.entity.VipInfoResult;
import com.android.bitglobal.entity.VipLevelInfo;
import com.android.bitglobal.entity.VipLevelRule;
import com.android.bitglobal.fragment.BaseFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.Utils;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.github.tifezh.kchartlib.utils.ViewUtil;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/3/8.
 */

public class AccountLevelInfoFragment extends BaseFragment {

    private static final String ARG_TYPE = "type";

    @BindView(R.id.level_info_listview)
    ListView levelInfoListView;
    @BindView(R.id.level_rule_listview)
    ListView levelRuleListView;

    private Activity context;
    private SubscriberOnNextListener vipRuleOnNext;
    private QuickAdapter<VipLevelInfo> levelInfoAdapter;
    private QuickAdapter<VipLevelRule> levelRuleAdapter;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_level_info, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        initData();
        getVipRuleResult();
        return view;
    }

    private void initData() {
        vipRuleOnNext = new SubscriberOnNextListener<VipInfoResult>() {
            @Override
            public void onNext(VipInfoResult vipInfoResult) {
                levelInfoAdapter.clear();
                levelRuleAdapter.clear();
                levelInfoAdapter.replaceAll(vipInfoResult.getUserVipLevelList());
                setInfoListViewHeight();
                levelRuleAdapter.replaceAll(vipInfoResult.getIntegralRuleList());
                setRuleListViewHeight();
            }
        };
        levelInfoAdapter = new QuickAdapter<VipLevelInfo>(context, R.layout.item_account_level_info) {
            @Override
            protected void convert(BaseAdapterHelper helper, VipLevelInfo item) {
                helper.setText(R.id.level_text, getString(R.string.user_vip) + "-" + item.getVipRate());
                helper.setText(R.id.points_text, item.getJifen() + "");
                if(item.getDiscount() == 0) {
                    helper.setText(R.id.fee_rate_text, getString(R.string.free));
                } else {
                    helper.setText(R.id.fee_rate_text, item.getDiscount() + "%");
                }
            }
        };
        levelInfoListView.setAdapter(levelInfoAdapter);
        levelRuleAdapter = new QuickAdapter<VipLevelRule>(context, R.layout.item_account_level_rule) {
            @Override
            protected void convert(BaseAdapterHelper helper, VipLevelRule item) {
                helper.setText(R.id.title_text, item.getType());
                helper.setText(R.id.rule_text, item.getRule());
                helper.setText(R.id.introduce_text, item.getMemo());
            }
        };
        levelRuleListView.setAdapter(levelRuleAdapter);
    }

    public void setInfoListViewHeight(){
        int listViewHeight = 0;
        int adaptCount = levelInfoAdapter.getCount();
        for(int i=0;i<adaptCount;i++){
            View temp = levelInfoAdapter.getView(i, null, levelInfoListView);
            temp.measure(0,0);
            listViewHeight += temp.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = this.levelInfoListView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = listViewHeight;
        levelInfoListView.setLayoutParams(layoutParams);
    }

    public void setRuleListViewHeight(){
        int listViewHeight = 0;
        int adaptCount = levelRuleAdapter.getCount();
        for(int i=0;i<adaptCount;i++){
            View temp = levelRuleAdapter.getView(i, null, levelRuleListView);
            temp.measure(0,0);
            listViewHeight += temp.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = this.levelRuleListView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        String language = ((UserVipActivity)context).language;
        if(SystemConfig.ENGLISH.equals(language)) {
            layoutParams.height = listViewHeight + ViewUtil.Px2Dp(context, 300);
        } else {
            layoutParams.height = listViewHeight;
        }
        levelRuleListView.setLayoutParams(layoutParams);
    }

    private void getVipRuleResult() {
        ProgressSubscriber progressSubscriber=new ProgressSubscriber(vipRuleOnNext, context);
        progressSubscriber.setIs_progress_show(false);
        progressSubscriber.setIs_showMessage(false);
        HttpMethods.getInstance(2).getVipRule(progressSubscriber);
    }


    public static AccountLevelInfoFragment newInstance(String param1) {
        AccountLevelInfoFragment fragment = new AccountLevelInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
