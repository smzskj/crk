package com.smzskj.crk.bean;

/**
 * Created by ztt on 2017/1/18.
 */

public class RkDhBean {


	/**
	 * res : true
	 * reason : 获取单号成功
	 * code : 100
	 * rows : {"dh":"170100004"}
	 */

	private String res;
	private String reason;
	private String code;
	/**
	 * dh : 170100004
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
		private String dh;

		public String getDh() {
			return dh;
		}

		public void setDh(String dh) {
			this.dh = dh;
		}
	}
}
