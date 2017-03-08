package com.smzskj.crk.offline.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smzskj.crk.offline.bean.OfflineBean;
import com.smzskj.crk.offline.bean.OfflineCountListBean;
import com.smzskj.crk.utils.L;
import com.smzskj.crk.utils.SPUtils;
import com.smzskj.crk.utils.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ztt on 2017/2/24.
 */

public class PdDBUtils {

	private Context mContext;
	private OfflinePdHelper mHelper;
	private SPUtils mSp;



	public PdDBUtils(Context context) {
		this.mContext = context;
		this.mHelper = new OfflinePdHelper(mContext);
		mSp = new SPUtils(mContext, UserInfo.SP_USER_INFO);

	}


//			"sjk text," + 	// 数据库
//			"ry text," +	// 人员
//			"kf text," + 	// 库房
//			"rq text," + 	// 日期(到毫秒)
//			"sph text," + 	// 商品号
//			"pch text," + 	// 批次号
//			"zt text," + 	// 状态
//			"lrrq text," + 	// 录入日期
//			"sjk_dm text," + // 数据库代码
//			"ry_dm text," + // 人员代码
//			"kf_dm text" + 	// 库房代码

	/**
	 * 插入商品
	 *
	 * @param sph
	 * @param pch
	 * @return
	 */
	public long insert(String sph, String pch, String rq,String lrrq) {
		if (querySphPch(sph, pch ,rq)) {
			L.e("-2");
			return -2L;
		}
		SQLiteDatabase db = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("sjk", mSp.getString(UserInfo.SP_DB_NAME, ""));
		values.put("ry", mSp.getString(UserInfo.SP_USER_NAME, ""));
		values.put("kf", mSp.getString(UserInfo.SP_CK_NAME, ""));
		values.put("sjk_dm", mSp.getString(UserInfo.SP_DB_CODE, ""));
		values.put("ry_dm", mSp.getString(UserInfo.SP_USER_CODE, ""));
		values.put("kf_dm", mSp.getString(UserInfo.SP_CK_CODE, ""));

		values.put("zt", OfflinePdHelper.NOT_UP);
		values.put("rq", rq);
		values.put("lrrq", lrrq);

		values.put("sph", sph);
		values.put("pch", pch);

		long l = db.insert(OfflinePdHelper.TABLE_NAME_C_PDB, null, values);
		db.close();
		return l;
	}

