package com.vimc.atk;

import com.vimc.atk.model.Configure;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

public class AtkApplication extends Application {
	public static final int SDK_VERSION = android.os.Build.VERSION.SDK_INT;
	private static AtkApplication application;
	private Configure configure;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(Constants.TAG, "App start");
		application = this;
		configure = new Configure(this);
	}
	
	public static AtkApplication getInstance() {
		return application;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.i(Constants.TAG, "App terminate");
	}
	
	public Configure getConfigure() {
		return configure;
	}

}
