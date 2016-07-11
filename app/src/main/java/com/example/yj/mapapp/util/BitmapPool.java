package com.example.yj.mapapp.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * @date 图片缓存池,默认使用可用内存的1/8
 */
public class BitmapPool {

	private static BitmapPool instance;
	private LruCache<Object, Bitmap> lruCache;

	public static BitmapPool getInstance() {
		if (instance == null)
			instance = new BitmapPool();
		return instance;
	}

	private BitmapPool() {
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		lruCache = new LruCache<Object, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(Object key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
	}

	public void clear() {
		lruCache.evictAll();
	}

	public void addBitmapToCatche(Object key, Bitmap bitmap) {
		if (getBitmapFromCatche(key) == null) {
			lruCache.put(key, bitmap);
		}
	}
	
	public void remove(Object key){
		lruCache.remove(key);
	}

	public Bitmap getBitmapFromCatche(Object key) {
		return lruCache.get(key);
	}
}
