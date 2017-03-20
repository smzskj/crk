package com.smzskj.crk.bean;

import java.util.List;

/**
 * Created by ztt on 2017/2/14.
 */

public class OutThZjk {


	/**
	 * code : 100
	 */

	private String code;
	/**
	 * 编号 : 2101100
	 * 商品名称 : 43 东芝彩电
	 */

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
		private String 编号;
		private String 商品名称;

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
