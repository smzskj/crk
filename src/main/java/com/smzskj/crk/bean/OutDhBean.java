package com.smzskj.crk.bean;

/**
 * Created by ztt on 2017/3/17.
 */

public class OutDhBean {

	/**
	 * res : true
	 * reason :
	 * code : 100
	 * rows : {"djsl":"-2.00000","res_sp":"{"code":"100","rows":[{"单位":"台  ","编号":"2101046         ","商品名称":"29JF5C 东芝彩电                         "},{"单位":"台  ","编号":"2101100         ","商品名称":"43 东芝彩电                             "}]}","res_pch":"{"code":"100","rows":[{"入库数":1,"编号":"2101046         ","出库数":-1,"商品名称":"29JF5C 东芝彩电                         ","售价":0,"摘要":"入库","id":"20170315082939209410|潘燕                                   ","单位":"台  ","登帐":" ","单位名称":"                                        ","库房名称":"电视三楼仓库    ","日期":"2017-03-15 00:00:00.0","单据号码":"170300042","批次号":"                "},{"入库数":1,"编号":"2101100         ","出库数":-1,"商品名称":"43 东芝彩电                             ","售价":0,"摘要":"入库","id":"201703150829391794F3|潘燕                                   ","单位":"台  ","登帐":" ","单位名称":"                                        ","库房名称":"电视三楼仓库    ","日期":"2017-03-15 00:00:00.0","单据号码":"170300042","批次号":"                "}]}"}
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
		 * djsl : -2.00000
		 * res_sp : {"code":"100","rows":[{"单位":"台  ","编号":"2101046         ","商品名称":"29JF5C 东芝彩电                         "},{"单位":"台  ","编号":"2101100         ","商品名称":"43 东芝彩电                             "}]}
		 * res_pch : {"code":"100","rows":[{"入库数":1,"编号":"2101046         ","出库数":-1,"商品名称":"29JF5C 东芝彩电                         ","售价":0,"摘要":"入库","id":"20170315082939209410|潘燕                                   ","单位":"台  ","登帐":" ","单位名称":"                                        ","库房名称":"电视三楼仓库    ","日期":"2017-03-15 00:00:00.0","单据号码":"170300042","批次号":"                "},{"入库数":1,"编号":"2101100         ","出库数":-1,"商品名称":"43 东芝彩电                             ","售价":0,"摘要":"入库","id":"201703150829391794F3|潘燕                                   ","单位":"台  ","登帐":" ","单位名称":"                                        ","库房名称":"电视三楼仓库    ","日期":"2017-03-15 00:00:00.0","单据号码":"170300042","批次号":"                "}]}
		 */

		private String djsl;
		private String res_sp;
		private String res_pch;

		public String getDjsl() {
			return djsl;
		}

		public void setDjsl(String djsl) {
			this.djsl = djsl;
		}

		public String getRes_sp() {
			return res_sp;
		}

		public void setRes_sp(String res_sp) {
			this.res_sp = res_sp;
		}

		public String getRes_pch() {
			return res_pch;
		}

		public void setRes_pch(String res_pch) {
			this.res_pch = res_pch;
		}
	}

}