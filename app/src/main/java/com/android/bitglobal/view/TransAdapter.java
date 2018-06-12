package com.android.bitglobal.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.entity.TransMarketDepth;

import java.util.ArrayList;
import java.util.List;

public class TransAdapter extends BaseAdapter {
	private Context mcontext;
	private String type;
	private List<TransMarketDepth> contents = new ArrayList<TransMarketDepth>();
	public TransAdapter() {

	}
	public TransAdapter(Context mcontext, List<TransMarketDepth> contents) {
		this.mcontext=mcontext;
		this.contents=contents;
		type="1";
	}
	public TransAdapter(Context mcontext, List<TransMarketDepth> contents,String type) {
		this.mcontext=mcontext;
		this.contents=contents;
		this.type=type;
	}
	@Override
	public int getCount() {
		return contents.size();
	}
	@Override
	public Object getItem(int arg0) {
		return contents.get(arg0);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	public class ViewCache {
		private TextView tv_name;
	}
	ViewCache viewCache;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mcontext);
			if(type.equals("2")){
				convertView = inflater.inflate(R.layout.my_spinner2,null);
			}else{
				convertView = inflater.inflate(R.layout.my_spinner,null);
			}

			viewCache = new ViewCache();
			viewCache.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			convertView.setTag(viewCache);
		} else {
			viewCache = (ViewCache) convertView.getTag();
		}
		final  TransMarketDepth tmd=(TransMarketDepth)getItem(position);
		viewCache.tv_name.setText(tmd.getName());
		return convertView;
	}
}
