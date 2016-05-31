package com.example.yj.mapapp.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class Screen {

	static final Screen SCREEN = new Screen();

	public int widthPixels;// 屏幕宽
	public int heightPixels;// 屏幕高
	public int barHeight;// 状态栏高度
	public float density;
	public float scaledDensity;
	public int densityDpi;
	public float xdpi;
	public float ydpi;
	
	private  Context contexts;

	private Screen() {

	}

	public static void initScreen(Context context) {
		DisplayMetrics display = context.getResources().getDisplayMetrics();
		SCREEN.widthPixels = display.widthPixels;
		SCREEN.heightPixels = display.heightPixels;
		SCREEN.density = display.density;
		SCREEN.scaledDensity = display.scaledDensity;
		SCREEN.densityDpi = display.densityDpi;
		SCREEN.xdpi = display.xdpi;
		SCREEN.ydpi = display.ydpi;
		SCREEN.contexts=context;
	}

	public static void setBarHeight(int barHeight) {
		getInstance().barHeight = barHeight;
	}

	public static Screen getInstance() {
		return SCREEN;
	}

	@Override
	public String toString() {
		return "Screen [widthPixels=" + widthPixels + ", heightPixels="
				+ heightPixels + ", barHeight=" + barHeight + ", density="
				+ density + ", scaledDensity=" + scaledDensity
				+ ", densityDpi=" + densityDpi + ", xdpi=" + xdpi + ", ydpi="
				+ ydpi + "]";
	}
	
	/**
	 * 分辨率适配，等比缩放
	 * 
	 * "540*960\720*1280\1080*1920\(720*1280) 1.5->2
	 */
	public static void setScale() {
		int width = 0;
		int height = 0;
		width = Screen.getInstance().widthPixels;
		height = Screen.getInstance().heightPixels;
//		float scaleX = width / 480f;
//		float scaleY = height / 770f;
//		float scale = scaleX < scaleY ? scaleX : scaleY;
//		String widthAndHeight = width + "/" + height + "/"
//				+ Screen.getInstance().density;
		if (width == 1080) {
			Screen.getInstance().contexts.getResources().getDisplayMetrics().density = 3;
		} else if (width == 720) {
			SCREEN.getInstance().contexts.getResources().getDisplayMetrics().density = 2;
		} else if (width == 800) {
			SCREEN.getInstance().contexts.getResources().getDisplayMetrics().density = 2;
		} else if (width == 1536) {
			SCREEN.getInstance().contexts.getResources().getDisplayMetrics().density = 4F;
		} else if (width == 1600) {
			SCREEN.getInstance().contexts.getResources().getDisplayMetrics().density = 4F;
			SCREEN.getInstance().contexts.getResources().getDisplayMetrics().scaledDensity = 4F; // 字体缩放比例
		} else if (width == 480) {
			SCREEN.getInstance().contexts.getResources().getDisplayMetrics().density = 1.5F;
			SCREEN.getInstance().contexts.getResources().getDisplayMetrics().scaledDensity = 1.7F;
		} else {
		}
	}
	
}
