package com.tencent.atk.activity;

import com.tencent.atk.AtkApplication;
import com.tencent.atk.Constants;
import com.tencent.atk.R;
import com.tencent.atk.model.Configure;

import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.RadioButton;

public class ConfigureActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{
	private RadioGroup frequencyGriup;
	private int originalTrafficFrequency;
	private int currentFreqency;
	private Configure configure;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configure);
		frequencyGriup = (RadioGroup) findViewById(R.id.frequency_radio_group);
		configure = AtkApplication.getInstance().getConfigure();
		originalTrafficFrequency = configure.getTrafficFreqency();
		setCurrentFrequency(originalTrafficFrequency);
		frequencyGriup.setOnCheckedChangeListener(this);
	}
	
	private void setCurrentFrequency(int frequency) {
		int index = 0;
		if (frequency == 250) {
			index = 0;
		} else if (frequency == 500) {
			index = 1;
		} else if (frequency == 1000) {
			index = 2;
		}
		((RadioButton)frequencyGriup.getChildAt(index)).setChecked(true);
	}
	
	
	@Override
	protected void onStop() {
		super.onStop();
		if (currentFreqency != originalTrafficFrequency) {
			configure.setTrafficFreqency(currentFreqency);
			originalTrafficFrequency = currentFreqency;
			Log.d(Constants.TAG, "Save traffic freqency:" + currentFreqency);
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		int id = group.getCheckedRadioButtonId();
		Log.d(Constants.TAG, "msg:" + id);
		if (id == R.id.frequency_250) {
			currentFreqency = 250;
		} else if (id == R.id.frequency_500) {
			currentFreqency = 500;
		} else if (id == R.id.frequency_1000) {
			currentFreqency = 1000;
		}
	}
	
}
