package com.smzskj.crk.offline.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smzskj.crk.bean.InSbmBean;
import com.smzskj.crk.offline.bean.OfflineInListBean;
import com.smzskj.crk.offline.bean.OfflineInUpBean;
import com.smzskj.crk.utils.L;
import com.smzskj.crk.utils.SPUtils;
import com.smzskj.crk.utils.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ztt on 2017/3/10.
 * <p>
 * 离线入库数据库操作
 */

public class InDbUtils {

	private OfflinePdHelper mHelper;
	private SPUtils mSp;

	public InDbUtils(Context mContext) {
		mHelper = new OfflinePdHelper(mContext);
		mSp = new SPUtils(mContext, UserInfo.SP_USER_INFO);
	}


	/**
	 * 单据号码
	 *
	 * @param djhm  单据号码
	 * @param datas 商品列表
	 */
	public void insertSp(String djhm, String rq, List<InSbmBean> datas) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		String rkdd = mSp.getString(UserInfo.SP_CK_NAME, "");
		String rkdd_dm = mSp.getString(UserInfo.SP_CK_CODE, "");
		String zdr = mSp.getString(UserInfo.SP_USER_NAME, "");
		String zdr_dm = mSp.getString(UserInfo.SP_USER_CODE, "");
		String sjk = mSp.getString(UserInfo.SP_DB_NAME, "");
		String sjk_dm = mSp.getString(UserInfo.SP_DB_CODE, "");
		ContentValues values = new ContentValues();
		values.put("djhm", djhm);
		values.put("rkdd", rkdd);
		values.put("rkdd_dm", rkdd_dm);
		values.put("zdr", zdr);
		values.put("zdr_dm", zdr_dm);
		values.put("sjk", sjk);
		values.put("sjk_dm", sjk_dm);
		values.put("rq", rq);
		values.put("zt", "未上传");

