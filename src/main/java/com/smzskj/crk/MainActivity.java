package com.smzskj.crk;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.smzskj.crk.activity.LoginActivity;
import com.smzskj.crk.base.BaseActivity;
import com.smzskj.crk.net.HttpJsonRequest;
import com.smzskj.crk.net.Method;
import com.smzskj.crk.utils.Constants;
import com.smzskj.crk.utils.JsonUtils;
import com.smzskj.crk.utils.L;
import com.smzskj.crk.utils.ThreadPoolUtils;
import com.smzskj.crk.utils.UpdateBean;
import com.smzskj.crk.utils.UserInfo;

public class MainActivity extends BaseActivity {

	private static final int SHOW_TIME_MIN = 2000;		// 最小显示时间
	private Handler mHandler = new Handler();
	private long startTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


//		DisplayMetrics dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
//		L.e(dm.widthPixels + "*" + dm.heightPixels);
//		L.e("状态栏高度" + getStatusBarHeight());


		Constants.IP = mSp.getString(UserInfo.SP_IP,Constants.IP_DEFAULT);
		Constants.PORT = mSp.getString(UserInfo.SP_PORT,Constants.PORT_DEFAULT);
		if (TextUtils.isEmpty(Constants.IP)) {
			Constants.IP = Constants.IP_DEFAULT;
		}
		if (TextUtils.isEmpty(Constants.PORT)) {
			Constants.PORT = Constants.PORT_DEFAULT;
		}
		Constants.setUrl();
		startTime = System.currentTimeMillis();
		update();
	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	private void splash(){

		long temp = SHOW_TIME_MIN - (System.currentTimeMillis() - startTime);
		if (temp < 0) {
			temp = 0;
		}
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				L.e("Main---Login");
				LoginActivity.startLoginActivity(MainActivity.this);
				finish();
			}
		}, temp);
	}


	public void update() {
		HttpJsonRequest request = new HttpJsonRequest(new UpdateCallBackListener(),
				Method.SERVICE_NAME_LOGIN, Method.GETVERSION);
		ThreadPoolUtils.execute(request);
	}

	class UpdateCallBackListener implements HttpJsonRequest.CallbackListener {

		@Override
		public void callBack(String result) {
			L.e(result);
			if (!TextUtils.isEmpty(result)) {
				UpdateBean bean = JsonUtils.getJsonParseObject(result,UpdateBean.class);
				if (bean != null && bean.getRows() != null && bean.getRows().size() > 0) {
					mSp.put(UserInfo.VCODE,bean.getRows().get(0).getVcode());
					mSp.put(UserInfo.VNAME,bean.getRows().get(0).getVname());
				}
			}
			// 启动
			splash();
		}
	}
}
