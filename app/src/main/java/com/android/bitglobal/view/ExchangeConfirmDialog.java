package com.android.bitglobal.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bitglobal.R;
import com.android.bitglobal.tool.StringUtils;
import com.android.bitglobal.ui.UIHelper;

public class ExchangeConfirmDialog {
    private Dialog mDialog;
    private Context mContext;
    private ExchangeConfirmCallback mListener;
    private TextView contentView;
    private LinearLayout transPasswdLy;
    private EditText passwdEt;
    private TextView confirmView;

    public ExchangeConfirmDialog(final Context context, ExchangeConfirmCallback callback) {
        mListener = callback;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_exchange_confirm, null);
        mContext = context;
        mDialog = new Dialog(context, R.style.Custom_Progress);
        mDialog.setContentView(view);
        mDialog.setCancelable(false);

        contentView = (TextView) view.findViewById(R.id.exchange_confirm_content);

        transPasswdLy = (LinearLayout) view.findViewById(R.id.exchange_confirm_trans_passwd_ly);
        passwdEt = (EditText) view.findViewById(R.id.exchange_confirm_safePwd);
        confirmView = (TextView) view.findViewById(R.id.exchange_confirm_confirm);

        view.findViewById(R.id.exchange_confirm_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });

        passwdEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    try {
                        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    //pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        confirmView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String passwd = passwdEt.getText().toString();
                        passwdEt.setText("");
                        if (StringUtils.isEmpty(passwd) && (transPasswdLy.getVisibility() == View.VISIBLE)) {
                            UIHelper.ToastMessage(context, context.getString(R.string.user_zjmm_hint_toast));
                        } else {
                            if (mListener != null) {
                                mListener.onConfirm(passwd);
                            }
                            dismiss();
                        }
                    }
                });

        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.dimAmount = 0.4f;
        mDialog.getWindow().setAttributes(lp);
    }

    public void show(String str) {
        contentView.setText(str);
        mDialog.show();
    }

    public void setTransPasswdInput(boolean value) {
        if (value) {
            transPasswdLy.setVisibility(View.VISIBLE);
        } else {
            transPasswdLy.setVisibility(View.GONE);
        }
    }
    public void dismiss() {
        ((InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(passwdEt.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        mDialog.dismiss();
    }

    public void setTransType(String type) {
        if ("0".equals(type)) {
            confirmView.setText(mContext.getString(R.string.trans_xhmc));
        } else {
            confirmView.setText(mContext.getString(R.string.trans_xhmr));

        }
    }

    public interface ExchangeConfirmCallback {
        void onConfirm(String passwd);
    }

}
