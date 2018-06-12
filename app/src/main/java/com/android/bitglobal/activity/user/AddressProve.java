package com.android.bitglobal.activity.user;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.subscribers.SubscriberOnNextListener;
import com.android.bitglobal.R;
import com.android.bitglobal.common.SystemConfig;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.FileactionResult;
import com.android.bitglobal.entity.IdentityAuthResult;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.http.HttpMethods;
import com.android.bitglobal.subscribers.ProgressSubscriber;
import com.android.bitglobal.tool.ImageLoadTask;
import com.android.bitglobal.tool.PicassoImageLoader;
import com.android.bitglobal.ui.UIHelper;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;
import com.android.bitglobal.view.UserSelect;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by bitbank on 16/9/28.
 * 住址证明
 */
public class AddressProve extends SwipeBackActivity implements View.OnClickListener {
    private Activity context;
    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;


    @BindView(R.id.img_identity_1)
    ImageView img_identity_1;

    @BindView(R.id.bnt_commit)
    Button bnt_commit;


    private String frontalImg = "";//身份证正面照
    private String backImg = "";//身份证背面照
    private String loadImg = "";//手持身份证照
    private String proofAddressImg = "";//住址证明照
    private String bankCardId = "";//银行卡号
    private String bankTel = "";//银行预留手机号
    private String bankId = "";//银行id
    private String realName = "";//真实姓名
    private String cardId = "";//证件号
    private String country = "";//证件所属国家
    private String type = "1";//操作类型 1：保存2：提交审核

    private FunctionConfig functionConfig;
    private UserSelect userSelect;
    private UserInfo userInfo;
    private ImageView imageView;
    List<String> list = new ArrayList<String>();
    private SubscriberOnNextListener depthIdentityAuthOnNext, getIdentityAuthOnNext;

