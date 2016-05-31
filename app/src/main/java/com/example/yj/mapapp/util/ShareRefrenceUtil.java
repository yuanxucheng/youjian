package com.example.yj.mapapp.util;


import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.example.yj.mapapp.base.MApplication;

public class ShareRefrenceUtil {

	public static final String TAG = "one_key_call";

	public static boolean save(String key, String value) {
		return edit().putString(key, value).commit();
	}

	public static boolean save(String key, int value) {
		return edit().putInt(key, value).commit();
	}

	public static boolean save(String key, boolean value) {
		return edit().putBoolean(key, value).commit();
	}

	public static boolean save(String key, float value) {
		return edit().putFloat(key, value).commit();
	}

	public static boolean remove(String key) {
		return edit().remove(key).commit();
	}

	private static Editor edit() {
		return MApplication.getApplication().getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
	}

	public static Object get(String key) {
		return MApplication.getApplication().getSharedPreferences(TAG, Context.MODE_PRIVATE).getAll().get(key);
	}
	public static boolean getBoolean(String key) {
		return (Boolean) MApplication.getApplication().getSharedPreferences(TAG, Context.MODE_PRIVATE).getBoolean(key, true);
	}

    public static String getString(String key, String faillValue){
//        return mSharedPreferences.getString(key, faillValue);
        return MApplication.getApplication().getSharedPreferences(TAG, Context.MODE_PRIVATE).getString(key,faillValue);
    }

//	public static final String REGIST_CODE = "regist_code";//注册获取的验证码
//	public static final String RESET_CODE = "reset_code";//重置密码获取的验证码
	public static final String USER_INFO = "user";//登陆用户的信息
}
