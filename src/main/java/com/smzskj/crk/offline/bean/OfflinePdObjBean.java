package com.smzskj.crk.offline.bean;

/**
 * Created by ztt on 2017/2/27.
 */

public class OfflinePdObjBean {


	/**
	 * id : 23
	 * err_code : 8
	 * sub_reason : 离线盘点-商品号或条形码查询商品不存在!
	 */

	private String id;
	private String err_code;
	private String sub_reason;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public String getSub_reason() {
		return sub_reason;
	}

	public void setSub_reason(String sub_reason) {
		this.sub_reason = sub_reason;
	}
}
