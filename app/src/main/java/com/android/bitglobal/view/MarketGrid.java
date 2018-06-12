package com.android.bitglobal.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ScrollView;

import com.android.bitglobal.R;

public class MarketGrid {
	private Dialog mDialog;
	private Context mContext;
	public ScrollView sv_market_grid;
	public GridView gridview;
	public MarketGrid(Context context, int man_top) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.market_grid, null);
		mContext=context;
		mDialog = new Dialog(context, R.style.Custom_Progress);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.getWindow().getAttributes().gravity = Gravity.TOP;
		sv_market_grid = (ScrollView) view.findViewById(R.id.sv_market_grid);
		gridview = (GridView) view.findViewById(R.id.gridview);
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
