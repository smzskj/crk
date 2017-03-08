package com.smzskj.crk.bean;

/**
 * Created by ztt on 2017/1/19.
 */

public class OutPchBean {

	/**
	 * res : true
	 * reason :
	 * code : 100
	 * rows : {"ckz":"{\"code\":\"100\",\"rows\":[{\"编号\":\"2101046         \",\"单据数量\":0,
	 * \"商品名称\":\"29JF5C 东芝彩电                         \"}]}","zjk":"{\"code\":\"100\",
	 * \"rows\":[]}","exist":"true"}
	 */

	private String res;
	private String reason;
	private String code;
	/**
	 * ckz : {"code":"100","rows":[{"编号":"2101046         ","单据数量":0,"商品名称":"29JF5C 东芝彩电
	 * "}]}
	 * zjk : {"code":"100","rows":[]}
	 * exist : true
	 */

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
		private String ckz;
		private String zjk;
		private String zjk_n;
		private String exist;
		private String th_spycz;

		public String getTh_spycz() {
			return th_spycz;
		}

		public void setTh_spycz(String th_spycz) {
			this.th_spycz = th_spycz;
		}

		public String getCkz() {
			return ckz;
		}

		public void setCkz(String ckz) {
			this.ckz = ckz;
		}

		public String getZjk() {
			return zjk;
		}

		public void setZjk(String zjk) {
			this.zjk = zjk;
		}

		public String getExist() {
			return exist;
		}

		public void setExist(String exist) {
			this.exist = exist;
		}

		public String getZjk_n() {
			return zjk_n;
		}

		public void setZjk_n(String zjk_n) {
			this.zjk_n = zjk_n;
		}
	}
}
