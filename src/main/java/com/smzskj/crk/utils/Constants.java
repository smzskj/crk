package com.smzskj.crk.utils;

import com.smzskj.crk.BuildConfig;

/**
 * Created by ztt on 2017/1/16.
 *
 * 常量x`
 */

public class Constants {

	public static boolean IS_DEBUG = BuildConfig.DEBUG;
//	public static boolean IS_DEBUG = true;

	public static final int KEY_SCAN = 135;
	public static final String CHAR_SET = "utf-8";



	// http://feixue.iask.in:29197/crk_service/services/PublicSevice?wsdl
//	public static String IP = "http://feixue.iask.in:";
//	public static String IP_DEFAULT = "192.168.19.45";
	public static String IP_DEFAULT = "127.0.0.1";
	public static String PORT_DEFAULT = "8080";
	public static String IP = "";
	public static String PORT = "8080";
	public static String URL = "http://" + IP + ":" + PORT + "/crk_service/services/CRKPublicSevice";
//	public static String URL = "http://192.168.19.45:8080/crk_service/services/CRKPublicSevice";

	public static String NAME_SPACE = "http://service.hhzs.com/";
	public static String METHOD = "exe_json";


	public static void setUrl() {
		URL = "http://" + IP + ":" + PORT + "/crk_service/services/CRKPublicSevice";
//		URL = "http://feixue.iask.in:20498/crk_service/services";
	}


}
