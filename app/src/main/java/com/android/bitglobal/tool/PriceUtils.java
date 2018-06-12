package com.android.bitglobal.tool;

/**
 * Created by joyson on 2017/8/2.
 * 用于价格显示时，限制小数的显示样式
 * 规则：无需四舍五入  对外提供方法：parsePrice
 */

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import static java.lang.Double.parseDouble;

public class PriceUtils {

    /**
     * AnalysisFragment 界面 最高价、最低价 小数位数设置 5位
     */
    public static final int analysisHighOrLowPrice =6;
    /**
     * AnalysisFragment 界面 成交量 2位
     */
    public static final int analysisDealCountPrice =2;
    /**
     * AnalysisFragment 界面 浮动分析 2位
     */
    public static final int analysisChangeAnalyzePrice =2;
    /**
     * AnalysisFragment 界面 涨跌量（数据变化量） 2位
     */
    public static final int analysisRiseRatePrice =2;
    /**
     * ExchangeNewSubFragment 交易总览界面 资金合计：法币 小数点 保留2位
     */
    public static final int ExNewSubLegalDecimalsRemain =2;
    /**
     * ExchangeNewSubFragment(最顶部text) 交易总览界面 资金合计：btc 小数点 保留6位
     */
    public static final int ExNewSubBtcAllDecimalsRemain =6;
    /**
     * ExchangeNewSubFragment 交易总览界面 持有量 btc 小数点 保留5位
     */
    public static final int ExNewSubBtcHoldDecimalsRemain =5;


    /**
     * 位数超过 limit 限制位，就截取,且忽略四舍五入
     * @param price  价格
     * @param limitNum 限制位
     * @return
     */
    private static String cutPrice(String price, int limitNum) {
//		float   ft   =   134.3435f;
        int scale = limitNum;//设置位数
//		int roundingMode = 9;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等./
//		double num = parseDouble(price);
        BigDecimal bd = new BigDecimal(price);
        bd = bd.setScale(scale, RoundingMode.DOWN);
//		float ft = bd.floatValue();
        return String.valueOf(bd.toPlainString());
    }

    /**
     * 价格不足限制位，则不足小数位用0补全
     *
     * @param text  需要格式化的数字
     * @param scale 小数点后保留几位
     * @return
     */
    private static String roundByScale(String text, int scale) {
        double v = parseDouble(text);
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");
        }
        if (scale == 0) {
            return new DecimalFormat("0").format(v);
        }
        String formatStr = "0.";
        for (int i = 0; i < scale; i++) {
            formatStr = formatStr + "0";
        }
        return new DecimalFormat(formatStr).format(v);
    }

    /**
     * 法1 ：通过传统方法对外提供价格解析方法
     * @param price 价格
     * @param limit 限制位
     * @param isMendZero 是否补零
     * @return
     */
    public static String parsePrice(String price, int limit,boolean isMendZero) {
        try {
            if (TextUtils.isEmpty(price)) {//没有值
                return "0";
            } else {
                //说明为科学计数法，将其转为普通数字
                price=getDefNum(price);
                String[] arr = price.split("\\.");
                if (arr.length == 2) {//  0.5 中间有小数
                    if (isMendZero){
                        if (arr[1].length() >= limit) {
                            return cutPrice(price, limit);
                        } else {
                            return roundByScale(price, limit);
                        }
                    }else {
                        String clearZeroPrice = getNoZeroNum(price);
                        String[] arr2 = clearZeroPrice.split("\\.");
                        if (arr2.length == 2 && arr2[1].length() >= limit) {
                            return cutPrice(price, limit);
                        }else {
                            return clearZeroPrice;
                        }
                    }

                }else if (arr.length==1){//整数
                    if (isMendZero){//整数补零，添加 . 并在后面添加limit长度
                        StringBuffer sb = new StringBuffer(price);
                        sb.append(".");
                        for(int i=0;i<limit;i++){
                            sb.append("0");
                        }
                        return sb.toString();
                    }else {
                        return price;
                    }
                }
                else {
                    return price;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * 法2 DecimalFormat 格式化小数
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

    /**
     * 对小数末尾是0的进行截取
     * @param s
     * @return
     */
    private static String clearLastZero(String s) //自定义格式化输出函数
    {
//		String s=String.format("%f", d);//将浮点数转为字符串
        int i;
        for( i=s.length()-1;i>=0;i-- ) //从串尾向前检查，遇到非0数据结束循环
        {
            if ( s.charAt(i)=='.' ) //遇到小数点结束，说明是个整数
                break;
            if ( s.charAt(i) != '0' ) //遇到小数中有非0值，结束
            {
                i++;
                break;
            }
        }
        return s.substring(0,i); //返回处理后的子串
    }

    /**
     * 对小数末尾是0的进行截取 方式2
     * @param price
     * @return
     */
    private static String clearLastZero2(int limit,String price){
//		float   ft   =   134.3435f;
//		int scale = limitNum;//设置位数
//		int roundingMode = 9;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等./
        double num = parseDouble(price);
        BigDecimal bd = new BigDecimal(price).setScale(limit,RoundingMode.DOWN);
//		bd = bd.setScale(scale, RoundingMode.UNNECESSARY);
//        float ft = bd.floatValue();
        return String.valueOf(bd.toPlainString());
    }

    /**
     * 乘法 避免科学计数法
     * @param d1
     * @param d2
     * @return
     */
    //相乘
    public static String mul(String d1,String d2){
        BigDecimal b1=new BigDecimal(d1);
        BigDecimal b2=new BigDecimal(d2);
        return b1.multiply(b2).setScale(20,RoundingMode.DOWN).toPlainString();
    }

    /**
     * 返回小数包含默认为0的数字，若 小数点后都为0，则去掉 小数点
     * @param num
     * @return
     */
    public static String getNoZeroNum(String num){
        if(num.indexOf(".") > 0){
            //正则表达
            num = num.replaceAll("0+?$", "");//去掉后面无用的零
            num = num.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
            return num;
        }else{//整数
            return num;
        }
    }


    /**
     * 返回进位数，每3位加 ，
     * @param data
     * @param num 保留小数位数
     * @return
     */
    public static String carryNumber(String data,int num) {
        //避免科学计数法
        data=getDefNum(data);
        String format;
        if (Double.valueOf(data)==0){//若为0，则在其前面补0
            format="0.";
        }else {
            format="#,###.";
        }

        StringBuilder builder=new StringBuilder();
        builder.append(format);
        for (int i=0;i<num;i++){
            builder.append("0");
        }
//        DecimalFormat df = new DecimalFormat("#,###.000000");
        DecimalFormat df = new DecimalFormat(builder.toString());
        //避免四舍五入
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(Double.valueOf(data));
    }


    public static String getDefNum(String num){
        if (num.contains("E")){
            return new BigDecimal(num).toPlainString();
        }else {
            return num;
        }

    }

}
