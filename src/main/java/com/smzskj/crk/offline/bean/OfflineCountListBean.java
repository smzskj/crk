package com.smzskj.crk.offline.bean;

/**
 * Created by ztt on 2017/2/27.
 */

public class OfflineCountListBean {

	private String rq;
	private int count;
	private int upCount;

	public OfflineCountListBean(String rq, int count, int upCount) {
		this.rq = rq;
		this.count = count;
		this.upCount = upCount;
	}

	public String getRq() {
		return rq;
	}

	public void setRq(String rq) {
		this.rq = rq;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getUpCount() {
		return upCount;
	}

	public void setUpCount(int upCount) {
		this.upCount = upCount;
	}
}
