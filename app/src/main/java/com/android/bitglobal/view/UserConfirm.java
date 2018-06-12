package com.android.bitglobal.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.bitglobal.R;

public class UserConfirm {
	private Dialog mDialog;
	private Context mContext;
	public Button bnt_user_commit,bnt_user_cancel;
	public TextView tv_user_title1,tv_user_title2;
	public EditText ed_user_safePwd;
	private View rl_user_commit;
	public UserConfirm(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.user_confirm, null);
		mContext=context;
		mDialog = new Dialog(context, R.style.Custom_Progress);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setOnKeyListener(keylistener);

		ed_user_safePwd = (EditText) view.findViewById(R.id.ed_user_safePwd);

		bnt_user_commit = (Button) view.findViewById(R.id.bnt_user_commit);
		bnt_user_cancel = (Button) view.findViewById(R.id.bnt_user_cancel);

		tv_user_title1 = (TextView) view.findViewById(R.id.tv_user_title1);
		tv_user_title2 = (TextView) view.findViewById(R.id.tv_user_title2);

		bnt_user_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		/*rl_user_commit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});*/
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
	DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode== KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	} ;

}
