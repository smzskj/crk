package com.smzskj.crk.bean;

import java.util.List;

/**
 * Created by ztt on 2017/1/18.
 */

public class InSpBean {


	/**
	 * prePage : 1
	 * nextPage : 2
	 * totalCount : 9371
	 * page : 1
	 * pageSize : 10
	 * code : 100
	 * totalPage : 938
	 * rows : [{"单位":"个  ","编号":"001             ","商品名称":"四件套
	 * "},{"单位":"元  ","编号":"1001            ","商品名称":"现金                                    "},
	 * {"单位":" 台 ","编号":"2101046         ","商品名称":"29JF5C 东芝彩电                         "},{"单位":"
	 * 台 ","编号":"2101047         ","商品名称":"29JF8UC 东芝彩电                        "},{"单位":" 台 ",
	 * "编号":"2101049         ","商品名称":"29VH9UC 东芝彩电                        "},{"单位":" 台 ",
	 * "编号":"2101050         ","商品名称":"29SH9UC 东芝彩电11                      "},{"单位":" 台 ",
	 * "编号":"2101051         ","商品名称":"29JH9UC 东芝彩电                        "},{"单位":" 台 ",
	 * "编号":"2101052         ","商品名称":"29CH9UC 东芝彩电                        "},{"单位":" 台 ",
	 * "编号":"2101053         ","商品名称":"29CF58C 东芝彩电                        "},{"单位":" 台 ",
	 * "编号":"2101054         ","商品名称":"29CH7UC 东芝彩电                        "}]
	 */

	private String prePage;
	private String nextPage;
	private String totalCount;
	private String page;
	private String pageSize;
	private String code;
	private String totalPage;
	/**
	 * 单位 : 个
	 * 编号 : 001
	 * 商品名称 : 四件套
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
		private String 商品名称;

		public String get单位() {
			return 单位.trim();
		}

		public void set单位(String 单位) {
			this.单位 = 单位.trim();
		}

		public String get编号() {
			return 编号.trim();
		}

		public void set编号(String 编号) {
			this.编号 = 编号.trim();
		}

		public String get商品名称() {
			return 商品名称.trim();
		}

		public void set商品名称(String 商品名称) {
			this.商品名称 = 商品名称.trim();
		}
	}
}
