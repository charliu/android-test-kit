package com.vimc.atk.core;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.vimc.atk.Constants;
import com.vimc.atk.model.AppInfo;

/**
 * 获取设备上的应用
 * @author CharLiu
 *
 */
public class AppInfoManager {
	private final static String TAG = "AppManager";

	public static ArrayList<AppInfo> getUserApp(Context context) {
		PackageManager pm = context.getPackageManager();
		List<ApplicationInfo> appInfoList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppList = am.getRunningAppProcesses();

		for (ApplicationInfo info : appInfoList) {
			if (((info.flags & ApplicationInfo.FLAG_SYSTEM) > 0 && !Constants.ANDROID_QQ.equals(info.packageName))
					|| info.packageName == null || (info.packageName != null && info.packageName.equals(Constants.APP_NAME)))
				continue;
			AppInfo app = new AppInfo();
			if (!info.packageName.equals(info.processName))
				Log.w(TAG, "Package name not equals process name, " + info.packageName + "  " + info.processName);
			for (RunningAppProcessInfo runningAppProcessInfo : runningAppList) {
				if (runningAppProcessInfo.processName != null && runningAppProcessInfo.processName.equals(info.processName)) {
					app.setPid(runningAppProcessInfo.pid);
					app.setUid(runningAppProcessInfo.uid);
					break;
				}
			}
			app.setPackageName(info.packageName);
			app.setAppName(info.loadLabel(pm).toString());
			app.setIcon(info.loadIcon(pm));
			appList.add(app);
		}

		return appList;
	}
	
	/**
	 *判断service是否运行
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(1000);
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
