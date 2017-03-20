package com.smzskj.crk.offline.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseActivity;
import com.smzskj.crk.net.HttpJsonRequest;
import com.smzskj.crk.net.Method;
import com.smzskj.crk.offline.bean.OfflineSpBean;
import com.smzskj.crk.offline.db.SpDbUtils;
import com.smzskj.crk.utils.JsonUtils;
import com.smzskj.crk.utils.L;
import com.smzskj.crk.utils.ThreadPoolUtils;

/**
 * Created by ztt on 2017/2/21.
 * <p>
 * 离线操作
 */

public class OffineActivity extends BaseActivity implements View.OnClickListener {

	public static void startOffineActivity(Activity context) {
		context.startActivity(new Intent(context, OffineActivity.class));
	}

	private Button btnPd, btnCk, btnPdUp, btnCkUp, btnUpdateSp;
	private Button btnRk, btnRkUp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offline);
		addBackListener();
		setTitle(R.string.offline);

		initView();

		handler = new Handler(mContext.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				cancleLoadDialog();
				makeShortToase("商品保存成功");
			}
		};
	}

	private void initView() {
		btnPd = findView(R.id.offline_btn_pd);
		btnCk = findView(R.id.offline_btn_ck);
		btnPdUp = findView(R.id.offline_btn_pd_up);
		btnCkUp = findView(R.id.offline_btn_ck_pu);
		btnUpdateSp = findView(R.id.offline_btn_update_sp);
		btnRk = findView(R.id.offline_btn_rk);
		btnRkUp = findView(R.id.offline_btn_rk_up);

		btnPd.setOnClickListener(this);
		btnCk.setOnClickListener(this);
		btnPdUp.setOnClickListener(this);
		btnCkUp.setOnClickListener(this);
		btnUpdateSp.setOnClickListener(this);
		btnRk.setOnClickListener(this);
		btnRkUp.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btnPd) {
			OfflinePdActivity.startPdOfflineActivity(mContext);
		} else if (v == btnPdUp) {
			OffinePdListActivity.startPdOfflineListActivity(mContext);
		} else if (v == btnRk) {
			OfflineInActivity.startInOfflineActivity(mContext);
		} else if (v == btnRkUp) {
			OfflineInListActivity.startInOfflineListActivity(mContext);
		}  else if (v == btnCk) {
			makeShortToase("功能开发中");
		}else if (v == btnCkUp) {
			makeShortToase("功能开发中");
		} else if (v == btnUpdateSp) {
			pd_lx_splb();
		}
	}

	private void pd_lx_splb() {
		showLoadDialog();
		HttpJsonRequest request = new HttpJsonRequest(new SpBackListener(),
				Method.SERVICE_NAME_PD, Method.PD_LX_SPLB);
		ThreadPoolUtils.execute(request);
	}

	class SpBackListener implements HttpJsonRequest.CallbackListener {

		@Override
		public void callBack(String result) {
			L.e(result);
			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
			}

			final OfflineSpBean spBean = JsonUtils.getJsonParseObject(result, OfflineSpBean.class);
			if (spBean == null || spBean.getRows() == null || spBean.getRows().size() == 0) {
				makeShortToase(R.string.loading_empty);
				return;
			}
			cancleLoadDialog();

			L.e("数量：" + spBean.getRows().size());

			showLoadDialog();
			ThreadPoolUtils.execute(new Runnable() {
				@Override
				public void run() {
					SpDbUtils spDbUtils = new SpDbUtils(mContext);
					spDbUtils.deleteSp();
					spDbUtils.insertSpList(spBean.getRows());
					handler.sendEmptyMessage(0);
				}
			});

		}
	}

	private Handler handler;
}
