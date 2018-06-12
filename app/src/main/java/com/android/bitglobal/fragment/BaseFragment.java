package com.android.bitglobal.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.bitglobal.R;
import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.entity.UserInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class BaseFragment extends Fragment {

    private AlertDialog dialog;

    private Context mContext= AppContext.getInstance();
    DecimalFormat df0 = new DecimalFormat("0.########");
    DecimalFormat df1 = new DecimalFormat("0.00######");
    DecimalFormat df2 = new DecimalFormat("0.00");
    DecimalFormat df3 = new DecimalFormat("0.###");
    DecimalFormat df4 = new DecimalFormat("0.####");
    DecimalFormat df5 = new DecimalFormat("0.#####");
    DecimalFormat df6 = new DecimalFormat("0.######");

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        df0.setGroupingSize(0);
        //FLOOR 为趋向负无穷  DOWN为趋向0
        df0.setRoundingMode(RoundingMode.FLOOR);
        df1.setGroupingSize(0);
        df1.setRoundingMode(RoundingMode.FLOOR);
        df2.setGroupingSize(0);
        df2.setRoundingMode(RoundingMode.FLOOR);
        df3.setGroupingSize(0);
        df3.setRoundingMode(RoundingMode.FLOOR);
        df4.setGroupingSize(0);
        df4.setRoundingMode(RoundingMode.FLOOR);
        df5.setGroupingSize(0);
        df5.setRoundingMode(RoundingMode.FLOOR);
        df6.setGroupingSize(0);
        df6.setRoundingMode(RoundingMode.FLOOR);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    public boolean is_token(UserInfo userInfo){
        boolean is_token;
        if(userInfo==null||userInfo.getToken()==null||userInfo.getToken().equals("null")||userInfo.getToken().equals("")){
            is_token=false;
        }else{
            is_token=true;
        }
        return is_token;
    }
    protected String getText(EditText ed){
        String str="";
        str=ed.getText().toString().trim();
        return str;
    }
    protected String getText(TextView ed){
        String str="";
        str=ed.getText().toString().trim();
        return str;
    }
    public String deFormat(String str){
        try{
            str=df1.format(Double.parseDouble(str));
        }catch (Exception e){
            str="0.00";
        }
        return str;
    }
    public String deFormat(double dou){
        String str="";
        try{
            str=df1.format(dou);
        }catch (Exception e){
            str="0.00";
        }
        return str;
    }
    public String deFormat(String str,int type){
        try{

            if(type==2){
                str=df2.format(Double.parseDouble(str));
            }else if(type==3){
                str=df3.format(Double.parseDouble(str));
            }else if(type==0){
                str=df0.format(Double.parseDouble(str));
            }else if(type==4){
                str=df4.format(Double.parseDouble(str));
            }else if(type==5){
                str=df5.format(Double.parseDouble(str));
            }else if(type==6){
                str=df6.format(Double.parseDouble(str));
            }else{
                str=df1.format(Double.parseDouble(str));
            }

        }catch (Exception e){
            str="0.00";
        }
        return str;
    }
    public String deFormat(double dou,int type){
        String str="";
        try{
            if(type==2){
                str=df2.format(dou);
            }else if(type==3){
                str=df3.format(dou);
            }else if(type==0){
                str=df0.format(dou);
            }else if(type==4){
                str=df4.format(dou);
            }else if(type==5){
                str=df5.format(dou);
            }else if(type==6){
                str=df6.format(dou);
            }else {
                str=df1.format(dou);
            }
        }catch (Exception e){
            str="0.00";
        }
        return str;
    }

    public String format(String num, int type) {
        String str = "0.";
        for (int i = 0; i < type; i++) {
            str += "0";
        }
        DecimalFormat df = new DecimalFormat(str);
        df.setRoundingMode(RoundingMode.DOWN);
        try {
            str = df.format(Double.parseDouble(num));
        } catch (NumberFormatException e) {
            str = "--";
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            str = "0.00";
            e.printStackTrace();
        }
        return str;
    }

    public String format(double num, int type) {
        String str = "0.";
        for (int i = 0; i < type; i++) {
            str += "0";
        }
        DecimalFormat df = new DecimalFormat(str);
        df.setRoundingMode(RoundingMode.DOWN);
        try {
            str = df.format(num);
        } catch (NumberFormatException e) {
            str = "--";
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            str = "0.00";
            e.printStackTrace();
        }
        return str;
    }

    /**
     *格式化小数
     * @param num   数字
     * @param limit   位数
     * @param isMendZero  是否补零
     * @return
     */
    public static String format(String num, int limit,boolean isMendZero) {
        StringBuilder builder=new StringBuilder();
        String str = "0.";
        builder.append(str);
        if (isMendZero){
            for (int i = 0; i < limit; i++) {
                builder.append("0");
            }
        }else {
            for (int i = 0; i < limit; i++) {
                builder.append("#");
            }
        }

        DecimalFormat df = new DecimalFormat(builder.toString());
        df.setRoundingMode(RoundingMode.DOWN);
        try {
            str = df.format(Double.parseDouble(num));
        } catch (NumberFormatException e) {
            str = "--";
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            str = "0.00";
            e.printStackTrace();
        }
        return str;
    }


    protected void alert(View view, boolean cancelable) {
        dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.setView(view);
        dialog.setCancelable(cancelable);
        dialog.show();
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public String getVolText(String vol) {
        BigDecimal totalBtcNum = new BigDecimal(vol);
        if(totalBtcNum.doubleValue() > 0 && totalBtcNum.doubleValue() >= 10000 && totalBtcNum.doubleValue() < 10000000) {
            return format(totalBtcNum.divide(new BigDecimal(1000)).doubleValue(), 2) + getString(R.string.thousand);
        } else if(totalBtcNum.doubleValue() >    0 && totalBtcNum.doubleValue() >= 10000000) {
            return format(totalBtcNum.divide(new BigDecimal(1000000)).doubleValue(), 2) + getString(R.string.millions);
        } else {
            return format(totalBtcNum.doubleValue(), 2);
        }
    }

}

