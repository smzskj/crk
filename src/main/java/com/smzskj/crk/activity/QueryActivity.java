package com.smzskj.crk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseActivity;

/**
 * Created by ztt on 2017/1/22.
 */

public class QueryActivity extends BaseActivity implements View.OnClickListener{

	private String repertoryCode;
	private String repertoryName;

	private Button btnCk,btnRk,btnPd;

	public static void startQueryActivity(Context context, String repertoryCode, String
			repertoryName) {
		Intent intent = new Intent(context, QueryActivity.class);
		intent.putExtra("repertoryCode", repertoryCode);
		intent.putExtra("repertoryName", repertoryName);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkLogin();
		setContentView(R.layout.activity_query);
		setTitle("查询");
		addBackListener();

		repertoryCode = getIntent().getStringExtra("repertoryCode");
		repertoryName = getIntent().getStringExtra("repertoryName");


		btnCk = findView(R.id.query_btn_out);
		btnRk = findView(R.id.query_btn_in);
		btnPd = findView(R.id.query_btn_pd);
		btnCk.setOnClickListener(this);
		btnRk.setOnClickListener(this);
		btnPd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btnRk) {
			InListActivity.startInListActivity(mContext,repertoryCode,repertoryName);
		} else if (v == btnCk) {
			OutListActivity.startOutListActivity(mContext,repertoryCode,repertoryName);
		} else if (v == btnPd) {
			PdListActivity.startPdListActivity(mContext);
		}
	}
}
