package com.android.bitglobal.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.bitglobal.activity.CountryActivity;
import com.android.bitglobal.R;
import com.android.bitglobal.entity.Country;

import java.util.List;

import cn.zhikaizhang.indexview.Util;

public class CountryAdapter extends BaseAdapter {
    private Context context;

    private List<Country> list;

    public CountryAdapter(Context context, List<Country> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final Country item = list.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_country_item, null);
            viewHolder.indexTextView = (TextView) convertView.findViewById(R.id.tv_index);
            viewHolder.userNameTextView = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.userName2TextView = (TextView) convertView.findViewById(R.id.tv_name2);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.userNameTextView.setText(item.getDes());
        viewHolder.userName2TextView.setText("("+item.getCode()+")");
      //  viewHolder.userName2TextView.setText("("+item.getLocalName()+")");
        char index = Util.getIndex(item.getDes());
        if(position == 0 || Util.getIndex(list.get(position - 1).getDes()) != index){
            viewHolder.indexTextView.setVisibility(View.VISIBLE);
            viewHolder.indexTextView.setText(String.valueOf(index));
        }else{
            viewHolder.indexTextView.setVisibility(View.GONE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("id", item.getId());
                bundle.putString("name", item.getDes());
                bundle.putString("code", item.getCode());
                intent.putExtras(bundle);
                ((CountryActivity)context).setResult(5278, intent);
                ((CountryActivity)context).finish();
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView indexTextView;
        TextView userNameTextView;
        TextView userName2TextView;
    }
}
