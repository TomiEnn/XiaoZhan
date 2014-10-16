package com.zzj.xiaozhan.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * 自定义的内存LRC缓存
 * 
 * 
 */
public class LruBitmapCache extends LruCache<String, Bitmap> implements
		ImageCache {

	public LruBitmapCache(int maxSize) {
		super(maxSize);
	}

	public LruBitmapCache(Context ctx) {
		this(getCacheSize(ctx));
	}

	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}

	@Override
	public Bitmap getBitmap(String url) {
		// LogUtil.info("获取bitmap�? + url);
		return get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap);
		// LogUtil.info("缓存bitmap�? + url + " " + bitmap);
	}

	/**
	 * Returns a cache size equal to approximately three screens worth of
	 * images.
	 */
	public static int getCacheSize(Context ctx) {
		final DisplayMetrics displayMetrics = ctx.getResources()
				.getDisplayMetrics();
		final int screenWidth = displayMetrics.widthPixels;
		final int screenHeight = displayMetrics.heightPixels;
		// 4 bytes per pixel
		final int screenBytes = screenWidth * screenHeight * 4;

		// LogUtil.info("缓存大小�? + screenBytes * 3);
		return screenBytes * 3;
	}

}
