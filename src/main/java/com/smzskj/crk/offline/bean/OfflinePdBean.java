package com.smzskj.crk.offline.bean;

/**
 * Created by ztt on 2017/2/27.
 */

public class OfflinePdBean {


	/**
	 * res : true
	 * reason : 离线盘点成功!
	 * code : 100
	 * rows : [{"id":"20","err_code":"2","sub_reason":"盘点批次号已经存在,不能重复!"},{"id":"21",
	 * "err_code":"2","sub_reason":"盘点批次号已经存在,不能重复!"},{"id":"22","err_code":"2",
	 * "sub_reason":"盘点批次号已经存在,不能重复!"},{"id":"23","err_code":"8",
	 * "sub_reason":"离线盘点-商品号或条形码查询商品不存在!"}]
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
