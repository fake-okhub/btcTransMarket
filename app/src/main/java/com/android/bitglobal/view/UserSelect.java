package com.android.bitglobal.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.bitglobal.R;

public class UserSelect {
	private Dialog mDialog;
	private Context mContext;
	public Button bnt_select_cancel,bnt_select_subject_1,bnt_select_subject_2,bnt_select_subject_3,bnt_select_subject_4;
	public TextView tv_select_title1,tv_select_title2;
	public LinearLayout ll_select_subject_2,ll_select_subject_3,ll_select_subject_4;
	private View ll_select;
	public UserSelect(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.user_select, null);
		mContext=context;
		mDialog = new Dialog(context, R.style.Custom_Progress);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(true);
		ll_select=view.findViewById(R.id.ll_select);
		bnt_select_cancel = (Button) view.findViewById(R.id.bnt_select_cancel);
		ll_select_subject_2 = (LinearLayout) view.findViewById(R.id.ll_select_subject_2);
		ll_select_subject_3 = (LinearLayout) view.findViewById(R.id.ll_select_subject_3);
		ll_select_subject_4 = (LinearLayout) view.findViewById(R.id.ll_select_subject_4);

		bnt_select_subject_1 = (Button) view.findViewById(R.id.bnt_select_subject_1);
		bnt_select_subject_2 = (Button) view.findViewById(R.id.bnt_select_subject_2);
		bnt_select_subject_3 = (Button) view.findViewById(R.id.bnt_select_subject_3);
		bnt_select_subject_4 = (Button) view.findViewById(R.id.bnt_select_subject_4);

		tv_select_title1 = (TextView) view.findViewById(R.id.tv_select_title1);
		tv_select_title2 = (TextView) view.findViewById(R.id.tv_select_title2);
		bnt_select_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		ll_select.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
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
