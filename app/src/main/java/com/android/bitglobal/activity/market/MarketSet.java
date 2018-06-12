package com.android.bitglobal.activity.market;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.dao.CurrencySetDao;
import com.android.bitglobal.entity.PlatformSet;
import com.android.bitglobal.R;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.dao.PlatformSetDao;
import com.android.bitglobal.entity.PlatformSetResult;
import com.android.bitglobal.ui.dslv.DragSortListView;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Sort;


/**
 * xiezuofei
 * 2016-09-26 15:10
 * 793169940@qq.com
 * 控制平台
 */
public class MarketSet extends SwipeBackActivity implements View.OnClickListener{
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;
    @BindView(R.id.group_list1)
    DragSortListView group_list1;
    @BindView(R.id.group_list2)
    DragSortListView group_list2;
    @BindView(R.id.group_list3)
    DragSortListView group_list3;
    @BindView(R.id.group_list4)
    DragSortListView group_list4;

    @BindView(R.id.img_btc)
    ImageView img_btc;
    @BindView(R.id.img_eth)
    ImageView img_eth;
    @BindView(R.id.img_etc)
    ImageView img_etc;
    @BindView(R.id.img_ltc)
    ImageView img_ltc;

    int n1=100,n2=200,n3=300,n4=400;
    int size1=0,size2=0,size3=0,size4=0;
    private QuickAdapter<PlatformSet> adapter1,adapter2,adapter3,adapter4;
    private PlatformSetResult platformSetResult;
    private List<PlatformSet> platformSetList1=new ArrayList<PlatformSet>();
    private List<PlatformSet> platformSetList2=new ArrayList<PlatformSet>();
    private List<PlatformSet> platformSetList3=new ArrayList<PlatformSet>();
    private List<PlatformSet> platformSetList4=new ArrayList<PlatformSet>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置显示的视图
        setContentView(R.layout.market_set);
        context=this;
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        platformSetResult=PlatformSetDao.getInstance().getIfon();
        String symbol_str[]={"androidbtccny","androidethcny","androidetccny","androidltccny"};
        for(PlatformSet ps:platformSetResult.getPlatformSets().sort("seq", Sort.ASCENDING)){
            int sfcz=0;
            for(String s: symbol_str){
                if(s.equals(ps.getSymbol())){
                    sfcz++;
                }
            }
            if(!"0".equals(ps.getIsVisible())&&sfcz==0){
                if(ps.getCurrencyType().equals("BTC")){
                    platformSetList1.add(ps);
                    size1++;
                }
                if(ps.getCurrencyType().equals("ETH")){
                    platformSetList2.add(ps);
                    size2++;
                }
                if(ps.getCurrencyType().equals("ETC")){
                    platformSetList3.add(ps);
                    size3++;
                }
                if(ps.getCurrencyType().equals("LTC")){
                    platformSetList4.add(ps);
                    size4++;
                }
            }
        }
        adapter1.replaceAll(platformSetList1);
        adapter2.replaceAll(platformSetList2);
        adapter3.replaceAll(platformSetList3);
        adapter4.replaceAll(platformSetList4);

