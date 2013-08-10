package com.ainur.hidevk;

import android.app.Application;
import android.content.Context;

public class HideVkApp extends Application {
	private static Context staticContext;

	public static Context getContext() {
		return staticContext;
	}

	@Override
	public void onCreate() {
		staticContext = this;
		super.onCreate();
	}
}