		for (InSbmBean bean : datas) {
			values.put("bh", bean.getSph());
			values.put("spmc", bean.getSpm());
			values.put("dw", bean.getDw());
			for (String pch : bean.getSbmList()) {
				values.put("pch", pch);
				long id = database.insert(OfflinePdHelper.TABLE_NAME_LXRK, null, values);
				L.e("插入成功：" + id);
			}
		}
		database.close();
	}

	/**
	 * 分页查询商品，离线上传列表数据集合
	 *
	 * @param zdr_dm    制单人代码
	 * @param sjk_dm    数据库代码
	 * @param rkdd_dm   入库地点代码
	 * @param timeStart 开始时间
	 * @param timeEnd   结束时间
	 * @param page      当前页数
	 * @return 数据信息
	 */
	public List<OfflineInListBean> queryInList(String zdr_dm, String sjk_dm,
											   String rkdd_dm, String timeStart,
											   String timeEnd, int page) {
		List<OfflineInListBean> list = new ArrayList<>();
		SQLiteDatabase database = mHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select  djhm , rq , zt , count(djhm)" +
						"from lxrk where zdr_dm = ? and sjk_dm = ? "
						+ "and (rq between ? and ?)  group by djhm , rq , zt order by djhm desc limit 10 offset ?"
				, new String[]{zdr_dm, sjk_dm, timeStart, timeEnd, page * 10 + ""});
		int cursourCount = cursor.getCount();
		L.e("cursourCount" + cursourCount);
		if (cursourCount > 0) {
			while (cursor.moveToNext()) {
				String djhm = cursor.getString(0);
				String rq = cursor.getString(1);
				String zt = cursor.getString(2);
				int count = cursor.getInt(3);
				OfflineInListBean inListBean = new OfflineInListBean();
				inListBean.setCount(count);
				inListBean.setRq(rq);
				inListBean.setZt(zt);
				inListBean.setDjhm(djhm);
				list.add(inListBean);
			}
		}
		cursor.close();
		database.close();
		return list;
	}


	/**
	 * 查询单据号下面所有批次号信息
	 *
	 * @param djhm    单据号码 （只用单据号即可，单据号相同，数据库人员库房都相同）
	 * @param sjk_dm  数据库代码
	 * @param rkdd_dm 入库地点代码
	 * @return 显示列表
	 */
	public List<OfflineInUpBean> queryUpListBean(String djhm, String sjk_dm, String rkdd_dm) {
		List<OfflineInUpBean> list = new ArrayList<>();
		SQLiteDatabase database = mHelper.getWritableDatabase();
		Cursor cursor = database.query(OfflinePdHelper.TABLE_NAME_LXRK, new String[]{"bh", "dw", "pch", "zt"},
				"djhm = ? and sjk_dm = ? and rkdd_dm = ?", new String[]{djhm, sjk_dm, rkdd_dm}, null, null, null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				String bh = cursor.getString(0);
				String dw = cursor.getString(1);
				String pch = cursor.getString(2);
				String zt = cursor.getString(3);
				OfflineInUpBean bean = new OfflineInUpBean();
				bean.setBh(bh);
				bean.setDw(dw);
				bean.setPch(pch);
				bean.setZt(zt);
				list.add(bean);
			}
		}
		cursor.close();
		database.close();
		return list;
	}


	/**
	 * 查询上传数据，
	 *
	 * @param djhm   单据号码 （只用单据号码也可以）
	 * @param sjk_dm 数据库代码 （多余，写上了）
	 * @return 上传数据
	 */
	public List<InSbmBean> queryUpBean(String djhm, String sjk_dm) {
		List<String> spbhList = new ArrayList<>();
		List<InSbmBean> list = new ArrayList<>();
		SQLiteDatabase database = mHelper.getWritableDatabase();

		/*
		查询 此单号下面的所有商品的单号，DISTINCT去重
		 */
		Cursor cursorSp = database.rawQuery("SELECT DISTINCT bh FROM lxrk WHERE djhm = ? AND sjk_dm = ?",
				new String[]{djhm, sjk_dm});
		while (cursorSp.moveToNext()) {
			spbhList.add(cursorSp.getString(0));
		}
		cursorSp.close();

		/*
		通过商品号和单据号分别查询商品批次号
		 */
		for (String spbh : spbhList) {
			Cursor cursor = database.query(OfflinePdHelper.TABLE_NAME_LXRK, new String[]{"dw", "pch", "spmc"},
					"djhm = ? and sjk_dm = ? and bh = ?", new String[]{djhm, sjk_dm, spbh}, null, null, null);
			if (cursor.getCount() > 0) {
				InSbmBean bean = new InSbmBean();

				List<String> pchList = new ArrayList<>();
				/*
				循环将批次号添加进来
				 */
				while (cursor.moveToNext()) {
					String pch = cursor.getString(1);
					pchList.add(pch);
				}
				/*
				如果批次号数量大于0，读取商品信息，并添加到列表中
				 */
				if (pchList.size() > 0) {
					bean.setSbmList(pchList);

					cursor.moveToFirst();
					bean.setSph(spbh);
					String dw = cursor.getString(0);
					String spmc = cursor.getString(2);
					bean.setDw(dw);
					bean.setSpm(spmc);

					list.add(bean);
				}
			}
			cursor.close();
		}
		database.close();
		return list;
	}

	/**
	 * 更新单据号码的状态
	 *
	 * @param djhm 单据号码
	 * @param zt   状态
	 */
	public void updateDjhmZt(String djhm, String zt) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("zt", zt);
		database.update(OfflinePdHelper.TABLE_NAME_LXRK, values, "djhm = ?", new String[]{djhm});
		database.close();
	}


	/**
	 * 删除单据号码下面的批次号
	 * @param djhm 单据号码
	 * @param pch 批次号
	 */
	public void deleteDjhmPch(String djhm, String pch) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		database.delete(OfflinePdHelper.TABLE_NAME_LXRK, "djhm = ? and pch = ?", new String[]{djhm, pch});
		database.close();
	}

	/**
	 * 删除单据号码下面的所有数据
	 *
	 * @param djhm 删除单据号码
	 */
	public void deleteDh(String djhm) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		database.delete(OfflinePdHelper.TABLE_NAME_LXRK, "djhm = ?", new String[]{djhm});
		database.close();
	}
}
