package com.smzskj.crk.bean;

import java.util.List;

/**
 * Created by ztt on 2017/2/9.
 */

public class InDetailBean {


	/**
	 * prePage : 1
	 * nextPage : 1
	 * totalCount : 3
	 * page : 1
	 * pageSize : 10
	 * code : 100
	 * totalPage : 1
	 * rows : [{"入库数":1,"编号":"2101268         ","入库地点":"电视三楼仓库    ","商品名称":"32WL66C 东芝液晶彩电
	 * ","日期":"2017-02-07 00:00:00.0","单据号码":"170200012"},{"入库数":2,"编号":"2101100         ",
	 * "入库地点":"电视三楼仓库    ","商品名称":"43 东芝彩电                             ","日期":"2017-02-07 00:00:00
	 * .0","单据号码":"170200004"},{"入库数":3,"编号":"2101100         ","入库地点":"电视总店仓库    ","商品名称":"43
	 * 东芝彩电                             ","日期":"2017-01-22 00:00:00.0","单据号码":"170100039"}]
	 */

	private String prePage;
	private String nextPage;
	private String totalCount;
	private String page;
	private String pageSize;
	private String code;
	private String totalPage;
	/**
	 * 入库数 : 1
	 * 编号 : 2101268
	 * 入库地点 : 电视三楼仓库
	 * 商品名称 : 32WL66C 东芝液晶彩电
	 * 日期 : 2017-02-07 00:00:00.0
	 * 单据号码 : 170200012
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
		private int 入库数;
		private String 编号;
		private String 入库地点;
		private String 商品名称;
		private String 日期;
		private String 单据号码;
		private String 批次号;

		public String get批次号() {
			return 批次号;
		}

		public void set批次号(String 批次号) {
			this.批次号 = 批次号;
		}

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

		public String get入库地点() {
			return 入库地点;
		}

		public void set入库地点(String 入库地点) {
			this.入库地点 = 入库地点;
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

		public String get单据号码() {
			return 单据号码;
		}

		public void set单据号码(String 单据号码) {
			this.单据号码 = 单据号码;
		}
	}
}
