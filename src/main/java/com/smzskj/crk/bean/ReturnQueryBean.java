package com.smzskj.crk.bean;

/**
 * Created by ztt on 2017/1/17.
 */

public class ReturnQueryBean {

	/**
	 * prePage : 1
	 * nextPage : 1
	 * totalCount :
	 * page : 1
	 * pageSize : 10
	 * code : 100
	 * totalPage : 0
	 * rows :
	 */

	private String prePage;
	private String nextPage;
	private String totalCount;
	private String page;
	private String pageSize;
	private String code;
	private String totalPage;
	private String rows;

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

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}
}
