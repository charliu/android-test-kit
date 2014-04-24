package com.tencent.atk.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Configure {
	private SharedPreferences sharedPreferences;

	public Configure(Context context) {
		sharedPreferences = context.getSharedPreferences("configure", 0);
	}

	public void setTrafficFreqency(int frequency) {
		Editor editor = sharedPreferences.edit();
		editor.putInt("TRAFFIC_FREQUENCY", frequency);
		editor.commit();
	}

	public int getTrafficFreqency() {
		return sharedPreferences.getInt("TRAFFIC_FREQUENCY", 500);
	}

}
