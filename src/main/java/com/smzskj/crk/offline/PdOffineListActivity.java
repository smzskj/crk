package com.smzskj.crk.offline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseActivity;
import com.smzskj.crk.offline.adapter.PdOfflineListAdapter;
import com.smzskj.crk.offline.bean.OfflineCountListBean;
import com.smzskj.crk.offline.db.PdDBUtils;
import com.smzskj.crk.utils.UserInfo;
import com.smzskj.crk.view.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ztt on 2017/2/25.
 *
 * 离线盘点列表
 */

public class PdOffineListActivity extends BaseActivity implements View.OnClickListener, XListView
		.IXListViewListener {


	private String ry_dm;
	private String db_dm;

	public static void startPdOfflineListActivity(Activity context) {
		context.startActivity(new Intent(context, PdOffineListActivity.class));
	}

	private Button btnRqq, btnRq, btnQuery;
	private XListView listView;
	private BaseAdapter adapter;

	private PdDBUtils pdDBUtils;
	private String rqq, rq;
	private List<OfflineCountListBean> dates = new ArrayList<>();
	private int page = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pdoffline_list);
		pdDBUtils = new PdDBUtils(mContext);
		initView();
	}

	private void initView() {
		addBackListener();
		setTitle("离线盘点列表");
		rq = getToday();
		ry_dm = mSp.getString(UserInfo.SP_USER_CODE, "");
		db_dm = mSp.getString(UserInfo.SP_DB_CODE, "");

		btnRq = findView(R.id.pdoffline_btn_rq);
		btnRqq = findView(R.id.pdoffline_btn_rqq);
		btnQuery = findView(R.id.pdoffline_btn_query);
		listView = findView(R.id.pdoffline_lv);
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(false);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int p = (int) id;
				PdOfflineUpActivity.startPdOfflineUpActivity(mContext, dates.get(p).getCount(),
						dates.get(p).getUpCount(), dates.get(p).getRq());
			}
		});

		String sjk = mSp.getString(UserInfo.SP_DB_NAME, "");
		String kf = mSp.getString(UserInfo.SP_CK_NAME, "");
		String ry = mSp.getString(UserInfo.SP_USER_NAME, "");
		adapter = new PdOfflineListAdapter(mContext, dates, sjk, kf, ry);
		listView.setAdapter(adapter);

		btnRq.setText(rq);
		btnRq.setOnClickListener(this);
		btnRqq.setText(rq);
		btnRqq.setOnClickListener(this);
		btnQuery.setOnClickListener(this);
		onClick(btnQuery);
	}

	@Override
	public void onClick(View v) {
		if (v == btnRqq) {
			showDatePicker(0, btnRqq);
		}
		if (v == btnRq) {
			showDatePicker(0, btnRq);
		} else if (v == btnQuery) {
			rqq = btnRqq.getText().toString().replace('-', '.');
			rq = btnRq.getText().toString().replace('-', '.');

			if (TextUtils.isEmpty(ry_dm) || TextUtils.isEmpty(db_dm)) {
				makeShortToase("人员代码或库房代码为空");
			} else {
				onRefresh();
			}
		}
	}

	@Override
	public void onRefresh() {
		listView.stopRefresh();
		page = 0;
		dates.clear();
		adapter.notifyDataSetChanged();
		List<OfflineCountListBean> list = pdDBUtils.queryCount(ry_dm, db_dm, rqq, rq, page);
		if (list.size() == 0) {
			listView.setPullLoadEnable(false);
		} else {
			listView.setPullLoadEnable(true);
			dates.addAll(list);
			adapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onLoadMore() {
		listView.stopLoadMore();
		List<OfflineCountListBean> list = pdDBUtils.queryCount(ry_dm, db_dm, rqq, rq, ++page);
		if (list.size() == 0) {
			listView.setPullLoadEnable(false);
		} else {
			listView.setPullLoadEnable(true);
			dates.addAll(list);
			adapter.notifyDataSetChanged();
		}
	}
}
