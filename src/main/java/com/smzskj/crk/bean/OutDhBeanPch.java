package com.smzskj.crk.bean;

import java.util.List;

/**
 * Created by ztt on 2017/3/17.
 */

public class OutDhBeanPch {
	/**
	 * code : 100
	 * rows : [{"入库数":1,"编号":"2101046         ","出库数":-1,"商品名称":"29JF5C 东芝彩电                         ","售价":0,"摘要":"入库","id":"20170315082939209410|潘燕                                   ","单位":"台  ","登帐":" ","单位名称":"                                        ","库房名称":"电视三楼仓库    ","日期":"2017-03-15 00:00:00.0","单据号码":"170300042","批次号":"                "},{"入库数":1,"编号":"2101100         ","出库数":-1,"商品名称":"43 东芝彩电                             ","售价":0,"摘要":"入库","id":"201703150829391794F3|潘燕                                   ","单位":"台  ","登帐":" ","单位名称":"                                        ","库房名称":"电视三楼仓库    ","日期":"2017-03-15 00:00:00.0","单据号码":"170300042","批次号":"                "}]
	 */

	private String code;
	private List<RowsBean> rows;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<RowsBean> getRows() {
		return rows;
	}

	public void setRows(List<RowsBean> rows) {
		this.rows = rows;
	}

	public static class RowsBean {
		/**
		 * 入库数 : 1
		 * 编号 : 2101046
		 * 出库数 : -1
		 * 商品名称 : 29JF5C 东芝彩电
		 * 售价 : 0
		 * 摘要 : 入库
		 * id : 20170315082939209410|潘燕
		 * 单位 : 台
		 * 登帐 :
		 * 单位名称 :
		 * 库房名称 : 电视三楼仓库
		 * 日期 : 2017-03-15 00:00:00.0
		 * 单据号码 : 170300042
		 * 批次号 :
		 */

		private int 入库数;
		private String 编号;
		private int 出库数;
		private String 商品名称;
		private int 售价;
		private String 摘要;
		private String id;
		private String 单位;
		private String 登帐;
		private String 单位名称;
		private String 库房名称;
		private String 日期;
		private String 单据号码;
		private String 批次号;

		public int get入库数() {
			return 入库数;
		}

		public void set入库数(int 入库数) {
			this.入库数 = 入库数;
		}

		public String get编号() {
			return 编号;
		}

		public void set编号(String 编号) {
			this.编号 = 编号;
		}

		public int get出库数() {
			return 出库数;
		}

		public void set出库数(int 出库数) {
			this.出库数 = 出库数;
		}

		public String get商品名称() {
			return 商品名称;
		}

		public void set商品名称(String 商品名称) {
			this.商品名称 = 商品名称;
		}

		public int get售价() {
			return 售价;
		}

		public void set售价(int 售价) {
			this.售价 = 售价;
		}

		public String get摘要() {
			return 摘要;
		}

		public void set摘要(String 摘要) {
			this.摘要 = 摘要;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String get单位() {
			return 单位;
		}

		public void set单位(String 单位) {
			this.单位 = 单位;
		}

		public String get登帐() {
			return 登帐;
		}

		public boolean getD登账(){
			return "D".equals(登帐);
		}


		public void set登帐(String 登帐) {
			this.登帐 = 登帐;
		}

		public void setD登帐() {
			this.登帐 = "D";
		}

		public String get单位名称() {
			return 单位名称;
		}

		public void set单位名称(String 单位名称) {
			this.单位名称 = 单位名称;
		}

		public String get库房名称() {
			return 库房名称;
		}

		public void set库房名称(String 库房名称) {
			this.库房名称 = 库房名称;
		}

		public String get日期() {
			return 日期;
		}

		public void set日期(String 日期) {
			this.日期 = 日期;
		}

		public String get单据号码() {
			return 单据号码;
		}

		public void set单据号码(String 单据号码) {
			this.单据号码 = 单据号码;
		}

		public String get批次号() {
			return 批次号;
		}

		public void set批次号(String 批次号) {
			this.批次号 = 批次号;
		}
	}
}