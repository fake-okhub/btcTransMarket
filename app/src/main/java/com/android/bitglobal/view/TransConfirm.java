package com.android.bitglobal.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.bitglobal.R;

public class TransConfirm {
	private Dialog mDialog;
	public Button btn_qr;
	public Button bnt_qx;
	public TextView tv_title_t;
	public TextView tv_title_t2;
	public TransConfirm(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.trans_confirm, null);
		mDialog = new Dialog(context, R.style.Custom_Progress);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		bnt_qx = (Button) view.findViewById(R.id.bnt_qx);
		btn_qr = (Button) view.findViewById(R.id.btn_qr);
		tv_title_t = (TextView) view.findViewById(R.id.tv_title_t);
		tv_title_t2 = (TextView) view.findViewById(R.id.tv_title_t2);
		WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
		lp.dimAmount = 0.4f;
		mDialog.getWindow().setAttributes(lp);
	}
	public void show() {
		mDialog.show();
	}

	public void dismiss() {
		mDialog.dismiss();
	}
}
