package com.smzskj.crk.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseActivity;
import com.smzskj.crk.bean.LoginBean;
import com.smzskj.crk.net.HttpJsonRequest;
import com.smzskj.crk.net.Method;
import com.smzskj.crk.net.ReturnBean;
import com.smzskj.crk.offline.activity.OffineActivity;
import com.smzskj.crk.utils.Constants;
import com.smzskj.crk.utils.DbBean;
import com.smzskj.crk.utils.HashUtils;
import com.smzskj.crk.utils.JsonUtils;
import com.smzskj.crk.utils.L;
import com.smzskj.crk.utils.ThreadPoolUtils;
import com.smzskj.crk.utils.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ztt on 2017/1/16.
 * <p>
 * 登录
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

	private Button btnLogin;
	private Button btnOffline;
	private EditText edName, edPwddd;
	private Spinner spinner;
	private Button btnGet;
	private List<DbBean.RowsBean> dbList = new ArrayList<>();

	public static void startLoginActivity(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		L.e("LoginActivity");

		try {
			int svcode = mSp.getInt(UserInfo.VCODE,0);
			int lvcode = getPackageManager().getPackageInfo(getPackageName(),0).versionCode;
			final String vname = mSp.getString(UserInfo.VNAME,"");
			if (lvcode < svcode && !TextUtils.isEmpty(vname)) {

				showAlertDialog("版本更新", "发现新版本，是否马上更新？", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" +Constants.IP + ":" + Constants.PORT + "/crk_service/downloads/" + vname));
						browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(browserIntent);
					}
				}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		getDBList();
	}

	private void initView() {
		setContentView(R.layout.activity_login);
		setTitle(R.string.login);

		edName = findView(R.id.login_ed_un);
		edPwddd = findView(R.id.login_ed_pwddd);
		String digists = getString(R.string.digists);
		edName.setKeyListener(DigitsKeyListener.getInstance(digists));
		edPwddd.setKeyListener(DigitsKeyListener.getInstance(digists));
		btnLogin = findView(R.id.login_btn_login);
		btnOffline = findView(R.id.login_btn_offline);
		btnLogin.setOnClickListener(this);
		btnOffline.setOnClickListener(this);
		edName.setText(mSp.getString(UserInfo.SP_USER_CODE, ""));

		setRightIbListener(R.drawable.ic_sz, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingActivity.startSettingActivity(mContext);
			}
		});

		spinner = findView(R.id.login_sp_db);
		btnGet = findView(R.id.login_btn_get);
		btnGet.setOnClickListener(this);


		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String code = dbList.get(position).getDb_dm();
				String name = dbList.get(position).getDb_mc();
				mSp.put(UserInfo.SP_DB_CODE, code);
				mSp.put(UserInfo.SP_DB_NAME, name);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == btnLogin) {
			int p = spinner.getSelectedItemPosition();
			if (p <= 0) {
				makeShortToase("请选择数据库");
				return;
			}

			String un = edName.getText().toString();
			String pwddd = edPwddd.getText().toString();

			if (TextUtils.isEmpty(un)) {
				makeShortToase(R.string.userName_null);
			} else if (TextUtils.isEmpty(pwddd)) {
				makeShortToase(R.string.pwddd_null);
			} else {
				login(un, pwddd);
			}
		} else if (v == btnGet) {
			getDBList();
		} else if (v == btnOffline) {
			String ck = mSp.getString(UserInfo.SP_CK_NAME,"");
			String zh = mSp.getString(UserInfo.SP_USER_NAME,"");
			String db = mSp.getString(UserInfo.SP_DB_NAME,"");
			if (TextUtils.isEmpty(ck) || TextUtils.isEmpty(zh) || TextUtils.isEmpty(db)){
				makeShortToase("请先登录选择仓库再进行离线操作");
			} else {
				OffineActivity.startOffineActivity(mContext);
			}
		}
	}


	private void login(String un, String pwd) {
		showLoadDialog();
		HttpJsonRequest.db_dm = mSp.getString(UserInfo.SP_DB_CODE,"");

		HttpJsonRequest request = new HttpJsonRequest(new LoginCallBackListener(),
				Method.SERVICE_NAME_LOGIN, Method.LOGIN_LOGIN,
				un, HashUtils.Md5(pwd));
		L.e(Constants.URL);
		ThreadPoolUtils.execute(request);
	}

	class LoginCallBackListener implements HttpJsonRequest.CallbackListener {

		@Override
		public void callBack(String result) {
			UserInfo.clearUserInfo();
			L.e(result);
			cancleLoadDialog();
			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
				return;
			}
			ReturnBean returnBean = JsonUtils.getJsonParseObject(result, ReturnBean.class);

			if (returnBean == null) {
				makeShortToase(R.string.loading_error);
				return;
			}
			// 登录成功
			if (returnBean.checkRes()) {
				LoginBean loginBean = JsonUtils.getJsonParseObject(returnBean.getRows(), LoginBean
						.class);
				if (loginBean != null && !TextUtils.isEmpty(loginBean.getRy_code())) {
					UserInfo.RY_CODE = loginBean.getRy_code();
					UserInfo.RY_NAME = loginBean.getRy_name();

					mSp.put(UserInfo.SP_USER_CODE, UserInfo.RY_CODE);
					mSp.put(UserInfo.SP_USER_NAME, UserInfo.RY_NAME);

					SelectRepertoryActivity.startSelectRepertoryActivity(mContext);

					finish();
				} else {
					makeShortToase(R.string.loading_error);
					return;
				}
			}
			if (!TextUtils.isEmpty(returnBean.getReason())) {
				makeShortToase(returnBean.getReason());
			}
		}
	}

	private static final int SHOW_TIME_MIN = 3000;        // 最小显示时间
	private long time;

	@Override
	public void onBackPressed() {
		showAlertDialog("是否退出程序", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.exit(0);
			}
		});
	}


	private void getDBList() {
		showLoadDialog();
		HttpJsonRequest request = new HttpJsonRequest(new DBCallBackListener(),
				Method.SERVICE_NAME_LOGIN, Method.GETDBLIST);
		L.e(Constants.URL);
		ThreadPoolUtils.execute(request);
	}
	class DBCallBackListener implements HttpJsonRequest.CallbackListener {

		@Override
		public void callBack(String result) {
			UserInfo.clearUserInfo();
			L.e(result);
			cancleLoadDialog();
			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
				return;
			}
			DbBean returnBean = JsonUtils.getJsonParseObject(result, DbBean.class);

			if (returnBean == null) {
				makeShortToase(R.string.loading_error);
			} else {
				if ("100".equals(returnBean.getCode()) && returnBean.getRows() != null && returnBean.getRows().size() > 0) {
					DbBean.RowsBean db = new DbBean.RowsBean();
					db.setDb_dm("00");
					db.setDb_mc("请选择数据库");
					dbList.add(db);
					dbList.addAll(returnBean.getRows());
					int size = dbList.size();
					String[] names = new String[size];
					for (int i = 0; i < size; i++) {
						names[i] = dbList.get(i).getDb_mc();
					}
					ArrayAdapter adapter = new ArrayAdapter<String>(mContext, R.layout
							.item_spinner_white,
							names);
					adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
					spinner.setAdapter(adapter);

					String db_dm = mSp.getString(UserInfo.SP_DB_CODE,"");
					for (int i = 0 ; i < size ;i++) {
						if (dbList.get(i).getDb_dm().equals(db_dm)) {
							spinner.setSelection(i);
						}
					}

					btnGet.setVisibility(View.GONE);
				} else {
					btnGet.setVisibility(View.VISIBLE);
				}
			}
		}
	}
}
