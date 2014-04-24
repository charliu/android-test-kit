package com.tencent.atk.util;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;

public class ServiceUtil {
	public static boolean isServiceRunning(Context mContext, String className) {
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
		if (serviceList.size() > 0) {
			for (int i = 0; i < serviceList.size(); i++) {
				if (serviceList.get(i).service.getClassName().equals(className) == true) {
					return true;
				}
			}
		}
		return false;
	}
}
