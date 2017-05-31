package com.smzskj.crk.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseActivity;
import com.smzskj.crk.bean.PdCheckBean;
import com.smzskj.crk.bean.RepertoryBean;
import com.smzskj.crk.net.HttpJsonRequest;
import com.smzskj.crk.net.Method;
import com.smzskj.crk.net.ReturnBean;
import com.smzskj.crk.offline.activity.OffineActivity;
import com.smzskj.crk.utils.JsonUtils;
import com.smzskj.crk.utils.L;
import com.smzskj.crk.utils.ThreadPoolUtils;
import com.smzskj.crk.utils.UserInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ztt on 2017/1/16.
 * <p>
 * 仓库选择
 */

public class SelectRepertoryActivity extends BaseActivity implements View.OnClickListener {

	private Spinner spinner;
	private Button btnIn, btnOut, btnCheck, btnGet, btnQuery, btnOffline;
	private List<RepertoryBean> repertoryList = new ArrayList<>();

	public static void startSelectRepertoryActivity(Context context) {
		Intent intent = new Intent(context, SelectRepertoryActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkLogin();
		setContentView(R.layout.activity_select_repertory);

		setTitle(R.string.select_repertory);
		initView();
		rk_get_kfinfo();
	}

	private void initView() {

		spinner = findView(R.id.select_repertory_sp);
		btnGet = findView(R.id.select_repertory_btn_get);
		btnGet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				rk_get_kfinfo();
			}
		});
		btnIn = findView(R.id.select_repertory_btn_in);
		btnIn.setOnClickListener(this);
		btnOut = findView(R.id.select_repertory_btn_out);
		btnOut.setOnClickListener(this);
		btnCheck = findView(R.id.select_repertory_btn_check);
		btnCheck.setOnClickListener(this);
		btnQuery = findView(R.id.select_repertory_btn_query);
		btnQuery.setOnClickListener(this);
		btnOffline = findView(R.id.select_repertory_btn_offline);
		btnOffline.setOnClickListener(this);

		setRightIbListener(R.drawable.ic_sz, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingActivity.startSettingActivity(mContext);
			}
		});

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position != 0) {
					String code = repertoryList.get(position).get库房号();
					String name = repertoryList.get(position).get库房名称();
					mSp.put(UserInfo.SP_CK_CODE, code);
					mSp.put(UserInfo.SP_CK_NAME, name);
				} else {
					mSp.put(UserInfo.SP_CK_CODE, "");
					mSp.put(UserInfo.SP_CK_NAME, "");
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		int p = spinner.getSelectedItemPosition();
		if (p <= 0) {
			makeShortToase("请选择仓库");
			return;
		}
		String code = repertoryList.get(p).get库房号();
		String name = repertoryList.get(p).get库房名称();

		if (TextUtils.isEmpty(code) || TextUtils.isEmpty(name)) {
			makeShortToase("仓库代码或名称为空");
			return;
		}
		if (v == btnIn) {
			InActivity.startInActivity(mContext, code, name);
		} else if (v == btnOut) {
//			OutActivity.startOutActivity(mContext, code, name);
			OutV2Activity.startOutActivity(mContext);
		} else if (v == btnCheck) {
			pd_chk_info();
		} else if (v == btnQuery) {
			QueryActivity.startQueryActivity(mContext, code, name);
		} else if (v == btnOffline) {
			OffineActivity.startOffineActivity(mContext);
		}
	}

	/**
	 * 获取库房信息
	 * <p>
	 * ry_code 使用人代码
	 */
	private void rk_get_kfinfo() {
		showLoadDialog();
		HttpJsonRequest request = new HttpJsonRequest(new KfinfoBackListener(),
				Method.SERVICE_NAME_RK, Method.RK_GET_KFINFO
				, UserInfo.RY_CODE);
		ThreadPoolUtils.execute(request);
	}

	class KfinfoBackListener implements HttpJsonRequest.CallbackListener {

		@Override
		public void callBack(String result) {
			L.e(result);
			cancleLoadDialog();
			List<RepertoryBean> beanList = null;
			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
			} else {
				ReturnBean returnBean = JsonUtils.getJsonParseObject(result, ReturnBean.class);
				if (returnBean == null) {
					makeShortToase(R.string.loading_error);
				} else {
					repertoryList.add(new RepertoryBean("00", "请选择仓库"));
					if (returnBean.checkCode()) {
						beanList = JsonUtils.getJsonParseArray(returnBean.getRows(),
								RepertoryBean.class);
						if (beanList == null) {
							makeShortToase(R.string.loading_empty);
						} else {
							repertoryList.addAll(beanList);
						}
						int size = repertoryList.size();
						String[] names = new String[size];
//						String[] codes = new String[size];
						for (int i = 0; i < size; i++) {
							names[i] = repertoryList.get(i).get库房名称();
//							codes[i] = repertoryList.get(i).get库房号();
						}
						ArrayAdapter adapter = new ArrayAdapter<String>(mContext, R.layout
								.item_spinner_white,
								names);
						adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
						spinner.setAdapter(adapter);

						String code = mSp.getString(UserInfo.SP_CK_CODE, "");
						String name = mSp.getString(UserInfo.SP_CK_NAME, "");
						for (int i = 0; i < size; i++) {
							if (code.equals(repertoryList.get(i).get库房号()) && name.equals
									(repertoryList.get(i).get库房名称())) {
								spinner.setSelection(i);
								break;
							}
						}
					}
				}
			}

			if (beanList == null || beanList.size() == 0) {
				btnGet.setVisibility(View.VISIBLE);
			} else {
				btnGet.setVisibility(View.GONE);
			}
		}
	}

	private static final int SHOW_TIME_MIN = 3000;        // 最小显示时间
	private long time;

	@Override
	public void onBackPressed() {
//		if (System.currentTimeMillis() - time > SHOW_TIME_MIN) {
//			time = System.currentTimeMillis();
//			makeShortToase("再按一次退出应用");
//		} else {
//			mApplication.exit();
//			System.exit(0);
//		}

		showAlertDialog("是否要退出应用？", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mApplication.exit();
				System.exit(0);
			}
		});
	}

	private static SimpleDateFormat sdf_ymd = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());

	/**
	 * 检查盘点信息
	 * <p>
	 * 库房名
	 * 日期
	 */
	private void pd_chk_info() {
		showLoadDialog();
		String kf = mSp.getString(UserInfo.SP_CK_NAME, "");
		String rq = sdf_ymd.format(new Date(System.currentTimeMillis()));
		HttpJsonRequest request = new HttpJsonRequest(new PDinfoBackListener(),
				Method.SERVICE_NAME_PD, Method.PD_CHK_INFO
				, mSp.getString(UserInfo.SP_USER_CODE, ""), kf, rq);
		ThreadPoolUtils.execute(request);
	}

	class PDinfoBackListener implements HttpJsonRequest.CallbackListener {

		@Override
		public void callBack(String result) {
			L.e(result);
			cancleLoadDialog();

			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
			} else {
				PdCheckBean returnBean = JsonUtils.getJsonParseObject(result, PdCheckBean.class);
				if (returnBean == null) {
					makeShortToase(R.string.loading_error);
				} else {
					if ("true".equals(returnBean.getRes())) {
						startPd();
					} else {
						if ("isCz".equals(returnBean.getRows())) {
							showAlertDialog(getString(R.string.dialog_alert), returnBean.getReason(), new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
									pdcz();
								}
							}, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
									startPd();
								}
							});
						}
					}
				}
			}
		}
	}

	private void pdcz() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(getString(R.string.dialog_alert));
		builder.setMessage("请选择重置类型");
		// 左
		builder.setNegativeButton("重置所有", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				pd_cz_info_kfc();
			}
		});

		builder.setPositiveButton("重置当天", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				pd_cz_info();
			}
		});
		builder.show();
	}

	/**
	 * 检查盘点信息
	 * <p>
	 * 库房名
	 * 日期
	 */
	private void pd_cz_info_kfc() {
		showLoadDialog();
		String kf = mSp.getString(UserInfo.SP_CK_NAME, "");
		String rq = sdf_ymd.format(new Date(System.currentTimeMillis()));
		HttpJsonRequest request = new HttpJsonRequest(new CzBackListener(),
				Method.SERVICE_NAME_PD, Method.PD_CZ_INFO_KFC
				, mSp.getString(UserInfo.SP_USER_CODE, ""), kf, rq);
		ThreadPoolUtils.execute(request);
	}

	/**
	 * 检查盘点信息
	 * <p>
	 * 库房名
	 * 日期
	 */
	private void pd_cz_info() {
		showLoadDialog();
		String kf = mSp.getString(UserInfo.SP_CK_NAME, "");
		String rq = sdf_ymd.format(new Date(System.currentTimeMillis()));
		HttpJsonRequest request = new HttpJsonRequest(new CzBackListener(),
				Method.SERVICE_NAME_PD, Method.PD_CZ_INFO
				, mSp.getString(UserInfo.SP_USER_CODE, ""), kf, rq);
		ThreadPoolUtils.execute(request);
	}

	class CzBackListener implements HttpJsonRequest.CallbackListener {

		@Override
		public void callBack(String result) {
			L.e(result);
			cancleLoadDialog();

			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
			} else {
//				{"res":"true","reason":"盘点数据初始化成功!","code":"100","rows":""}
				PdCheckBean returnBean = JsonUtils.getJsonParseObject(result, PdCheckBean.class);
				if (returnBean == null) {
					makeShortToase(R.string.loading_error);
				} else {
					if ("true".equals(returnBean.getRes())) {
						startPd();
					} else {

						showAlertDialog(getString(R.string.dialog_alert), returnBean.getReason(), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								startPd();
							}
						}, null);
					}
				}
			}
		}
	}

	private void startPd() {
		PdActivity.startPdActivity(mContext);
	}
}
