package com.example.yj.mapapp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringUtil {

	public static String LOAD_FAIL="网络异常加载失败！";
	public static String LOAD_SUCCESS="网络加载成功！";

	/**
	 * 拆分字符串
	 * @param content
	 * @param split  间隔符
	 * @return
	 */
	public static String[] split(String content,String split){
		String [] temp = null;  
		   temp = content.split(split);  
		  
		   return temp;
	}
	public static String subStr(String content){
		return content.substring(0,content.length()-1); 
		
	}
	/**
	 * 删除数字前面的0
	 * @param str
	 * @return
	 */
	public static String delZero(String str) {
		String s = "";

		if (str.startsWith("0")) {
			s = str.substring(1, str.length());

			return delZero(str.substring(1, str.length()));
		} else {
			return str;
		}

	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}
	/**
	 * 拼接字符
	 * @param res原始字符串
	 * @param a  添加的字符串
	 * @return
	 */
	public static String append(String res, String a) {
		StringBuffer str = new StringBuffer(res);
		str.append(a);
		return str.toString();
	}
	/**
	 * 拼接字符
	 * @param res原始字符串
	 * @param a  添加的字符串
	 * @return
	 */
	public static String append(String res, char a) {
		StringBuffer str = new StringBuffer(res);
		str.append(a);
		return str.toString();
	}
	/**
	 * 拼接字符
	 * @param res原始字符串
	 * @param a  添加的字符串
	 * @return
	 */
	public static String append(String res, int a) {
		StringBuffer str = new StringBuffer(res);
		str.append(a);
		return str.toString();
	}
	/**
	 * 拼接字符
	 * @param res原始字符串
	 * @param a  添加的字符串
	 * @return
	 */
	public static String append(String res, float a) {
		StringBuffer str = new StringBuffer(res);
		str.append(a);
		return str.toString();
	}
	/**
	 * 拼接字符
	 * @param res原始字符串
	 * @param a  添加的字符串
	 * @return
	 */
	public static String append(String res, long a) {
		StringBuffer str = new StringBuffer(res);
		str.append(a);
		return str.toString();
	}
	
	
	
	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try {
			return Long.parseLong(obj);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 字符串转布尔值
	 * 
	 * @param b
	 * @return 转换异常返回 false
	 */
	public static boolean toBool(String b) {
		try {
			return Boolean.parseBoolean(b);
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 将一个InputStream流转换成字符串
	 * 
	 * @param is
	 * @return
	 */
	public static String toConvertString(InputStream is) {
		StringBuffer res = new StringBuffer();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader read = new BufferedReader(isr);
		try {
			String line;
			line = read.readLine();
			while (line != null) {
				res.append(line);
				line = read.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != isr) {
					isr.close();
					isr.close();
				}
				if (null != read) {
					read.close();
					read = null;
				}
				if (null != is) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
			}
		}
		return res.toString();
	}
	
	/**
	 * 大写首字母
	 */
	public static String firstToUp(String str) {
		if (str == null || "".equals(str))
			return str;
		return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
	}

	/**
	 * 小写首字母
	 */
	public static String firstToLower(String str) {
		if (str == null || "".equals(str))
			return str;
		return str.substring(0, 1).toLowerCase() + str.substring(1, str.length());
	}



	public static String transNullToEmpty(String str) {
		if (str == null)
			return "";
		return str;
	}
}
