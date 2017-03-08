package com.smzskj.crk.utils;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.smartdevicesdk.device.DeviceInfo;
import com.smartdevicesdk.device.DeviceManage;
import com.smartdevicesdk.scanner.ScannerHelper3501;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ztt on 2017/1/18.
 * <p>
 * 扫描
 */

public class ScannerUtils {

	private ScannerHelper3501 scanner = null;

	public static boolean scanning = false;
	public static Timer timer = new Timer();

	public ScannerUtils(final ScannerListener listener) {
		DeviceInfo devInfo = DeviceManage.getDevInfo("PDA3501");
		// 获取扫描模块串口名称
		String device = devInfo.getScannerSerialport();
		// 获取扫描模块串口波特率
		int baudrate = devInfo.getScannerBaudrate();
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
//				scanning = false;
//				if (timer != null) {
//					timer.cancel();
//					timer.purge();
//					timer = null;
//				}

				L.e("扫描结束");
				if (listener != null) {
					String barCode = msg.obj.toString();
					if (!TextUtils.isEmpty(barCode)) {
						listener.onSuccess(barCode);
					}
				} else {
					cloase();
				}
			}
		};
		scanner = new ScannerHelper3501(device, baudrate, handler);
	}

	public void scanner() {
		try {
			if (!scanning) {
				scanning = true;
				if (timer == null) {
					timer = new Timer();
				}
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						L.e("Timer");
						scanning = false;
					}
				},1000);
				L.e("开始扫描");
				scanner.scan();
			}
		} catch (Exception e) {
			L.e("扫描异常" + e.getMessage());
			scanning = false;
		}
	}


	public void cloase() {
		if (scanner != null) {
			scanner.Close();
			scanner = null;
		}
	}

	public interface ScannerListener {
		void onSuccess(String barCode);
	}
}
