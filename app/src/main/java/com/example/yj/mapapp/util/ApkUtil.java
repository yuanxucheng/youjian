package com.example.yj.mapapp.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ApkUtil {
	
	public static String getCurrentPackageName(Context context){
		return context.getPackageName();
	}

	public static void openApk(String path, Context context) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	public static int getInstalledApkVersion(Context context, String packageName) {
		PackageInfo info = getInstalledPackageInfo(context, packageName);
		if (info != null)
			return info.versionCode;
		return -1;
	}

	public static PackageInfo getInstalledPackageInfo(Context context, String packageName) {
		try {
			return context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static int getApkVersion(Context context, String apkPath) {
		PackageInfo info = getPackageInfo(context, apkPath);
		if (info != null) {
			return info.versionCode;
		}
		return -1;
	}

	public static PackageInfo getPackageInfo(Context context, String apkPath) {
		boolean result = moveApkToPad(context, apkPath);
		if (result) {
			String archiveFilePath = getApkPath() + apkPath;
			PackageManager pm = context.getPackageManager();
			PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
			return info;
		}
		return null;
	}

	public static boolean moveApkToPad(Context context, String apkPath) {

		boolean result = true;

		InputStream is = null;
		FileOutputStream out = null;

		try {
			is = context.getAssets().open(apkPath);
			File f = new File(getApkPath() + apkPath);
			out = new FileOutputStream(f);

			byte[] buffer = new byte[1024];
			while (is.read(buffer) > 0) {
				out.write(buffer);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}

	public static String getApkPath() {

		String path = Environment.getExternalStorageDirectory() + "/apk/";
		File f = new File(path);
		if (!f.exists())
			f.mkdirs();
		return path;
	}
}
