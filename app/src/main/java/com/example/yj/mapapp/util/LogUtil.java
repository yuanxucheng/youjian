package com.example.yj.mapapp.util;

import android.util.Log;

public class LogUtil {

	public static final String DEFAULT_TAG = "debug";

	public static boolean debug = true;

	public static void d(String msg) {
		d(DEFAULT_TAG, msg);
	}

	public static void d(String tag, String msg) {
		if (debug)
			Log.d(tag, msg);
	}

	public static void w(String msg) {
		if (debug)
			Log.w(DEFAULT_TAG, msg);
	}

	public static void w(String tag, String msg) {
		if (debug)
			Log.w(tag, msg);
	}

	public static void w(Throwable tr) {
		w(DEFAULT_TAG, tr);
	}

	public static void w(String tag, Throwable tr) {
		if (debug)
			Log.w(tag, tr);
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (debug)
			Log.w(tag, msg, tr);
	}

	public static void e(String msg) {
		if (debug)
			Log.e(DEFAULT_TAG, msg);
	}

	public static void e(Throwable tr) {
		e(DEFAULT_TAG, tr);
	}

	public static void e(String tag, Throwable tr) {
		if (debug)
			Log.e(tag, DEFAULT_TAG, tr);
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (debug)
			Log.e(tag, msg, tr);
	}
}
