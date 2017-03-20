package com.smzskj.crk.offline.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smzskj.crk.offline.bean.OfflineSpBean;
import com.smzskj.crk.utils.L;

import java.util.ArrayList;
import java.util.List;

import static com.smzskj.crk.offline.db.OfflinePdHelper.TABLE_NAME_LXSP;

/**
 * Created by ztt on 2017/2/28.
 */

public class SpDbUtils {

	private Context mContext;
	private OfflinePdHelper mHelper;

	public SpDbUtils(Context context) {
		this.mContext = context;
		this.mHelper = new OfflinePdHelper(mContext);
	}

	/**
	 * 删除所有数据
	 */
	public int deleteSp() {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		int id = db.delete(TABLE_NAME_LXSP, null, null);
		db.close();
		L.e("删除商品："+id);
		return id;
	}

	/**
	 * 插入商品
	 *
	 * @param list 商品列表
	 */
	public void insertSpList(List<OfflineSpBean.RowsBean> list) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		for (OfflineSpBean.RowsBean bean : list) {
			ContentValues values = new ContentValues();
			values.put("bh", bean.get编号().trim());
			values.put("spmc", bean.get商品名称().trim());
			values.put("txm", bean.get条形码().trim());
			values.put("dw", bean.get单位().trim());
			long id = db.insert(TABLE_NAME_LXSP, null, values);
			L.e("插入商品：" + id);
		}
		db.close();
	}

	/**
	 * @param bh 编号
	 * @return 商品号
	 */
	public List<OfflineSpBean.RowsBean> querySpList(String bh , int page) {
		List<OfflineSpBean.RowsBean> list = new ArrayList<>();
		SQLiteDatabase db = mHelper.getWritableDatabase();
//		Cursor cursor = db.query(TABLE_NAME_LXSP, null, "bh + txm like '%" +bh+ "%' limit 10 offset ?", new String[]{page * 10 + ""},
//				null, null, "bh");
		Cursor cursor = db.rawQuery("SELECT * FROM lxsp WHERE bh || txm like '%" +
				bh + "%' ORDER BY bh limit 10 offset ? ",new String[]{page * 10 + ""});
		L.e("商品数量：" + cursor.getCount());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				OfflineSpBean.RowsBean rowsBean = new OfflineSpBean.RowsBean();
				rowsBean.set单位(cursor.getString(cursor.getColumnIndex("dw")));
				rowsBean.set商品名称(cursor.getString(cursor.getColumnIndex("spmc")));
				rowsBean.set条形码(cursor.getString(cursor.getColumnIndex("txm")));
				rowsBean.set编号(cursor.getString(cursor.getColumnIndex("bh")));
				list.add(rowsBean);
			}
		}
		cursor.close();
		db.close();
		return list;
	}
}
