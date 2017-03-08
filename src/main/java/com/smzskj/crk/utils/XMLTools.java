package com.smzskj.crk.utils;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class XMLTools {

	public static String GetResoultCode(String Resoult) {
		String resoultcode = "";
		try {

			String front = Resoult.split("</code>")[0];
			String code = front.split("<code>")[1];
			resoultcode = code;
		} catch (Exception e) {
			e.printStackTrace();
//			Log.e("XMLTools:", e.getMessage());
		}
		return resoultcode;
	}

	public static String GetSingleObj(String mark, String Resoult) {
//		Log.i("Resoult", Resoult);
		String text = "";
		try {

			String front = Resoult.split("</" + mark + ">")[0];
			String code = front.split("<" + mark + ">")[1];
			text = code;
		} catch (Exception e) {
			e.printStackTrace();
//			Log.e("XMLTools:", e.getMessage());
		}
		return text;
	}
	
	public static String GetSingleValue(String mark, String result){
		String value = "";
		try {
			String s1 = result.split("<" + mark + ">")[1];
			String s2 = s1.split("</" + mark + ">")[0];
			value = s2;
		} catch (Exception e) {
			value = "";
		}
		return value;
	}
	
	public static List<String > GetObjList( String Resoult) {
//		String text = "";
		List<String> list=new ArrayList<String>();
		try {
			String[] re=Resoult.split("/OBJ");
		if(Resoult.contains("/OBJ")){
			for(int i=0;i<re.length;i++){
				if(re[i].length()>10){
					list.add(re[i]);
				}
			
			}
		}
			

		} catch (Exception e) {
//			Log.e("XMLTools:", e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	public static List<String > GetSubObjList( String Resoult) {
		String text = "";
		List<String> list=new ArrayList<String>();
		try {
			String[] re=Resoult.split("/SUBOBJ");
		if(Resoult.contains("/SUBOBJ")){
			for(int i=0;i<re.length;i++){
				if(re[i].length()>10){
					list.add(re[i]);
				}
			
			}
		}
			

		} catch (Exception e) {
//			Log.e("XMLTools:", e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	
	public static List<String > GetDeviceList( String Resoult) {
		String text = "";
		List<String> list=new ArrayList<String>();
		try {
			String[] re=Resoult.split("/DEVICE");
		if(Resoult.contains("/DEVICE")){
			for(int i=0;i<re.length;i++){
				if(re[i].length()>0 && re[i].contains("DEVICE")){
					list.add(re[i]);
				}
			}
		}
			

		} catch (Exception e) {
//			Log.e("XMLTools:", e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	
	public static List<String > GetDeviceListV2( String Resoult) {
		String text = "";
		List<String> list=new ArrayList<String>();
		try {
			String[] re=Resoult.split("/device");
		if(Resoult.contains("/device")){
			for(int i=0;i<re.length;i++){
				if(re[i].length()>0 && re[i].contains("device")){
					list.add(re[i]);
				}
			}
		}
			

		} catch (Exception e) {
//			Log.e("XMLTools:", e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 解析返回数据
	 * 
	 */
	public static <T> List<T> parseResult(String result , Class<T> cla){
		List<String> listStr = new ArrayList<String>();
		List<T> dataList = new ArrayList<T>();
		Field[] fields = cla.getDeclaredFields();

		listStr.addAll(XMLTools.GetObjList(result));
		for (String string : listStr) {
			try {
				T t = cla.newInstance();
				for (Field field : fields) {
					field.setAccessible(true);
					Class<?> classType = field.getType();
					if (classType == String.class) {
						String fieldName = field.getName();
						Method meth;
						meth = cla.getMethod("set" + firstUpcase(fieldName),field.getType());
						meth.invoke(t, XMLTools.GetSingleValue(fieldName, string));
				    }else {
						continue ;
					}
				}
				dataList.add(t);
			} catch (Exception e) {
				throw new Error("数据解析出现错误");
			}
		}
		return dataList;
	}
	/**
	 */
	public static String firstUpcase(String data) {
		return TextUtils.isEmpty(data) ? "" : data.substring(0,1).toUpperCase(Locale.getDefault())+data.substring(1);
	}
	public static <T> List<T> parseSubResult(String result , Class<T> cla){
		List<String> listStr = new ArrayList<String>();
		List<T> dataList = new ArrayList<T>();
		Field[] fields = cla.getDeclaredFields();
		
		listStr.addAll(XMLTools.GetSubObjList(result));
		for (String string : listStr) {
			try {
				T t = cla.newInstance();
				for (Field field : fields) {
					field.setAccessible(true);
					Class<?> classType = field.getType();
					if (classType == String.class) {
						String fieldName = field.getName();
						Method meth;
						meth = cla.getMethod("set" + firstUpcase(fieldName),field.getType());
						meth.invoke(t, XMLTools.GetSingleValue(fieldName, string));
				    }else {
						continue ;
					}
				}
				dataList.add(t);
			} catch (Exception e) {
				throw new Error("数据解析出现错误");
			}
		}
		return dataList;
	}
	
}
// <?xml version="1.0"
// encoding="UTF-8"?><code>100</code><OBJ><NSRSBH>371325751788249</NSRSBH></OBJ></xml>