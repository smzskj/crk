package com.smzskj.crk.bean;

import java.util.List;

/**
 * Created by ztt on 2017/1/17.
 */

public class OutBean {


	/**
	 * prePage : 1
	 * nextPage : 1
	 * totalCount : 2
	 * page : 1
	 * pageSize : 10
	 * code : 100
	 * totalPage : 1
	 * rows : [{"单位":" 套 ","编号":"2102310         ","库房名称":"电视YJ                          ",
	 * "商品名称":"TY-S42PX60W松下机柜                     ","日期":"2008-01-17 00:00:00.0","提货地点":"电视YJ
	 * ","摘要":"销售","数量":1,"单据号码":"080103554","批次号":"                "},{"单位":" 台 ","编号":"2101046
	 * ","库房名称":"电视YJ                          ","商品名称":"29JF5C 东芝彩电                         ",
	 * "日期":"2008-01-17 00:00:00.0","提货地点":"电视YJ          ","摘要":"销售","数量":1,"单据号码":"\\        ",
	 * "批次号":"                "}]
	 */

	private String prePage;
	private String nextPage;
	private String totalCount;
	private String page;
	private String pageSize;
	private String code;
	private String totalPage;
	/**
	 * 单位 :  套
	 * 编号 : 2102310
	 * 库房名称 : 电视YJ
	 * 商品名称 : TY-S42PX60W松下机柜
	 * 日期 : 2008-01-17 00:00:00.0
	 * 提货地点 : 电视YJ
	 * 摘要 : 销售
	 * 数量 : 1
	 * 单据号码 : 080103554
	 * 批次号 :
	 */

	private List<RowsBean> rows;

	public String getPrePage() {
		return prePage;
	}

	public void setPrePage(String prePage) {
		this.prePage = prePage;
	}

	public String getNextPage() {
		return nextPage;
	}

	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}

	public List<RowsBean> getRows() {
		return rows;
	}

	public void setRows(List<RowsBean> rows) {
		this.rows = rows;
	}

	public static class RowsBean {
		private String 单位;
		private String 编号;
		private String 库房名称;
		private String 商品名称;
		private String 日期;
		private String 提货地点;
		private String 摘要;
		private int 数量;
		private String 单据号码;
		private String 批次号;

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

		public String get库房名称() {
			return 库房名称;
		}

		public void set库房名称(String 库房名称) {
			this.库房名称 = 库房名称;
		}

		public String get商品名称() {
			return 商品名称;
		}

		public void set商品名称(String 商品名称) {
			this.商品名称 = 商品名称;
		}

		public String get日期() {
			return 日期;
		}

		public void set日期(String 日期) {
			this.日期 = 日期;
		}

		public String get提货地点() {
			return 提货地点;
		}

		public void set提货地点(String 提货地点) {
			this.提货地点 = 提货地点;
		}

		public String get摘要() {
			return 摘要;
		}

		public void set摘要(String 摘要) {
			this.摘要 = 摘要;
		}

		public int get数量() {
			return 数量;
		}

		public void set数量(int 数量) {
			this.数量 = 数量;
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
