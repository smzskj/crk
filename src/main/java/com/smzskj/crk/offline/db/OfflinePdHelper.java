package com.smzskj.crk.offline.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ztt on 2017/2/24.
 * <p>
 * DB_VERSION:
 * 1：只有离线盘点
 * 2：添加离线商品表
 * 3：添加离线入库表
 * 4：添加离线出库表
 */

public class OfflinePdHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "offline_pd";
	/**
	 * 离线盘点
	 */
	public static final String TABLE_NAME_C_PDB = "c_pdb";
	/**
	 * 离线商品
	 */
	public static final String TABLE_NAME_LXSP = "lxsp";
	/**
	 * 离线入库
	 */
	public static final String TABLE_NAME_LXRK = "lxrk";
	/**
	 * 离线入库
	 */
	public static final String TABLE_NAME_LXCK = "lxck";
	public static final int DB_VERSION = 4;


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
	private String sqlCreatePd = "create table " + TABLE_NAME_C_PDB + " (_id integer primary key  autoincrement," +
			"sjk text," +    // 数据库
			"ry text," +    // 人员
			"kf text," +    // 库房
			"rq text," +    // 日期
			"sph text," +    // 商品号
			"pch text," +    // 批次号
			"zt text," +    // 状态
			"lrrq text," +    // 录入时间(到秒)
			"sjk_dm text," + // 数据库代码
			"ry_dm text," + // 人员代码
			"kf_dm text" +    // 库房代码
			")";

	/**
	 * 离线商品
	 */
	private String SqlCreateLxsp = "create table " + TABLE_NAME_LXSP + " (_id integer primary key  autoincrement " +
			",bh" +    // 编号
			",spmc" +    // 商品名称
			",txm" +    // 条形码
			",dw" +        // 单位
			")";

	/**
	 * 离线入库
	 */
	private String SqlCreateLxrk = "create table " + TABLE_NAME_LXRK + " (_id integer primary key  autoincrement " +
			",djhm" +    // 单据号码
			",rkdd" +    // 入库地点
			",rkdd_dm" +// 入库地点代码
			",zdr" +    // 制单人
			",zdr_dm" +    // 制单人代码
			",sjk" +    // 数据库
			",sjk_dm" +    // 数据库代码
			",bh" +        // 编号
			",spmc" +    // 商品名称
			",dw" +        // 单位
			",pch" +    // 批次号
			",rq" +    // 日期
			",zt" +    // 状态(成功或其他)
			")";

	/**
	 * 离线出库
	 */
	private String SqlCreateLxck = "create table " + TABLE_NAME_LXCK + " (_id integer primary key  autoincrement " +
			",djhm" +    // 单据号码
			",ckdd" +    // 出库地点
			",ckdd_dm" +// 出库地点代码
			",zdr" +    // 制单人
			",zdr_dm" +    // 制单人代码
			",sjk" +    // 数据库
			",sjk_dm" +    // 数据库代码
			",bh" +        // 编号
			",spmc" +    // 商品名称
			",dw" +        // 单位
			",pch" +    // 批次号
			",rq" +    // 日期
			",sj" +    // 时间(到秒)
			",zt" +    // 状态(成功或其他)
			")";

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sqlCreatePd);
		db.execSQL(SqlCreateLxsp);
		db.execSQL(SqlCreateLxrk);
		db.execSQL(SqlCreateLxck);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 1) {
			db.execSQL(SqlCreateLxsp);
			db.execSQL(SqlCreateLxrk);
			db.execSQL(SqlCreateLxck);
		} else if (oldVersion == 2) {
			db.execSQL(SqlCreateLxrk);
			db.execSQL(SqlCreateLxck);
		} else if (oldVersion ==3) {
			db.execSQL(SqlCreateLxck);
		}
	}
}
