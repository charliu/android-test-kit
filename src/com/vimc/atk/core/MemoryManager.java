package com.vimc.atk.core;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;

public class MemoryManager {
	public static int getPidMemorySize(Context context, int pid) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		int[] myMempid = new int[] { pid };
		Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(myMempid);
		memoryInfo[0].getTotalSharedDirty();

		// int memSize = memoryInfo[0].dalvikPrivateDirty;
		// TODO PSS
		int memSize = memoryInfo[0].getTotalPss();
		// int memSize = memoryInfo[0].getTotalPrivateDirty();
		return memSize;
	}
}
