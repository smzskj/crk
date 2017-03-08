package com.smzskj.crk.bean;

/**
 * Created by ztt on 2017/2/6.
 */

public class PdCheckBean {


	/**
	 * res : false
	 * reason : 已经存在:2017.02.06的盘点表，可以继续录入或者重新建立盘点表!如果要重新建立,原来的盘点表将作废,真要重新建立盘点表吗？
	 * code : 100
	 * rows : isCz
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
