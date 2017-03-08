package com.smzskj.crk.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import com.smzskj.crk.R;

/**
 * Created by ztt on 2017/1/16.
 *
 * 加载dialog
 */

public class LoadProgressDialog extends ProgressDialog {

	public LoadProgressDialog(Context context) {
		this(context, R.style.ProgressDialog);
	}

	public LoadProgressDialog(Context context, int theme) {
		this(context, theme , null);
	}

	public LoadProgressDialog(Context context, int theme, String msg) {
		super(context, theme);
		setCanceledOnTouchOutside(false);
		if (TextUtils.isEmpty(msg)) {
			setMessage(context.getResources().getString(R.string.loading));
		} else {
			setMessage(msg);
		}
	}
}
