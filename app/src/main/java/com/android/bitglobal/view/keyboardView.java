package com.android.bitglobal.view;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.Selection;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.android.bitglobal.R;

import java.text.DecimalFormat;

/**
 * Created by bitbank on 16/10/25.
 */
public class keyboardView extends LinearLayout implements View.OnClickListener
{
    private Button button_numkeyboard_one;// 数字一
    private Button button_numkeyboard_two;// 数字二
    private Button button_numkeyboard_three;// 数字三
    private Button button_numkeyboard_four;// 数字四
    private Button button_numkeyboard_five;// 数字五
    private Button button_numkeyboard_six;// 数字六
    private Button button_numkeyboard_seven;// 数字七
    private Button button_numkeyboard_eight;// 数字八
    private Button button_numkeyboard_nine;// 数字九
    private Button button_numkeyboard_zero;// 数字零
//    private Button button_numkeyboardview_sub;// 加
//    private Button button_numkeyboardview_plus;// 减
    private Button button_numkeyboard_point;// 符号点
    public Button button_numkeyboard_all_clean;// 清空全部
    private View button_numkeyboard_clear;// 清空
//    private ImageView button_numkeyboard_add;// 加
//    private ImageView button_numkeyboardview_minus;//减
//    private LinearLayout mLinear;
    public RelativeLayout buy;// 买入或卖出
    private View exit;// 清空
    private boolean isHave = false;
    private boolean isBuy = true;
    private Context mContext;
    private String mText="";
//    private SeekBar mSeekBar;
    private EditText mInPutEditText;
    //最大值
    private int mMax=100;
    //乘方 扩大的倍数
    private int multiplier=4;
    //加减的最小值
    private double step=0.1;
    //文本显示的数字
    private double showDouble = 0.0;
    private String msg="";
    //保留几位小数
    private  int mNum=6;
    private int mProgress = 0;
    private String value;

    public keyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init()
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.keyboard_layout, this);

//        mLinear = (LinearLayout) v.findViewById(R.id.linear);
//        mSeekBar = (SeekBar)v.findViewById(R.id.seekBar);

//        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                if(b)
//                {
//                    if(mInPutEditText==null)
//                        return;
//                    if(!isHave)
//                        return;
//                    showDouble = Double.valueOf(i)/Double.valueOf(multiplier);
//                    setTextShow();
//                }
//
//            }
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
        setVisible();
        button_numkeyboard_one = (Button) v
                .findViewById(R.id.button_numkeyboardview_one);
        button_numkeyboard_two = (Button) v
                .findViewById(R.id.button_numkeyboardview_two);
        button_numkeyboard_three = (Button) v
                .findViewById(R.id.button_numkeyboardview_three);
        button_numkeyboard_four = (Button) v
                .findViewById(R.id.button_numkeyboardview_four);
        button_numkeyboard_five = (Button) v
                .findViewById(R.id.button_numkeyboardview_five);
        button_numkeyboard_six = (Button) v
                .findViewById(R.id.button_numkeyboardview_six);
        button_numkeyboard_seven = (Button) v
                .findViewById(R.id.button_numkeyboardview_seven);
        button_numkeyboard_eight = (Button) v
                .findViewById(R.id.button_numkeyboardview_eight);
        button_numkeyboard_nine = (Button) v
                .findViewById(R.id.button_numkeyboardview_nine);
        button_numkeyboard_zero = (Button) v
                .findViewById(R.id.button_numkeyboardview_zero);
        button_numkeyboard_point = (Button) v
                .findViewById(R.id.button_numkeyboardview_point);
        button_numkeyboard_clear =  v
                .findViewById(R.id.button_numkeyboardview_clear);
//        button_numkeyboard_add = (ImageView) v
//                .findViewById(R.id.button_numkeyboardview_add);
//        button_numkeyboardview_minus = (ImageView) v
//                .findViewById(R.id.button_numkeyboardview_minus);
//        button_numkeyboardview_sub = (Button) v
//                .findViewById(R.id.button_numkeyboardview_sub);
//        button_numkeyboardview_plus = (Button) v
//                .findViewById(R.id.button_numkeyboardview_plus);
        button_numkeyboard_all_clean = (Button) v
                .findViewById(R.id.button_numkeyboardview_clean_all);


        buy = (RelativeLayout) v
                .findViewById(R.id.buy);
        exit = v
                .findViewById(R.id.exit);

