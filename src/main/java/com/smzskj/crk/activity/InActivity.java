package com.smzskj.crk.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseActivity;
import com.smzskj.crk.base.BaseViewAdapter;
import com.smzskj.crk.base.BaseViewHolder;
import com.smzskj.crk.bean.InRkBean;
import com.smzskj.crk.bean.InSbmBean;
import com.smzskj.crk.bean.InSpBean;
import com.smzskj.crk.bean.RkDhBean;
import com.smzskj.crk.net.HttpJsonRequest;
import com.smzskj.crk.net.Method;
import com.smzskj.crk.utils.Constants;
import com.smzskj.crk.utils.JsonUtils;
import com.smzskj.crk.utils.L;
import com.smzskj.crk.utils.ScannerUtils;
import com.smzskj.crk.utils.ThreadPoolUtils;
import com.smzskj.crk.utils.UserInfo;
import com.smzskj.crk.view.ListViewInScroll;
import com.smzskj.crk.view.xlistview.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ztt on 2017/1/17.
 * <p>
 * 入库
 * <p>
 * 1、获得一个单号：rk_get_dh
 * 2、根据商品号获得商品信息列表，dialog中选择一个商品(对此商品入库)：rk_get_spinfo
 * 3、扫描输入商品码，添加到列表
 * 3+1、切换商品
 * 4、入库：rk_rk_v2
 */

