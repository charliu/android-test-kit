package com.tencent.atk.activity;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.tencent.atk.Constants;
import com.tencent.atk.R;
import com.tencent.atk.adapter.AppListAdapter;
import com.tencent.atk.core.AppInfoManager;
import com.tencent.atk.core.TrafficManager;
import com.tencent.atk.model.AppInfo;
import com.tencent.atk.service.TrafficService;
import com.tencent.atk.util.ToastUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener {
	private static final String LOG_TAG = "MainActivity";
	private ListView listView;
	private Button button;
	private AppListAdapter appAdapter;
	private List<AppInfo> appList;
	private boolean isTesting = false;
	private int pid;
	private int uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		listView = (ListView) findViewById(R.id.app_list);
		View emptyView = findViewById(R.id.app_empty);
		listView.setEmptyView(emptyView);
		button = (Button) findViewById(R.id.start_or_stop);
		button.setOnClickListener(this);
		String str = TrafficManager.getXTraffic();
		if (str != null)
			System.out.println("msg:" + str);
		else
			System.out.println("msg:none");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		appList = AppInfoManager.getUserApp(this);
		System.out.println(appList);
		appAdapter = new AppListAdapter(this);
		appAdapter.setItems(appList);
		if (AppInfoManager.isServiceRunning(this, TrafficService.class.getName())) {
			String appName = TrafficService.getCurrentTestPackage();
			if (appName != null) {
				appAdapter.setSelectItemName(appName);
				isTesting = true;
				button.setText(getString(R.string.stop_test));
			}
		} else {
			isTesting = false;
			button.setText(getString(R.string.start_test));
		}
		listView.setAdapter(appAdapter);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}


	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private AppInfo waitForAppStart(String packageName) {
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() < startTime + 20000) {
			ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> processList = am.getRunningAppProcesses();
			for (RunningAppProcessInfo process : processList) {
				if ((process.processName != null) && (process.processName.equals(packageName))) {
					pid = process.pid;
					uid = process.uid;
					if (pid != 0 && uid != 0) {
						AppInfo info = new AppInfo();
						info.setPid(pid);
						info.setUid(uid);
						info.setPackageName(packageName);
						return info;
					}
				}
			}
		}
		return null;
	}

	@Override
	public void onClick(View view) {
		if (!isTesting) {
			if (appAdapter.getSelectedId() != -1) {
				int position = appAdapter.getSelectedId();
				AppInfo info = appList.get(position);
				Intent intent = getPackageManager().getLaunchIntentForPackage(info.getPackageName());
				if (intent != null) {
					startActivity(intent);
					AppInfo testAppInfo = waitForAppStart(info.getPackageName());
					if (testAppInfo == null) {
						ToastUtil.shortToast(this, R.string.launch_app_fail);
						return;
					}
					Intent service = new Intent(MainActivity.this, TrafficService.class);
					service.putExtra(Constants.UID, testAppInfo.getUid());
					service.putExtra(Constants.PID, testAppInfo.getPid());
					service.putExtra(Constants.TEST_PACKAGE, testAppInfo.getPackageName());
					Log.d(LOG_TAG, "MainActivity " + testAppInfo.getPackageName() + " pid:" + info.getPid() + "  uid:" + info.getUid());
					startService(service);
					isTesting = true;
					button.setText(getString(R.string.stop_test));
				} else {
					ToastUtil.longToast(this, R.string.no_app_entrance);
				}
				
			} else {
				ToastUtil.shortToast(this, R.string.select_test_app);
			}
		} else {
			isTesting = false;
			Intent stopService = new Intent(MainActivity.this, TrafficService.class);
			stopService(stopService);
			appAdapter.setSelectItemName(null);
			button.setText(getString(R.string.start_test));
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "设置");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == 1) {
			Intent intent = new Intent(this, ConfigureActivity.class);
			startActivity(intent);
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
}
