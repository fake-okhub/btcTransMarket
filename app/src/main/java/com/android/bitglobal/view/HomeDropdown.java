package com.android.bitglobal.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.R;
import com.android.bitglobal.activity.MainActivity;
import com.android.bitglobal.dao.UserDao;
import com.android.bitglobal.entity.UserInfo;
import com.android.bitglobal.ui.UIHelper;

public class HomeDropdown {
	private Dialog mDialog;
	private Context mContext;
	private TextView tv_hqsz;
	private TextView tv_jgyj;
	private TextView tv_rzrb;
	private TextView tv_sys;
	private TextView tv_yhgl;

	public HomeDropdown(Context context, int man_top) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.home_title_select, null);
		mContext=context;
		mDialog = new Dialog(context, R.style.Custom_Progress);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.getWindow().getAttributes().gravity = Gravity.TOP;

		tv_hqsz = (TextView) view.findViewById(R.id.tv_hqsz);
		tv_jgyj = (TextView) view.findViewById(R.id.tv_jgyj);
		tv_rzrb = (TextView) view.findViewById(R.id.tv_rzrb);
		tv_sys = (TextView) view.findViewById(R.id.tv_sys);
		tv_yhgl = (TextView) view.findViewById(R.id.tv_yhgl);

		tv_hqsz.setOnClickListener(onclick);
		tv_jgyj.setOnClickListener(onclick);
		tv_rzrb.setOnClickListener(onclick);
		tv_sys.setOnClickListener(onclick);
		tv_yhgl.setOnClickListener(onclick);

		WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
		lp.y = man_top; // 新位置Y坐标
		lp.dimAmount = 0.2f;
		lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
		mDialog.getWindow().setAttributes(lp);
	}
	View.OnClickListener onclick = new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			UserInfo userInfo= UserDao.getInstance().getIfon();
			switch (v.getId()){
				case R.id.tv_hqsz:
					UIHelper.showMarketSet(MainActivity.getInstance());
					break;
				case R.id.tv_jgyj:
					if(is_token(userInfo)){
//						UIHelper.showSetPrice(MainActivity.getInstance(), market_btc.getShowMarketAndCurrencyList());
					}else{
						//UIHelper.showLoginOrRegister(MainActivity.getInstance());
						UIHelper.showLogin(MainActivity.getInstance());
					}
					break;
				case R.id.tv_yhgl:
					UIHelper.showUserManage(MainActivity.getInstance());
					break;
				case R.id.tv_rzrb:
					if(is_token(userInfo)){
						UIHelper.showLendActivity(MainActivity.getInstance());
					}else{
					//	UIHelper.showLoginOrRegister(MainActivity.getInstance());
						UIHelper.showLogin(MainActivity.getInstance());
					}
					break;
				case R.id.tv_sys:
					AppContext.customScan(MainActivity.getInstance());
					break;
			}
			mDialog.dismiss();
		}
	};

	public boolean is_token(UserInfo userInfo){
		boolean is_token;
		if(userInfo==null||userInfo.getToken()==null||userInfo.getToken().equals("null")||userInfo.getToken().equals("")){
			is_token=false;
		}else{
			is_token=true;
		}
		return is_token;
	}
	public void show() {
		mDialog.show();
	}

	public void dismiss() {
		mDialog.dismiss();
	}



}
