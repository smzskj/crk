package com.smzskj.crk.utils;

public enum  JsonCode  {

	List("List", 101),
	Map("Map", 102),
	Object("Object", 103),
	String("String", 104),
	List_Map("List_Map", 105), ;

	private String name;
	private int code;

	// 构造方法
	private JsonCode(String name, int code) {
		this.name = name;
		this.code = code;
	}

	public static String getCodeName(int code) {

		for (JsonCode upload_code : JsonCode.values()) {
			if(upload_code.code == code){
				return upload_code.name;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}

