package com.smzskj.crk.offline.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smzskj.crk.bean.OutSbmBean;
import com.smzskj.crk.offline.bean.OfflineInUpBean;
import com.smzskj.crk.offline.bean.OfflineOutListBean;
import com.smzskj.crk.utils.L;
import com.smzskj.crk.utils.SPUtils;
import com.smzskj.crk.utils.UserInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ztt on 2017/3/10.
 * <p>
 * 离线入库数据库操作
 */

public class OutDbUtils {

	private OfflinePdHelper mHelper;
	private SPUtils mSp;

	public OutDbUtils(Context mContext) {
		mHelper = new OfflinePdHelper(mContext);
		mSp = new SPUtils(mContext, UserInfo.SP_USER_INFO);
	}


	/**
	 * 单据号码
	 *
	 * @param djhm 单据号码
	 * @param data 商品列表
	 */
	public void insertSp(String djhm, String rq, OutSbmBean data) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		String ckdd = mSp.getString(UserInfo.SP_CK_NAME, "");
		String ckdd_dm = mSp.getString(UserInfo.SP_CK_CODE, "");
		String zdr = mSp.getString(UserInfo.SP_USER_NAME, "");
		String zdr_dm = mSp.getString(UserInfo.SP_USER_CODE, "");
		String sjk = mSp.getString(UserInfo.SP_DB_NAME, "");
		String sjk_dm = mSp.getString(UserInfo.SP_DB_CODE, "");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		String sj = simpleDateFormat.format(new Date());
		ContentValues values = new ContentValues();
		values.put("djhm", djhm);
		values.put("ckdd", ckdd);
		values.put("ckdd_dm", ckdd_dm);
		values.put("zdr", zdr);
		values.put("zdr_dm", zdr_dm);
		values.put("sjk", sjk);
		values.put("sjk_dm", sjk_dm);
		values.put("rq", rq);
		values.put("zt", "未上传");
		values.put("ztcode", 9);
		values.put("bh", data.getSph());
		values.put("spmc", data.getSpm());
		values.put("dw", data.getDw());
		values.put("sj", sj);
		for (String pch : data.getSbmList()) {
			values.put("pch", pch);
			long id = database.insert(OfflinePdHelper.TABLE_NAME_LXCK, null, values);
			L.e("插入成功：" + id);
		}
		database.close();
	}

	/**
	 * 分页查询商品，离线上传列表数据集合
	 *
	 * @param zdr_dm    制单人代码
	 * @param sjk_dm    数据库代码
	 * @param ckdd_dm   入库地点代码
	 * @param timeStart 开始时间
	 * @param timeEnd   结束时间
	 * @param page      当前页数
	 * @return 数据信息
	 */
	public List<OfflineOutListBean> queryInList(String zdr_dm, String sjk_dm,
												String ckdd_dm, String timeStart,
												String timeEnd, int page) {
		List<OfflineOutListBean> list = new ArrayList<>();
		SQLiteDatabase database = mHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select  djhm , rq , zt , count(djhm) , sj " +
						"from lxck where zdr_dm = ? and sjk_dm = ? "
						+ "and (rq between ? and ?)  group by djhm , rq ,sj order by ztcode desc, sj desc , djhm desc limit 10 offset ?"
				, new String[]{zdr_dm, sjk_dm, timeStart, timeEnd, page * 10 + ""});
		int cursourCount = cursor.getCount();
		L.e("cursourCount" + cursourCount);
		if (cursourCount > 0) {
			while (cursor.moveToNext()) {
				String djhm = cursor.getString(0);
				String rq = cursor.getString(1);
				String zt = cursor.getString(2);
				int count = cursor.getInt(3);
				String sj = cursor.getString(4);
				OfflineOutListBean outListBean = new OfflineOutListBean();
				outListBean.setCount(count);
				outListBean.setRq(rq);
				outListBean.setZt(zt);
				outListBean.setDjhm(djhm);
				outListBean.setSj(sj);
				list.add(outListBean);
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
	 * @param ckdd_dm 入库地点代码
	 * @return 显示列表
	 */
	public List<OfflineInUpBean> queryUpListBean(String djhm, String sjk_dm, String ckdd_dm, String sj) {
		List<OfflineInUpBean> list = new ArrayList<>();
		SQLiteDatabase database = mHelper.getWritableDatabase();
		Cursor cursor = database.query(OfflinePdHelper.TABLE_NAME_LXCK, new String[]{"bh", "dw", "pch", "zt"},
				"djhm = ? and sjk_dm = ? and ckdd_dm = ? and sj = ?", new String[]{djhm, sjk_dm, ckdd_dm, sj}, null, null, null);
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
	 * 更新单据号码的状态
	 *
	 * @param djhm 单据号码
	 * @param zt   状态
	 */
	public void updateDjhmZt(String djhm, String zt, String sj,int ztcode) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("zt", zt);
		values.put("ztcode", ztcode);
		database.update(OfflinePdHelper.TABLE_NAME_LXCK, values, "djhm = ? and sj = ?", new String[]{djhm, sj});
		database.close();
	}


	public void deleteDjhmPch(String djhm, String pch, String sj) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		database.delete(OfflinePdHelper.TABLE_NAME_LXCK, "djhm = ? and pch = ? and sj = ?", new String[]{djhm, pch, sj});
		database.close();
	}

	public void deleteDjhmPch(String djhm, String sj) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		database.delete(OfflinePdHelper.TABLE_NAME_LXCK, "djhm = ? and sj = ?", new String[]{djhm, sj});
		database.close();
	}
}
