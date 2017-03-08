package com.smzskj.crk.bean;

import java.util.List;

/**
 * Created by ztt on 2017/2/9.
 */

public class PdSubBean {


	/**
	 * code : 100
	 * rows : [{"盘点批次号":"11027812101100                          "},{"盘点批次号":"11027812101400
	 * "},{"盘点批次号":"11027812101268"}]
	 */

	private String code;
	/**
	 * 盘点批次号 : 11027812101100
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
		private String 盘点批次号;

		public String get盘点批次号() {
			return 盘点批次号;
		}

		public void set盘点批次号(String 盘点批次号) {
			this.盘点批次号 = 盘点批次号;
		}
	}
}
