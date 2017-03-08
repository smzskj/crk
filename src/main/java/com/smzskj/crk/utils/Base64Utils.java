package com.smzskj.crk.utils;


import android.util.Base64;

public class Base64Utils {

	/**
	 * 加密
	 *
	 * @param base
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String enBase(String base, String charset) {

		String result;
		try {
			result = new String(Base64.encode(base.getBytes(charset), Base64.DEFAULT));
		} catch (Exception e) {
			result = null;
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 解密
	 *
	 * @param base
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String deBase(String base, String charset) {

		String result;
		try {
			result = new String(Base64.decode(base.getBytes(charset), Base64.DEFAULT));
		} catch (Exception e) {
			result = null;
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 加密
	 *
	 * @param base
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static byte[] enBase2Byte(String base, String charset) {

		byte[] result;
		try {
			result = Base64.encode(base.getBytes(charset), Base64.DEFAULT);
		} catch (Exception e) {
			result = null;
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 解密
	 *
	 * @param base
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static byte[] deBase2Byte(String base, String charset) {

		byte[] result;
		try {
			result = Base64.decode(base.getBytes(charset), Base64.DEFAULT);
		} catch (Exception e) {
			result = null;
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 加密
	 *
	 * @param base
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String enByte2Base(byte[] base, String charset) {


		String result;
		try {
			result = new String(Base64.encode(base, Base64.DEFAULT));
		} catch (Exception e) {
			result = null;
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 解密
	 *
	 * @param base
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String deByte2Base(byte[] base, String charset) {

		String result;
		try {
			result = new String(Base64.decode(base, Base64.DEFAULT));
		} catch (Exception e) {
			result = null;
			e.printStackTrace();
		}

		return result;
	}
}
