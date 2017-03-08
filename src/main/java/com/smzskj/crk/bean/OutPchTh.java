package com.smzskj.crk.bean;

import java.util.List;

/**
 * Created by ztt on 2017/2/20.
 */

public class OutPchTh {


	/**
	 * code : 100
	 * rows : [{"编号":"2101075         "}]
	 */

	private String code;
	/**
	 * 编号 : 2101075
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

		public String get编号() {
			return 编号;
		}

		public void set编号(String 编号) {
			this.编号 = 编号;
		}
	}
}
