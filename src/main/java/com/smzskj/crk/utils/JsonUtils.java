package com.smzskj.crk.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 解析json类
 * @date 2015-12-11
 */
public class JsonUtils {

	/**
	 * 获取JSON解析后的数据
	 * 
	 * @param result
	 * @param c
	 * @return
	 */
	public static <T> T getJsonParseObject(String result, Class<T> c) {
		T data;
		try {
			data = JSON.parseObject(result, c);
		} catch (Exception e) {
			return null;
		}

		return data;
	}

	public static <T> List<T> getJsonParseArray(String result, Class<T> c) {
		List<T> list;
		try {
			list = JSON.parseArray(result, c);
		} catch (Exception e) {
			return null;
		}

		return list;
	}

	public static   String getJsonString( Object o) {
		String s = "";
		try {
			 s = JSON.toJSONString(o);
		} catch (Exception e) {
			return null;
		}
		return s;
	}

	public static <k,v>String getJsonByMap(Map<k,v> map) {

		String    jsonString = JSON.toJSONString(map);

		return jsonString;
	}

	public static <k>String getJsonByList(ArrayList<k> list) {

		String    jsonString = JSON.toJSONString(list);

		return jsonString;
	}

	public static <k,v>  Map<k,v> getMapWithClassByJson(String jsonString , Class<k> key, Class<v> value) {

		return JSON.parseObject(jsonString , new TypeReference<Map<k, v>>(){});
	}

	public static <k>  ArrayList<k> getListWithClassByJson(String jsonString ) {

		return JSON.parseObject(jsonString , new TypeReference<ArrayList<k>>(){});
	}
	
	
//	public static <k>  ArrayList<k> getListWithClassByJsonArrayString(String jsonArrayString ) {
//		JSONArray jsonArray = JSON.parseArray(jsonArrayString);
//		List<k> list = new ArrayList<k>();
//		for (int i = 0; i < jsonArray.size(); i++) {
//			
//			list JSOn.
//		}
//		return JSON.parseObject(jsonString , new TypeReference<ArrayList<k>>(){});
//	}

}