        Picasso.with(context).load(CurrencySetDao.getInstance().getIfon("BTC").getCoinUrl()).into(img_btc);
        Picasso.with(context).load(CurrencySetDao.getInstance().getIfon("LTC").getCoinUrl()).into(img_ltc);
        Picasso.with(context).load(CurrencySetDao.getInstance().getIfon("ETC").getCoinUrl()).into(img_etc);
        Picasso.with(context).load(CurrencySetDao.getInstance().getIfon("ETH").getCoinUrl()).into(img_eth);
    }

    private void initView() {
        tv_head_title.setText(getString(R.string.market_hqsz));
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);
        adapter1= new QuickAdapter<PlatformSet>(context, R.layout.market_set_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, final PlatformSet item) {
                final int position=helper.getPosition();
                if (SystemConfig.getLanguageEnv().equals("cn")) {
                    helper.setText(R.id.tv_currency_name, item.getName());
                } else {
                    helper.setText(R.id.tv_currency_name, item.getEnglishName());
                }
                if("0".equals(item.getIsxs())){
                    helper.setImageResource(R.id.img_xz, R.mipmap.ico_checkmark_gray);
                }else{
                    helper.setImageResource(R.id.img_xz, R.mipmap.ico_checkmark);
                }
                helper.setOnClickListener(R.id.ll_currency, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PlatformSet pfs_ls=new PlatformSet();
                        PlatformSet pfs=adapter1.getItem(position);
                        pfs_ls.setSeq(pfs.getSeq());
                        pfs_ls.setIsVisible(pfs.getIsVisible());
                        pfs_ls.setCurrencyType(pfs.getCurrencyType());
                        pfs_ls.setName(pfs.getName());
                        pfs_ls.setEnglishName(pfs.getEnglishName());
                        pfs_ls.setSymbol(pfs.getSymbol());
                        if("0".equals(pfs.getIsxs())){
                            pfs_ls.setIsxs("1");
                        }else{
                            pfs_ls.setIsxs("0");
                        }
                        adapter1.set(position,pfs_ls);
                        PlatformSetDao.getInstance().setPlatform(item.getSymbol());

                    }
                });
            }
        };
        adapter2= new QuickAdapter<PlatformSet>(context, R.layout.market_set_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, final PlatformSet item) {
                final int position=helper.getPosition();
                if (SystemConfig.getLanguageEnv().equals("cn")) {
                    helper.setText(R.id.tv_currency_name, item.getName());
                } else {
                    helper.setText(R.id.tv_currency_name, item.getEnglishName());
                }
                if("0".equals(item.getIsxs())){
                    helper.setImageResource(R.id.img_xz, R.mipmap.ico_checkmark_gray);
                }else{
                    helper.setImageResource(R.id.img_xz, R.mipmap.ico_checkmark);
                }
                helper.setOnClickListener(R.id.ll_currency, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PlatformSet pfs_ls=new PlatformSet();
                        PlatformSet pfs=adapter2.getItem(position);
                        pfs_ls.setSeq(pfs.getSeq());
                        pfs_ls.setIsVisible(pfs.getIsVisible());
                        pfs_ls.setCurrencyType(pfs.getCurrencyType());
                        pfs_ls.setName(pfs.getName());
                        pfs_ls.setEnglishName(pfs.getEnglishName());
                        pfs_ls.setSymbol(pfs.getSymbol());
                        if("0".equals(pfs.getIsxs())){
                            pfs_ls.setIsxs("1");
                        }else{
                            pfs_ls.setIsxs("0");
                        }
                        adapter2.set(position,pfs_ls);
                        PlatformSetDao.getInstance().setPlatform(item.getSymbol());

                    }
                });
            }
        };
        adapter3= new QuickAdapter<PlatformSet>(context, R.layout.market_set_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, final PlatformSet item) {
                final int position=helper.getPosition();
                if (SystemConfig.getLanguageEnv().equals("cn")) {
                    helper.setText(R.id.tv_currency_name, item.getName());
                } else {
                    helper.setText(R.id.tv_currency_name, item.getEnglishName());
                }
                if("0".equals(item.getIsxs())){
                    helper.setImageResource(R.id.img_xz, R.mipmap.ico_checkmark_gray);
                }else{
                    helper.setImageResource(R.id.img_xz, R.mipmap.ico_checkmark);
                }
                helper.setOnClickListener(R.id.ll_currency, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PlatformSet pfs_ls=new PlatformSet();
                        PlatformSet pfs=adapter3.getItem(position);
                        pfs_ls.setSeq(pfs.getSeq());
                        pfs_ls.setIsVisible(pfs.getIsVisible());
                        pfs_ls.setCurrencyType(pfs.getCurrencyType());
                        pfs_ls.setName(pfs.getName());
                        pfs_ls.setEnglishName(pfs.getEnglishName());
                        pfs_ls.setSymbol(pfs.getSymbol());
                        if("0".equals(pfs.getIsxs())){
                            pfs_ls.setIsxs("1");
                        }else{
                            pfs_ls.setIsxs("0");
                        }
                        adapter3.set(position,pfs_ls);
                        PlatformSetDao.getInstance().setPlatform(item.getSymbol());
                    }
                });
            }
        };
        adapter4= new QuickAdapter<PlatformSet>(context, R.layout.market_set_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, final PlatformSet item) {
                final int position=helper.getPosition();
                if (SystemConfig.getLanguageEnv().equals("cn")) {
                    helper.setText(R.id.tv_currency_name, item.getName());
                } else {
                    helper.setText(R.id.tv_currency_name, item.getEnglishName());
                }
                if("0".equals(item.getIsxs())){
                    helper.setImageResource(R.id.img_xz, R.mipmap.ico_checkmark_gray);
                }else{
                    helper.setImageResource(R.id.img_xz, R.mipmap.ico_checkmark);
                }
                helper.setOnClickListener(R.id.ll_currency, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PlatformSet pfs_ls=new PlatformSet();
                        PlatformSet pfs=adapter4.getItem(position);
                        pfs_ls.setSeq(pfs.getSeq());
                        pfs_ls.setIsVisible(pfs.getIsVisible());
                        pfs_ls.setCurrencyType(pfs.getCurrencyType());
                        pfs_ls.setName(pfs.getName());
                        pfs_ls.setEnglishName(pfs.getEnglishName());
                        pfs_ls.setSymbol(pfs.getSymbol());
                        if("0".equals(pfs.getIsxs())){
                            pfs_ls.setIsxs("1");
                        }else{
                            pfs_ls.setIsxs("0");
                        }
                        adapter4.set(position,pfs_ls);
                        PlatformSetDao.getInstance().setPlatform(item.getSymbol());
                    }
                });
            }
        };
        ViewTreeObserver observer1 = group_list1.getViewTreeObserver();
        observer1.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                Message msg = new Message();
                msg.what = n1;
                msg.arg1 = group_list1.getHeight();
                mHandler.sendMessage(msg);
                return true;
            }
        });
        ViewTreeObserver observer2 = group_list2.getViewTreeObserver();
        observer2.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                Message msg = new Message();
                msg.what = n2;
                msg.arg1 = group_list2.getHeight();
                mHandler.sendMessage(msg);
                return true;
            }
        });
        ViewTreeObserver observer3 = group_list3.getViewTreeObserver();
        observer3.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                Message msg = new Message();
                msg.what = n3;
                msg.arg1 = group_list3.getHeight();
                mHandler.sendMessage(msg);
                return true;
            }
        });
        ViewTreeObserver observer4 = group_list4.getViewTreeObserver();
        observer4.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                Message msg = new Message();
                msg.what = n4;
                msg.arg1 = group_list4.getHeight();
                mHandler.sendMessage(msg);
                return true;
            }
        });
        group_list1.setDropListener(onDrop1);
        group_list2.setDropListener(onDrop2);
        group_list3.setDropListener(onDrop3);
        group_list4.setDropListener(onDrop4);
        group_list1.setAdapter(adapter1);
        group_list2.setAdapter(adapter2);
        group_list3.setAdapter(adapter3);
        group_list4.setAdapter(adapter4);
    }
    private DragSortListView.DropListener onDrop1 =
        new DragSortListView.DropListener() {
            @Override
            public void drop(int from, int to) {//from to 分别表示 被拖动控件原位置 和目标位置
                if (from != to) {
                    List<PlatformSet> platformSetList1_ls=new ArrayList<PlatformSet>();
                    for(int n=0;n<platformSetList1.size();n++){
                        platformSetList1_ls.add(adapter1.getItem(n));
                    }
                    if(from<to){
                        for(int n=from;n<to;n++){
                            adapter1.set(n,platformSetList1_ls.get(n+1));
                        }
                    }else{
                        for(int n=to;n<from;n++){
                            adapter1.set(n+1,platformSetList1_ls.get(n));
                        }
                    }
                    adapter1.set(to,platformSetList1_ls.get(from));


                    List<PlatformSet> platformSetList_lss=new ArrayList<PlatformSet>();
                    for(int n=0;n<platformSetList1.size();n++){
                        PlatformSet cs_1=new PlatformSet();
                        cs_1.setSeq(n+1);
                        cs_1.setIsxs(adapter1.getItem(n).getIsxs());
                        cs_1.setIsVisible(adapter1.getItem(n).getIsVisible());
                        cs_1.setCurrencyType(adapter1.getItem(n).getCurrencyType());
                        cs_1.setName(adapter1.getItem(n).getName());
                        cs_1.setEnglishName(adapter1.getItem(n).getEnglishName());
                        cs_1.setSymbol(adapter1.getItem(n).getSymbol());
                        platformSetList_lss.add(cs_1);
                        Log.e("cs_1",cs_1+"==");
                    }
                    PlatformSetDao.getInstance().sort(platformSetList_lss);
                }
            }
    };
    private DragSortListView.DropListener onDrop2 =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {//from to 分别表示 被拖动控件原位置 和目标位置
                    if (from != to) {
                        List<PlatformSet> platformSetList2_ls=new ArrayList<PlatformSet>();
                        for(int n=0;n<platformSetList2.size();n++){
                            platformSetList2_ls.add(adapter2.getItem(n));
                        }
                        if(from<to){
                            for(int n=from;n<to;n++){
                                adapter2.set(n,platformSetList2_ls.get(n+1));
                            }
                        }else{
                            for(int n=to;n<from;n++){
                                adapter2.set(n+1,platformSetList2_ls.get(n));
                            }
                        }
                        adapter2.set(to,platformSetList2_ls.get(from));

                        List<PlatformSet> platformSetList_lss=new ArrayList<PlatformSet>();
                        for(int n=0;n<platformSetList2.size();n++){
                            PlatformSet cs_1=new PlatformSet();
                            cs_1.setSeq(n+1);
                            cs_1.setIsxs(adapter2.getItem(n).getIsxs());
                            cs_1.setIsVisible(adapter2.getItem(n).getIsVisible());
                            cs_1.setCurrencyType(adapter2.getItem(n).getCurrencyType());
                            cs_1.setName(adapter2.getItem(n).getName());
                            cs_1.setEnglishName(adapter2.getItem(n).getEnglishName());
                            cs_1.setSymbol(adapter2.getItem(n).getSymbol());
                            platformSetList_lss.add(cs_1);
                        }
                        PlatformSetDao.getInstance().sort(platformSetList_lss);

                    }
                }
            };
    private DragSortListView.DropListener onDrop3 =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {//from to 分别表示 被拖动控件原位置 和目标位置
                    if (from != to) {
                        List<PlatformSet> platformSetList3_ls=new ArrayList<PlatformSet>();
                        for(int n=0;n<platformSetList3.size();n++){
                            platformSetList3_ls.add(adapter3.getItem(n));
                        }
                        if(from<to){
                            for(int n=from;n<to;n++){
                                adapter3.set(n,platformSetList3_ls.get(n+1));
                            }
                        }else{
                            for(int n=to;n<from;n++){
                                adapter3.set(n+1,platformSetList3_ls.get(n));
                            }
                        }
                        adapter3.set(to,platformSetList3_ls.get(from));

                        List<PlatformSet> platformSetList_lss=new ArrayList<PlatformSet>();
                        for(int n=0;n<platformSetList3.size();n++){
                            PlatformSet cs_1=new PlatformSet();
                            cs_1.setSeq(n+1);
                            cs_1.setIsxs(adapter3.getItem(n).getIsxs());
                            cs_1.setIsVisible(adapter3.getItem(n).getIsVisible());
                            cs_1.setCurrencyType(adapter3.getItem(n).getCurrencyType());
                            cs_1.setName(adapter3.getItem(n).getName());
                            cs_1.setEnglishName(adapter3.getItem(n).getEnglishName());
                            cs_1.setSymbol(adapter3.getItem(n).getSymbol());
                            platformSetList_lss.add(cs_1);
                        }
                        PlatformSetDao.getInstance().sort(platformSetList_lss);
                    }
                }
            };
    private DragSortListView.DropListener onDrop4 =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {//from to 分别表示 被拖动控件原位置 和目标位置
                    if (from != to) {
                        List<PlatformSet> platformSetList4_ls=new ArrayList<PlatformSet>();
                        for(int n=0;n<platformSetList4.size();n++){
                            platformSetList4_ls.add(adapter4.getItem(n));
                        }
                        if(from<to){
                            for(int n=from;n<to;n++){
                                adapter4.set(n,platformSetList4_ls.get(n+1));
                            }
                        }else{
                            for(int n=to;n<from;n++){
                                adapter4.set(n+1,platformSetList4_ls.get(n));
                            }
                        }
                        adapter4.set(to,platformSetList4_ls.get(from));

                        List<PlatformSet> platformSetList_lss=new ArrayList<PlatformSet>();
                        for(int n=0;n<platformSetList4.size();n++){
                            PlatformSet cs_1=new PlatformSet();
                            cs_1.setSeq(n+1);
                            cs_1.setIsxs(adapter4.getItem(n).getIsxs());
                            cs_1.setIsVisible(adapter4.getItem(n).getIsVisible());
                            cs_1.setCurrencyType(adapter4.getItem(n).getCurrencyType());
                            cs_1.setName(adapter4.getItem(n).getName());
                            cs_1.setEnglishName(adapter4.getItem(n).getEnglishName());
                            cs_1.setSymbol(adapter4.getItem(n).getSymbol());
                            platformSetList_lss.add(cs_1);
                        }
                        PlatformSetDao.getInstance().sort(platformSetList_lss);
                    }
                }
            };
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 100){
                n1++;
                int height =  msg.arg1;
                ViewGroup.LayoutParams params= group_list1.getLayoutParams();
                params.height=height*size1;
                group_list1.setLayoutParams(params);
            }
            if(msg.what == 200){
                n2++;
                int height =  msg.arg1;
                ViewGroup.LayoutParams params= group_list2.getLayoutParams();
                params.height=height*size2;
                group_list2.setLayoutParams(params);
            }
            if(msg.what == 300){
                n3++;
                int height =  msg.arg1;
                ViewGroup.LayoutParams params= group_list3.getLayoutParams();
                params.height=height*size3;
                group_list3.setLayoutParams(params);
            }
            if(msg.what == 400){
                n4++;
                int height =  msg.arg1;
                ViewGroup.LayoutParams params= group_list4.getLayoutParams();
                params.height=height*size4;
                group_list4.setLayoutParams(params);
            }
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_head_back:
                finish();
                break;

        }
    }
}
