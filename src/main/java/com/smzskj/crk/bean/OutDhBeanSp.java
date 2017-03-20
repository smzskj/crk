package com.smzskj.crk.bean;

import java.util.List;

/**
 * Created by ztt on 2017/3/17.
 */

public class OutDhBeanSp {

	/**
	 * code : 100
	 * rows : [{"单位":"台","编号":"2101046","商品名称":"29JF5C东芝彩电"},{"单位":"台","编号":"2101100","商品名称":"43东芝彩电"}]
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
		 * 单位 : 台
		 * 编号 : 2101046
		 * 商品名称 : 29JF5C东芝彩电
		 */

		private String 单位;
		private String 编号;
		private String 商品名称;

		public String get单位() {
			return 单位;
		}

		public void set单位(String 单位) {
			this.单位 = 单位;
		}

		public String get编号() {
			return 编号;
		}

		public void set编号(String 编号) {
			this.编号 = 编号;
		}

		public String get商品名称() {
			return 商品名称;
		}

		public void set商品名称(String 商品名称) {
			this.商品名称 = 商品名称;
		}
	}
}
