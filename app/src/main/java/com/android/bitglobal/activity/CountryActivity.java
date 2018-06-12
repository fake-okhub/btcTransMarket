package com.android.bitglobal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.dao.CountryDao;
import com.android.bitglobal.R;
import com.android.bitglobal.entity.Country;
import com.android.bitglobal.entity.CountryResult;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.view.CountryAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.zhikaizhang.indexview.Binder;
import cn.zhikaizhang.indexview.IndexView;

/**
 * xiezuofei
 * 2016-09-07 16:10
 * 793169940@qq.com
 * 获取国家信息
 */
public class CountryActivity extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.indexView)
    IndexView indexView;
    private List<Country> list_data=new ArrayList<Country>();
    private CountryResult countryResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();

    }
    private void initData() {
        countryResult= CountryDao.getInstance().getIfon();
        for(Country cu:countryResult.getCountries()){
            list_data.add(cu);
        }
        Log.e("list_data",list_data+"");
    }
    private void initView() {
        tv_head_title.setText(getString(R.string.safety_gjxx));
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);

        /*Collections.sort(list_data, new PinyinComparator<Country>() {
            @Override
            public int compare(Country s1, Country s2) {
                return compare(s1.getName(), s2.getName());
            }
        });*/
        listview.setAdapter(new CountryAdapter(this, list_data));

        Binder binder = new Binder(listview, indexView) {
            @Override
            public String getListItemKey(int position) {
                return ((Country)(listview.getAdapter().getItem(position))).getDes();
            }
        };
        binder.bind();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;
        }
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
}
