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
import com.smzskj.crk.bean.OutBean;
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

public class OutListActivity extends BaseActivity implements View.OnClickListener
		, XListView.IXListViewListener {

	private String repertoryCode;
	private String repertoryName;

	private LinearLayout llOut, llQuery;
	private XListView listView;

	private List<OutBean.RowsBean> datas = new ArrayList<>();
	private OutAdapter adapter;

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

	public static void startOutListActivity(Context context, String repertoryCode, String
			repertoryName) {
		Intent intent = new Intent(context, OutListActivity.class);
		intent.putExtra("repertoryCode", repertoryCode);
		intent.putExtra("repertoryName", repertoryName);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkLogin();
		setContentView(R.layout.activity_out_list);
		repertoryCode = getIntent().getStringExtra("repertoryCode");
		repertoryName = getIntent().getStringExtra("repertoryName");

		inflater = LayoutInflater.from(mContext);
		player = MediaPlayer.create(getApplicationContext(), R.raw.scan);

		initView();
		rq = getMonthFirst();
		rq2 = getToday();
		onRefresh();
	}

	private void initView() {
		addBackListener();
		setTitle(R.string.repertory_out);
		llOut = findView(R.id.out_ll_out);
		llOut.setOnClickListener(this);
		llQuery = findView(R.id.out_ll_query);
		llQuery.setOnClickListener(this);

		listView = findView(R.id.out_lv);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);

		adapter = new OutAdapter(mContext, datas);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if (v == llOut) {
			OutActivity.startOutActivity(mContext, repertoryCode, repertoryName);
		} else if (v == llQuery) {
			queryDialog();
		} else if (v == ibSm) {
			scanner();
		} else if (v == rlRq) {
			showDatePicker(0,edRq);
		} else if (v == rlRq2) {
			showDatePicker(0,edRq2);
		} else if (v == btnQuery) {
			// 商品号
			sph = edSph.getText().toString();
			// 商品名修改为单据号
			spm = edSpm.getText().toString();
			// 时间
			rq = edRq.getText().toString();
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

	private void scanner() {
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
	public void onRefresh() {
		currentPage = 1;
		listView.setRefreshTime();
		datas.clear();
		adapter.notifyDataSetChanged();
		ck_get_ckinfo(currentPage, rq);
	}

	@Override
	public void onLoadMore() {
		if (currentPage <= pageCount) {
			ck_get_ckinfo(currentPage, rq);
		}
	}

	/**
	 * 出库信息查询
	 */
	private void ck_get_ckinfo(int page, String rq) {
		showLoadDialog();
		String pageStr = "" + page;
		HttpJsonRequest request = new HttpJsonRequest(new CkinfoBackListener(),
				Method.SERVICE_NAME_CK, Method.CK_GET_CKINFO
				, UserInfo.RY_NAME, sph, spm, rq,rq2, pageStr, pageSize);
		ThreadPoolUtils.execute(request);
	}

	class CkinfoBackListener implements HttpJsonRequest.CallbackListener {

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
			OutBean outBean = JsonUtils.getJsonParseObject(result, OutBean.class);
			if (outBean == null || outBean.getRows() == null || outBean.getRows().size() == 0) {
				makeShortToase(R.string.loading_empty);
			} else {
				datas.addAll(outBean.getRows());
				adapter.notifyDataSetChanged();

				currentPage++;
				try {
					pageCount = Integer.valueOf(outBean.getTotalPage());
				} catch (NumberFormatException e) {
					pageCount = 0;
				}

				listView.setPullLoadEnable((currentPage <= pageCount));
			}
		}
	}

	class OutAdapter extends BaseViewAdapter<OutBean.RowsBean> {

		public OutAdapter(Context context, List<OutBean.RowsBean> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_out, parent, false);
			}

			TextView tvDjhm = BaseViewHolder.get(convertView, R.id.out_item_tv_djhm);
			TextView tvBh = BaseViewHolder.get(convertView, R.id.out_item_tv_bh);
			TextView tvSpmc = BaseViewHolder.get(convertView, R.id.out_item_tv_spmc);
			TextView tvPch = BaseViewHolder.get(convertView, R.id.out_item_tv_pch);
			TextView tvDw = BaseViewHolder.get(convertView, R.id.out_item_tv_dw);
			TextView tvZy = BaseViewHolder.get(convertView, R.id.out_item_tv_zy);
			TextView tvThdd = BaseViewHolder.get(convertView, R.id.out_item_tv_thdd);
			TextView tvThrq = BaseViewHolder.get(convertView, R.id.out_item_tv_thrq);
			TextView tvKfmc = BaseViewHolder.get(convertView, R.id.out_item_tv_kfmc);
			TextView tvSl = BaseViewHolder.get(convertView, R.id.out_item_tv_sl);

			OutBean.RowsBean bean = list.get(position);
			tvDjhm.setText("单据号码:" + bean.get单据号码());
			tvBh.setText("编号:" + bean.get编号());
			tvSpmc.setText("商品名称:" + bean.get商品名称());
			tvPch.setText("批次号:" + bean.get批次号());
			tvDw.setText("单位:" + bean.get单位());
			tvZy.setText("摘要:" + bean.get摘要());
			tvThdd.setText("提货地点:" + bean.get提货地点());
			tvThrq.setText("提货日期:" + bean.get日期());
			tvKfmc.setText("库房名称:" + bean.get库房名称());
			tvSl.setText("数量:" + bean.get数量());

			tvThdd.setVisibility(View.GONE);
			return convertView;
		}
	}
}
