package com.smzskj.crk.base;

import android.app.Activity;
import android.app.Application;

import com.smzskj.crk.utils.L;

import java.util.LinkedList;

/**
 * Created by ztt on 2017/1/19.
 */

public class BaseApplication extends Application {

	private LinkedList<Activity> activities = new LinkedList<>();
	private static BaseApplication instance;


	//实例化一次
	public synchronized static BaseApplication getInstance() {
		if (null == instance) {
			instance = new BaseApplication();
		}
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
//		if (!Constants.IS_DEBUG) {
			SysCrashHandler sysCrashHandler = SysCrashHandler.getInstance(getApplicationContext());
			Thread.setDefaultUncaughtExceptionHandler(sysCrashHandler);
//		}
	}

	// add Activity
	public void addActivity(Activity activity) {
		activities.add(activity);
	}

	// remove Activity
	public void removeActivity(Activity activity) {
		activities.remove(activity);
	}

	//关闭每一个list内的activity
	public void exit() {
		try {
			for (Activity activity : activities) {
				if (activity != null) {
					activity.finish();
				}
			}
		} catch (Exception e) {
			L.e("关闭异常");
		}
	}

	public void clear(Activity activity) {
		try {
			for (Activity act : activities) {
				if (act != null && act != activity) {
					act.finish();
				}
			}
		} catch (Exception e) {
			L.e("关闭异常");
		}
	}

	//杀进程
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}
}
