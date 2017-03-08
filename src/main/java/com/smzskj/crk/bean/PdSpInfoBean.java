package com.smzskj.crk.bean;

/**
 * Created by ztt on 2017/2/6.
 */

public class PdSpInfoBean {


	/**
	 * res : true
	 * reason : 查询成功
	 * code : 100
	 * rows : {"sub":"{\"code\":\"100\",\"rows\":[{\"盘点批次号\":\"11027812101100
	 * \"},{\"盘点批次号\":\"11027812101400                          \"},{\"盘点批次号\":\"11027812101268
	 * \"}]}","main":"{\"code\":\"100\",\"rows\":[{\"ypdsl\":0,\"单位\":\" 台 \",\"编号\":\"2101100
	 * \",\"zkcsl\":6,\"商品名称\":\"43 东芝彩电                             \"}]}"}
	 */

	private String res;
	private String reason;
	private String code;
	/**
	 * sub : {"code":"100","rows":[{"盘点批次号":"11027812101100                          "},
	 * {"盘点批次号":"11027812101400                          "},{"盘点批次号":"11027812101268
	 * "}]}
	 * main : {"code":"100","rows":[{"ypdsl":0,"单位":" 台 ","编号":"2101100         ","zkcsl":6,
	 * "商品名称":"43 东芝彩电                             "}]}
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
		private String sub;
		private String main;

		public String getSub() {
			return sub;
		}

		public void setSub(String sub) {
			this.sub = sub;
		}

		public String getMain() {
			return main;
		}

		public void setMain(String main) {
			this.main = main;
		}
	}
}
