package com.smzskj.crk.offline;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseActivity;
import com.smzskj.crk.net.HttpJsonRequest;
import com.smzskj.crk.net.Method;
import com.smzskj.crk.offline.adapter.PdOfflineUpAdapter;
import com.smzskj.crk.offline.bean.OfflineBean;
import com.smzskj.crk.offline.bean.OfflinePdBean;
import com.smzskj.crk.offline.bean.OfflinePdObjBean;
import com.smzskj.crk.offline.db.OfflinePdHelper;
import com.smzskj.crk.offline.db.PdDBUtils;
import com.smzskj.crk.utils.JsonUtils;
import com.smzskj.crk.utils.L;
import com.smzskj.crk.utils.ThreadPoolUtils;
import com.smzskj.crk.utils.UserInfo;
import com.smzskj.crk.view.xlistview.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ztt on 2017/2/25.
 * <p>
 * 离线盘点上传
 */

public class PdOfflineUpActivity extends BaseActivity {

	private List<OfflineBean> datas = new ArrayList<>();
	private TextView tvScs;
	private TextView tvPds;
	private String ry_dm;
	private String db_dm;

	public static void startPdOfflineUpActivity(Activity activity, int pds, int scs, String rq) {
		Intent intent = new Intent(activity, PdOfflineUpActivity.class);
		intent.putExtra("rq", rq);
		activity.startActivity(intent);
	}

	private int pds, scs;
	private String rq;

	private Button btnUp;
	private XListView listView;
	private BaseAdapter adapter;
	private PdDBUtils pdDBUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pdofflineup);
		addBackListener();
		setTitle("离线盘点上传");

		initView();
		initDate();
	}

	private void initDate() {
		pdDBUtils = new PdDBUtils(mContext);
		ry_dm = mSp.getString(UserInfo.SP_USER_CODE, "");
		db_dm = mSp.getString(UserInfo.SP_DB_CODE, "");

		initSl();

		if (TextUtils.isEmpty(ry_dm) || TextUtils.isEmpty(db_dm)) {
			makeShortToase("人员代码或数据库代码为空");
		} else {
			datas.addAll(pdDBUtils.queryAllList(rq, ry_dm, db_dm));
			adapter.notifyDataSetChanged();
		}
	}

	private void initSl() {
		int[] sl = pdDBUtils.queryCounts(rq,ry_dm,db_dm);
		pds = sl[0];
		scs = sl[1];
		tvPds.setText(String.format(getString(R.string.pds_int), pds));
		tvScs.setText(String.format(getString(R.string.scs_int), scs));
	}

	private void initView() {
		Intent intent = getIntent();
		rq = intent.getStringExtra("rq");

		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.item_pdoffline, null, false);

		TextView tvSjk = findView(view, R.id.item_pfoffline_tv_sjk);
		TextView tvKf = findView(view, R.id.item_pfoffline_tv_kf);
		TextView tvRy = findView(view, R.id.item_pfoffline_tv_ry);
		TextView tvRq = findView(view, R.id.item_pfoffline_tv_rq);
		tvPds = findView(view, R.id.item_pfoffline_tv_pds);
		tvScs = findView(view, R.id.item_pfoffline_tv_scs);

		tvSjk.setText("数据库:" + mSp.getString(UserInfo.SP_DB_NAME, ""));
		tvKf.setText("库房:" + mSp.getString(UserInfo.SP_CK_NAME, ""));
		tvRy.setText("人员:" + mSp.getString(UserInfo.SP_USER_NAME, ""));
		tvRq.setText("日期:" + rq);


		btnUp = findView(view, R.id.pdup_btn_up);
		btnUp.setVisibility(View.VISIBLE);
		listView = findView(R.id.pdup_lv);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		listView.addHeaderView(view);
		adapter = new PdOfflineUpAdapter(mContext, datas);
		listView.setAdapter(adapter);
		btnUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (pds > scs) {
					pd_pd_lx();
				} else {
					makeShortToase("没有未上传数据");
				}
			}
		});
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position,
										   long
												   id) {
				final int p = (int) id;
				showAlertDialog("删除", "是否要删除" + datas.get(p).getPch() + "?", new DialogInterface
						.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						int id = pdDBUtils.deletePchSph(datas.get(p).getSph(), datas.get(p).getPch
								());
						if (id > 0) {
							if (OfflinePdHelper.SUCCESS.equals(datas.get(p).getZt())) {
								tvScs.setText(String.format(getString(R.string.scs_int), --scs));
							}
							datas.remove(p);
							adapter.notifyDataSetChanged();
							tvPds.setText(String.format(getString(R.string.pds_int), --pds));

						} else {
							makeShortToase("删除失败，请重试");
						}
						dialog.cancel();
					}
				}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				return false;
			}
		});
	}


	private void pd_pd_lx() {

		try {
			JSONArray jp_sub_a = new JSONArray();
			for (OfflineBean bean : datas) {
				if (!OfflinePdHelper.SUCCESS.equals(bean.getZt())) {
					JSONObject jp_sub = new JSONObject();
					jp_sub.put("id", bean.getId());
					jp_sub.put("rq", bean.getRq());
					jp_sub.put("kf", bean.getKf());
					jp_sub.put("ry", bean.getRy_dm());
					jp_sub.put("sph", bean.getSph());
					jp_sub.put("pch", bean.getPch());
					jp_sub_a.put(jp_sub);
				}
			}
			String json = jp_sub_a.toString();
			showLoadDialog();
			HttpJsonRequest request = new HttpJsonRequest(new PdBackListener(),
					Method.SERVICE_NAME_PD, Method.PD_PD_LX, json);
			ThreadPoolUtils.execute(request);
		} catch (JSONException e) {
			cancleLoadDialog();
		}
	}

	class PdBackListener implements HttpJsonRequest.CallbackListener {

		@Override
		public void callBack(String result) {
			L.e(result);
			cancleLoadDialog();
			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
				return;
			}
			OfflinePdBean bean = JsonUtils.getJsonParseObject(result, OfflinePdBean.class);
			if (bean == null) {
				makeShortToase(R.string.loading_error);
				return;
			}
			if ("true".equals(bean.getRes())) {
				List<OfflinePdObjBean> list = JsonUtils.getJsonParseArray(bean.getRows(),
						OfflinePdObjBean.class);
				if (list != null) {
					for (OfflinePdObjBean objBean : list) {
						if (OfflinePdHelper.SUCCESS.equals(objBean.getErr_code())) {
							pdDBUtils.deleteID(objBean.getId());
						} else {
							pdDBUtils.updateZt(objBean.getId(), objBean.getErr_code());
						}
					}
					datas.clear();
					adapter.notifyDataSetChanged();
					datas.addAll(pdDBUtils.queryAllList(rq, ry_dm, db_dm));
					adapter.notifyDataSetChanged();
					initSl();
				} else {
					makeShortToase("上传失败");
				}
			} else {
				makeShortToase("上传失败");
			}
		}
	}
}
