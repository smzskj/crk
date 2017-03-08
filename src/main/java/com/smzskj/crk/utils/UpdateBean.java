package com.smzskj.crk.utils;

import java.util.List;

/**
 * Created by ztt on 2017/2/14.
 */

public class UpdateBean {

	/**
	 * code : 100
	 * rows : [{"rq":"2017-02-13 16:45:50.477","vname":"","vcode":2}]
	 */

	private String code;
	/**
	 * rq : 2017-02-13 16:45:50.477
	 * vname :
	 * vcode : 2
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
		private String rq;
		private String vname;
		private int vcode;

		public String getRq() {
			return rq;
		}

		public void setRq(String rq) {
			this.rq = rq;
		}

		public String getVname() {
			return vname;
		}

		public void setVname(String vname) {
			this.vname = vname;
		}

		public int getVcode() {
			return vcode;
		}

		public void setVcode(int vcode) {
			this.vcode = vcode;
		}
	}
}
