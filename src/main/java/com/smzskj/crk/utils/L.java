package com.smzskj.crk.utils;

import android.util.Log;

/**
 * Created by ztt on 16/7/26.
 * <p>
 * log工具类
 */

public class L {
    private static boolean IS_DEBUG = Constants.IS_DEBUG;
    private static String TAG = "TAG";

    public static void setTAG(String TAG) {
        L.TAG = TAG;
    }

    public static void v(String msg) {
        if (IS_DEBUG)
            Log.v(TAG, msg);
    }

    public static void d(String msg) {
        if (IS_DEBUG)
            Log.d(TAG, msg);
    }

    public static void i(String msg) {
        if (IS_DEBUG)
            Log.i(TAG, msg);
    }

    public static void w(String msg) {
        if (IS_DEBUG)
            Log.w(TAG, msg);
    }

    public static void e(String msg) {
        if (IS_DEBUG)
            Log.e(TAG, msg);
    }

}