	/**
	 * 更新数据库中盘点状态
	 *
	 * @param sph 商品号
	 * @param pch 批次号
	 * @param zt  状态
	 * @return 修改的id
	 */
	public int updateZt(String sph, String pch, String zt) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("zt", zt);
		int _id = db.update(OfflinePdHelper.TABLE_NAME_C_PDB, values, "sph = ? and pch = ?", new
				String[]{sph,
				pch});
		db.close();
		return _id;
	}

	/**
	 * 更新数据库中盘点状态
	 *
	 * @param id id
	 * @param zt  状态
	 * @return 修改的id
	 */
	public int updateZt(String id, String zt) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("zt", zt);
		int _id = db.update(OfflinePdHelper.TABLE_NAME_C_PDB, values, "_id = ?", new
				String[]{id});
		db.close();
		return _id;
	}

	/**
	 * 更新数据库中盘点状态
	 *
	 * @param list 上传修改列表
	 * @return 上传失败再list中的下标值
	 */
	public List<Integer> updateListZt(List<OfflineBean> list) {
		List<Integer> failId = new ArrayList<>();
		SQLiteDatabase db = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("zt", OfflinePdHelper.SUCCESS);
		int size = list.size();
		for (int i = 0; i < size; i++) {
			int _id = db.update(OfflinePdHelper.TABLE_NAME_C_PDB, values, "sph = ? and pch = ?", new
					String[]{list.get(i).getSph(),
					list.get(i).getPch()});
			if (_id < 0) {
				failId.add(i);
			}
		}
		db.close();
		return failId;
	}


	/**
	 * 查询此批次号和商品号的信息是否存在
	 *
	 * @param sph 商品号
	 * @param pch 批次号
	 * @return 存在返回true，不存在返回false
	 */
	public boolean querySphPch(String sph, String pch, String rq) {
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor = db.query(OfflinePdHelper.TABLE_NAME_C_PDB, new String[]{"_id"}, "sph=? and " +
				"pch=? and rq = ?", new String[]{sph,
				pch,rq}, null, null, null);
		boolean b = !(cursor.getCount() == 0);
		cursor.close();
		db.close();
		return b;
	}

	/**
	 * 查询 此日期之前，某个人的数据数量
	 *
	 * @param rq    日期
	 * @param ry_dm 人员代码
	 * @param db_dm 数据库代码
	 * @return 数量
	 */
	public int queryPdCount(String rq, String ry_dm, String db_dm) {
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor = db.query(OfflinePdHelper.TABLE_NAME_C_PDB, new String[]{"_id"},
				"sjk_dm = ? and ry_dm = ? and (rq between ? and ? )", new
						String[]{db_dm, ry_dm, rq, rq}, null,
				null, null);
		int count = cursor.getCount();
		L.e("数量：" + count);
		cursor.close();
		db.close();

		return count;
	}


	/**
	 * 查询数据
	 *
	 * @param rq	日期
	 * @param ry_dm 人员代码
	 * @param db_dm 数据库代码
	 * @return
	 */
	public List<OfflineBean> queryAllList(String rq, String ry_dm, String db_dm) {
		List<OfflineBean> datas = new ArrayList<>();
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor = db.query(OfflinePdHelper.TABLE_NAME_C_PDB, new String[]{"_id", "sph", "pch", "zt", "ry_dm", "kf", "rq"},
				"sjk_dm = ? and ry_dm = ? and (rq between ? and ? )", new String[]{db_dm, ry_dm,
						rq, rq}, null,
				null, "zt desc");
		while (cursor.moveToNext()) {
			OfflineBean bean = new OfflineBean();
			L.e("_id" + cursor.getColumnIndex("_id") + ",zt" + cursor.getColumnIndex("zt") + "," +
					"sph" + cursor.getColumnIndex("sph"));
			bean.setId(cursor.getLong(cursor.getColumnIndex("_id")));
			bean.setZt(cursor.getString(cursor.getColumnIndex("zt")));
			bean.setSph(cursor.getString(cursor.getColumnIndex("sph")));
			bean.setPch(cursor.getString(cursor.getColumnIndex("pch")));
			bean.setRy_dm(cursor.getString(cursor.getColumnIndex("ry_dm")));
			bean.setKf(cursor.getString(cursor.getColumnIndex("kf")));
			bean.setRq(cursor.getString(cursor.getColumnIndex("rq")));
			datas.add(bean);
		}
		cursor.close();
		db.close();
		return datas;
	}


	/**
	 * 数量数组
	 *
	 * @param rq    日期
	 * @param ry_dm 人员代码
	 * @param db_dm 数据库代码
	 * @return arg[0]为总数，arg[1]为已上传数量
	 */
	public int[] queryCounts(String rq, String ry_dm, String db_dm) {
		int[] arr = new int[2];
		arr[0] = this.queryPdCount(rq, ry_dm, db_dm);
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor = db.query(OfflinePdHelper.TABLE_NAME_C_PDB, new String[]{"_id"},
				"zt = '2' and ry_dm = ? and sjk_dm = ? and (rq between ? and ? )", new String[]{ry_dm, db_dm , rq, rq},
				null,
				null, null);
		arr[1] = cursor.getCount();
		cursor.close();
		db.close();
		return arr;
	}


	/**
	 * 单条删除
	 *
	 * @param sph 商品和
	 * @param pch 批次号
	 * @return 删除id
	 */
	public int deletePchSph(String sph, String pch) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		int _id = db.delete(OfflinePdHelper.TABLE_NAME_C_PDB, "sph = ? and pch = ?", new
				String[]{sph,
				pch});
		db.close();
		return _id;
	}

	/**
	 * 单条删除
	 *
	 * @param id
	 * @return 删除id
	 */
	public int deleteID(String id) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		int _id = db.delete(OfflinePdHelper.TABLE_NAME_C_PDB, "_id = ?", new
				String[]{id});
		db.close();
		return _id;
	}


	/**
	 * 分页查询数据库
	 * @param ry_dm 人员列表
	 * @param db_dm 数据库代码
	 * @param dateStart 开始时间
	 * @param dateEnd 结束时间
	 * @param page 页数(从0开始)
	 * @return OfflineCountListBean
	 */
	public List<OfflineCountListBean> queryCount(String ry_dm, String db_dm, String dateStart, String dateEnd, int
			page) {
		List<OfflineCountListBean> datas = new ArrayList<>();
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select distinct c.rq," +
						"(select count(_id) from c_pdb where sjk=c.sjk and ry=c.ry and rq=c.rq) as " +
				"pds,(select count(_id) from c_pdb where zt='2' and sjk=c.sjk and ry=c.ry and " +
				"rq=c.rq) as scs from c_pdb c "
				+ "where c.ry_dm = ? and c.sjk_dm = ? "
				+ "and (c.rq between ? and ?) "
//				+ "c.rq >= ? and c.rq <= ? "
				+ "order by c.rq desc limit 10 offset ? "
				, new String[]{ry_dm,db_dm,dateStart,dateEnd,10 * page + ""});
		int cursourCount = cursor.getCount();
		if (cursourCount > 0) {
			while (cursor.moveToNext()) {
				String rq = cursor.getString(0);
				int count = cursor.getInt(1);
				int upCount = cursor.getInt(2);
				OfflineCountListBean offlineBean = new OfflineCountListBean(rq, count, upCount);
				datas.add(offlineBean);
			}
		}
		cursor.close();
		db.close();
		return datas;
	}
}
