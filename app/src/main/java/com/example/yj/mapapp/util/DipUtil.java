package com.example.yj.mapapp.util;

import android.content.Context;


public class DipUtil {

	/**
	 * dip 转  pixels
	 */
	public static int dipToPixels(float dip) {
		return (int) (dip * Screen.getInstance().density + 0.5f);
	}

	/**
	 * pixels 转  dip
	 */
	public static int pixelsToDip(float pixels) {
		return (int) (pixels / Screen.getInstance().density + 0.5f);
	}

	/**
	 * sp 转  pixels
	 */
	public static int spToPixels(float sp) {
		return (int) (sp * Screen.getInstance().scaledDensity + 0.5f);
	}

	/**
	 * pixels 转  sp
	 */
	public static int pixelsToSp(float pixels) {
		return (int) (pixels / Screen.getInstance().scaledDensity + 0.5f);
	}

	
	
	/** 
     * 将px值转换为dip或dp值，保证尺寸大小不变 
     *  
     * @param pxValue 
     * @param scale 
     *            （DisplayMetrics类中属性density） 
     * @return 
     */ 
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
   
    /** 
     * 将dip或dp值转换为px值，保证尺寸大小不变 
     *  
     * @param dipValue 
     * @param scale 
     *            （DisplayMetrics类中属性density） 
     * @return 
     */ 
    public static int dip2px(Context context, float dipValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dipValue * scale + 0.5f);  
    }  
   
    /** 
     * 将px值转换为sp值，保证文字大小不变 
     *  
     * @param pxValue 
     * @param fontScale 
     *            （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */ 
    public static int px2sp(Context context, float pxValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (pxValue / fontScale + 0.5f);  
    }  
   
    /** 
     * 将sp值转换为px值，保证文字大小不变 
     *  
     * @param spValue 
     * @param fontScale 
     *            （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */ 
    public static int sp2px(Context context, float spValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (spValue * fontScale + 0.5f);  
    }  

}
