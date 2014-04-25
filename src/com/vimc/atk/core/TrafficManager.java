package com.vimc.atk.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.util.Log;

import com.vimc.atk.Constants;

/**
 * 获取流量数据
 * @author CharLiu
 *
 */
public class TrafficManager {
	private static final int TCP_SND = 1;
	private static final int TCP_RCV = 2;

	public static long getSndTraffic(int uid) {
		return getTraffic(TCP_SND, uid);
	}

	public static long getRcvTraffic(int uid) {
		return getTraffic(TCP_RCV, uid);
	}

	public static long getAllTraffic(int uid) {
		return getTraffic(TCP_SND, uid) + getTraffic(TCP_RCV, uid);
	}

	private static long getTraffic(int type, int uid) {
		String trafficPath = null;
		if (type == TCP_SND)
			trafficPath = "/proc/uid_stat/" + uid + "/tcp_snd";
		else if (type == TCP_RCV)
			trafficPath = "/proc/uid_stat/" + uid + "/tcp_rcv";
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(trafficPath, "r");
			return Long.parseLong(raf.readLine());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (raf != null)
					raf.close();
			} catch (IOException e) {
				Log.i(Constants.TAG, "Close RandomAccessFile exception: " + e.getMessage());
			}
		}
		return -1;
	}
}
