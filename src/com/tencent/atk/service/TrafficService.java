package com.tencent.atk.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.*;

import com.tencent.atk.AtkApplication;
import com.tencent.atk.Constants;
import com.tencent.atk.R;
import com.tencent.atk.core.CpuManager;
import com.tencent.atk.core.TrafficManager;
import com.tencent.atk.util.ToastUtil;
import com.tencent.atk.view.FloatView;
import com.tencent.atk.view.FloatView.ResetTrafficListener;

/**
 * 更新流量服务
 */
public class TrafficService extends Service {
	private int delayTime = 500;
	private WindowManager windowManager;
	private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
	private FloatView floatView;
	private float mTouchStartX;
	private float mTouchStartY;
	private float x;
	private float y;
	private Handler handler = new Handler();
	private volatile boolean isServiceRunning = false;
	private long initTrafficT = -1;
	private long initTrafficR = -1;
	private int uid;
	private int pid;
	private static String currentTestPackage = null;
	
	private CpuManager cpuManager;
	private String percent = "";
	
	@Override
	public void onCreate() {
		super.onCreate();
		createFloatingWindow();
		delayTime = AtkApplication.getInstance().getConfigure().getTrafficFreqency();
		Log.d(Constants.TAG, "MainService delay time is:" + delayTime);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			uid = intent.getIntExtra(Constants.UID, -1);
			pid = intent.getIntExtra(Constants.PID, -1);
			currentTestPackage = intent.getStringExtra(Constants.TEST_PACKAGE);
			Log.d(Constants.TAG, "MainService uid:" + uid);
			if (uid != -1) {
				isServiceRunning = true;
				cpuManager = new CpuManager(pid);
				handler.postDelayed(task, delayTime);
			} else {
				ToastUtil.longToast(this, R.string.read_fail_try_again);
				stopSelf();
			}
		} else {
			stopSelf();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(Constants.TAG, "MainService onDestory");
		clear();
	}

	private void clear() {
		if (floatView != null)
			windowManager.removeView(floatView);
		isServiceRunning = false;
		currentTestPackage = null;
		uid = -1;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private Runnable task = new Runnable() {
		@Override
		public void run() {
			if (isServiceRunning) {
				updateTrafficInfo();
				handler.postDelayed(this, delayTime);
			} else {
				stopSelf();
			}
		}
	};
	
	/**
	 * 更新流量
	 */
	private void updateTrafficInfo() {
		long incTrafficT = 0;
		long incTrafficR = 0;
//		long trafficSnd = TrafficStats.getUidTxBytes(uid);  //some phone not support this API
//		long trafficRecv = TrafficStats.getUidRxBytes(uid);
		
		long trafficSnd = TrafficManager.getSndTraffic(uid);
		long trafficRecv = TrafficManager.getRcvTraffic(uid);
		
		if (initTrafficT == -1) {
			initTrafficT = trafficSnd;
			Log.d(Constants.TAG, "Init send Traffic is:" + initTrafficT);
		} else {
			incTrafficT = trafficSnd - initTrafficT;
		}
		if (initTrafficR == -1) {
			initTrafficR = trafficRecv;
			Log.d(Constants.TAG, "Init recv Traffic is:" + initTrafficR);
		} else {
			incTrafficR = trafficRecv - initTrafficR;
		}
//		Log.d(Constants.TAG, "Current UID: " + uid + " INIT Send:" + initTrafficT + " INIT Recv:" + initTrafficR + " CURR Send:" + trafficSnd + " CURR Recv:" + trafficRecv);
		percent = cpuManager.getCpuRatioInfo().toString();
		floatView.updateTraffic(incTrafficT, incTrafficR, percent);
	}

	private void createFloatingWindow() {
		floatView = new FloatView(this);
		windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		wmParams.type = 2002;
		wmParams.flags |= 8;
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;
		wmParams.x = 0;
		wmParams.y = 0;
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.format = 1;
		windowManager.addView(floatView, wmParams);
		floatView.setResetTrafficListener(new ResetTrafficListener() {
			@Override
			public void onResetListener() {
//				initTrafficT = TrafficStats.getUidTxBytes(uid);  //some phone not support this API
//				initTrafficR = TrafficStats.getUidRxBytes(uid);
				initTrafficT = TrafficManager.getSndTraffic(uid);
				initTrafficR = TrafficManager.getRcvTraffic(uid);
				floatView.updateTraffic(0, 0, percent);
			}
		});
		
		floatView.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				x = event.getRawX();
				y = event.getRawY() - 25;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mTouchStartX = event.getX();
					mTouchStartY = event.getY();
					//Log.d("startP", "startX" + mTouchStartX + "====startY" + mTouchStartY);
					break;
				case MotionEvent.ACTION_MOVE:
					updateViewPosition();
					break;

				case MotionEvent.ACTION_UP:
					updateViewPosition();
					mTouchStartX = mTouchStartY = 0;
					break;
				}
				return true;
			}
		});
	}

	private void updateViewPosition() {
		wmParams.x = (int) (x - mTouchStartX);
		wmParams.y = (int) (y - mTouchStartY);
		windowManager.updateViewLayout(floatView, wmParams);
	}
	
	public static String getCurrentTestPackage() {
		return currentTestPackage;
	}
	
	
}
