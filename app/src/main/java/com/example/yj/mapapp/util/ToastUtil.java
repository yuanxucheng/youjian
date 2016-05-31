package com.example.yj.mapapp.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	public static void longT(Context context,String str){
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}
	public static void shortT(Context context,String str){
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}
	public static void T(Context context,String str,int time){
		Toast.makeText(context, str, time).show();
	}
}
