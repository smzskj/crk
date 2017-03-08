package com.smzskj.crk.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseActivity;
import com.smzskj.crk.base.BaseViewAdapter;
import com.smzskj.crk.base.BaseViewHolder;
import com.smzskj.crk.bean.InBean;
import com.smzskj.crk.net.HttpJsonRequest;
import com.smzskj.crk.net.Method;
import com.smzskj.crk.utils.Constants;
import com.smzskj.crk.utils.JsonUtils;
import com.smzskj.crk.utils.L;
import com.smzskj.crk.utils.ScannerUtils;
import com.smzskj.crk.utils.ThreadPoolUtils;
import com.smzskj.crk.utils.UserInfo;
import com.smzskj.crk.view.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ztt on 2017/1/17.
 * <p>
 * 出库
 */

public class InListActivity extends BaseActivity implements View.OnClickListener, XListView
		.IXListViewListener {

	private String repertoryCode;
	private String repertoryName;

	private LinearLayout llIn, llQuery;
	private XListView listView;

	private List<InBean.RowsBean> datas = new ArrayList<>();
	private InAdapter adapter;

	private LayoutInflater inflater;
	private AlertDialog dialog;
	private ImageButton ibSm;
	private RelativeLayout rlRq,rlRq2;
	private EditText edSph, edSpm;
	private TextView edRq,edRq2;
	private Button btnQuery;
	private String sph = "";
	private String spm = "";
	private String rq = "";
	private String rq2 = "";

	private MediaPlayer player;
	private ScannerUtils scanner;

	public static void startInListActivity(Context context, String repertoryCode, String
			repertoryName) {
		Intent intent = new Intent(context, InListActivity.class);
		intent.putExtra("repertoryCode", repertoryCode);
		intent.putExtra("repertoryName", repertoryName);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkLogin();
		setContentView(R.layout.activity_in_list);
		repertoryCode = getIntent().getStringExtra("repertoryCode");
		repertoryName = getIntent().getStringExtra("repertoryName");

		inflater = LayoutInflater.from(mContext);

		initView();
		player = MediaPlayer.create(getApplicationContext(), R.raw.scan);


		rq = getMonthFirst();
		rq2 = getToday();
		onRefresh();
	}

	private void initView() {
		addBackListener();
		setTitle(R.string.repertory_in);
		llIn = findView(R.id.in_ll_in);
		llIn.setOnClickListener(this);
		llQuery = findView(R.id.in_ll_query);
		llQuery.setOnClickListener(this);

		listView = findView(R.id.in_lv);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);

		adapter = new InAdapter(mContext, datas);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				InListDetailActivity.startInListDetailActivity(mContext,datas.get((int)id).get单据号码());
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == llIn) {
			InActivity.startInActivity(mContext, repertoryCode, repertoryName);
		} else if (v == llQuery) {
			queryDialog();
		} else if (v == ibSm) {
			scanner();
		} else if (v == rlRq) {
			showDatePicker(0,edRq);
		}  else if (v == rlRq2) {
			showDatePicker(0,edRq2);
		} else if (v == btnQuery) {
			// 商品号
			sph = edSph.getText().toString();
			// 商品名修改为单据号
			spm = edSpm.getText().toString();
			// 时间
			rq = edRq.getText().toString();
			rq2 = edRq2.getText().toString();
			onRefresh();
			dialog.cancel();
		}
	}


	private void queryDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.ProgressDialog);

		View view = inflater.inflate(R.layout.dialog_query, null, false);
		ibSm = findView(view, R.id.query_ib_sm);
		ibSm.setOnClickListener(this);
		rlRq = findView(view, R.id.query_rl_rq);
		rlRq.setOnClickListener(this);
		rlRq2 = findView(view, R.id.query_rl_rq2);
		rlRq2.setOnClickListener(this);
		edSph = findView(view, R.id.query_ed_sph);
		edSpm = findView(view, R.id.query_ed_spm);
		edRq = findView(view, R.id.query_ed_rq);
		edRq2 = findView(view, R.id.query_ed_rq2);
		btnQuery = findView(view, R.id.query_btn_query);
		btnQuery.setOnClickListener(this);

		edRq.setText(rq);
		edRq2.setText(rq2);

		builder.setView(view);
		dialog = builder.show();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == Constants.KEY_SCAN && event.getAction() == KeyEvent.ACTION_UP) {
					L.e("key" + keyCode);
					scanner();
				}
				return false;
			}
		});
	}

	@Override
	public void onRefresh() {
		currentPage = 1;
		listView.setRefreshTime();
		datas.clear();
		adapter.notifyDataSetChanged();
		rk_get_rkinfo(currentPage, sph, spm, rq);
	}

	@Override
	public void onLoadMore() {
		if (currentPage <= pageCount) {
			rk_get_rkinfo(currentPage, sph, spm, rq);
		}
	}

	/**
	 * 入库信息查询
	 */
	private void rk_get_rkinfo(int page, String sph, String spm, String rq) {
		showLoadDialog();
		String pageStr = "" + page;
		HttpJsonRequest request = new HttpJsonRequest(new RkinfoBackListener(),
				Method.SERVICE_NAME_RK, Method.RK_GET_RKINFO
				, sph, spm, UserInfo.RY_NAME, rq, rq2 , pageStr, pageSize);
		ThreadPoolUtils.execute(request);
	}


	class RkinfoBackListener implements HttpJsonRequest.CallbackListener {

		@Override
		public void callBack(String result) {
			L.e(result);
			cancleLoadDialog();
			listView.stopRefresh();
			listView.stopLoadMore();
			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
				return;
			}
			InBean inBean = JsonUtils.getJsonParseObject(result, InBean.class);
			if (inBean == null || inBean.getRows() == null || inBean.getRows().size() == 0) {
				makeShortToase(R.string.loading_empty);
			} else {
				datas.addAll(inBean.getRows());
				adapter.notifyDataSetChanged();

				currentPage++;
				try {
					pageCount = Integer.valueOf(inBean.getTotalPage());
				} catch (NumberFormatException e) {
					pageCount = 0;
				}
				listView.setPullLoadEnable((currentPage <= pageCount));
			}
		}
	}

	class InAdapter extends BaseViewAdapter<InBean.RowsBean> {
		public InAdapter(Context context, List<InBean.RowsBean> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_in, parent, false);
			}

			TextView tvDjbh = BaseViewHolder.get(convertView, R.id.in_item_tv_djhm);
			TextView tvBh = BaseViewHolder.get(convertView, R.id.in_item_tv_bh);
			TextView tvSpmc = BaseViewHolder.get(convertView, R.id.in_item_tv_spmc);
			TextView tvPch = BaseViewHolder.get(convertView, R.id.in_item_tv_rks);
			TextView tvRkdd = BaseViewHolder.get(convertView, R.id.in_item_tv_rkdd);
			TextView tvRq = BaseViewHolder.get(convertView, R.id.in_item_tv_rq);

			InBean.RowsBean bean = list.get(position);
			tvDjbh.setText("单据号码:" + bean.get单据号码());
			tvBh.setText("编号:" + bean.get编号());
			tvSpmc.setText("商品名称:" + bean.get商品名称());
			tvPch.setText("入库数:" + bean.get入库数());
			tvRkdd.setText("入库地点:" + bean.get入库地点());
			tvRq.setText("日期:" + bean.get日期());

			return convertView;
		}
	}

	private void scanner(){
		scanner = new ScannerUtils(new ScannerUtils.ScannerListener() {

			@Override
			public void onSuccess(String barCode) {
				player.start();
				if (edSph != null) {
					edSph.setText(barCode);
				}
			}
		});
		scanner.scanner();
	}


	@Override
	protected void onStop() {
		super.onStop();
		if (scanner != null) {
			scanner.cloase();
			scanner = null;
		}
	}
}
