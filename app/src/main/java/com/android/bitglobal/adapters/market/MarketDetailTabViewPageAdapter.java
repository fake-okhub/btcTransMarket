package com.android.bitglobal.adapters.market;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * 币种行情页ViewPage Adapter
 * Created by elbert on 2017/3/2.
 */

public class MarketDetailTabViewPageAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments;
    private ArrayList<String> mTitles;

    public MarketDetailTabViewPageAdapter(FragmentManager fm,
                                          ArrayList<Fragment> list,
                                          ArrayList<String> titles) {
        super(fm);
        mFragments = list;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        if (mFragments != null) {
            return mFragments.get(position);
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        if (mFragments != null) {
            return mFragments.size();
        } else {
            return 0;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

}
