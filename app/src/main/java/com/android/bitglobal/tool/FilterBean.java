package com.android.bitglobal.tool;

import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterBean {

	/**
	 * ����С��indexλ
	 * 
	 * @param number
	 * @param index
	 * @return
	 */
	public static double doubleRound(double number, int index) {
		double result = 0;
		double temp = Math.pow(10, index);
		result = Math.round(number * temp) / temp;
		return result;
	}

	/**
	 * ����С��indexλ
	 * 
	 * @param number
	 * @param index
	 * @return
	 */
	public static float floatRound(float number, int index) {
		float result = 0;
		float temp = (float) Math.pow(10, index);
		result = Math.round(number * temp) / temp;
		return result;
	}

	public static String nullOfString(String str) {
		if (str == null) {
			str = "";
		}
		return str;
	}

	public static byte stringToByte(String str) {
		byte b = 0;
		if (str != null) {
			try {
				b = Byte.parseByte(str);
			} catch (Exception e) {

			}
		}
		return b;
	}

	public static boolean stringToBoolean(String str) {
		if (str == null) {
			return false;
		} else {
			if (str.equals("1")) {
				return true;
			} else if (str.equals("0")) {
				return false;
			} else {
				try {
					return Boolean.parseBoolean(str);
				} catch (Exception e) {
					return false;
				}
			}
		}
	}

	public static int stringToInt(String str) {
		int i = 0;
		if (str != null) {
			try {
				i = Integer.parseInt(str.trim());
			} catch (Exception e) {
				i = 0;
			}

		} else {
			i = 0;
		}
		return i;
	}

	public static short stringToShort(String str) {
		short i = 0;
		if (str != null) {
			try {
				i = Short.parseShort(str.trim());
			} catch (Exception e) {
				i = 0;
			}
		} else {
			i = 0;
		}
		return i;
	}

	public static double stringToDouble(String str) {
		double i = 0;
		if (str != null) {
			try {
				i = Double.parseDouble(str.trim());
			} catch (Exception e) {
				i = 0;
			}
		} else {
			i = 0;
		}
		return i;
	}

	public static double stringToDouble(String str, double def) {
		double i = def;
		if (str != null) {
			try {
				i = Double.parseDouble(str.trim());
			} catch (Exception e) {
				i = def;
			}
		} else {
			i = def;
		}
		return i;
	}

	public static String intToString(int i) {
		String str = "";
		try {
			str = String.valueOf(i);
		} catch (Exception e) {
			str = "";
		}
		return str;
	}

	public static long doubleToLong(double d) {
		long lo = 0;
		try {
			// double�???��??long???�?�?滤�??double类�??�???��?��????��??
			lo = Long.parseLong(String.valueOf(d).substring(0, String.valueOf(d).lastIndexOf(".")));
		} catch (Exception e) {
			lo = 0;
		}
		return lo;
	}

	public static int doubleToInt(double d) {
		int i = 0;
		try {
			// double�???��??long???�?�?滤�??double类�??�???��?��????��??
			i = Integer.parseInt(String.valueOf(d).substring(0, String.valueOf(d).lastIndexOf(".")));
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}

	public static double longToDouble(long d) {
		double lo = 0;
		try {
			lo = Double.parseDouble(String.valueOf(d));
		} catch (Exception e) {
			lo = 0;
		}
		return lo;
	}

	public static int longToInt(long d) {
		int lo = 0;
		try {
			lo = Integer.parseInt(String.valueOf(d));
		} catch (Exception e) {
			lo = 0;
		}
		return lo;
	}

	public static long intToLong(Integer d) {
		long lo = 0;
		try {
			lo = Long.parseLong(String.valueOf(d));
		} catch (Exception e) {
			lo = 0;
		}
		return lo;
	}

	public static long stringToLong(String str) {
		if (null == str || "".equals(str)) {
			return 0L;
		}
		Long li = new Long(0);
		try {
			li = Long.valueOf(str);
		} catch (Exception e) {
			// li = new Long(0);
		}
		return li.longValue();
	}

	public static String longToString(long li) {
		String str = "";
		try {
			str = String.valueOf(li);
		} catch (Exception e) {

		}
		return str;
	}

	public static String longToString(Long li) {
		String str = "";
		try {
			if (li != null) {
				str = String.valueOf(li);
			}

		} catch (Exception e) {

		}
		return str;
	}

	// �????�???��?��??�????
	// number为�??�?????????��??digit为�??�???????�????
	public static double saveNumber(Double number, int digit) {
		if (number == null) {
			number = 0.00;
		}
		NumberFormat ddf1 = NumberFormat.getNumberInstance();
		ddf1.setMaximumFractionDigits(digit);
		String s = ddf1.format(number);
		double Num = Double.parseDouble(s);
		return Num;

	}

	// �????�???��?��??�????
	// number为�??�?????????��??digit为�??�???????�????
	public static double saveNumber(double number, int digit) {
		NumberFormat ddf1 = NumberFormat.getNumberInstance();
		ddf1.setMaximumFractionDigits(digit);
		String s = ddf1.format(number);
		double Num = Double.parseDouble(s);
		return Num;

	}

	/**
	 * ƥ���0������
	 * 
	 * @param s
	 * @return
	 */
	public static boolean checkUnsignIntNumber(String s) {
		return s.matches("^[1-9]\\d*$");
	}

	private static final String NUMBER_PATTERN = "^[0-9]+(.[0-9]{0,1})?$";// �ж�С�����һλ�����ֵ�������ʽ

	private static final String CNUMBER_PATTERN = "^[0-9]*$";// �ж����ֵ�������ʽ

	/**
	 * ��֤�ǲ�������(��֤��С�����һλ)
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isDecimalNumber(String number) {
		return match(NUMBER_PATTERN, number);
	}

	/**
	 * ��֤�ǲ�������(û��С����)
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isInteger(String number) {
		return match(CNUMBER_PATTERN, number);
	}

	/**
	 * ִ��������ʽ
	 * 
	 * @param pattern
	 *            ���ʽ
	 * @param str
	 *            ����֤�ַ���
	 * @return ���� <b>true </b>,����Ϊ <b>false </b>
	 */
	private static boolean match(String pattern, String str) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.find();
	}
}
