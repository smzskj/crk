package com.smzskj.crk.offline.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ztt on 2017/2/24.
 */

public class OfflinePdHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "offline_pd";
	/** 离线盘点 */
	public static final String TABLE_NAME_C_PDB = "c_pdb";
	/** 离线商品 */
	public static final String TABLE_NAME_LXSP = "lxsp";
	public static final int DB_VERSION = 2;


	public OfflinePdHelper(Context context) {
		this(context, DB_NAME, null, DB_VERSION);
	}

	private OfflinePdHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int
			version) {
		super(context, name, factory, version);
	}
//	状态
//	2，成功
//	3，日期不能为空
//	4，库房名称不能为空
//	5，人员不能为空
//	6，商品号不能为空
//	7，批次号不能为空
//	8，离线盘点-商品号或条形码查询商品不存在!
//	9，未上传

	public static final String SUCCESS = "2";
	public static final String RQ_NULL = "3";
	public static final String KF_NULL = "4";
	public static final String RY_NULL = "5";
	public static final String SPH_NULL = "6";
	public static final String PCH_NULL = "7";
	public static final String SP_NULL = "8";
	public static final String NOT_UP = "9";
	/**
	 * 盘点
	 */
	private String sqlCreatePd = "create table c_pdb (_id integer primary key  autoincrement," +
			"sjk text," + 	// 数据库
			"ry text," +	// 人员
			"kf text," + 	// 库房
			"rq text," + 	// 日期
			"sph text," + 	// 商品号
			"pch text," + 	// 批次号
			"zt text," + 	// 状态
			"lrrq text," + 	// 录入时间(到秒)
			"sjk_dm text," + // 数据库代码
			"ry_dm text," + // 人员代码
			"kf_dm text" + 	// 库房代码
			")";

	/**
	 * 离线商品
	 */
	private String SqlCreateLxsp = "create table lxsp (_id integer primary key  autoincrement " +
			",bh" + 	// 编号
			",spmc" +	// 商品名称
			",txm" +	// 条形码
			",dw" +		// 单位
			")";

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sqlCreatePd);
		db.execSQL(SqlCreateLxsp);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 1 && newVersion == 2) {
			db.execSQL(SqlCreateLxsp);
		}
	}
}
