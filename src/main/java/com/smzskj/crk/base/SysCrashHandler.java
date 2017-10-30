package com.smzskj.crk.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SysCrashHandler implements UncaughtExceptionHandler {
	// 需求是 整个应用程序 只有一个 MyCrash-Handler   
	private static SysCrashHandler sysCrashHandler;
	private Context context;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	private SysCrashHandler(Context context) {
		this.context = context;
	}

	public static synchronized SysCrashHandler getInstance(Context context) {
		if (sysCrashHandler != null) {
			return sysCrashHandler;
		} else {
			sysCrashHandler = new SysCrashHandler(context);
			return sysCrashHandler;
		}
	}

	public void uncaughtException(Thread arg0, Throwable arg1) {
		// 1.获取当前程序的版本号. 版本的id
		String versioninfo = getVersionInfo();
		// 2.获取手机的硬件信息.
		String mobileInfo = getMobileInfo();
		// 3.把错误的堆栈信息 获取出来
		String errorinfo = getErrorInfo(arg1);
		String mobile_sys_version = getAndroidOSVersion();
		// 4.把所有的信息 还有信息对应的时间 提交到服务器
		try {
			uploadException(mobile_sys_version, versioninfo, mobileInfo, errorinfo);
		} catch (Exception e) {
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
			System.gc();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
		System.gc();
	}


	/**
	 * 获取错误的信息
	 *
	 * @param arg1
	 * @return
	 */
	private String getErrorInfo(Throwable arg1) {
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		arg1.printStackTrace(pw);
		pw.close();
		String error = writer.toString();
		return error;
	}

	/**
	 * 获取手机的硬件信息
	 *
	 * @return
	 */
	private String getMobileInfo() {
		StringBuffer sb = new StringBuffer();
		//通过反射获取系统的硬件信息
		try {

			Field[] fields = Build.class.getDeclaredFields();
			for (Field field : fields) {
				//暴力反射 ,获取私有的信息
				field.setAccessible(true);
				String name = field.getName();
				String value = field.get(null).toString();
				sb.append(name + "=" + value);
				sb.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 获取手机的版本信息
	 *
	 * @return
	 */
	private String getVersionInfo() {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return "unknow version";
		}
	}

	public String getAndroidOSVersion() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * 网络是否可用
	 *
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = mgr.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	private void uploadException(String mobile_sys_version, String versioninfo, String mobileInfo, String errorinfo) throws Exception{
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return;
		}
		File toDir = new File(Environment.getExternalStorageDirectory() + "/crk/" + "log");
		if (!toDir.exists()) {
			boolean dirMk = toDir.mkdirs();
			if (!dirMk) {
				return;
			}
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
		File file = new File(toDir, dateFormat.format(new Date()) + ".txt");
		try {
			boolean b = file.createNewFile();
			if (b) {
				FileWriter writer = new FileWriter(file, true);
				writer.append(mobile_sys_version);
				writer.append("\r\n");
				writer.append(versioninfo);
				writer.append("\r\n");
				writer.append(mobileInfo);
				writer.append("\r\n");
				writer.append(errorinfo);
				writer.flush();
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
