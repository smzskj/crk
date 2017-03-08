package com.smzskj.crk.bean;

import java.util.List;

/**
 * Created by ztt on 2017/2/6.
 */

public class PdListBean {

	/**
	 * prePage : 1
	 * nextPage : 2
	 * totalCount : 42
	 * page : 1
	 * pageSize : 10
	 * code : 100
	 * totalPage : 5
	 * rows : [{"id":"14864515263680bb4563c-e010-47f9-a2eb-f679a4981317           ","单位":" 台 ",
	 * "编号":"2301012         ","余额":0,"盘点数":0,"单价":4,"库房名称":"电视三楼仓库    ","日期":"2017.02.07",
	 * "商品名称":"格兰仕微波炉D8023TL-                    ","余数":6,"盘点批次号":"
	 * ","批次号":"4                                       "}]
	 */

	private String prePage;
	private String nextPage;
	private String totalCount;
	private String page;
	private String pageSize;
	private String code;
	private String totalPage;
	/**
	 * id : 14864515263680bb4563c-e010-47f9-a2eb-f679a4981317
	 * 单位 :  台
	 * 编号 : 2301012
	 * 余额 : 0
	 * 盘点数 : 0
	 * 单价 : 4
	 * 库房名称 : 电视三楼仓库
	 * 日期 : 2017.02.07
	 * 商品名称 : 格兰仕微波炉D8023TL-
	 * 余数 : 6
	 * 盘点批次号 :
	 * 批次号 : 4
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
		private String id;
		private String 编号;
		private String 单位;
		private int 余额;
		private int 盘点数;
		private int 单价;
		private String 库房名称;
		private String 日期;
		private String 商品名称;
		private int 余数;
		private String 盘点批次号;
		private String 批次号;

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

		public String get编号() {
			return 编号;
		}

		public void set编号(String 编号) {
			this.编号 = 编号;
		}

		public int get余额() {
			return 余额;
		}

		public void set余额(int 余额) {
			this.余额 = 余额;
		}

		public int get盘点数() {
			return 盘点数;
		}

		public void set盘点数(int 盘点数) {
			this.盘点数 = 盘点数;
		}

		public int get单价() {
			return 单价;
		}

		public void set单价(int 单价) {
			this.单价 = 单价;
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

		public String get商品名称() {
			return 商品名称;
		}

		public void set商品名称(String 商品名称) {
			this.商品名称 = 商品名称;
		}

		public int get余数() {
			return 余数;
		}

		public void set余数(int 余数) {
			this.余数 = 余数;
		}

		public String get盘点批次号() {
			return 盘点批次号;
		}

		public void set盘点批次号(String 盘点批次号) {
			this.盘点批次号 = 盘点批次号;
		}

		public String get批次号() {
			return 批次号;
		}

		public void set批次号(String 批次号) {
			this.批次号 = 批次号;
		}
	}
}