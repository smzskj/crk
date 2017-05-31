package com.smzskj.crk.utils;

import java.io.File;
import java.io.FileInputStream;
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
			StringBuilder builder = new StringBuilder("");
			for (byte b : md5) {
				// toHexString 以十六进制返回一个整数字符串
				String s = Integer.toHexString(0xff & b);
				if (s.length() == 1) {
					builder.append("0").append(s);
				} else {
					builder.append(s);
				}
			}
			Ciphertext = builder.toString();
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
		return Ciphertext;
	}

	/**
	 * 获取文件的md5值
	 * @param path 文件的全路径名称
	 * @return md5
	 */
	public static String getFileMd5(String path){
		try {
			// 获取一个文件的特征信息，签名信息。
			File file = new File(path);
			// md5
			MessageDigest digest = MessageDigest.getInstance("md5");
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = fis.read(buffer)) != -1) {
				digest.update(buffer, 0, len);
			}
			byte[] result = digest.digest();
			StringBuilder sb  = new StringBuilder();
			for (byte b : result) {
				// 与运算
				int number = b & 0xff;
				String str = Integer.toHexString(number);
				// System.out.println(str);
				if (str.length() == 1) {
					sb.append("0");
				}
				sb.append(str);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
