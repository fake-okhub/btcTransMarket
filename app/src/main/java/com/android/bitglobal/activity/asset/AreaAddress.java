package com.android.bitglobal.activity.asset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.dao.AreaDataDao;
import com.android.bitglobal.entity.AreaData;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * xiezuofei
 * 2016-09-25 16:10
 * 793169940@qq.com
 * 地区
 */
public class AreaAddress extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @BindView(R.id.tv_no_ts)
    TextView tv_no_ts;
    @BindView(R.id.listview)
    ListView listview;

    private QuickAdapter<AreaData> adapter;
    private List<AreaData> list_data=new ArrayList<AreaData>();
    private List<AreaData> list_data2=new ArrayList<AreaData>();
    private String province_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_currency_withdraw_address);
        context=this;
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        tv_head_title.setText(getString(R.string.safety_xzdq));
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        adapter = new QuickAdapter<AreaData>(context, R.layout.asset_bank_address_item,list_data) {
            @Override
            protected void convert(final BaseAdapterHelper helper, final AreaData item) {
                helper.setText(R.id.tv_bank_name,item.getName());
                helper.setVisible(R.id.tv_bank_number,false);
                helper.setOnClickListener(R.id.ll_bank_address, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if("1".equals(item.getParentId())){
                            initData2(item.getId());
                            province_name=item.getName();
                        }else{
                            Intent intent = getIntent();
                            Bundle bundle = new Bundle();
                            bundle.putString("id", item.getId());
                            bundle.putString("name", province_name+" "+item.getName());
                            bundle.putString("parentId", item.getParentId());
                            bundle.putString("province_name", province_name);
                            bundle.putString("city_name", item.getName());
                            intent.putExtras(bundle);
                            setResult(5289, intent);
                            finish();
                        }

                    }
                });

            }
        };
        listview.setAdapter(adapter);

    }
    private void initData() {
        list_data= AreaDataDao.getInstance().getIfon("1");
    }
    private void initData2(String parentId) {
        adapter.clear();
        list_data2=AreaDataDao.getInstance().getIfon(parentId);
        adapter.replaceAll(list_data2);
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
