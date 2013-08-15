package com.ainur.hidevk;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.ainur.hidevk.util.Log;
import com.nostra13.universalimageloader.cache.disc.impl.FileCountLimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class HideVkApp extends Application {
	private static Context staticContext;

	public static Context getContext() {
		return staticContext;
	}

	@Override
	public void onCreate() {
		staticContext = this;
		super.onCreate();

		try {
			File cacheDir = Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState()) ? getExternalCacheDir()
					: getCacheDir();
			Log.d("cache dir: " + cacheDir.getAbsolutePath());

			DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder()
					.cacheInMemory(true).cacheOnDisc(true).build();

			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					this).threadPoolSize(3)
					.denyCacheImageMultipleSizesInMemory()
					.discCache(new FileCountLimitedDiscCache(cacheDir, 100))
					.defaultDisplayImageOptions(defaultDisplayImageOptions)
					.build();

			ImageLoader.getInstance().init(config);
		} catch (Exception e) {
			ImageLoader.getInstance().init(
					ImageLoaderConfiguration.createDefault(this));
		}
	}
}
