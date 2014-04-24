package com.vimc.atk.model;

import android.graphics.drawable.Drawable;

public class AppInfo {
	private Drawable icon;
	private String packageName;
	private String appName;
	private int pid;
	private int uid;

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
	
	@Override
	public String toString() {
		return "AppInfo [icon=" + icon + ", packageName=" + packageName + ", appName=" + appName + ", pid=" + pid + ", uid="
				+ uid + "]";
	}

}
