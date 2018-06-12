package com.android.bitglobal.fragment.trans;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.EdgeEffectCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.activity.trans.TransActivity;
import com.android.bitglobal.fragment.BaseFragment;
import com.android.bitglobal.tool.SharedPreferences;
import com.android.bitglobal.view.NoScrollViewpager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class TransTypeFragment extends BaseFragment implements ViewPager.OnPageChangeListener ,View.OnClickListener {
    private Activity context;
    private EdgeEffectCompat leftEdge;
    private EdgeEffectCompat rightEdge;
    List<Fragment> listFragment = new ArrayList<Fragment>();
    private View mView;
    @BindView(R.id.view_header_line1)
    View view_header_line1;
    @BindView(R.id.view_header_line2)
    View view_header_line2;
    @BindView(R.id.view_header_line3)
    View view_header_line3;
    @BindView(R.id.view_header_line4)
    View view_header_line4;
    @BindView(R.id.tv_header_title1)
    TextView tv_header_title1;
    @BindView(R.id.tv_header_title2)
    TextView tv_header_title2;
    @BindView(R.id.tv_header_title3)
    TextView tv_header_title3;
    @BindView(R.id.tv_header_title4)
    TextView tv_header_title4;
    @BindView(R.id.viewPager)
    NoScrollViewpager viewPager;
//    public static ExchangeFragment transBuyOrSellFragment;
    public static TransBuyOrSellFragment transBuyFragment;
    public static TransBuyOrSellFragment transSellFragment;
//    public static TransBuyFragment transBuyFragment;
//    public static TransSellFragment transSellFragment;
//    public static SellFragment trans_sell;
    public static OrdersFragment ordersFragment;
                  HistoryFragment historyFragment;
    int select_position=0;
    private Unbinder unbinder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_trans_type, container, false);
        unbinder= ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        initData();
        initView();
        loadData();
    }

    private void initView() {
        tv_header_title1.setText(R.string.trans_mai);
        tv_header_title2.setText(R.string.trans_mao);
        tv_header_title3.setText(R.string.trans_dqwt);
        tv_header_title4.setText(R.string.trans_lswt);
        tv_header_title4.setVisibility(View.VISIBLE);
        transBuyFragment = new TransBuyOrSellFragment(TransBuyOrSellFragment.TYPE_TRANS_BUY);
        transBuyFragment.setT_Currency(((TransActivity)getActivity()).getT_Currency());
        listFragment.add(transBuyFragment);
        transSellFragment = new TransBuyOrSellFragment(TransBuyOrSellFragment.TYPE_TRANS_SELL);
        transSellFragment.setT_Currency(((TransActivity)getActivity()).getT_Currency());
        listFragment.add(transSellFragment);
//        transBuyFragment = new TransBuyFragment();
//        listFragment.add(transBuyFragment);
//        transSellFragment = new TransSellFragment();
//        listFragment.add(transSellFragment);
//        trans_sell = new SellFragment();
//        listFragment.add(trans_sell);
        ordersFragment = new OrdersFragment();
        ordersFragment.setT_Currency(((TransActivity)getActivity()).getT_Currency());
        listFragment.add(ordersFragment);
        historyFragment = new HistoryFragment();
        historyFragment.setT_Currency(((TransActivity)getActivity()).getT_Currency());
        listFragment.add(historyFragment);
        tv_header_title1.setOnClickListener(this);
        tv_header_title2.setOnClickListener(this);
        tv_header_title3.setOnClickListener(this);
        tv_header_title4.setOnClickListener(this);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
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
        view_header_line1.setVisibility(View.VISIBLE);
        view_header_line4.setVisibility(View.INVISIBLE);
        String type = ((TransActivity)context).getType();
        if(!"".equals(type)) {
            if(type.equals(TransBuyOrSellFragment.TYPE_TRANS_BUY)) {
                transBuyFragment.setTransType(TransBuyOrSellFragment.TYPE_TRANS_BUY);
                showPage(0);
            } else if(type.equals(TransBuyOrSellFragment.TYPE_TRANS_SELL)) {
                transSellFragment.setTransType(TransBuyOrSellFragment.TYPE_TRANS_SELL);
                showPage(1);
            }
        }
    }

    public void addFragment(){

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

    private void initData() {

    }

    private void loadData() {

    }
    public void selectFragment() {
        if (select_position==0){
            if (transBuyFragment !=null&& transBuyFragment.isVisible()) {
                transBuyFragment.setTransType(TransBuyOrSellFragment.TYPE_TRANS_BUY);
                transBuyFragment.initViewData();
            }
        }  else if (select_position==1){
            if (transSellFragment !=null&& transSellFragment.isVisible()) {
                transSellFragment.setTransType(TransBuyOrSellFragment.TYPE_TRANS_SELL);
                transSellFragment.initViewData();
            }
        }
//        else if (select_position==1){
//            if (trans_sell!=null&&trans_sell.isVisible()) {
//                trans_sell.initViewData();
//            }
//        }

    }
    public void selectFragment_index() {
        if (select_position==0){
            if (transBuyFragment !=null&& transBuyFragment.isVisible()) {
                transBuyFragment.setTransType(TransBuyOrSellFragment.TYPE_TRANS_BUY);
                transBuyFragment.initViewData();
            }
        }  else if (select_position==1){
            if (transSellFragment !=null&& transSellFragment.isVisible()) {
                transBuyFragment.setTransType(TransBuyOrSellFragment.TYPE_TRANS_SELL);
                transSellFragment.initViewData();
            }
        }
//        else if (select_position==1){
//            if (trans_sell!=null&&trans_sell.isVisible()) {
//                trans_sell.initViewData();
//            }
//        }
        if (historyFragment !=null) {
            historyFragment.clearData();
        }
        if (ordersFragment !=null) {
            ordersFragment.clearData();
        }
    }
    public void clear_ed(){
        try {
//            if (trans_sell!=null) {
//                trans_sell.clear_ed();
//            }
            if (transBuyFragment != null/*&&transBuyOrSellFragment.isVisible()*/) {
                transBuyFragment.clear_ed();
            }
            if (transSellFragment != null/*&&transBuyOrSellFragment.isVisible()*/) {
                transSellFragment.clear_ed();
            }
        }catch (Exception e){

        }

    }
    @Override
    public void onClick(View v) {
        MainActivity.getInstance().showKeyboardView(false);
        switch (v.getId()) {

            case R.id.tv_header_title1:
//                if (trans_sell!=null) {
//                    trans_sell.clear_ed();
//                }
                if (transBuyFragment != null/*&&transBuyOrSellFragment.isVisible()*/) {
                    transBuyFragment.clear_ed();
                }
                if (transSellFragment != null/*&&transBuyOrSellFragment.isVisible()*/) {
                    transSellFragment.clear_ed();
                }
                transBuyFragment.setTransType(TransBuyOrSellFragment.TYPE_TRANS_BUY);
                ((TransActivity)getActivity()).showKeyboardView(false);
                showPage(0);
                break;
            case R.id.tv_header_title2:
                if (transBuyFragment != null/*&&transBuyOrSellFragment.isVisible()*/) {
                    transBuyFragment.clear_ed();
                }
                if (transSellFragment != null/*&&transBuyOrSellFragment.isVisible()*/) {
                    transSellFragment.clear_ed();
                }
//                if (trans_sell!=null) {
//                    trans_sell.clear_ed();
//                }
                transSellFragment.setTransType(TransBuyOrSellFragment.TYPE_TRANS_SELL);
                ((TransActivity)getActivity()).showKeyboardView(false);
                showPage(1);
                break;
            case R.id.tv_header_title3:
                if (transBuyFragment != null/*&&transBuyOrSellFragment.isVisible()*/) {
                    transBuyFragment.clear_ed();
                }
                if (transSellFragment != null/*&&transBuyOrSellFragment.isVisible()*/) {
                    transSellFragment.clear_ed();
                }
//                if (trans_sell!=null) {
//                    trans_sell.clear_ed();
//                }
                showPage(2);
                break;
            case R.id.tv_header_title4:
                if (transBuyFragment != null/*&&transBuyOrSellFragment.isVisible()*/) {
                    transBuyFragment.clear_ed();
                }
                if (transSellFragment != null/*&&transBuyOrSellFragment.isVisible()*/) {
                    transSellFragment.clear_ed();
                }
//                if (trans_sell!=null) {
//                    trans_sell.clear_ed();
//                }
                showPage(3);
                break;
        }
    }

    public void showPage(int item) {
        viewPager.setCurrentItem(item);
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

    @Override
    public void onPageSelected(int position) {
        select_position=position;
        MainActivity.getInstance().showKeyboardView(false);
        if (position == 0) {
            view_header_line1.setVisibility(View.VISIBLE);
            view_header_line2.setVisibility(View.INVISIBLE);
            view_header_line3.setVisibility(View.INVISIBLE);
            view_header_line4.setVisibility(View.INVISIBLE);
        } else if (position == 1) {
            view_header_line1.setVisibility(View.INVISIBLE);
            view_header_line2.setVisibility(View.VISIBLE);
            view_header_line3.setVisibility(View.INVISIBLE);
            view_header_line4.setVisibility(View.INVISIBLE);
        } else if (position == 2) {
            view_header_line1.setVisibility(View.INVISIBLE);
            view_header_line2.setVisibility(View.INVISIBLE);
            view_header_line3.setVisibility(View.VISIBLE);
            view_header_line4.setVisibility(View.INVISIBLE);
        }
        else if (position == 3) {
            view_header_line1.setVisibility(View.INVISIBLE);
            view_header_line2.setVisibility(View.INVISIBLE);
            view_header_line3.setVisibility(View.INVISIBLE);
            view_header_line4.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}