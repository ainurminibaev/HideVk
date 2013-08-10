package com.ainur.hidevk.util;

import java.util.Locale;

import com.ainur.hidevk.BuildConfig;

public class Log {

	private static final String TAG = "Hidevk";

	public static void d(String msg) {
		if (BuildConfig.DEBUG && msg != null) {
			android.util.Log.d(TAG, msg);
		}
	}

	public static void d(String msg, long time) {
		if (BuildConfig.DEBUG && msg != null) {
			d(String.format(Locale.ENGLISH, "%s in %d ms", msg,
					System.currentTimeMillis() - time));
		}
	}

	public static void d(Throwable e) {
		if (BuildConfig.DEBUG && e != null) {
			android.util.Log.d(TAG, e.getMessage(), e);
		}
	}

	public static void e(String msg) {
		if (BuildConfig.DEBUG && msg != null) {
			android.util.Log.e(TAG, msg);
		}
	}

	public static void e(Throwable e) {
		if (BuildConfig.DEBUG && e != null) {
			android.util.Log.e(TAG, e.getMessage(), e);
		}
	}
}
