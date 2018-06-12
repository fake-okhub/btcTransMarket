package com.android.bitglobal.tool;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.bitglobal.R;

/**
 * Created by joyson on 2017/8/7.
 */

public class DialogUtils {

    public interface OnDialogCallback {
        void onCancel();
    }

    /**
     * 展示订单取消 dialog
     * @param context
     */
    public static void showOrderCancelDialog(Context context,final OnDialogCallback mListener) {
        try {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            View root = inflater.inflate(R.layout.dialog_confirm, null);
            final AlertDialog dialog = new AlertDialog.Builder(context).create();
            dialog.setView(root);
            dialog.setCancelable(false);
            TextView description = (TextView) root.findViewById(R.id.dialog_confirm_content);
            String str = context.getString(R.string.dialog_cancel_order_content);
            description.setText(str);
            root.findViewById(R.id.dialog_confirm_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            TextView confirm = (TextView) root.findViewById(R.id.dialog_confirm_confirm);
            confirm.setText(context.getString(R.string.dialog_confirm));
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null){
                        mListener.onCancel();
                    }
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
