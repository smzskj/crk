package com.smzskj.crk.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by ztt on 2016/12/15.
 * <p>
 * 应用工具类
 */

public class AppUtils {

	public static String getAppVersionName(Context context) {
		String versionName = "";
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			if (info != null) {
				versionName = info.versionName;
			}
		} catch (PackageManager.NameNotFoundException e) {
			versionName = "";
		}
		return versionName;
	}
}
