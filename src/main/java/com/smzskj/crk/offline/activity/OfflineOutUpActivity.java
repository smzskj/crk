package com.smzskj.crk.offline.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseActivity;
import com.smzskj.crk.bean.InRkBean;
import com.smzskj.crk.net.HttpJsonRequest;
import com.smzskj.crk.net.Method;
import com.smzskj.crk.offline.adapter.OfflineInUpAdapter;
import com.smzskj.crk.offline.bean.OfflineInUpBean;
import com.smzskj.crk.offline.db.OutDbUtils;
import com.smzskj.crk.utils.JsonUtils;
import com.smzskj.crk.utils.L;
import com.smzskj.crk.utils.ThreadPoolUtils;
import com.smzskj.crk.utils.UserInfo;
import com.smzskj.crk.view.xlistview.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ztt on 2017/3/13.
 *
 * 离线出库信息，上传
 */

public class OfflineOutUpActivity extends BaseActivity implements View.OnClickListener {

	private String djhm;
	private String zt;
	private String rq;
	private String sl;

	private String zdr_dm;
	private String sjk_dm;
	private String rkdd_dm;

	private OutDbUtils outDbUtils;
	private LayoutInflater layoutInflater;
	private XListView listView;
	private OfflineInUpAdapter adapter;

	private Button btnUp;
	private List<OfflineInUpBean> datas;
	private String rkdd;
	private String sj;
	private String sjk;
	private String fhr;
	private TextView tvZt;
	private TextView tvSl;

	public static void startOfflineOutUpActivity(Activity activity, String djhm, String zt,
												String rq, String sl,String sj) {
		Intent intent = new Intent(activity, OfflineOutUpActivity.class);
		intent.putExtra("djhm", djhm);
		intent.putExtra("zt", zt);
		intent.putExtra("rq", rq);
		intent.putExtra("sj", sj);
		intent.putExtra("sl", sl);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offline_pdup);
		addBackListener();
		setTitle("离线出库上传");
		djhm = getIntent().getStringExtra("djhm");
		zt = getIntent().getStringExtra("zt");
		rq = getIntent().getStringExtra("rq");
		sj = getIntent().getStringExtra("sj");
		sl = getIntent().getStringExtra("sl");
		zdr_dm = mSp.getString(UserInfo.SP_USER_CODE, "");
		sjk_dm = mSp.getString(UserInfo.SP_DB_CODE, "");
		rkdd_dm = mSp.getString(UserInfo.SP_CK_CODE, "");
		fhr = mSp.getString(UserInfo.SP_USER_NAME, "");
		sjk = mSp.getString(UserInfo.SP_DB_NAME, "");
		rkdd = mSp.getString(UserInfo.SP_CK_NAME, "");
		outDbUtils = new OutDbUtils(mContext);
		layoutInflater = LayoutInflater.from(mContext);

		initView();

		/*
		查询数据 并添加到listview中
		 */
		datas = outDbUtils.queryUpListBean(djhm, sjk_dm, rkdd_dm,sj);
		adapter = new OfflineInUpAdapter(mContext, datas, rkdd);
		listView.setAdapter(adapter);
		tvSl.setText("数量:" + datas.size());

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
				if (id < 0) {
					return false;
				}
				showAlertDialog("是否删除此批次号？", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						outDbUtils.deleteDjhmPch(djhm,datas.get((int) id).getPch(), sj);
						dialog.cancel();
						datas = outDbUtils.queryUpListBean(djhm, sjk_dm, rkdd_dm,sj);
						adapter = new OfflineInUpAdapter(mContext, datas, rkdd);
						listView.setAdapter(adapter);
						tvSl.setText("数量:" + datas.size());
					}
				});
				return true;
			}
		});
	}

	private void initView() {
		listView = findView(R.id.pdup_lv);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		View view = layoutInflater.inflate(R.layout.item_offline_in_up_head, null, false);
		TextView tvSjk = findView(view, R.id.item_offline_in_tv_sjk);
		TextView tvRy = findView(view, R.id.item_offline_in_tv_ry);
		TextView tvRq = findView(view, R.id.item_offline_in_tv_rq);
		TextView tvDh = findView(view, R.id.item_offline_in_tv_dh);
		tvZt = findView(view, R.id.item_offline_in_tv_zt);
		tvSl = findView(view, R.id.item_offline_in_tv_sl);

		tvSjk.setText("数据库:" + sjk);
		tvRy.setText("发货人:" + fhr);
		tvRq.setText("日期:" + rq);
		tvDh.setText("单据号:" + djhm);
		tvZt.setText("状态:" + zt);
		tvSl.setText("数量:" + sl);
		btnUp = findView(view, R.id.item_offline_in_btn_up);
		btnUp.setOnClickListener(this);
		listView.addHeaderView(view);
		if ("成功".equals(zt)) {
			btnUp.setVisibility(View.GONE);
		} else {
			btnUp.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onClick(View v) {
		if (v == btnUp) {
			showLoadDialog();

			ThreadPoolUtils.execute(new Runnable() {
				@Override
				public void run() {

					try {
						JSONObject jp_sub = new JSONObject();
						JSONArray jsp_arr = new JSONArray();
						for (OfflineInUpBean bean : datas) {
							JSONObject jsp = new JSONObject();
							jsp.put("库房名称", rkdd);
							jsp.put("单据号码", djhm);
							jsp.put("发货人", fhr);
							jsp.put("编号", bean.getBh());
							jsp.put("批次号", bean.getPch());
							jsp_arr.put(jsp);
						}
						jp_sub.put("出库商品",jsp_arr);
						String json = jp_sub.toString();
						HttpJsonRequest request = new HttpJsonRequest(new CkBackListener(),
								Method.SERVICE_NAME_CK, Method.CK_CK_LX, json);
						ThreadPoolUtils.execute(request);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}


	class CkBackListener implements HttpJsonRequest.CallbackListener {

		@Override
		public void callBack(String result) {
			L.e(result);
			cancleLoadDialog();
			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
				return;
			}
			InRkBean bean = JsonUtils.getJsonParseObject(result, InRkBean.class);
			if (bean == null) {
				makeShortToase(R.string.loading_error);
				return;
			}

			if ("true".equals(bean.getRes())) {
				outDbUtils.updateDjhmZt(djhm, "成功" , sj);
				btnUp.setVisibility(View.GONE);
				tvZt.setText("状态:成功");
			} else {
				outDbUtils.updateDjhmZt(djhm, bean.getReason(), sj);
			}
			makeShortToase(bean.getReason());
		}
	}
}
