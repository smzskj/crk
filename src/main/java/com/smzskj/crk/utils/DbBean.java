package com.smzskj.crk.utils;

import java.util.List;

/**
 * Created by ztt on 2017/2/8.
 */

public class DbBean {


	/**
	 * code : 100
	 * rows : [{"db_dm":"dsr_ml","db_mc":"数据库01"}]
	 */

	private String code;
	/**
	 * db_dm : dsr_ml
	 * db_mc : 数据库01
	 */

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
		private String db_dm;
		private String db_mc;

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
	}
}