public class InActivity extends BaseActivity implements View.OnClickListener, View
		.OnFocusChangeListener, XListView.IXListViewListener {

	private String repertoryCode;
	private String repertoryName;

	private EditText edSph, tvSbm;
	private TextView edSpm;
	private TextView tvCount, tvDh, tvCk;
	private ImageButton ibScanner;
	private ImageButton btnScanner;
	private LinearLayout llIn, llCancle, llQh;

	private String getDh = "获取单号";

	private MediaPlayer player;
	private ScannerUtils scanner;
	private ScannerUtils scannerSph;

	private LayoutInflater inflater;
	private AlertDialog dialog;

	private List<InSpBean.RowsBean> datas = new ArrayList<>();
	private SpAdapter adapter;
	private XListView listView;

	private String dw = "";
	private List<String> sbmList = new ArrayList<>();
	/**
	 * 商品信息
	 */
	private List<InSbmBean> sbmBeanList = new ArrayList<>();

	private EditText edSbm;
	private Button btnSbm;
	/**
	 * 商品码
	 */
	private ListViewInScroll lvSbm;
	/**
	 * 商品码适配器
	 */
	private SbhAdapter adapterSbm;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkLogin();
		setContentView(R.layout.activity_in);
		repertoryCode = getIntent().getStringExtra("repertoryCode");
		repertoryName = getIntent().getStringExtra("repertoryName");
		player = MediaPlayer.create(getApplicationContext(), R.raw.scan);
		inflater = LayoutInflater.from(mContext);

		initView();
		rk_get_dh();
	}

	public static void startInActivity(Context context, String repertoryCode, String
			repertoryName) {
		Intent intent = new Intent(context, InActivity.class);
		intent.putExtra("repertoryCode", repertoryCode);
		intent.putExtra("repertoryName", repertoryName);
		context.startActivity(intent);
	}

	private void initView() {
		addBackListener();
		setTitle(R.string.repertory_in);

		tvDh = findView(R.id.in_ed_dh);
		edSph = findView(R.id.in_ed_sph);
		edSpm = findView(R.id.in_ed_spm);
		tvSbm = findView(R.id.in_tv_sbm);
		tvCount = findView(R.id.in_tv_count);
		tvCk = findView(R.id.in_tv_ck);
		llIn = findView(R.id.in_ll_in);
		llCancle = findView(R.id.in_ll_query);
		llQh = findView(R.id.in_ll_qh);
		ibScanner = findView(R.id.in_ib_sm);
		btnScanner = findView(R.id.in_btn_scanner);

		tvDh.setText(getDh);
		tvCk.setText(repertoryName);

		tvDh.setOnClickListener(this);
		llIn.setOnClickListener(this);
		llCancle.setOnClickListener(this);
		llQh.setOnClickListener(this);
		ibScanner.setOnClickListener(this);
		btnScanner.setOnClickListener(this);

		edSph.setOnFocusChangeListener(this);
		edSpm.setOnFocusChangeListener(this);

		edSph.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				dw = "";
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		edSpm.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				dw = "";
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		setRightBtnListener(getResources().getString(R.string.repertory_query), new View
				.OnClickListener() {


			@Override
			public void onClick(View v) {
				rk_get_spinfo();
			}
		});


		edSbm = findView(R.id.in_ed_sbm);
		btnSbm = findView(R.id.in_btn_sbm);
		btnSbm.setOnClickListener(this);
		lvSbm = findView(R.id.in_lv_sbm);
		adapterSbm = new SbhAdapter(mContext, sbmList);
		lvSbm.setAdapter(adapterSbm);
		lvSbm.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long
					id) {

				showAlertDialog("确定要删除" + sbmList.get(position) + "吗？", new DialogInterface.OnClickListener() {


					@Override
					public void onClick(DialogInterface dialog, int which) {
						sbmList.remove(sbmList.get(position));
						adapterSbm.notifyDataSetChanged();
						tvCount.setText("" + sbmList.size());

						dialog.cancel();
					}
				});
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == llIn) {
			showAlertDialog("是否要入库？", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					rk_rk_v2();
				}
			});
		} else if (v == llCancle) {
			showAlertDialog("是否要取消入库？", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					finish();
				}
			});
		} else if (v == btnScanner) {
			scanner();
		} else if (v == ibScanner) {
			scanner();
		} else if (v == tvDh) {
			if (getDh.equals(tvDh.getText())) {
				rk_get_dh();
			}
		} else if (v == btnSbm) {
			String sbm = edSbm.getText().toString().trim();
			edSbm.setText("");
			if (TextUtils.isEmpty(sbm)) {
				makeShortToase("请输入识别码");
			} else if (edSph.isEnabled()) {
				makeShortToase("请选择商品");
			} else {
				addSbm(sbm);
			}
		} else if (v == llQh) {
			// 切换商品
			switchSp();
		}
	}

	/**
	 * 切换商品
	 */
	private void switchSp() {
		edSph.setText("");
		edSph.setEnabled(true);
		edSpm.setText("");
		tvCount.setText("0");
		sbmList = new ArrayList<>();
		adapterSbm = new SbhAdapter(mContext, sbmList);
		lvSbm.setAdapter(adapterSbm);
	}


	/**
	 * 获得单号
	 */
	public void rk_get_dh() {
		showLoadDialog();
		HttpJsonRequest request = new HttpJsonRequest(new RkDhBackListener(),
				Method.SERVICE_NAME_RK, Method.RK_GET_DH);
		ThreadPoolUtils.execute(request);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {
			rk_get_spinfo();
		}
	}

	@Override
	public void onRefresh() {
		currentPage = 1;
		listView.setRefreshTime();
		datas.clear();
		adapter.notifyDataSetChanged();
		rk_get_spinfo();
	}

	@Override
	public void onLoadMore() {
		if (currentPage <= pageCount) {
			rk_get_spinfo();
		}
	}

	private class RkDhBackListener implements HttpJsonRequest.CallbackListener {

		@Override
		public void callBack(String result) {
			L.e(result);
			cancleLoadDialog();
			if (TextUtils.isEmpty(result)) {
				tvDh.setText(getDh);
				return;
			}
			RkDhBean bean = JsonUtils.getJsonParseObject(result, RkDhBean.class);
			if (bean == null) {
				tvDh.setText(getDh);
				return;
			}
			if ("100".equals(bean.getCode())) {
				String dh = bean.getRows().getDh();
				if (TextUtils.isEmpty(dh)) {
					tvDh.setText(getDh);
				} else {
					tvDh.setText(dh);
				}
			}
		}
	}

	/**
	 * 获得商品信息（商品名，商品编号。失去焦点就要重新查询）
	 * sp_code 商品编号
	 * sp_name 商品名称
	 * sp_txm 条形码
	 * page 页数
	 * pageSize 每页大小
	 * reType 数据返回类型 json/xml
	 */
	public void rk_get_spinfo() {
		showLoadDialog();

		String sph = edSph.getText().toString();
		String spm = edSpm.getText().toString();
		String pageStr = "" + currentPage;

		HttpJsonRequest request = new HttpJsonRequest(new SpInfoBackListener(),
				Method.SERVICE_NAME_RK, Method.RK_GET_SPINFO, sph, spm, "", pageStr, pageSize);
		ThreadPoolUtils.execute(request);
	}

	class SpInfoBackListener implements HttpJsonRequest.CallbackListener {

		@Override
		public void callBack(String result) {
			L.e(result);
			cancleLoadDialog();
			if (listView != null) {
				listView.stopRefresh();
				listView.stopLoadMore();
			}
			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
				return;
			}
			InSpBean bean = JsonUtils.getJsonParseObject(result, InSpBean.class);
			if (bean == null || bean.getRows() == null) {
				makeShortToase(R.string.loading_error);
				return;
			}

			if (bean.getRows().size() == 0) {
				makeShortToase(R.string.loading_empty);
				return;
			}

			datas.addAll(bean.getRows());
			// 如果只有一条记录，不需再选择，直接带入
			if (currentPage == 1 && bean.getRows().size() == 1) {
				selectSp(0);
				return;
			}

			if (currentPage == 1) {
				adapter = new SpAdapter(mContext, datas);
				if (dialog == null) {
					spDialog();
				}
			} else {
				if (dialog != null && dialog.isShowing() && listView != null && adapter != null) {
					adapter.notifyDataSetChanged();
				}
			}
			currentPage++;
			try {
				pageCount = Integer.valueOf(bean.getTotalPage());
			} catch (NumberFormatException e) {
				pageCount = 0;
			}
			if (listView != null) {
				listView.setPullLoadEnable((currentPage <= pageCount));
			}
		}
	}

	/**
	 * 选择商品
	 *
	 * @param index 选择
	 */
	private void selectSp(int index) {
		edSph.setText(datas.get(index).get编号().trim());
		edSpm.setText(datas.get(index).get商品名称().trim());
		dw = datas.get(index).get单位();
		edSph.setEnabled(false);
		edSpm.setEnabled(false);
		setRightBtnListener(getResources().getString(R.string.repertory_query), null);
		ibScanner.setOnClickListener(null);

		// 列表中是否存在此商品
		boolean spFlag = false;
		/*
		 * 选中商品
		 */
		for (InSbmBean bean : sbmBeanList) {
			// 如果商品已经存在
			if (datas.get(index).get编号().trim().equals(bean.getSph())) {
				spFlag = true;
				sbmList = bean.getSbmList();
				break;
			}
		}
		/*
		如果商品不存在，添加到商品列表
		 */
		if (!spFlag) {
			InSbmBean bean = new InSbmBean();
			bean.setSph(datas.get(index).get编号().trim());
			bean.setSpm(datas.get(index).get商品名称().trim());
			bean.setDw(datas.get(index).get单位().trim());
			sbmBeanList.add(bean);
			sbmList = bean.getSbmList();
		}
		tvCount.setText("" + sbmList.size());
		adapterSbm = new SbhAdapter(mContext, sbmList);
		lvSbm.setAdapter(adapterSbm);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == Constants.KEY_SCAN && event.getAction() == KeyEvent.ACTION_DOWN) {
			scanner();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 扫描结束才能扫描下一次
	 */
	private void scanner() {
		scanner = new ScannerUtils(new ScannerUtils.ScannerListener() {

			@Override
			public void onSuccess(String barCode) {
				player.start();
				barCode = barCode.trim();
				if (edSph.isEnabled()) {
					player.start();
					edSph.setText(barCode);
					if (scannerSph != null) {
						scannerSph.cloase();
						scannerSph = null;
					}
					rk_get_spinfo();
				} else {
					if (barCode.length() < 8) {
						makeShortToase(R.string.scanner_error);
						return;
					}

					if (sbmList.contains(barCode)) {
						makeShortToase("此商品标识码已经录入");
					} else {
						addSbm(barCode);
					}
				}

				if (scanner != null) {
					scanner.cloase();
					scanner = null;
				}
			}
		});
		scanner.scanner();
	}

	/**
	 * 添加商品号
	 * @param barCode 商品号
	 */
	private void addSbm(String barCode) {
		boolean isSbm = false;
		for (InSbmBean bean : sbmBeanList) {
			if (bean.getSbmList().contains(barCode)) {
				isSbm = true;
				break;
			}
		}
		if (!isSbm) {
			try {
				sbmList.add(0, barCode);
				adapterSbm.notifyDataSetChanged();
				tvCount.setText("" + sbmList.size());
			} catch (NumberFormatException e) {
				tvCount.setText("1");
			}
		} else {
			makeShortToase("批次号已经存在");
		}
	}

	/**
	 * 商品弹窗
	 */
	private void spDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.ProgressDialog);
		View view = inflater.inflate(R.layout.dialog_sp, null, false);
		listView = findView(view, R.id.sp_lv);
		if (adapter != null) {
			listView.setAdapter(adapter);
		}
		if (currentPage <= pageCount) {
			listView.setPullLoadEnable(true);
		} else {
			listView.setPullLoadEnable(false);
		}
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int p = (int) id;
				selectSp(p);
				dialog.cancel();
			}
		});
		Button btnCancle = findView(view, R.id.sp_cancle);
		btnCancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		builder.setView(view);
		dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface d) {
				currentPage = 1;
				listView = null;
				datas.clear();
				adapter = null;
				dialog = null;
			}
		});
	}

	class SpAdapter extends BaseViewAdapter<InSpBean.RowsBean> {

		public SpAdapter(Context context, List<InSpBean.RowsBean> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_sp, parent, false);
			}
			TextView tvSph = BaseViewHolder.get(convertView, R.id.sp_item_sph);
			TextView tvSpm = BaseViewHolder.get(convertView, R.id.sp_item_spm);
			TextView tvDw = BaseViewHolder.get(convertView, R.id.sp_item_dw);

			InSpBean.RowsBean bean = list.get(position);
			tvSph.setText("编号" + bean.get编号());
			tvSpm.setText("商品名称" + bean.get商品名称());
			tvDw.setText("单位" + bean.get单位());

			return convertView;
		}
	}


	/**
	 * 出库
	 */
	private void rk_rk() {

		JSONObject jp_sub = new JSONObject();
		try {
			String sjhm = tvDh.getText().toString();
			if (getDh.equals(sjhm)) {
				makeShortToase("请获取单号");
				return;
			}
			jp_sub.put("单据号码", sjhm);
			jp_sub.put("编号", edSph.getText().toString());
			jp_sub.put("商品名称", edSpm.getText().toString());
			jp_sub.put("入库地点", tvCk.getText().toString());
			jp_sub.put("制单人", UserInfo.RY_NAME);
			jp_sub.put("单位", dw.trim());
			JSONArray jp_sub_a = new JSONArray();


			for (String s : sbmList) {
				jp_sub_a.put(s);
			}
			jp_sub.put("批次号", jp_sub_a);

			String json = jp_sub.toString();
			showLoadDialog();
			HttpJsonRequest request = new HttpJsonRequest(new CkBackListener(),
					Method.SERVICE_NAME_RK, Method.RK_RK, json);
			ThreadPoolUtils.execute(request);
		} catch (JSONException e) {
			cancleLoadDialog();
		}
	}

	/**
	 * 出库V2 一个单号，多个商品
	 */
	private void rk_rk_v2() {

		JSONObject jp_sub = new JSONObject();
		try {
			String sjhm = tvDh.getText().toString();
			if (getDh.equals(sjhm)) {
				makeShortToase("请获取单号");
				return;
			}
			jp_sub.put("单据号码", sjhm);
			jp_sub.put("入库地点", tvCk.getText().toString());
			jp_sub.put("制单人", UserInfo.RY_NAME);

			JSONArray jsp_arr = new JSONArray();
			for (InSbmBean bean : sbmBeanList) {
				// 识别码数量为0，不上传
				if (bean.getSbmList().size() > 0) {
					JSONObject jsp = new JSONObject();
					jsp.put("编号", bean.getSph());
					jsp.put("商品名称", bean.getSpm());
					jsp.put("单位", bean.getDw());
					JSONArray jp_sub_a = new JSONArray();
					for (String string : bean.getSbmList()) {
						jp_sub_a.put(string);
					}
					jsp.put("批次号", jp_sub_a);
					jsp_arr.put(jsp);
				}
			}
			jp_sub.put("入库数据", jsp_arr);
			String json = jp_sub.toString();
			showLoadDialog();
			HttpJsonRequest request = new HttpJsonRequest(new CkBackListener(),
					Method.SERVICE_NAME_RK, Method.RK_RK_V2, json);
			ThreadPoolUtils.execute(request);
		} catch (JSONException e) {
			cancleLoadDialog();
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
				rk_get_dh();
				edSph.setText("");
				edSph.setEnabled(true);
				setRightBtnListener(getResources().getString(R.string.repertory_query), new View
						.OnClickListener() {


					@Override
					public void onClick(View v) {
						rk_get_spinfo();
					}
				});

				edSbm.setText("");
				edSpm.setText("");
				edSpm.setEnabled(true);
				tvCount.setText("");
				tvSbm.setText("");
				sbmList.clear();
				sbmBeanList.clear();
				adapterSbm.notifyDataSetChanged();
				ibScanner.setOnClickListener(InActivity.this);
			}
			makeShortToase(bean.getReason());
		}
	}


	class SbhAdapter extends BaseViewAdapter<String> {

		public SbhAdapter(Context context, List<String> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_tv, parent, false);
			}
			TextView textView = findView(convertView, R.id.item_tv_tv);
			textView.setText(list.get(position));
			return convertView;
		}
	}
}
