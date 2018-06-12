package com.android.bitglobal.fragment.asset;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.bitglobal.R;
import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.dao.CurrencyAddressDao;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.*;
import com.android.bitglobal.entity.BillDetail;
import com.android.bitglobal.fragment.BaseFragment;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.tool.StringUtils;
import com.android.bitglobal.ui.UIHelper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * xiezuofei
 * 2016-08-19 16:10
 * 793169940@qq.com
 *货币充值
 */
public class DepositFragment extends BaseFragment implements View.OnClickListener,SwipyRefreshLayout.OnRefreshListener{

    private static final String ARG_TYPE = "type";

    private Activity context;
//    @BindView(R.id.tv_head_title)
//    TextView tv_head_title;
//    @BindView(R.id.btn_head_back)
//    ImageView btn_head_back;

    @BindView(R.id.img_ewm)
    ImageView img_ewm;
    @BindView(R.id.tv_czdz)
    TextView tv_czdz;
    @BindView(R.id.asset_currency_info_text)
    TextView assetCurrencyInfoText;
    @BindView(R.id.head_title_message_text)
    TextView headTitleMessageText;

    @BindView(R.id.btn_copy)
    Button mBtnCopy;

    @BindView(R.id.tv_no_ts)
    TextView tv_no_ts;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.swipyrefreshlayout)
    SwipyRefreshLayout mSwipyRefreshLayout;
    private String dataType="0",yue="";
    private int pageIndex=1,pageSize=20,sfjz=0;
    private QuickAdapter<com.android.bitglobal.entity.BillDetail> adapter;
    private SubscriberOnNextListener searchBillOnNext;
    private List<com.android.bitglobal.entity.BillDetail> list_data=new ArrayList<BillDetail>();
    private List<String> list=new ArrayList<String>();


    private SubscriberOnNextListener getRechargeAddressOnNext;
    private CurrencyAddressResult currencyAddressResult;
    private int is_rechargeAddress=0;
    private String currencyType,address="";
    private Unbinder unbinder;
    private boolean isGetRechargeAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deposit, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        Bundle bundle = context.getIntent().getExtras();
        currencyType = bundle.getString("currencyType");
        initData();
        initView();
        getRechargeAddress();
        searchBill();
        return view;
    }

    public static DepositFragment newInstance(String param1) {
        DepositFragment fragment = new DepositFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public void clearData(){
        dataType="0";
        pageIndex=1;
        sfjz=0;
        searchBill();
    }
    private void searchBill() {
        list.clear();
        //1：充值7系统充值
        list.add("");
        list.add(currencyType);
        list.add(dataType);
        list.add(pageIndex+"");
        list.add(pageSize+"");
        ProgressSubscriber mProgressSubscriber = new ProgressSubscriber(searchBillOnNext, context);
        mProgressSubscriber.setIs_progress_show(false);
        HttpMethods.getInstance(3).searchBill(mProgressSubscriber,list);
    }
    private void initView() {

//        tv_head_title.setText(getString(R.string.trans_sb)+"-"+currencyType);
//        btn_head_back.setVisibility(View.VISIBLE);
//        btn_head_back.setOnClickListener(this);
        mBtnCopy.setOnClickListener(this);

        img_ewm.setOnClickListener(this);

        mSwipyRefreshLayout.setOnRefreshListener(this);

        //备注确认次数，BTC为3次，LTC为3次,ETC,DASH,ETH为10次,ZEC为4次
        String confirmTimes = "3";
        if("ZEC".equals(currencyType.toUpperCase())) {
            confirmTimes = "4";
        }
        if("ETC".equals(currencyType.toUpperCase()) || "DASH".equals(currencyType.toUpperCase()) ||
                "ETH".equals(currencyType.toUpperCase())) {
            confirmTimes = "10";
        }
        assetCurrencyInfoText.setText(getString(R.string.account_currency_info).replaceAll("###", currencyType).replaceAll("##", confirmTimes));
        headTitleMessageText.setText(headTitleMessageText.getText().toString().replaceAll("#", currencyType));
        adapter = new QuickAdapter<BillDetail>(context, R.layout.records_fragment_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, final BillDetail item) {
                final String show=item.getShow();
                final String balance=item.getBalance();
                final String showBalance=deFormat(balance)+" "+currencyType;
                String showChang_ls="";
                try{
                    showChang_ls =show.substring(0,show.indexOf("="));
                }catch (Exception e){
                    showChang_ls=show;
                }
                final String showChang=showChang_ls;
                final String typeName=item.getTypeName();
                final String date_time= StringUtils.dateFormat1(item.getBillDate());
               /* if("".equals(item.getYue())){
                    helper.setVisible(R.id.tv_index,false);
                }else{
                    helper.setVisible(R.id.tv_index,true);
                }*/
                if(item.getType().equals("1")) {
                    helper.setText(R.id.img_bill_type_text, getString(R.string.account_deposit));
                    helper.setBackgroundColor(R.id.img_bill_type_text, ContextCompat.getColor(context, R.color.asset_bill_grren));
                } else {
                    helper.setText(R.id.img_bill_type_text, getString(R.string.account_withdraw));
                    helper.setBackgroundColor(R.id.img_bill_type_text, ContextCompat.getColor(context, R.color.asset_bill_red));
                }
//                helper.setImageUrl(R.id.img_bill_type,item.getTypeImg());
                helper.setText(R.id.tv_bill_name,item.getTypeName());
                //  helper.setText(R.id.tv_index,item.getYue());
                helper.setText(R.id.tv_bill_number,showChang+" "+currencyType);
                helper.setText(R.id.tv_bill_tiem, StringUtils.dateFormat1(item.getBillDate()));
//                helper.setText(R.id.tv_bill_tiem1, StringUtils.dateFormat3(item.getBillDate()));
//                helper.setText(R.id.tv_bill_tiem2,StringUtils.dateFormat6(item.getBillDate()));
                helper.setOnClickListener(R.id.ll_asset_bill, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("typeName", typeName);
                        bundle.putString("showChang", showChang);
                        bundle.putString("showBalance", showBalance);
                        bundle.putString("time", date_time);
                        UIHelper.showBillDetail((Activity) context,bundle);
                    }
                });

            }
        };

        getRechargeAddressOnNext= new SubscriberOnNextListener<CurrencyAddressResult>() {
            @Override
            public void onNext(CurrencyAddressResult currencyAddressResult) {
                currencyAddressResult.setCurrencyType(currencyType);
                currencyAddressResult.setUserId(UserDao.getInstance().getUserId());
                CurrencyAddressDao.getInstance().add(currencyAddressResult);
                is_rechargeAddress=1;
                initRechargeAddress();
            }
        };

        searchBillOnNext = new SubscriberOnNextListener<PageResult>() {
            @Override
            public void onNext(PageResult pageResult) {
                if(pageIndex==1&&dataType.equals("0")){
                    list_data.clear();
                    adapter.clear();
                }
                List<BillDetail> detailList=pageResult.getBillDetails();

                if(detailList!=null&&detailList.size()>0){
                    pageIndex++;
                }
                if (detailList!=null&&detailList.size()>0){
                    for(BillDetail db:detailList){
                        String yue_l= StringUtils.dateFormat7(db.getBillDate());
                  /*  if(yue.equals(yue_l)){
                        db.setYue("");
                    }else{
                        db.setYue(yue_l);
                    }*/
                        list_data.add(db);
                        yue=yue_l;
                    }
                }
                if ((detailList==null&&dataType.equals("0"))||(detailList.size()==0&&dataType.equals("0"))) {
                    dataType = "1";
                    pageIndex = 1;
                    searchBill();
                }
                if (detailList!=null && detailList.size() < 1&&list_data.size()!=0) {
                    sfjz++;
                }
                if(list_data.size()>0){
                    tv_no_ts.setVisibility(View.GONE);
                }else{
                    tv_no_ts.setVisibility(View.VISIBLE);
                }
                adapter.replaceAll(list_data);
            }

        };
        listview.setAdapter(adapter);
    }

    private void initData() {
        currencyAddressResult= CurrencyAddressDao.getInstance().getIfon(currencyType, UserDao.getInstance().getUserId());
    }

    public void getRechargeAddress() {
        if(currencyAddressResult==null||(new Date().getTime()-currencyAddressResult.getVersion()>60*1000*24)){
            if(isGetRechargeAddress) {
                HttpMethods.getInstance(3).getRechargeAddress(new ProgressSubscriber(getRechargeAddressOnNext, context),currencyType);
            }
        }else{
            is_rechargeAddress=1;
            initRechargeAddress();
        }
    }

    private void initRechargeAddress(){
        initData();
        try {
            if(currencyAddressResult!=null&&currencyAddressResult.getRechargeAddrs()!=null){
                for(CurrencyAddress currencyAddress:currencyAddressResult.getRechargeAddrs()){
                    if(currencyAddress.getCurrencyType().equals(currencyType.toLowerCase())||currencyAddress.getCurrencyType().equals(currencyType.toUpperCase())){
                        address=currencyAddress.getAddress();
                        tv_czdz.setText(address);
                        img_ewm.setImageBitmap(createBitmap(Create2DCode(address)));
                        mBtnCopy.setVisibility(View.VISIBLE);
                    }
                }
            }
        }catch (Exception e){

        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.btn_head_back:
//                finish();
//                break;
            case R.id.img_ewm:
            case R.id.btn_copy:
                AppContext.getInstance().copy(address+"",context);
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
        unbinder.unbind();

    }
    public Bitmap Create2DCode(String str) throws WriterException, UnsupportedEncodingException {
        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败

        BitMatrix matrix = new MultiFormatWriter().encode(new String(str.getBytes("GBK"),"ISO-8859-1"), BarcodeFormat.QR_CODE, 600, 600);

        int width = matrix.getWidth();
        int height = matrix.getHeight();
        //二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(matrix.get(x, y)){
                    pixels[y * width + x] = 0xff000000;
                }

            }
        }
        int[] colors={R.color.text_color_white};
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private Bitmap createBitmap( Bitmap src)

    {

        if( src == null )
        {
            return null;
        }
        Paint paint=new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);

        int w = 600;
        int h = 600;
        Bitmap newb = Bitmap.createBitmap( w, h, Bitmap.Config.ARGB_8888 );
        Canvas cv = new Canvas( newb );

        cv.drawColor(Color.WHITE);

        cv.drawBitmap(src, 0, 0, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();//存储
        return newb;

    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if(direction == SwipyRefreshLayoutDirection.TOP){
            clearData();
        }else{
            if(sfjz==0){
                searchBill();
            }else{
                UIHelper.ToastMessage(context,R.string.trans_wgdsjl_toast);
            }
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

    public void setIsGetRechargeAddress(boolean isGetRechargeAddress) {
        this.isGetRechargeAddress = isGetRechargeAddress;
    }

}
