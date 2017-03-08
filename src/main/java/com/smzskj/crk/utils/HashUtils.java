package com.smzskj.crk.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ztt on 2017/1/16.
 *
 * 哈希
 */

public class HashUtils {

	/**
	 * MD5
	 *
	 * @param plainText 需要MD5的数据
	 * @return MD5
	 */
	public static String Md5(String plainText) {
		String Ciphertext = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte md5[] = md.digest();
			StringBuffer buf = new StringBuffer("");
			for (byte b : md5) {
				// toHexString 以十六进制返回一个整数字符串
				String s = Integer.toHexString(0xff & b);
				if (s.length() == 1) {
					buf.append("0").append(s);
				} else {
					buf.append(s);
				}
			}
			Ciphertext = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
		return Ciphertext;
	}
}
