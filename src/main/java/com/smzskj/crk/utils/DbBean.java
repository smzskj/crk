package com.smzskj.crk.utils;

import java.util.List;

/**
 * Created by ztt on 2017/2/8.
 */

public class DbBean {


	/**
	 * code : 100
	 * rows : [{"db_dm":"dsr_ml","db_mc":"数据库01","db_bh":"tzbh01","db_jc":"01"}]
	 */

	private String code;
	private List<RowsBean> rows;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<RowsBean> getRows() {
		return rows;
	}

	public void setRows(List<RowsBean> rows) {
		this.rows = rows;
	}

	public static class RowsBean {
		/**
		 * db_dm : dsr_ml
		 * db_mc : 数据库01
		 * db_bh : tzbh01
		 * db_jc : 01
		 */

		private String db_dm;
		private String db_mc;
		private String db_bh;
		private String db_jc;

		public String getDb_dm() {
			return db_dm;
		}

		public void setDb_dm(String db_dm) {
			this.db_dm = db_dm;
		}

		public String getDb_mc() {
			return db_mc;
		}

		public void setDb_mc(String db_mc) {
			this.db_mc = db_mc;
		}

		public String getDb_bh() {
			return db_bh;
		}

		public void setDb_bh(String db_bh) {
			this.db_bh = db_bh;
		}

		public String getDb_jc() {
			return db_jc;
		}

		public void setDb_jc(String db_jc) {
			this.db_jc = db_jc;
		}
	}
}
