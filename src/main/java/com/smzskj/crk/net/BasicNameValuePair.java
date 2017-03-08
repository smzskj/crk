package com.smzskj.crk.net;

/**
 * Created by ztt on 2016/12/13.
 * <p>
 * 键值对，键非空
 */

public class BasicNameValuePair {

	private final String name;
	private final String value;

	public BasicNameValuePair(String name, String value) {
		if (name == null) {
			throw new IllegalArgumentException("Name may not be null");
		} else {
			this.name = name;
			this.value = value;
		}
	}

	public String getName() {
		return this.name;
	}

	public String getValue() {
		return this.value;
	}

	public String toString() {
		if (this.value == null) {
			return this.name;
		} else {
			int len = this.name.length() + 1 + this.value.length();
			return this.name +
					"=" +
					this.value;
		}
	}

}
