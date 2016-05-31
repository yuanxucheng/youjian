package com.example.yj.mapapp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {

	/**
	 * 判断字符串是否为空
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str))
			return true;
		return false;
	}

	/**
	 * 是否为数字
	 */
	public static boolean isNumber(String str) {
		try {
			Double.valueOf(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 是否为整数
	 */
	public static boolean isInteger(String str) {
		try {
			Integer.valueOf(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 是否为姓名
	 */
	public static boolean isName(String name) {
		Pattern pattern = Pattern.compile("[\u4e00-\u9fa5a-zA-Z·. 0-9]{1,20}");
		return pattern.matcher(name).matches();
	}

	/**
	 * 是否为密码（字母+数字）
	 */
	public static boolean isPassword(String name) {
		Pattern pattern = Pattern
				.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{1,}$");
		return pattern.matcher(name).matches();
	}

	/**
	 * 判断是否相同
	 */
	public static boolean isSame(String str) {
		String regex = str.substring(0, 1) + "{" + str.length() + "}";
		return str.matches(regex);
	}

	/**
	 * 是否为手机号码
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isMobile(String phone) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
		m = p.matcher(phone);
		b = m.matches();
		return b;

	}

	/**
	 * 是否为邮箱地址
	 */
	public static boolean isEmail(String email) {
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		return matcher(check, email);
	}

	/**
	 * 正则表达式匹配
	 * 
	 * @param check
	 * @param str
	 * @return
	 */
	public static boolean matcher(String check, String str) {
		boolean flag = false;
		try {
			Pattern p = Pattern.compile(check);
			Matcher matcher = p.matcher(str);
			flag = matcher.matches();
			return flag;
		} catch (Exception e) {
			return false;
		}
	}

}
