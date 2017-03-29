package com.smzskj.crk.offline.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseActivity;
import com.smzskj.crk.offline.adapter.OfflineInListAdapter;
import com.smzskj.crk.offline.bean.OfflineInListBean;
import com.smzskj.crk.offline.db.InDbUtils;
import com.smzskj.crk.utils.UserInfo;
import com.smzskj.crk.view.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ztt on 2017/3/9.
 * <p>
 * 离线入库上传列表
 */

public class OfflineInListActivity extends BaseActivity implements View.OnClickListener, XListView
		.IXListViewListener {


	private Button btnRqq, btnRq, btnQuery;
	private XListView listView;
	private InDbUtils inDbUtils;
	private String zdr_dm;
	private String sjk_dm;
	private String rkdd_dm;
	private List<OfflineInListBean> datas = new ArrayList<>();
	private OfflineInListAdapter adapter;

	private String rqq, rq;
	private int page = 0;

	public static void startInOfflineListActivity(Activity context) {
		context.startActivity(new Intent(context, OfflineInListActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offline_pd_list);
		setTitle("离线入库列表");
		addBackListener();
		inDbUtils = new InDbUtils(mContext);
		zdr_dm = mSp.getString(UserInfo.SP_USER_CODE, "");
		sjk_dm = mSp.getString(UserInfo.SP_DB_CODE, "");
		rkdd_dm = mSp.getString(UserInfo.SP_CK_CODE, "");
		initView();

		onRefresh();
	}

	private void initView() {
		btnRq = findView(R.id.pdoffline_btn_rq);
		btnRqq = findView(R.id.pdoffline_btn_rqq);
		btnQuery = findView(R.id.pdoffline_btn_query);
		btnRqq.setOnClickListener(this);
		btnQuery.setOnClickListener(this);
		btnRq.setOnClickListener(this);
		listView = findView(R.id.pdoffline_lv);
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(false);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int p = (int) id;
				OfflineInUpActivity.startOfflineInUpActivity(mContext,datas.get(p).getDjhm(),datas.get(p).getZt()
				,datas.get(p).getRq(),""+datas.get(p).getCount());
			}
		});

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
				showAlertDialog("是否删除此离线入库数据？", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						inDbUtils.deleteDh(datas.get((int) id).getDjhm());
						onRefresh();
					}
				});
				return false;
			}
		});

		String zdr = mSp.getString(UserInfo.SP_USER_NAME, "");
		String sjk = mSp.getString(UserInfo.SP_DB_NAME, "");
		String rkdd = mSp.getString(UserInfo.SP_CK_NAME, "");
		adapter = new OfflineInListAdapter(mContext, datas, sjk, rkdd, zdr);
		listView.setAdapter(adapter);

		rq = getToday();
		rqq = rq;
		btnRqq.setText(rq);
		btnRq.setText(rq);
	}

	@Override
	public void onClick(View v) {
		if (v == btnRqq) {
			showDatePicker(0, btnRqq);
		} if (v == btnRq) {
			showDatePicker(0, btnRq);
		} else if (v == btnQuery) {
			rqq = btnRqq.getText().toString();
			rq = btnRq.getText().toString();

			if (TextUtils.isEmpty(zdr_dm) || TextUtils.isEmpty(rkdd_dm)) {
				makeShortToase("人员代码或库房代码为空");
			} else {
				onRefresh();
			}
		}
	}

	@Override
	public void onRefresh() {
		datas.clear();
		adapter.notifyDataSetChanged();
		page = 0;
		List<OfflineInListBean> data = inDbUtils.queryInList(zdr_dm, sjk_dm, rkdd_dm, rqq, rq, page);
		datas.addAll(data);
		adapter.notifyDataSetChanged();
		if (data.size() == 0) {
			makeShortToase(R.string.loading_empty);
		} else if (data.size() == 10) {
			listView.setPullLoadEnable(true);
		} else {
			listView.setPullLoadEnable(false);
		}
		listView.stopRefresh();
	}

	@Override
	public void onLoadMore() {
		List<OfflineInListBean> data = inDbUtils.queryInList(zdr_dm, sjk_dm, rkdd_dm, rqq, rq, ++page);
		if (data.isEmpty()) {
			listView.setPullLoadEnable(false);
		} else {
			datas.addAll(data);
			adapter.notifyDataSetChanged();
		}
		listView.stopLoadMore();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		onRefresh();
	}
}
