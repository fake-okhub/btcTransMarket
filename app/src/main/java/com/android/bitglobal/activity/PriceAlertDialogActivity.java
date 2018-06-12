package com.android.bitglobal.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.jpush.MyReceiver;

/**
 * Created by Administrator on 2017/5/4.
 */

public class PriceAlertDialogActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_message);

        TextView titleText = (TextView) findViewById(R.id.title_text);
        TextView closeText = (TextView) findViewById(R.id.close_text);

        titleText.setText(getIntent().getStringExtra(MyReceiver.PUSH_PRICE_ALERT_MESSAGE));
        closeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

}
