package com.example.yj.mapapp.util;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.example.yj.mapapp.base.MApplication;

public class CacheShareRefrenceUtil {

	public static final String TAG = "http_cache";

	public static boolean save(String key, String value) {
		return edit().putString(key, value).commit();
	}

	public static boolean save(String key, int value) {
		return edit().putInt(key, value).commit();
	}
	public static boolean save(String user,String key, String value) {
		return edit(user).putString(key, value).commit();
	}

    /**
     * 保存user的缓存数据
     * @param user
     * @param key
     * @param value
     * @return
     */
	public static boolean save(String user,String key, int value) {
		return edit(user).putInt(key, value).commit();
	}
	public static boolean remove(String key) {
		return edit().remove(key).commit();
	}

    /**
     * 移除user的缓存数据
     * @param user
     * @param key
     * @return
     */
	public static boolean remove(String user,String key) {
		return edit(user).remove(key).commit();
	}

	private static Editor edit() {
		return MApplication.getApplication().getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
	}
	private static Editor edit(String cache_user) {
		return MApplication.getApplication().getSharedPreferences(cache_user, Context.MODE_PRIVATE).edit();
	}

	public static Object get(String key) {
		return MApplication.getApplication().getSharedPreferences(TAG, Context.MODE_PRIVATE).getAll().get(key);
	}


    public static String getString(String key, String faillValue){
//        return mSharedPreferences.getString(key, faillValue);
        return MApplication.getApplication().getSharedPreferences(TAG, Context.MODE_PRIVATE).getString(key, faillValue);
    }
    public static String getString(String user,String key, String faillValue){
//        return mSharedPreferences.getString(key, faillValue);
        return MApplication.getApplication().getSharedPreferences(user, Context.MODE_PRIVATE).getString(key,faillValue);
    }

//	public static final String REGIST_CODE = "regist_code";//注册获取的验证码
//	public static final String RESET_CODE = "reset_code";//重置密码获取的验证码
//	public static final String USER_INFO = "user";//登陆用户的信息
}
