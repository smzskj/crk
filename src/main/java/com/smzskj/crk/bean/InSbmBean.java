package com.smzskj.crk.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ztt on 2017/3/9.
 *
 * 入库，切换商品信息
 */

public class InSbmBean {

	/**
	 * 商品号
	 */
	private String sph = "";
	/**
	 * 商品码
	 */
	private String spm = "";
	/**
	 * 单位
	 */
	private String dw = "";
	/**
	 * 识别码，批次号列表
	 */
	private List<String> sbmList = new ArrayList<>();


	public String getSph() {
		return sph;
	}

	public void setSph(String sph) {
		this.sph = sph;
	}

	public String getSpm() {
		return spm;
	}

	public void setSpm(String spm) {
		this.spm = spm;
	}

	public String getDw() {
		return dw;
	}

	public void setDw(String dw) {
		this.dw = dw;
	}

	public List<String> getSbmList() {
		return sbmList;
	}

	public void setSbmList(List<String> sbmList) {
		this.sbmList = sbmList;
	}
}
