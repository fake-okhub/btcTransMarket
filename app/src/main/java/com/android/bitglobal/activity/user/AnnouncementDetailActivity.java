package com.android.bitglobal.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.activity.BaseActivity;
import com.android.bitglobal.tool.StringUtils;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/7/24.
 */

public class AnnouncementDetailActivity extends BaseActivity {

    @BindView(R.id.btn_head_back)
    protected ImageView headBackImage;
    @BindView(R.id.tv_head_title)
    protected TextView headTitleText;
    @BindView(R.id.announcementDetailTitleText)
    protected TextView announcementDetailTitleText;
    @BindView(R.id.announcementDetailTimeText)
    protected TextView announcementDetailTimeText;
    @BindView(R.id.announcementDetailText)
    protected TextView announcementDetailText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement_detail);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        headBackImage.setVisibility(View.VISIBLE);
        headTitleText.setText(getString(R.string.user_announcement_detail));
        announcementDetailTitleText.setText(getIntent().getStringExtra("title"));
        SimpleDateFormat date = new SimpleDateFormat("MM dd, yyyy", Locale.getDefault());
        announcementDetailTimeText.setText(date.format(new Date(Long.parseLong(getIntent().getStringExtra("publishTime")))));
//        String str = getIntent().getStringExtra("content");
//        if (StringUtils.isEmpty(str)) {
//            str = "";
//        }
//        CharSequence charSequence = Html.fromHtml(str);
        announcementDetailText.setText(getIntent().getStringExtra("content"));
    }

    @OnClick(R.id.btn_head_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_head_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
