package com.smzskj.crk.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES加解密
 */
public class DESUtils {

	private static byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};
	private static String KEY = "LinYiShiGuoShuiJuWangShangShuiWuJu-HHzs-2510";


	public static String encryptDES(String encryptString){
		try {
			return encryptDES(encryptString, KEY);
		} catch (Exception e) {
			return "";
		}
	}

	public static String decryptDES(String decryptString){
		try {
			return decryptDES(decryptString, KEY);
		} catch (Exception e) {
			return "";
		}
	}


	public static String encryptDES(String encryptString, String encryptKey)
			throws Exception {
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(getKey(encryptKey.getBytes()), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
		byte[] encryptedData = cipher.doFinal(encryptString.getBytes("UTF-8"));
		byte[] data = Base64.encode(encryptedData, Base64.DEFAULT);
		return new String(data, "UTF-8");
	}

	public static String decryptDES(String decryptString, String decryptKey)
			throws Exception {
		byte[] byteMi = Base64.decode(decryptString, Base64.DEFAULT);
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(getKey(decryptKey.getBytes()), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
		byte decryptedData[] = cipher.doFinal(byteMi);
		return new String(decryptedData, "UTF-8");
	}


	/**
	 * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
	 *
	 * @param arrBTmp 构成该字符串的字节数组
	 * @return 生成的密钥
	 * @throws Exception
	 */
	private static byte[] getKey(byte[] arrBTmp) throws Exception {
		// 创建一个空的8位字节数组（默认值为0）
		byte[] arrB = new byte[8];
		// 将原始字节数组转换为8位
		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}
		return arrB;
	}
}
