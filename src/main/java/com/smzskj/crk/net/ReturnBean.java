package com.smzskj.crk.net;

/**
 * Created by ztt on 2017/1/16.
 *
 * 联网请求返回
 */

public class ReturnBean {


	/**
	 * res : false
	 * reason : 用户名或密码错误
	 * code : 100
	 * rows : {}
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


	public boolean checkCode(){
		return "100".equals(code);
	}

	public boolean checkRes(){
		return "true".equals(res);
	}
}
