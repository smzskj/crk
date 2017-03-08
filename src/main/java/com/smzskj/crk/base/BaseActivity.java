package com.smzskj.crk.base;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.smzskj.crk.R;
import com.smzskj.crk.activity.LoginActivity;
import com.smzskj.crk.net.HttpJsonRequest;
import com.smzskj.crk.net.LoadProgressDialog;
import com.smzskj.crk.utils.L;
import com.smzskj.crk.utils.SPUtils;
import com.smzskj.crk.utils.ScannerUtils;
import com.smzskj.crk.utils.UserInfo;

import java.util.Calendar;

/**
 * Created by ztt on 2017/1/16.
 */

public class BaseActivity extends Activity {

	public static final float ALPHA_5 = 0.5F;
	public static final float ALPHA_1 = 1.0F;

	protected BaseApplication mApplication;
	protected Activity mContext;
	private LoadProgressDialog loadProgressDialog;

	protected int currentPage = 1;
	protected String pageSize = "10";
	protected int pageCount = 0;

	protected SPUtils mSp;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		L.e(getClass().getCanonicalName());
		mContext = this;
		mSp = new SPUtils(mContext, UserInfo.SP_USER_INFO);
		ScannerUtils.scanning = false;

		HttpJsonRequest.db_dm = mSp.getString(UserInfo.SP_DB_CODE, "");
		mApplication = BaseApplication.getInstance();
		mApplication.addActivity(this);
	}

	/**
	 * 判断静态常量来判断是否登录，如果常量为空，跳到登录界面
	 */
	protected void checkLogin(){
		if (TextUtils.isEmpty(UserInfo.RY_NAME)) {
			mApplication.clear(this);
			LoginActivity.startLoginActivity(this);
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mApplication.removeActivity(this);
	}

	@Override
	public void setTitle(CharSequence title) {
		TextView textView = findView(R.id.top_tv_title);
		textView.setText(title);
	}

	@Override
	public void setTitle(int titleId) {
		TextView textView = findView(R.id.top_tv_title);
		textView.setText(titleId);
	}

	/**
	 * 设置返回监听
	 */
	public void addBackListener() {
		ImageButton imageButton = findView(R.id.top_ib_left);
		imageButton.setImageResource(R.drawable.ic_fh);
		imageButton.setVisibility(View.VISIBLE);
		imageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 设置标题栏右侧按钮
	 *
	 * @param res      按钮图标
	 * @param listener 点击事件
	 */
	protected void setRightIbListener(int res, View.OnClickListener listener) {
		ImageButton imageButton = findView(R.id.top_ib_right);
		imageButton.setImageResource(res);
		imageButton.setVisibility(View.VISIBLE);
		imageButton.setOnClickListener(listener);
	}

	/**
	 * 设置标题栏右侧按钮
	 *
	 * @param text     按钮图标
	 * @param listener 点击事件
	 */
	protected Button setRightBtnListener(String text, View.OnClickListener listener) {
		Button btn = findView(R.id.top_btn_right);
		btn.setText(text);
		btn.setVisibility(View.VISIBLE);
		btn.setOnClickListener(listener);
		return btn;
	}


	@SuppressWarnings("unchecked")
	protected <V extends View> V findView(View view, int id) {
		return (V) view.findViewById(id);
	}

	@SuppressWarnings("unchecked")
	protected <V extends View> V findView(int id) {
		return (V) findViewById(id);
	}


	protected void makeShortToase(String content) {
//		ToastUtils.makeShortToast(mContext, content);
		showToastDialog(content);
	}

	protected void makeShortToase(int resId) {
//		ToastUtils.makeShortToast(mContext, resId);
		showToastDialog(getString(resId));
	}


	protected void showLoadDialog() {
		if (loadProgressDialog == null) {
			loadProgressDialog = new LoadProgressDialog(mContext);
		}
		if (!loadProgressDialog.isShowing()) {
			loadProgressDialog.show();
			;
		}
	}

	protected void cancleLoadDialog() {
		if (loadProgressDialog != null) {
			try {
				loadProgressDialog.cancel();
			} catch (Exception e) {
				L.e("loadProgressDialog关闭异常");
			}
		}
	}

	/**
	 * 设置透明度
	 *
	 * @param alpha 参数为想要的透明度
	 */
	protected void setWindowAlpha(float alpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = alpha;
		getWindow().setAttributes(lp);
	}

	protected String getToday() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String strMonth = month > 9 ? String.valueOf(month) : "0" + month;
		String strDay = day > 9 ? String.valueOf(day) : "0" + day;
		return year + "-" + strMonth + "-" + strDay;
	}


	protected String getMonthFirst() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
//		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String strMonth = month > 9 ? String.valueOf(month) : "0" + month;
//		String strDay = day > 9 ? String.valueOf(day) : "0" + day;
		return year + "-" + strMonth + "-01";
	}


	/**
	 * 提示dialog，取消按钮默认为关闭
	 *
	 * @param msg       提示信息
	 * @param yListener 确定按钮
	 * @return dialog
	 */
	protected AlertDialog showAlertDialog(String msg,
										  DialogInterface.OnClickListener yListener) {
		return this.showAlertDialog(getString(R.string.dialog_alert), msg, yListener, new
				DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
	}

	/**
	 * 提示dialog
	 *
	 * @param title     标题
	 * @param msg       信息
	 * @param yListener 确认监听
	 * @param nListener 取消监听
	 * @return dialod
	 */
	protected AlertDialog showAlertDialog(String title, String msg,
										  DialogInterface.OnClickListener yListener,
										  DialogInterface.OnClickListener nListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setNegativeButton(R.string.dialog_y, yListener);

		if (nListener != null) {
			builder.setPositiveButton(R.string.dialog_n, nListener);
		}
		return builder.show();
	}

	/**
	 * Toast提示框
	 * @param msg 提示信息
	 * @return dialog
	 */
	protected AlertDialog showToastDialog(String msg) {
		return this.showAlertDialog(getString(R.string.dialog_alert), msg, new DialogInterface
				.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}, null);
	}

	protected void showDatePicker(int style, final TextView textView) {

		String time = textView.getText().toString();
		int year = Integer.valueOf(time.substring(0, 4));
		int monthOfYear = Integer.valueOf(time.substring(5, 7)) - 1;
		int dayOfMonth = Integer.valueOf(time.substring(8));
		DatePickerDialog dialog = new DatePickerDialog(mContext, style, new DatePickerDialog
				.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
				String strMonth = ++month > 9 ? String.valueOf(month) : "0" + month;
				String strDay = dayOfMonth > 9 ? String.valueOf(dayOfMonth) : "0" + dayOfMonth;
				textView.setText(year + "-" + strMonth + "-" + strDay);
			}
		}, year, monthOfYear, dayOfMonth);
		dialog.show();
	}
}
