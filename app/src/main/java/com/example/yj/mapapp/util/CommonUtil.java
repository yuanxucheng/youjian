package com.example.yj.mapapp.util;

import android.graphics.Paint;
import android.graphics.Rect;

import java.io.Closeable;

public class CommonUtil {
    
	/**
	 * 关闭流
	 * @param c
	 */
	public static void close(Closeable c){
    	try{
    		if(c != null){
    			c.close();
    		}
    	}catch(Exception e){   	
    		LogUtil.w(e);
    	}
    }
	
	/**
	 * 获取字符串占用宽高
	 */
	public static int[] getSize(String text,Paint paint){
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		return new int[]{bounds.width(),bounds.height()};
	}
	
}
