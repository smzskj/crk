package com.smzskj.crk.utils;

/**
 * Created by ztt on 2017/1/17.
 *
 * 用户信息
 */

public class UserInfo {

	public static final String SP_USER_INFO = "user_info";
	public static final String SP_USER_CODE = "user_code";
	public static final String SP_USER_NAME = "user_name";
	public static final String SP_CK_CODE = "ck_code";
	public static final String SP_CK_NAME = "ck_name";
	public static final String SP_DB_CODE = "db_code";
	public static final String SP_DB_NAME = "db_name";
	public static final String VCODE = "vcode";
	public static final String VNAME = "vname";

	public static final String SP_IP = "ip";
	public static final String SP_PORT = "port";


	public static String RY_CODE = "";
	public static String RY_NAME = "";


	public static void clearUserInfo(){
		RY_CODE = "";
		RY_NAME = "";
	}
}
