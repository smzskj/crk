package com.smzskj.crk.bean;

/**
 * Created by ztt on 2017/1/18.
 */

public class InRkBean {

	/**
	 * res : false
	 * reason : 入库失败
	 * code : 100
	 * rows :
	 */

	private String res;
	private String reason;
	private String code;
	private String rows;

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

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}
}
