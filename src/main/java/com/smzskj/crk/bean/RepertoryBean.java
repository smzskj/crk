package com.smzskj.crk.bean;

/**
 * Created by ztt on 2017/1/17.
 */

public class RepertoryBean {


	/**
	 * 库房号 : 02
	 * 库房名称 : 电视YJ
	 */

	private String 库房号;
	private String 库房名称;

	public RepertoryBean() {
	}

	public RepertoryBean(String 库房号, String 库房名称) {
		this.库房号 = 库房号;
		this.库房名称 = 库房名称;
	}

	public String get库房号() {
		return 库房号;
	}

	public void set库房号(String 库房号) {
		this.库房号 = 库房号;
	}

	public String get库房名称() {
		return 库房名称;
	}

	public void set库房名称(String 库房名称) {
		this.库房名称 = 库房名称;
	}
}
