package com.smzskj.crk.bean;

/**
 * Created by ztt on 2017/3/17.
 *
 * 出库退货
 */

public class OutThBean {


	/**
	 * res : true
	 * reason :
	 * code : 100
	 * rows : {"zjk_n":"{\"code\":\"100\",\"rows\":[]}","th_spycz":"{\"code\":\"100\",\"rows\":[]}","zjk":"{\"code\":\"100\",\"rows\":[]}"}
	 */

	private String res;
	private String reason;
	private String code;
	private RowsBean rows;

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public RowsBean getRows() {
		return rows;
	}

	public void setRows(RowsBean rows) {
		this.rows = rows;
	}

	public static class RowsBean {
		/**
		 * zjk_n : {"code":"100","rows":[]}
		 * th_spycz : {"code":"100","rows":[]}
		 * zjk : {"code":"100","rows":[]}
		 */

		private String zjk_n;
		private String th_spycz;
		private String zjk;

		public String getZjk_n() {
			return zjk_n;
		}

		public void setZjk_n(String zjk_n) {
			this.zjk_n = zjk_n;
		}

		public String getTh_spycz() {
			return th_spycz;
		}

		public void setTh_spycz(String th_spycz) {
			this.th_spycz = th_spycz;
		}

		public String getZjk() {
			return zjk;
		}

		public void setZjk(String zjk) {
			this.zjk = zjk;
		}
	}
}
