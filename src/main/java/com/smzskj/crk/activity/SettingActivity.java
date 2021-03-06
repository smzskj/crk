package com.smzskj.crk.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseActivity;
import com.smzskj.crk.net.HttpJsonRequest;
import com.smzskj.crk.net.Method;
import com.smzskj.crk.offline.db.OfflinePdHelper;
import com.smzskj.crk.utils.Constants;
import com.smzskj.crk.utils.HashUtils;
import com.smzskj.crk.utils.JsonUtils;
import com.smzskj.crk.utils.L;
import com.smzskj.crk.utils.ThreadPoolUtils;
import com.smzskj.crk.utils.UpdateBean;
import com.smzskj.crk.utils.UserInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.smzskj.crk.utils.Constants.setUrl;

/**
 * Created by ztt on 2017/1/20.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {


	private TextView tvVersion;
	private Button btnLogout, btnPort, btnUppwd, btnIp, btnUpdate;
	private Button btnOutput, btnInput, btnWeb;
	private EditText edPort, edIp;

	public static void startSettingActivity(Context context) {
		Intent intent = new Intent(context, SettingActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		initView();
	}

	private void initView() {
		addBackListener();
		setTitle("设置");
		tvVersion = findView(R.id.set_tv_version);
		try {
			tvVersion.setText("V " + getPackageManager().getPackageInfo(getPackageName(), 0)
					.versionName);
		} catch (PackageManager.NameNotFoundException e) {
			tvVersion.setText("");
		}

		btnLogout = findView(R.id.set_btn_logout);
		btnPort = findView(R.id.set_btn_port);
		edPort = findView(R.id.set_ed_port);
		edPort.setText(Constants.PORT);
		btnUppwd = findView(R.id.set_btn_uppwd);
		btnUppwd.setOnClickListener(this);
		btnLogout.setOnClickListener(this);
		btnPort.setOnClickListener(this);
		edIp = findView(R.id.set_ed_ip);
		btnIp = findView(R.id.set_btn_ip);
		btnIp.setOnClickListener(this);
		btnUpdate = findView(R.id.set_btn_update);
		btnUpdate.setOnClickListener(this);

		btnInput = findView(R.id.set_btn_input);
		btnOutput = findView(R.id.set_btn_output);
		btnWeb = findView(R.id.set_btn_web);
		btnInput.setOnClickListener(this);
		btnOutput.setOnClickListener(this);
		btnWeb.setOnClickListener(this);

		if (TextUtils.isEmpty(UserInfo.RY_NAME)) {
			btnLogout.setVisibility(View.GONE);
			btnUppwd.setVisibility(View.GONE);
		}

		edIp.setText(Constants.IP);
		edPort.setText(Constants.PORT);
	}

	@Override
	public void onClick(View v) {
		if (v == btnLogout) {
			UserInfo.RY_NAME = "";
			UserInfo.RY_CODE = "";
			mApplication.clear(SettingActivity.this);
			LoginActivity.startLoginActivity(mContext);

			L.e("Setting---Login");

			finish();
		} else if (v == btnPort) {
			String port = edPort.getText().toString();
			if (TextUtils.isEmpty(port)) {
				makeShortToase("端口号不能为空");
			} else {
				Constants.PORT = port;
				setUrl();
				makeShortToase("端口号设置完成");
				mSp.put(UserInfo.SP_PORT, port);
			}
		} else if (v == btnUppwd) {
			UppwdActivity.startUppwdActivity(mContext);
		} else if (v == btnIp) {
			String ip = edIp.getText().toString();
			if (TextUtils.isEmpty(ip)) {
				makeShortToase("IP不能为空");
			} else {
				Constants.IP = ip;
				setUrl();
				makeShortToase("IP设置完成");
				mSp.put(UserInfo.SP_IP, ip);
			}
		} else if (v == btnUpdate) {
			update();
		} else if (v == btnInput) {
			if (copySDToDataBase(mContext, OfflinePdHelper.DB_NAME)) {
				makeShortToase("拷贝成功");
			} else {
				makeShortToase("拷贝失败");
			}
		} else if (v == btnOutput) {
			if (copyDataBaseToSD(mContext, OfflinePdHelper.DB_NAME)) {
				makeShortToase("拷贝成功");
			} else {
				makeShortToase("拷贝失败");
			}
		} else if (v == btnWeb) {
			try {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(Constants.URL + "?wsdl"));
				startActivity(intent);
			} catch (Exception e) {
				makeShortToase("请安装浏览器");
			}

		}
	}

	public void update() {
		showLoadDialog();
		HttpJsonRequest request = new HttpJsonRequest(new UpdateCallBackListener(),
				Method.SERVICE_NAME_LOGIN, Method.GETVERSION);
		ThreadPoolUtils.execute(request);
	}

	class UpdateCallBackListener implements HttpJsonRequest.CallbackListener {

		@Override
		public void callBack(String result) {
			L.e(result);
			cancleLoadDialog();
			if (!TextUtils.isEmpty(result)) {
				UpdateBean bean = JsonUtils.getJsonParseObject(result, UpdateBean.class);
				if (bean != null && bean.getRows() != null && bean.getRows().size() > 0) {
					try {
						int svcode = bean.getRows().get(0).getVcode();
						int lvcode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
						final String vname = bean.getRows().get(0).getVname();
						if (lvcode < svcode && !TextUtils.isEmpty(vname)) {
							showAlertDialog("版本更新", "发现新版本，是否马上更新？", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + Constants.IP + ":" + Constants.PORT + "/crk_service/downloads/" + vname));
									browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(browserIntent);
								}
							}, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
								}
							});
						} else {
							showToastDialog("没有发现新版本");
						}
					} catch (PackageManager.NameNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}


	/**
	 * 拷贝数据库到sd卡
	 * <p>
	 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	 */
	public boolean copyDataBaseToSD(Context context, String dbname) {
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return false;
		}

		File dbFile = context.getApplicationContext().getDatabasePath(dbname).getAbsoluteFile();
		if (!dbFile.exists()) {
			showToastDialog("数据库文件不存在");
			return false;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
		String dateTime = dateFormat.format(new Date());
		File toDir = new File(Environment.getExternalStorageDirectory() + "/crk/" + dateTime);
		if (!toDir.exists()) {
			boolean dirMk = toDir.mkdirs();
			if (!dirMk) {
				return false;
			}
		}

		File file = new File(toDir, dbname);
		FileChannel inChannel = null, outChannel = null;
		try {
			boolean b = file.createNewFile();
			if (!b) {
				return false;
			}

			if (file.exists()) {
				inChannel = new FileInputStream(dbFile).getChannel();
				outChannel = new FileOutputStream(file).getChannel();
				inChannel.transferTo(0, inChannel.size(), outChannel);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			Log.e("TAG", "copy dataBase to SD error.");
			return false;
		} finally {
			try {
				if (inChannel != null) {
					inChannel.close();
					inChannel = null;
				}
				if (outChannel != null) {
					outChannel.close();
					outChannel = null;
				}
			} catch (IOException e) {
				Log.e("TAG", "file close error.");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 拷贝数据库到sd卡
	 * <p>
	 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	 */
	public boolean copySDToDataBase(Context context, String dbname) {
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return false;
		}
//		File dbFile = new File();
//		File file = context.getApplicationContext().getDatabasePath(dbname).getAbsoluteFile();

		File databases = context.getApplicationContext().getDatabasePath(dbname).getAbsoluteFile();
		File dbFile = new File(Environment.getExternalStorageDirectory() + "/crk", dbname);

		if (dbFile.exists()) {
			if (databases.exists()) {
				String databasesMD5 = HashUtils.getFileMd5(databases.getPath());
				String dbMD5 = HashUtils.getFileMd5(dbFile.getPath());
				if (databasesMD5.equals(dbMD5)) {
					makeShortToase("数据库文件相同，不需要拷贝");
					return false;
				}
			}
		} else {
			showToastDialog("数据库文件不存在");
			return false;
		}

		File fileDir = new File("/data/data/com.smzskj.crk/databases/");
		FileChannel inChannel = null, outChannel = null;
		try {
			if (!fileDir.exists()) {
				if (!fileDir.mkdirs()) {
					return false;
				}
			}
			File file = new File(fileDir, dbname);

			if (file.exists()) {
				if (copyDataBaseToSD(context,dbname)) {
					if (!file.delete()) {
						return false;
					}
				} else {
					return false;
				}
			}

			boolean b = file.createNewFile();
			if (b) {
				inChannel = new FileInputStream(dbFile).getChannel();
				outChannel = new FileOutputStream(file).getChannel();
				inChannel.transferTo(0, inChannel.size(), outChannel);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			Log.e("TAG", "copy dataBase to SD error.");
			return false;
		} finally {
			try {
				if (inChannel != null) {
					inChannel.close();
					inChannel = null;
				}
				if (outChannel != null) {
					outChannel.close();
					outChannel = null;
				}
			} catch (IOException e) {
				Log.e("TAG", "file close error.");
				e.printStackTrace();
			}
		}
	}

}
