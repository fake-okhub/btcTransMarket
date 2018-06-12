package com.android.bitglobal.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.bitglobal.R;

public class DialogActivity {
	public Dialog mDialog;
	public ImageView img_close;
	public Button bnt_commit;
	public ImageView img_bg;
	public RelativeLayout rl_bg;
	public DialogActivity(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_activity, null);
		mDialog = new Dialog(context, R.style.Custom_Progress);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		img_close = (ImageView) view.findViewById(R.id.img_close);
		bnt_commit = (Button) view.findViewById(R.id.bnt_commit);
		img_bg = (ImageView) view.findViewById(R.id.img_bg);
		rl_bg = (RelativeLayout) view.findViewById(R.id.rl_bg);
		WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
		WindowManager m = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display d = m.getDefaultDisplay();
		lp.dimAmount = 0.4f;
		lp.width = (int) (d.getWidth()*0.8);
		lp.height = (int) (lp.width * (1.3));
		mDialog.getWindow().setAttributes(lp);
	}
	public void show() {
		if(mDialog!=null){
			try {
				mDialog.show();
			}catch (Exception e){
			}
		}
	}

	public void dismiss() {
		if(mDialog!=null){
			try {
				mDialog.dismiss();
			}catch (Exception e){

			}
		}
	}
}
