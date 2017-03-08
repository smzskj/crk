package com.smzskj.crk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseActivity;
import com.smzskj.crk.bean.UppwdBean;
import com.smzskj.crk.net.HttpJsonRequest;
import com.smzskj.crk.net.Method;
import com.smzskj.crk.utils.JsonUtils;
import com.smzskj.crk.utils.L;
import com.smzskj.crk.utils.ThreadPoolUtils;
import com.smzskj.crk.utils.UserInfo;

/**
 * Created by ztt on 2017/2/9.
 */

public class UppwdActivity extends BaseActivity implements View.OnClickListener {

	private EditText edOld, edNew1, edNew2;
	private Button btnOk;


	public static void startUppwdActivity(Context context) {
		Intent intent = new Intent(context, UppwdActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkLogin();
		setContentView(R.layout.activity_uppwd);

		addBackListener();
		setTitle("修改密码");

		edOld = findView(R.id.uppwd_ed_old);
		edNew1 = findView(R.id.uppwd_ed_new1);
		edNew2 = findView(R.id.uppwd_ed_new2);

		String digists = getString(R.string.digists);
		edOld.setKeyListener(DigitsKeyListener.getInstance(digists));
		edNew1.setKeyListener(DigitsKeyListener.getInstance(digists));
		edNew2.setKeyListener(DigitsKeyListener.getInstance(digists));
		btnOk = findView(R.id.uppwd_btn_ok);
		btnOk.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btnOk) {
			String oldPwd = edOld.getText().toString();
			String newPwd1 = edNew1.getText().toString();
			String newPwd2 = edNew2.getText().toString();
			if (TextUtils.isEmpty(oldPwd)) {
				makeShortToase("旧密码不能为空");
			} else if (TextUtils.isEmpty(newPwd1)) {
				makeShortToase("新密码不能为空");
			} else if (TextUtils.isEmpty(newPwd2)) {
				makeShortToase("重复密码不能为空");
			} else if (!newPwd1.equals(newPwd2)) {
				makeShortToase("两次密码输入不一致");
			} else {
				xg_psw(oldPwd,newPwd1);
			}
		}
	}

	/**
	 * 获得单号
	 */
	public void xg_psw(String oldPwd, String newPwd) {
		showLoadDialog();
		HttpJsonRequest request = new HttpJsonRequest(new PwdBackListener(),
				Method.SERVICE_NAME_LOGIN, Method.XG_PSW
				, UserInfo.RY_CODE, oldPwd, newPwd, newPwd);
		ThreadPoolUtils.execute(request);
	}

	class PwdBackListener implements HttpJsonRequest.CallbackListener {

		@Override
		public void callBack(String result) {
			L.e(result);
			cancleLoadDialog();
			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
				return;
			}
			UppwdBean bean = JsonUtils.getJsonParseObject(result, UppwdBean.class);
			if (bean == null) {
				makeShortToase(R.string.loading_error);
			} else {
				makeShortToase(bean.getReason());
			}
		}
	}
}
