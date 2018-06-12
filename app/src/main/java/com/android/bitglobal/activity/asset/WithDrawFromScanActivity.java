package com.android.bitglobal.activity.asset;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.fragment.asset.WithdrawFragment;
import com.android.bitglobal.ui.swipebacklayout.SwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WithDrawFromScanActivity extends SwipeBackActivity {

    @BindView(R.id.tv_head_title)
    TextView tv_head_title;
    @BindView(R.id.btn_head_back)
    ImageView btn_head_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_draw_from_scan);
        ButterKnife.bind(this);
        tv_head_title.setText(getIntent().getExtras().getString("currencyType") + " " + getString(R.string.account_balance));
        btn_head_back.setVisibility(View.VISIBLE);
        btn_head_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getExtras();
        WithdrawFragment fragment = (WithdrawFragment) getSupportFragmentManager()
                .findFragmentById(R.id.carry_coin);
        fragment.setCarrycoinAddressText(bundle.getString("address"));
    }
}