    private SubscriberOnNextListener mUpdateImageSubscriberOnNextListener;
    private static final int OPEN_GALLERY = 1;//打开相册
    private static final int OPEN_CAMERA = 2; // 打开相机
    private static final String IMAGE_FILE_LOCATION = Environment.getExternalStorageDirectory().getPath();
    private File mImgFile = new File(IMAGE_FILE_LOCATION, "portrait.png");
    private int mClickType = 0;//0表示点击上传手持照1表示上传正面照2表示上传反面照
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_safety_identity_address_prove);
        context = this;
        ButterKnife.bind(this);
        initData();
        initView();
        getIdentityAuthStatus();
    }

    private void initView() {
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        //配置功能
        functionConfig = functionConfigBuilder
                .setEnableEdit(false)
                .setEnableCrop(false)
                .setEnablePreview(false).build();
        cn.finalteam.galleryfinal.ImageLoader imageLoader;
        imageLoader = new PicassoImageLoader();
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.parseColor("#1F2A40"))
                .setTitleBarTextColor(Color.WHITE)
                .build();
        ThemeConfig themeConfig = theme;

        CoreConfig coreConfig = new CoreConfig.Builder(this, imageLoader, themeConfig)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(null)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);

        userSelect = new UserSelect(context);
        userSelect.tv_select_title1.setVisibility(View.GONE);
        userSelect.tv_select_title2.setText(R.string.asset_xzczfs);
        userSelect.bnt_select_subject_1.setText(R.string.user_xc);
        userSelect.bnt_select_subject_2.setText(R.string.user_pz);
        userSelect.bnt_select_subject_1.setOnClickListener(this);
        userSelect.bnt_select_subject_2.setOnClickListener(this);

        tv_head_title.setText(R.string.safety_dzzm);
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(this);

        img_identity_1.setOnClickListener(this);

        bnt_commit.setOnClickListener(this);

        mUpdateImageSubscriberOnNextListener = new SubscriberOnNextListener<FileactionResult>() {
            @Override
            public void onNext(FileactionResult o) {

                if(!o.getCode().equals(SystemConfig.SUCCESS))
                {
                    UIHelper.ToastMessage(context,o.getMessage());
                    return;
                }
                proofAddressImg = o.getFileName();
            }
        };

        depthIdentityAuthOnNext = new SubscriberOnNextListener() {
            @Override
            public void onNext(Object o) {
                finish();
                UIHelper.showSafetyBank(context);
            }
        };
        getIdentityAuthOnNext = new SubscriberOnNextListener<IdentityAuthResult>() {
            @Override
            public void onNext(IdentityAuthResult identityAuthResult) {
                frontalImg = identityAuthResult.getFrontalImg();
                backImg = identityAuthResult.getBackImg();
                loadImg = identityAuthResult.getLoadImg();
                proofAddressImg = identityAuthResult.getProofAddressImg();
                bankCardId = identityAuthResult.getBankCardId();
                bankTel = identityAuthResult.getBankTel();
                bankId = identityAuthResult.getBankId();
                realName = identityAuthResult.getRealName();
                cardId = identityAuthResult.getCardId();
                country = identityAuthResult.getCountry();
                initImageView(identityAuthResult);
            }
        };
    }

    private void initImageView(IdentityAuthResult identityAuthResult)
    {

        Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO 接收消息并且去更新UI线程上的控件内容
                if (msg.what == 100) {
                }
                super.handleMessage(msg);
            }
        };
        String userid = UserDao.getInstance().getUserId();
        if(!proofAddressImg.equals(""))
        {

            ImageLoadTask mImageFrontalLoadTask = new ImageLoadTask(handler);
            mImageFrontalLoadTask.execute(identityAuthResult.getFileBasePath()+identityAuthResult.getFrontalImg()+"&rs=1",UserDao.getInstance().getToken(),userid);
            mImageFrontalLoadTask.setImageView(img_identity_1);

        }

    }

    private void initData() {
        userInfo = UserDao.getInstance().getIfon();
        if (is_token(userInfo)) {

        } else {
            finish();
        }

    }

    private void depthIdentityAuth() {
        list.clear();
        list.add(frontalImg);
        list.add(backImg);
        list.add(loadImg);
        list.add(proofAddressImg);
        list.add(bankCardId);
        list.add(bankTel);
        list.add(bankId);
        list.add(realName);
        list.add(cardId);
        list.add(country);
        list.add(type);
        HttpMethods.getInstance(3).depthIdentityAuth(new ProgressSubscriber(depthIdentityAuthOnNext, this), list);
    }

    private void getIdentityAuthStatus() {
        HttpMethods.getInstance(3).getIdentityAuthStatus(new ProgressSubscriber(getIdentityAuthOnNext, this));
    }

    private void openGallery() {
        GalleryFinal.openGallerySingle(OPEN_GALLERY, functionConfig, mOnHanlderResultCallback);
    }

    private void openCamera() {
        GalleryFinal.openCamera(OPEN_CAMERA, functionConfig, mOnHanlderResultCallback);
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                Log.e("resultList", resultList.get(0).getPhotoPath() + "====");

                File file  = new File(resultList.get(0).getPhotoPath());
                Picasso.with(context)
                        .load(file)
                        .into(imageView);
                updateImage(file);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            UIHelper.ToastMessage(context, errorMsg);
        }
    };

    private void updateImage(File file) {
        HttpMethods.getInstance(5).fileaction(new ProgressSubscriber(mUpdateImageSubscriberOnNextListener, this), file);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_head_back:
                finish();
                break;
            case R.id.bnt_select_subject_1:
                userSelect.dismiss();
                openGallery();
                break;
            case R.id.bnt_select_subject_2:
                userSelect.dismiss();
                String[] perms = {"android.permission.CAMERA"};
                int permsRequestCode = 200;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(perms, permsRequestCode);
                } else {
                    openCamera();
                }
                break;
            case R.id.img_identity_1:
                mClickType = 0;
                imageView = img_identity_1;
                userSelect.show();
                break;
            case R.id.bnt_commit:
                if (proofAddressImg.equals("")) {
                    UIHelper.ToastMessage(context, getString(R.string.safety_sczzzzm_xz_toast));
                    return;
                }
                depthIdentityAuth();
                break;


        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case 200:

                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (cameraAccepted) {
                    //授权成功之后，调用系统相机进行拍照操作等
                    openCamera();
                } else {
                    //用户授权拒绝之后，友情提示一下就可以了
                    UIHelper.ToastMessage(context, R.string.safety_myqkqx_toast);
                }
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
