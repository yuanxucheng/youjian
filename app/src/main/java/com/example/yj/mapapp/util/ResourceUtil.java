package com.example.yj.mapapp.util;

import com.example.yj.mapapp.base.MApplication;

import java.io.IOException;
import java.io.InputStream;

public class ResourceUtil {

	public static String getString(int id) {
		return MApplication.getApplication().getResources().getString(id);
	}

	public static InputStream getRaw(int id) {
		return MApplication.getApplication().getResources().openRawResource(id);
	}

	public static int getColor(int id) {
		return MApplication.getApplication().getResources().getColor(id);
	}

	public static int[] getIntegerArray(int id) {
		return MApplication.getApplication().getResources().getIntArray(id);
	}

	public static String[] getStringArray(int id) {
		return MApplication.getApplication().getResources().getStringArray(id);
	}

	public static float getDimensionPixelSize(int id){
		return MApplication.getApplication().getResources().getDimensionPixelSize(id);
	}
	
	public static InputStream getAssets(String path) {
		try {
			return MApplication.getApplication().getAssets().open(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
