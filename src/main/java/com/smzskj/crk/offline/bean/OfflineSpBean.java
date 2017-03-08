package com.smzskj.crk.offline.bean;

import java.util.List;

/**
 * Created by ztt on 2017/2/28.
 */

public class OfflineSpBean {


	/**
	 * code : 100
	 * rows : [{"单位":"个  ","编号":"00037           ","商品名称":"可爱对杯                                ",
	 * "条形码":"                "}]
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
		 * 单位 : 个
		 * 编号 : 00037
		 * 商品名称 : 可爱对杯
		 * 条形码 :
		 */

		private String 单位;
		private String 编号;
		private String 商品名称;
		private String 条形码;

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

		public String get条形码() {
			return 条形码;
		}

		public void set条形码(String 条形码) {
			this.条形码 = 条形码;
		}
	}
}
