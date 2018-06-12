package com.android.bitglobal.activity.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.entity.TestFeedbackBean;
import com.android.bitglobal.tool.DeviceUtil;
import com.android.bitglobal.tool.L;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by joyson on 2017/7/27.
 */

public class FeedbackActivity extends SwipeBackActivity  implements TakePhoto.TakeResultListener,InvokeListener {

    @BindView(R.id.btn_head_back)
    ImageView btnHeadBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.tv_head_price)
    TextView tvHeadPrice;
    @BindView(R.id.rl_fb_function_suggest)
    RelativeLayout rlFbFunctionSuggest;
    @BindView(R.id.rl_fb_operator_fault)
    RelativeLayout rlFbOperatorFault;
    @BindView(R.id.et_fb_leave_suggest)
    EditText etFbLeaveSuggest;
    @BindView(R.id.tv_fb_leave_words)
    TextView tvFbLeaveWords;
    @BindView(R.id.et_fb_contact)
    EditText etFbContact;
    @BindView(R.id.btn_fb_sumbit)
    Button btnFbSumbit;
    @BindView(R.id.tv_fb_function_suggest)
    TextView tvFbFunctionSuggest;
    @BindView(R.id.tv_fb_operator_fault)
    TextView tvFbOperatorFault;
    @BindView(R.id.iv_fb_add_image)
    ImageView ivFbAddImage;

    @BindView(R.id.layout_feedback)
    LinearLayout llFeedBack;
    @BindView(R.id.layout_feedback_history)
    RelativeLayout rlFeedBackHistory;

    @BindView(R.id.srl_fb_my_history)
    SwipyRefreshLayout srlFbMyFistory;
    @BindView(R.id.rv_fb_my_history)
    RecyclerView rvFbMyHistory;
    @BindView(R.id.tv_feedback_history_nodata)
    TextView tvFeedbackHistoryNodata;

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    private Context context;
    private LinearLayout.LayoutParams llParams;
    private RelativeLayout.LayoutParams rlParams;
    private int charLength;
    private Bitmap bm;

    /**
     * 图片压缩的宽度
     */
    private final int IMAGE_WIDTH = 20;
    /**
     * 图片压缩的高度
     */
    private final int IMAGE_HEIGHT = 20;

    private interface IFeedPageType{
        int feedBackHome=1;
        int feedBackHistory=2;
    }
    IFeedPageType iFeedPageType;
    //当前页面标识
    private int iPageFlag=0;


    private Handler mHandler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTakePhoto().onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        getCurrentPage(IFeedPageType.feedBackHome);
        initView();
    }


    private void initView() {
        context=this;
        btnHeadBack.setVisibility(View.VISIBLE);
        tvHeadPrice.setVisibility(View.VISIBLE);
        tvHeadPrice.setText(getString(R.string.setting_feedback_history));
        tvHeadTitle.setText(getString(R.string.setting_feedback_title));

        //默认设置第一个为选中
        changeAppearance(rlFbFunctionSuggest,tvFbFunctionSuggest,rlFbOperatorFault,tvFbOperatorFault);

        etFbLeaveSuggest.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        etFbLeaveSuggest.setHorizontallyScrolling(false);
        etFbLeaveSuggest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                charLength=s.length();
                tvFbLeaveWords.setText(charLength+"/500");
            }
        });

        rvFbMyHistory.setLayoutManager(new LinearLayoutManager(this));
        FeedBackAdapter adapter= new FeedBackAdapter();
        adapter.setNewData(testAddData());
        rvFbMyHistory.setAdapter(adapter);
        srlFbMyFistory.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(srlFbMyFistory != null) {
                            srlFbMyFistory.setRefreshing(false);
                        }
                    }
                },2000);

            }
        });

    }

    class FeedBackAdapter extends BaseQuickAdapter<TestFeedbackBean.FeedBackBean, BaseViewHolder> {
        public FeedBackAdapter() {
            super(R.layout.feedback_myhistory_item, testAddData());
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, TestFeedbackBean.FeedBackBean item) {
            viewHolder.setText(R.id.tv_fb_history_time, item.getTime())
                    .setText(R.id.tv_fb_history_content, item.getContent())
                    .setText(R.id.tv_fb_history_customer_response, item.getCustomResponse());
//            Glide.with(mContext).load(item.getUserAvatar()).crossFade().into((ImageView) viewHolder.getView(R.id.iv));
        }
    }

    private List<TestFeedbackBean.FeedBackBean> testAddData() {
        TestFeedbackBean testFeedbackBean = new TestFeedbackBean();
        List<TestFeedbackBean.FeedBackBean> feedBackBeens = new ArrayList<>();
        TestFeedbackBean.FeedBackBean feedBackBean=new TestFeedbackBean.FeedBackBean();
        for (int i = 0; i < 5; i++) {
            feedBackBean.setTime("时间"+i);
            feedBackBean.setContent("内容"+i);
            feedBackBean.setCustomResponse("resopnse--"+i);
            feedBackBeens.add(i,feedBackBean);
        }
        testFeedbackBean.setFeedBackBeens(feedBackBeens);
        return feedBackBeens;
    }

    @OnClick({R.id.btn_head_back, R.id.tv_head_price, R.id.rl_fb_function_suggest, R.id.rl_fb_operator_fault,
            R.id.btn_fb_sumbit,R.id.iv_fb_add_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_head_back:
                if (iPageFlag==IFeedPageType.feedBackHome){
                    finish();
                }else if (iPageFlag==IFeedPageType.feedBackHistory){
                    getCurrentPage(IFeedPageType.feedBackHome);
                }

                break;
            case R.id.tv_head_price:
                getCurrentPage(IFeedPageType.feedBackHistory);
                break;
            case R.id.rl_fb_function_suggest:
                changeAppearance(rlFbFunctionSuggest,tvFbFunctionSuggest,rlFbOperatorFault,tvFbOperatorFault);
                break;
            case R.id.rl_fb_operator_fault:
                changeAppearance(rlFbOperatorFault,tvFbOperatorFault,rlFbFunctionSuggest,tvFbFunctionSuggest);
                break;
            case R.id.btn_fb_sumbit:
                break;
            case R.id.iv_fb_add_image:
                configCompress(takePhoto);
                takePhoto.onPickFromGallery();
                break;
        }
    }

    /**
     * 点击改变控件外观
     * @param rlseleView 设置selected外部容器
     * @param tvseleView 设置selected内部容器
     * @param rlunseleView 设置unselect外部容器
     * @param tvunseleView 设置unselect内部容器
     */
    private void changeAppearance(RelativeLayout rlseleView,TextView tvseleView,
                                  RelativeLayout rlunseleView,TextView tvunseleView){
        llParams =(LinearLayout.LayoutParams)rlseleView.getLayoutParams();
        //selected之后容器高度为46dp
        llParams.height= DeviceUtil.dp2px(context,46);
        rlseleView.setLayoutParams(llParams);
        rlseleView.setBackgroundResource(R.mipmap.btn_feedback_pressed);

        tvseleView.setTextColor(getColor(R.color.white));
        rlParams= (RelativeLayout.LayoutParams) tvseleView.getLayoutParams();
        //selected之后 tv距下面距离 5dp
        tvseleView.setPadding(0,0,0,DeviceUtil.dp2px(context,5));
        tvseleView.setLayoutParams(rlParams);

        //unselected之后容器高度为39dp
        llParams =(LinearLayout.LayoutParams)rlunseleView.getLayoutParams();
        llParams.height= DeviceUtil.dp2px(context,39);
        rlunseleView.setLayoutParams(llParams);
        rlunseleView.setBackgroundResource(R.drawable.ed_trans_bg);

        tvunseleView.setTextColor(getColor(R.color.feedback_textview_unselected_color));
        rlParams= (RelativeLayout.LayoutParams) tvunseleView.getLayoutParams();
        //selected之后 tv距下面距离 0dp
        tvunseleView.setPadding(0,0,0,0);
        tvunseleView.setLayoutParams(rlParams);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionManager.handlePermissionsResult(this,type,invokeParam,this);
    }

    @Override
    public void takeSuccess(TResult result) {
        L.d("takeSuccess 图片位置："+result.getImage().getCompressPath());
        Glide.with(context).load(new File(result.getImage().getCompressPath())).into(ivFbAddImage);
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
        if(PermissionManager.TPermissionType.WAIT.equals(type)){
            this.invokeParam=invokeParam;
        }
        return type;
    }

    /**
     *  获取TakePhoto实例
     * @return
     */
    public TakePhoto getTakePhoto(){
        if (takePhoto==null){
            takePhoto= (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
        }
        return takePhoto;
    }


    /**
     * 压缩图片参数
     * @param takePhoto
     */
    private void configCompress(TakePhoto takePhoto){
        CompressConfig config;
        config=new CompressConfig.Builder()
                //设置图片最大大小
                .setMaxSize(4080)

                //设置图片最大像素
                .setMaxPixel(800)
                //是否保存图片
                .enableReserveRaw(true)
                .create();
        takePhoto.onEnableCompress(config,false);
    }

    /**
     * 切换当前页面
     * @param pageFlag
     */
    private void getCurrentPage(int pageFlag){
        switch (pageFlag){
            case IFeedPageType.feedBackHome:
                iPageFlag=IFeedPageType.feedBackHome;
                llFeedBack.setVisibility(View.VISIBLE);
                rlFeedBackHistory.setVisibility(View.GONE);
//                llFeedBackHistory.startAnimation(pageAnimation(true));/
                break;
            case IFeedPageType.feedBackHistory:
                iPageFlag=IFeedPageType.feedBackHistory;
                llFeedBack.setVisibility(View.GONE);
//                llFeedBack.startAnimation(pageAnimation(true));
                rlFeedBackHistory.setVisibility(View.VISIBLE);
                break;
            default:

                break;
        }
    }

    /**
     * 页面动画
     * @param isHidden true隐藏动画 false显示动画
     */
    private TranslateAnimation pageAnimation(boolean isHidden){
        TranslateAnimation mHiddenAction;
        if (isHidden){
            mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    -1.0f);
            mHiddenAction.setDuration(500);
        }else {
            mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            mHiddenAction.setDuration(500);
        }
        return mHiddenAction;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
