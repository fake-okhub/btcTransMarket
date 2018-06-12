package com.android.bitglobal.view;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import com.android.bitglobal.R;
import com.android.bitglobal.ui.UIHelper;

import java.text.DecimalFormat;

/**
 * Created by bitbank on 16/10/18.
 */
public class keyboardPopWindow extends PopupWindow implements View.OnClickListener{

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


    private Button button_numkeyboard_point;// 符号点

    private View button_numkeyboard_clear;// 清空

    private ImageView button_numkeyboard_add;// 加

    private ImageView button_numkeyboardview_minus;//减
//    private LinearLayout mLinear;
    private Button buy;// 买入或卖出

    private View exit;// 清空

    private boolean isHave = false;

    private boolean isBuy = true;

    private Context mContext;
    private String mText="";
    private SeekBar mSeekBar;
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
    private  int mNum=4;

    private int mProgress = 0;

    private onkeyboardListener onkeyboardListener;
    private void setErrorMsg(String s)
    {
        this.msg = s;
    }
    public void setBuyOrSell(boolean mbuy)
    {
        isBuy = mbuy;
        if(isBuy)
        {
            buy.setText(mContext.getResources().getString(R.string.trans_jy));
            setErrorMsg(mContext.getResources().getString(R.string.no_money));
        }else
        {
            buy.setText(mContext.getResources().getString(R.string.trans_mc));
            setErrorMsg(mContext.getResources().getString(R.string.no_coin));
        }

    }
    public keyboardPopWindow(Context context)
    {
        super(context);
        mContext = context;
        init();
        this.setBackgroundDrawable(null);
    }

    public void setEditText(EditText mEditText)
    {
        mInPutEditText=mEditText;
    }

    public void show(View v)
    {
        this.showAtLocation(v, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        showDouble = 0.0;
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
        mNum = num;
        DecimalFormat mDecimalformat=null ;
        switch (mNum)
        {
            case 1:
                mDecimalformat= new DecimalFormat("#.#");
                break;
            case 2:
                mDecimalformat= new DecimalFormat("#.##");
                break;
            case 3:
                mDecimalformat= new DecimalFormat("#.###");
                break;
            case 4:
                mDecimalformat= new DecimalFormat("#.####");
                break;
        }
        try
        {
            step=Double.parseDouble(mDecimalformat.format(step));

        }catch (Exception e)
        {

        }

        this.mMax = m;
        mSeekBar.setMax(mMax);
        showDouble = 0.0;
        multiplier = (int)Math.pow(10,num);
    }

    public void setonBuyListener(onkeyboardListener onListener)
    {
        onkeyboardListener=onListener;
    }

    private void setTextShow()
    {
        DecimalFormat mDecimalformat=null ;
        switch (mNum)
        {
            case 1:
                mDecimalformat= new DecimalFormat("#.#");
                break;
            case 2:
                mDecimalformat= new DecimalFormat("#.##");
                break;
            case 3:
                mDecimalformat= new DecimalFormat("#.###");
                break;
            case 4:
                mDecimalformat= new DecimalFormat("#.####");
                break;
        }

        String s = mDecimalformat.format(showDouble);
        mInPutEditText.setText(s);
    }
    public void setViewVisible(boolean viewVisible)
    {
        this.isHave = viewVisible;
//        setVisible();
    }
//    private void setVisible()
//    {
//        if(isHave)
//        {
//            mLinear.setVisibility(View.VISIBLE);
//        }else
//        {
//            mLinear.setVisibility(View.GONE);
//        }
//    }

    private void setProgress(int pro)
    {
        mProgress = pro;
        mSeekBar.setProgress(mProgress);
    }
    private void init()
    {
        View  v= LayoutInflater.from(mContext).inflate(R.layout.keyboard_layout, null);
        setContentView(v);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
//        mLinear = (LinearLayout) v.findViewById(R.id.linear);
        mSeekBar = (SeekBar)v.findViewById(R.id.seekBar);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b)
                {
                    if(mInPutEditText==null)
                        return;
                    if(!isHave)
                        return;
                    showDouble = Double.valueOf(i)/Double.valueOf(multiplier);
                    setTextShow();
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                    if(mMax==0)
                    {
                        UIHelper.ToastMessage(mContext,msg);
                    }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
//        setVisible();
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


        buy = (Button) v
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
        button_numkeyboard_add.setOnClickListener(this);
        button_numkeyboardview_minus.setOnClickListener(this);
        exit.setOnClickListener(this);
        buy.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Editable etext;
        switch (view.getId())
        {
            case R.id.button_numkeyboardview_clear:
                if(mInPutEditText.getText().toString().length()<=0)
                    return;
                mText = mInPutEditText.getText().toString().substring(0,mInPutEditText.getText().toString().length()-1);
                mInPutEditText.setText(mText);
                etext = mInPutEditText.getText();
                Selection.setSelection(etext, etext.length());
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
//
//                    }
//
//        }
//                showDouble = showDouble+step;
//                setTextShow();
//                setSeekBarPro();
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
//                setSeekBarPro();
//                break;
            case R.id.exit:
                this.dismiss();
                break;
            case R.id.buy:
                if(onkeyboardListener!=null)
                    if(isBuy)
                    {
                        onkeyboardListener.buy();
                    }else
                    {
                        onkeyboardListener.sell();
                    }

                this.dismiss();
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

                setSeekBarPro();
        }

    }

    private void setSeekBarPro()
    {
        int progress = (int)(multiplier*showDouble);
        mSeekBar.setProgress(progress);

    }

    public interface onkeyboardListener{
        public void buy();
        public void sell();
    }
}
