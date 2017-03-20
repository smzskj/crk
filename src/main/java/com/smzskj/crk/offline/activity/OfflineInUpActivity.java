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
import com.smzskj.crk.bean.InSbmBean;
import com.smzskj.crk.net.HttpJsonRequest;
import com.smzskj.crk.net.Method;
import com.smzskj.crk.offline.adapter.OfflineInUpAdapter;
import com.smzskj.crk.offline.bean.OfflineInUpBean;
import com.smzskj.crk.offline.db.InDbUtils;
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
 * 离线入库信息，上传
 */

public class OfflineInUpActivity extends BaseActivity implements View.OnClickListener {

	private String djhm;
	private String zt;
	private String rq;
	private String sl;

	private String zdr_dm;
	private String sjk_dm;
	private String rkdd_dm;

	private InDbUtils inDbUtils;
	private LayoutInflater layoutInflater;
	private XListView listView;
	private OfflineInUpAdapter adapter;

	private Button btnUp;
	private List<OfflineInUpBean> datas;
	private String rkdd;
	private String sjk;
	private String zdr;
	private TextView tvZt;
	private TextView tvSl;

	public static void startOfflineInUpActivity(Activity activity, String djhm, String zt,
												String rq, String sl) {
		Intent intent = new Intent(activity, OfflineInUpActivity.class);
		intent.putExtra("djhm", djhm);
		intent.putExtra("zt", zt);
		intent.putExtra("rq", rq);
		intent.putExtra("sl", sl);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offline_pdup);
		addBackListener();
		setTitle("离线入库上传");
		djhm = getIntent().getStringExtra("djhm");
		zt = getIntent().getStringExtra("zt");
		rq = getIntent().getStringExtra("rq");
		sl = getIntent().getStringExtra("sl");
		zdr_dm = mSp.getString(UserInfo.SP_USER_CODE, "");
		sjk_dm = mSp.getString(UserInfo.SP_DB_CODE, "");
		rkdd_dm = mSp.getString(UserInfo.SP_CK_CODE, "");
		zdr = mSp.getString(UserInfo.SP_USER_NAME, "");
		sjk = mSp.getString(UserInfo.SP_DB_NAME, "");
		rkdd = mSp.getString(UserInfo.SP_CK_NAME, "");
		inDbUtils = new InDbUtils(mContext);
		layoutInflater = LayoutInflater.from(mContext);

		initView();

		/*
		查询数据 并添加到listview中
		 */
		datas = inDbUtils.queryUpListBean(djhm, sjk_dm, rkdd_dm);
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
						inDbUtils.deleteDjhmPch(djhm,datas.get((int) id).getPch());
						dialog.cancel();
						datas = inDbUtils.queryUpListBean(djhm, sjk_dm, rkdd_dm);
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
		tvRy.setText("制单人:" + zdr);
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

					List<InSbmBean> upDate = inDbUtils.queryUpBean(djhm, sjk_dm);
					try {
						JSONArray jsp_arr = new JSONArray();
						for (InSbmBean bean : upDate) {
							JSONObject jsp = new JSONObject();
							jsp.put("入库地点", rkdd);
							jsp.put("制单人", zdr);
							jsp.put("编号", bean.getSph());

							JSONArray jp_sub_a = new JSONArray();
							for (String pch : bean.getSbmList()) {
								jp_sub_a.put(pch);
							}
							jsp.put("批次号", jp_sub_a);
							jsp_arr.put(jsp);
						}

						String json = jsp_arr.toString();
						HttpJsonRequest request = new HttpJsonRequest(new CkBackListener(),
								Method.SERVICE_NAME_RK, Method.RK_RK_LX, json);
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
				inDbUtils.updateDjhmZt(djhm, "成功");
				btnUp.setVisibility(View.GONE);
				tvZt.setText("状态:成功");
			} else {
				inDbUtils.updateDjhmZt(djhm, bean.getReason());
			}
			makeShortToase(bean.getReason());
		}
	}
}
