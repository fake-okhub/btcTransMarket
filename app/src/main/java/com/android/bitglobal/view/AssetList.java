package com.android.bitglobal.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

import com.android.bitglobal.R;

public class AssetList {
	private Dialog mDialog;
	private Context mContext;
	public ListView listview;
	public AssetList(Context context, int man_top) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.asset_select_list, null);
		mContext=context;
		mDialog = new Dialog(context, R.style.Custom_Progress);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.getWindow().getAttributes().gravity = Gravity.TOP|Gravity.CENTER_HORIZONTAL;
		listview = (ListView) view.findViewById(R.id.listview);
		WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
		lp.y = man_top; // 新位置Y坐标
		lp.dimAmount = 0.4f;
		lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
		mDialog.getWindow().setAttributes(lp);
	}
	public void show() {
		mDialog.show();
	}

	public void dismiss() {
		mDialog.dismiss();
	}

}
