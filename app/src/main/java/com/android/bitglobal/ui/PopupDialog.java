package com.android.bitglobal.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.android.bitglobal.R;
import com.android.bitglobal.entity.TransCurrency;
import com.android.bitglobal.tool.L;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.List;

/**
 * Popup对话框统一适配帮助类
 * Created by elbert on 2017/3/8.
 */

public class PopupDialog {

    private Dialog mDialog;
    private Context mContext;

    public PopupDialog(Activity context) {
        mContext = context;
        mDialog = new Dialog(context, R.style.Custom_Progress);
        mDialog.setCancelable(true);

    }

    public void createAssetSelectList(List<TransCurrency> list, final PopupDialogCallback listener) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.asset_select_list, null);
        mDialog.setContentView(rootView);
        ListView listView = (ListView) rootView.findViewById(R.id.listview);
        QuickAdapter adapter = new QuickAdapter<TransCurrency>(mContext, R.layout.asset_select_list_item, list) {
            @Override
            protected void convert(BaseAdapterHelper helper, final TransCurrency item) {
                try {
                    helper.setText(R.id.tv_asset_name,item.getCurrencyType().toUpperCase()+"/"+item.getExchangeType().toUpperCase());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    L.e(item.toString());
                }
                final  View view=helper.getView();
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.dataBack(item);
                    }
                });
            }
        };
        listView.setAdapter(adapter);
    }
    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public interface PopupDialogCallback<T> {
        void dataBack(T item);
    }
}
