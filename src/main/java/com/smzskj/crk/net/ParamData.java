package com.smzskj.crk.net;
public class ParamData<T> {
	private Object data;
	private Class<T> type;
	public ParamData(Class<T> type,Object data) {
		this.type = type;
		this.data = data;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Class<T> getType() {
		return type;
	}
	public void setType(Class<T> type) {
		this.type = type;
	}
}
