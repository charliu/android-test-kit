package com.tencent.atk.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import android.util.Log;

import com.tencent.atk.Constants;

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

	public static String getXTraffic() {
		String xt_qtaguid_path = "/proc/net/xt_qtaguid/stats";
		String xt = "/proc/net/xt_qtaguid/iface_stat_all";
		String encoding = "UTF-8";

		return null;
	}

	public static String readLastLine(File file, String charset) throws IOException {
		if (!file.exists() || file.isDirectory() || !file.canRead()) {
			return null;
		}
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "r");
			long len = raf.length();
			if (len == 0L) {
				return "";
			} else {
				long pos = len - 1;
				while (pos > 0) {
					pos--;
					raf.seek(pos);
					if (raf.readByte() == '\n') {
						break;
					}
				}
				if (pos == 0) {
					raf.seek(0);
				}
				byte[] bytes = new byte[(int) (len - pos)];
				raf.read(bytes);
				if (charset == null) {
					return new String(bytes);
				} else {
					return new String(bytes, charset);
				}
			}
		} catch (FileNotFoundException e) {
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (Exception e2) {
				}
			}
		}
		return null;
	}
}