//        button_numkeyboard_add = (ImageView) v
//                .findViewById(R.id.button_numkeyboardview_add);
//        button_numkeyboardview_minus = (ImageView) v
//                .findViewById(R.id.button_numkeyboardview_minus);

        button_numkeyboard_one.setOnClickListener(this);
        button_numkeyboard_two.setOnClickListener(this);
        button_numkeyboard_three.setOnClickListener(this);
        button_numkeyboard_four.setOnClickListener(this);
        button_numkeyboard_five.setOnClickListener(this);
        button_numkeyboard_six.setOnClickListener(this);
        button_numkeyboard_seven.setOnClickListener(this);
        button_numkeyboard_eight.setOnClickListener(this);
        button_numkeyboard_nine.setOnClickListener(this);
        button_numkeyboard_zero.setOnClickListener(this);
        button_numkeyboard_point.setOnClickListener(this);
        button_numkeyboard_clear.setOnClickListener(this);
//        button_numkeyboard_add.setOnClickListener(this);
        button_numkeyboard_all_clean.setOnClickListener(this);
//        button_numkeyboardview_minus.setOnClickListener(this);
        exit.setOnClickListener(this);
        buy.setOnClickListener(this);
//        button_numkeyboardview_sub.setOnClickListener(this);
//        button_numkeyboardview_plus.setOnClickListener(this);

    }

    public void setBuyOrSell(boolean mbuy)
    {
//        Resources res = mContext.getResources();
//        isBuy = mbuy;
//        if(isBuy)
//        {
//            buy.setVisibility(VISIBLE);
//
//        }else
//        {
//            buy.setVisibility(GONE);
//        }

    }

    public void setEditText(EditText mEditText, int precision)
    {
        mInPutEditText=mEditText;
        mNum = precision;
    }

    public void setEditText(EditText mEditText, int precision, String value)
    {
        mInPutEditText=mEditText;
        mNum = precision;
        this.value = value;
    }

    public void processingData(double d)
    {
        String s=d+"";
        int index = s.indexOf(".");
        int num = 0;
        int max =100;
        if(index>0)
        {
            s=s.substring(index+1);
            num=s.length();
            max=(int)(Math.pow(10,num)*d);
        }
        setMax(max,num);
        setProgress(0);
    }
    public void setMax(int m,int num)
    {
        step = Math.pow(0.1,num);
//        mNum = num;
        DecimalFormat mDecimalformat=null ;
        String str = "0.";
        for (int i = 0; i < mNum; i++) {
            str += "0";
        }
        mDecimalformat = new DecimalFormat(str);
        try {
            step = Double.parseDouble(mDecimalformat.format(step));
        } catch (IllegalArgumentException e) {
            step = 0.00f;
            e.printStackTrace();
        }

        this.mMax = m;
//        mSeekBar.setMax(mMax);
        showDouble = 0.0;
        multiplier = (int)Math.pow(10,num);
    }

    private void setTextShow()
    {
        String s = formatNum();

        mInPutEditText.setText(s);
        Editable  etext = mInPutEditText.getText();
        Selection.setSelection(etext, etext.length());
    }

    private String formatNum() {
        DecimalFormat mDecimalformat=null ;
        String str = "0.";
        for (int i = 0; i < mNum; i++) {
            str += "0";
        }
        mDecimalformat = new DecimalFormat(str);
        String s;
        try {
            s = mDecimalformat.format(showDouble);
        } catch (IllegalArgumentException e) {
            s = "0.00";
            e.printStackTrace();
        }
        return s;
    }

    private double getStep() {
        String str = "0.";
        for (int i = 0; i < mNum - 1; i++) {
            str += "0";
        }
        str += "1";
        try {
            return Double.valueOf(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0f;
        }
    }

    public void setViewVisible(boolean viewVisible)
    {
        this.isHave = viewVisible;
        setVisible();
    }
    private void setVisible()
    {
        if(isHave)
        {
//            mLinear.setVisibility(View.VISIBLE);
            try {
                if(mInPutEditText.getText().toString().equals(""))
                {
                    showDouble = 0;
                }else
                {
                    showDouble=Double.parseDouble(mInPutEditText.getText().toString());
                }
            }catch (Exception e)
            {
                showDouble = 0;
            }
//            setSeekBarPro();

        }else
        {
//            mLinear.setVisibility(View.GONE);
        }
    }

    private void setProgress(int pro)
    {
        mProgress = pro;
//        mSeekBar.setProgress(mProgress);
    }

    @Override
    public void onClick(View view) {
        Editable etext;
        switch (view.getId())
        {
            case R.id.button_numkeyboardview_clean_all:
//                if(mInPutEditText.getId() == R.id.ed_trans_jg) {
//                    mInPutEditText.setText(value);
//                    mInPutEditText.setSelection(mInPutEditText.getText().length());
////                    setVisibility(GONE);
//                } else {
                    mInPutEditText.setText("");
//                }
                break;
            case R.id.buy:
                String price = mInPutEditText.getText().toString();
                if("".equals(price) || Double.parseDouble(price) == 0) {
                    mInPutEditText.setText(value);
                    mInPutEditText.setSelection(mInPutEditText.getText().length());
                }
                setVisibility(GONE);
                break;
//            case R.id.button_numkeyboardview_plus:
//                if (mInPutEditText.getText().toString().equals("")) {
//                    showDouble = 0.0f;
//                } else {
//                    try {
//                        showDouble = Double.parseDouble(mInPutEditText.getText().toString());
//
//                    } catch (Exception e) {
//                        showDouble = 0.0f;
//                    }
//
//                }
//                showDouble = showDouble + getStep();
//                mInPutEditText.setText(formatNum());
//                mInPutEditText.setSelection(mInPutEditText.getText().length());
//                break;
//            case R.id.button_numkeyboardview_sub:
//                if (mInPutEditText.getText().toString().equals("")) {
//                    showDouble = 0.0f;
//                } else {
//                    try {
//                        showDouble = Double.parseDouble(mInPutEditText.getText().toString());
//
//                    } catch (Exception e) {
//                        showDouble = 0.0f;
//                    }
//
//                }
//
//                if (Double.compare(showDouble, 0) > 0) {
//                    showDouble = showDouble - getStep();
//                }
//                mInPutEditText.setText(formatNum());
//                mInPutEditText.setSelection(mInPutEditText.getText().length());
//                break;
            case R.id.button_numkeyboardview_clear:
                if(mInPutEditText.getText().toString().length()<=0)
                    return;
                mText = mInPutEditText.getText().toString().substring(0,mInPutEditText.getText().toString().length()-1);
                mInPutEditText.setText(mText);
                etext = mInPutEditText.getText();
                Selection.setSelection(etext, etext.length());

                if(mInPutEditText.getText().toString().equals(""))
                {
                    showDouble=0.0;
                }else
                {
                    try
                    {
                        mText=mInPutEditText.getText().toString();
                        if(mText.indexOf(".")==mText.length())
                        {
                            showDouble=Double.parseDouble(mInPutEditText.getText().toString().substring(0,mInPutEditText.getText().toString().length()-1));
                        }else{
                            showDouble=Double.parseDouble(mInPutEditText.getText().toString());
                        }
                    }catch (Exception e)
                    {
                        showDouble=0.0;
                    }
                }
//                setSeekBarPro();
                break;
//            case R.id.button_numkeyboardview_add:
//                if(mInPutEditText.getText().toString().equals(""))
//                {
//                    showDouble=0.0;
//                }else
//                {
//                    try
//                    {
//                        showDouble=Double.parseDouble(mInPutEditText.getText().toString());
//
//                    }catch (Exception e)
//                    {
//                        showDouble=0.0;
//                    }
//
//                }
//                showDouble = showDouble+step;
//                setTextShow();
////                setSeekBarPro();
//                break;
//            case R.id.button_numkeyboardview_minus:
//                if(mInPutEditText.getText().toString().equals(""))
//                {
//                    showDouble=0.0;
//                }else
//                {
//                    try
//                    {
//                        showDouble=Double.parseDouble(mInPutEditText.getText().toString());
//                    }catch (Exception e)
//                    {
//
//                    }
//
//                }
//                if(showDouble<=0)
//                    return;
//                showDouble = showDouble-step;
//                setTextShow();
////                setSeekBarPro();
//                break;
            case R.id.exit:
//                this.setVisibility(View.GONE);
                break;
            default:
                Button v = (Button)view;
                etext = mInPutEditText.getText();
                if(v.getId()==R.id.button_numkeyboardview_point)
                {
                    if(etext.toString().indexOf(".")>0)
                        return;
                }
                etext.append(v.getText());
                try
                {
                    showDouble = Double.parseDouble(mInPutEditText.getText().toString());
                }catch (Exception e)
                {

                }

//                setSeekBarPro();
        }

    }

//    private void setSeekBarPro()
//    {
//        int progress = (int)(multiplier*showDouble);
//        mSeekBar.setProgress(progress);
//    }
}
