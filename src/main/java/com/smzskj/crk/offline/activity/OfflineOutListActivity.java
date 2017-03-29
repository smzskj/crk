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
import com.smzskj.crk.offline.adapter.OfflineOutListAdapter;
import com.smzskj.crk.offline.bean.OfflineOutListBean;
import com.smzskj.crk.offline.db.OutDbUtils;
import com.smzskj.crk.utils.UserInfo;
import com.smzskj.crk.view.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ztt on 2017/3/20.
 * <p>
 * 出库列表
 */

public class OfflineOutListActivity extends BaseActivity implements View.OnClickListener, XListView
		.IXListViewListener {


	private Button btnRqq, btnRq, btnQuery;
	private XListView listView;
	private OutDbUtils outDbUtils;
	private String zdr_dm;
	private String sjk_dm;
	private String rkdd_dm;
	private List<OfflineOutListBean> datas = new ArrayList<>();
	private OfflineOutListAdapter adapter;

	private String rqq, rq;
	private int page = 0;


	public static void startOfflineOutListActivity(Activity context) {
		context.startActivity(new Intent(context, OfflineOutListActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offline_pd_list);
		setTitle("离线出库列表");
		addBackListener();
		outDbUtils = new OutDbUtils(mContext);
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
				OfflineOutUpActivity.startOfflineOutUpActivity(mContext, datas.get(p).getDjhm(), datas.get(p).getZt()
						, datas.get(p).getRq(), "" + datas.get(p).getCount(), datas.get(p).getSj());
			}
		});

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final int p = (int) id;
				showAlertDialog("是否要删除此离线出库数据？", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						outDbUtils.deleteDjhmPch(datas.get(p).getDjhm(), datas.get(p).getSj());
						onRefresh();
					}
				});
				return false;
			}
		});

		String zdr = mSp.getString(UserInfo.SP_USER_NAME, "");
		String sjk = mSp.getString(UserInfo.SP_DB_NAME, "");
		String rkdd = mSp.getString(UserInfo.SP_CK_NAME, "");
		adapter = new OfflineOutListAdapter(mContext, datas, sjk, rkdd, zdr);
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
		}
		if (v == btnRq) {
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
		List<OfflineOutListBean> data = outDbUtils.queryInList(zdr_dm, sjk_dm, rkdd_dm, rqq, rq, page);
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
		List<OfflineOutListBean> data = outDbUtils.queryInList(zdr_dm, sjk_dm, rkdd_dm, rqq, rq, ++page);
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
