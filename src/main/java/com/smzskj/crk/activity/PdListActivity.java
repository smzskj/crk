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
import com.smzskj.crk.bean.PdListBean;
import com.smzskj.crk.net.HttpJsonRequest;
import com.smzskj.crk.net.Method;
import com.smzskj.crk.utils.Constants;
import com.smzskj.crk.utils.JsonUtils;
import com.smzskj.crk.utils.L;
import com.smzskj.crk.utils.ScannerUtils;
import com.smzskj.crk.utils.ThreadPoolUtils;
import com.smzskj.crk.utils.UserInfo;
import com.smzskj.crk.view.xlistview.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ztt on 2017/2/6.
 */

public class PdListActivity extends BaseActivity implements XListView
		.IXListViewListener, View.OnClickListener {

	private XListView listView;
	private static SimpleDateFormat sdf_ymd = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault
			());
	private String time;
	private List<PdListBean.RowsBean> datas = new ArrayList<>();
	private PdAdapter adapter;

	private LinearLayout llQuery;

	private LayoutInflater inflater;
	private AlertDialog dialog;
	private ImageButton ibSm;
	private RelativeLayout rlRq;
	private RelativeLayout rlRq2;
	private EditText edSph, edSpm;
	private TextView edRq;
	private TextView edRq2;
	private Button btnQuery;
	private String sph = "";
	private String spm = "";
	private String rq = "";
	private String rq2 = "";

	private MediaPlayer player;
	private ScannerUtils scanner;

	public static void startPdListActivity(Context context) {
		Intent intent = new Intent(context, PdListActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkLogin();
		setContentView(R.layout.activity_pdlist);
		addBackListener();
		setTitle("盘点");
		listView = findView(R.id.pd_lv);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		time = sdf_ymd.format(new Date(System.currentTimeMillis()));
		llQuery = findView(R.id.pd_ll_query);
		llQuery.setOnClickListener(this);

		adapter = new PdAdapter(mContext, datas);
		listView.setAdapter(adapter);
		inflater = LayoutInflater.from(mContext);
		player = MediaPlayer.create(getApplicationContext(), R.raw.scan);

		rq = getMonthFirst();
		rq2 = getToday();
		onRefresh();
	}

	@Override
	public void onRefresh() {
		datas.clear();
		adapter.notifyDataSetChanged();
		currentPage = 1;
		listView.setPullLoadEnable(false);
		pd_get_pdinfo();
	}

	@Override
	public void onLoadMore() {
		pd_get_pdinfo();
	}


	/**
	 * 获得商品信息
	 * 库房名
	 * 日期
	 */
	private void pd_get_pdinfo() {
		showLoadDialog();
		String kf = mSp.getString(UserInfo.SP_CK_NAME, "");
		HttpJsonRequest request = new HttpJsonRequest(new PdBackListener(),
				Method.SERVICE_NAME_PD, Method.PD_GET_PDINFO
				, kf.trim(), rq.replace("-","."), rq2.replace("-","."), sph, currentPage + "", pageSize);
		ThreadPoolUtils.execute(request);
	}

	@Override
	public void onClick(View v) {
		if (v == llQuery) {
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
			onRefresh();
			dialog.cancel();
		}
	}

	class PdBackListener implements HttpJsonRequest.CallbackListener {

		@Override
		public void callBack(String result) {
			L.e(result);
			cancleLoadDialog();
			listView.stopLoadMore();
			listView.stopRefresh();

			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
				return;
			}
			PdListBean bean = JsonUtils.getJsonParseObject(result, PdListBean.class);
			if (bean == null) {
				makeShortToase(R.string.loading_error);
				return;
			}

			if (bean.getRows() == null || bean.getRows().size() == 0) {
				makeShortToase(R.string.loading_empty);
				return;
			}
			datas.addAll(bean.getRows());
			adapter.notifyDataSetChanged();
			if (Integer.valueOf(bean.getTotalPage()) > currentPage) {
				currentPage++;
				listView.setPullLoadEnable(true);
			} else {
				listView.setPullLoadEnable(false);
			}
		}
	}

	private class PdAdapter extends BaseViewAdapter<PdListBean.RowsBean> {

		public PdAdapter(Context context, List<PdListBean.RowsBean> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_pdlist, parent, false);
			}

			TextView tvBh = BaseViewHolder.get(convertView, R.id.item_pd_bh);
			TextView tvSpmc = BaseViewHolder.get(convertView, R.id.item_pd_spmc);
			TextView tvDw = BaseViewHolder.get(convertView, R.id.item_pd_dw);
			TextView tvPdpch = BaseViewHolder.get(convertView, R.id.item_pd_pdpch);
			TextView tvPch = BaseViewHolder.get(convertView, R.id.item_pd_pch);
			TextView tvRq = BaseViewHolder.get(convertView, R.id.item_pd_rq);

			tvBh.setText("编号:" + list.get(position).get编号());
			tvSpmc.setText("商品名称:" + list.get(position).get商品名称());
			tvDw.setText("单位:" + list.get(position).get单位());
			tvPdpch.setText("盘点批次号:" + list.get(position).get盘点批次号());
			tvPch.setText("批次号:" + list.get(position).get批次号());
			tvRq.setText("日期:" + list.get(position).get日期());
			return convertView;
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
		LinearLayout linearLayout = findView(view, R.id.query_ll_djh);
		linearLayout.setVisibility(View.GONE);
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

}
