package com.smzskj.crk.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;

/**
 * Created by ztt on 2016/12/13.
 * <p>
 * SharedPreferences 工具类
 * String类型进行加密
 */
public class SPUtils {

	private SharedPreferences mSp;

	/**
	 * 构造方法。
	 *
	 * @param context 上下文
	 */
	public SPUtils(Context context, String spName) {
		mSp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
	}

	/**
	 * @param key   键名称。
	 * @param value 值。
	 * @return 引用的KV对象。
	 */
	public SPUtils put(String key, Object value) {
		Editor editor = mSp.edit();
		if (value instanceof Boolean) {
			editor.putBoolean(key, (Boolean) value).apply();
		} else if (value instanceof Integer || value instanceof Byte) {
			editor.putInt(key, (int) value).apply();
		} else if (value instanceof Long) {
			editor.putLong(key, (long) value).apply();
		} else if (value instanceof Float) {
			editor.putFloat(key, (float) value).apply();
		} else if (value instanceof String) {
			// 加密
			editor.putString(key, DESUtils.encryptDES((String) value)).apply();
		}
		return this;
	}


	/**
	 * 获取保存着的boolean对象。
	 *
	 * @param key      键名
	 * @param defValue 当不存在时返回的默认值。
	 * @return 返回获取到的值，当不存在时返回默认值。
	 */
	public boolean getBoolean(String key, boolean defValue) {
		return mSp.getBoolean(key, defValue);
	}

	/**
	 * 获取保存着的int对象。
	 *
	 * @param key      键名
	 * @param defValue 当不存在时返回的默认值。
	 * @return 返回获取到的值，当不存在时返回默认值。
	 */
	public int getInt(String key, int defValue) {
		return mSp.getInt(key, defValue);
	}

	/**
	 * 获取保存着的long对象。
	 *
	 * @param key      键名
	 * @param defValue 当不存在时返回的默认值。
	 * @return 返回获取到的值，当不存在时返回默认值。
	 */
	public long getLong(String key, long defValue) {
		return mSp.getLong(key, defValue);
	}

	/**
	 * 获取保存着的float对象。
	 *
	 * @param key      键名
	 * @param defValue 当不存在时返回的默认值。
	 * @return 返回获取到的值，当不存在时返回默认值。
	 */
	public float getFloat(String key, float defValue) {
		return mSp.getFloat(key, defValue);
	}

	/**
	 * 获取保存着的String对象。
	 *
	 * @param key      键名
	 * @param defValue 当不存在时返回的默认值。
	 * @return 返回获取到的值，当不存在时返回默认值。
	 */
	public String getString(String key, String defValue) {
		// 解密
		return DESUtils.decryptDES(mSp.getString(key, defValue));
	}

	/**
	 * 获取所有键值对。
	 *
	 * @return 获取到的所胡键值对。
	 */
	public Map<String, ?> getAll() {
		return mSp.getAll();
	}


	/**
	 * 移除键值对。
	 *
	 * @param key 要移除的键名称。
	 * @return 引用的KV对象。
	 */
	public SPUtils remove(String key) {
		Editor editor = mSp.edit();
		editor.remove(key);
		editor.apply();
		return this;
	}

	/**
	 * 清除所有键值对。
	 *
	 * @return 引用的KV对象。
	 */
	public SPUtils clear() {
		Editor editor = mSp.edit();
		editor.clear();
		editor.apply();
		return this;
	}

	/**
	 * 是否包含某个键。
	 *
	 * @param key 查询的键名称。
	 * @return 当且仅当包含该键时返回true, 否则返回false.
	 */
	public boolean contains(String key) {
		return mSp.contains(key);
	}
}
