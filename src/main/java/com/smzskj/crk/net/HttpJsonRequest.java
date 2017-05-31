package com.smzskj.crk.net;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.smzskj.crk.utils.AESUtils;
import com.smzskj.crk.utils.Constants;
import com.smzskj.crk.utils.JsonCode;
import com.smzskj.crk.utils.L;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.EOFException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;


public class HttpJsonRequest implements Runnable {

	public static String db_dm = "";

	public static final int DID_START = 0;
	public static final int DID_ERROR = 1;
	public static final int DID_SUCCEED = 2;

	private CallbackListener listener;
	private String service;
	private String method;
	private String[] args;

	/** 异常重连次数 */
	private int size = 0;

	public HttpJsonRequest(CallbackListener listener, String service, String method, String...
			args) {
		this.listener = listener;
		this.service = service;
		this.method = method;
		this.args = args;
	}

	@Override
	public void run() {
		L.e(Thread.currentThread().getName());

		handler.sendMessage(Message.obtain(handler, HttpJsonRequest.DID_START));
		HttpTransportSE ht = null;
		try {
			/*
				JSON参数拼接
			 */
			List<ParamData<?>> params = new ArrayList<ParamData<?>>();
			JSONObject jreq = new JSONObject();
			jreq.put("rType", JsonCode.String.getName());
			jreq.put("direct", true);
			jreq.put("service", service);
			jreq.put("method", method);
			jreq.put("zq", 2 * 60 * 1000 );
			jreq.put("curMillis", System.currentTimeMillis());
			JSONArray jparams = new JSONArray();
			for (String arg : args) {
				jparams.put(arg);
			}
			jparams.put("json");
			jparams.put(db_dm);
			jreq.put("params", jparams);

			L.e(jreq.toString());
			L.e(handler.toString());
			params.clear();
			params.add(new ParamData<String>(String.class, jreq.toString()));

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
			// 指定WebService的命名空间和调用方法
			SoapObject soapObject = new SoapObject(Constants.NAME_SPACE, Constants.METHOD);
			for (int i = 0; i < params.size(); i++) {
				String val = String.valueOf(params.get(i).getData());
				soapObject.addProperty("arg" + i, AESUtils.en2us(val, Constants.CHAR_SET));
			}
			envelope.setOutputSoapObject(soapObject);
//			envelope.dotNet = true;
//			ht = new HttpTransportSE(address, 1000*10);
			ht = new HttpTransportSE(Constants.URL);
//			ht.debug = true;
			ht.call(null, envelope);
			if (envelope.getResponse() != null) {
				SoapObject soapResult = (SoapObject) envelope.bodyIn;
				Object detail = (Object) soapResult.getProperty(0);
				String result = detail.toString();
				result = URLDecoder.decode(result, Constants.CHAR_SET);
				this.sendMessage(result);
			} else {
				this.sendMessage("");
			}

			L.e("正常流程");
		}catch (EOFException eofe){
			// 异常重连机制
			if (size < 3) {
				size++;
				L.e("EOFException");
				run();
			} else {
				this.sendMessage("");
			}
		} catch (Exception e) {
			L.e("Exception" + e.getMessage());
			this.sendMessage("");
		} finally {
			try {
				if (ht != null) {
					ht.getServiceConnection().disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
				case HttpJsonRequest.DID_START: {
					// CommonDoBack.print(CommonDoBack.LOG_TYPE_DEBUG,Tag,"Http请求开始");
					break;
				}
				case HttpJsonRequest.DID_SUCCEED: {
					L.e(Thread.currentThread().getName());
					// CommonDoBack.print(CommonDoBack.LOG_TYPE_DEBUG,Tag,"Http请求返回成功");
					CallbackListener listener = (CallbackListener) message.obj;
					Object data = message.getData();
					if (listener != null) {
						if (data != null) {
							Bundle bundle = (Bundle) data;
							String result = bundle.getString("callbackkey");
							listener.callBack(result);
						}
					}
					break;
				}
				case HttpJsonRequest.DID_ERROR: {
					// CommonDoBack.print(CommonDoBack.LOG_TYPE_DEBUG,Tag,"Http请求返回失败");
					break;
				}
			}
		}
	};


	private void sendMessage(String result) {
		Message message = Message.obtain(handler, DID_SUCCEED, listener);
		Bundle data = new Bundle();
		data.putString("callbackkey", result);
		message.setData(data);
		handler.sendMessage(message);
	}

	/*
	 * 回调函数接口
	 */
	public interface CallbackListener {
		public void callBack(String result);
	}
}
