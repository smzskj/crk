package com.smzskj.crk.activity;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseActivity;
import com.smzskj.crk.base.BaseViewAdapter;
import com.smzskj.crk.base.BaseViewHolder;
import com.smzskj.crk.bean.InSpBean;
import com.smzskj.crk.bean.PdCheckBean;
import com.smzskj.crk.bean.PdMainBean;
import com.smzskj.crk.bean.PdSpInfoBean;
import com.smzskj.crk.bean.PdSubBean;
import com.smzskj.crk.net.HttpJsonRequest;
import com.smzskj.crk.net.Method;
import com.smzskj.crk.utils.Constants;
import com.smzskj.crk.utils.JsonUtils;
import com.smzskj.crk.utils.L;
import com.smzskj.crk.utils.ScannerUtils;
import com.smzskj.crk.utils.ThreadPoolUtils;
import com.smzskj.crk.utils.UserInfo;
import com.smzskj.crk.view.xlistview.XListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ztt 2017/2/6.
 */

public class PdActivity extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener {


	public static void startPdActivity(Context context) {
		Intent intent = new Intent(context, PdActivity.class);
		context.startActivity(intent);
	}

	private ScannerUtils scanner;
	private MediaPlayer player;

	private static SimpleDateFormat sdf_ymd = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault
			());

	private ImageButton ibSm1, ibSm2;
	private EditText edSpbh, edPch;
	private TextView tvSpmc, tvDw, tvKcs, tvPdsl ,tvPdpch;
	private LinearLayout llQr,llQh;


	private LayoutInflater inflater;
	private AlertDialog dialog;
	private List<InSpBean.RowsBean> datas = new ArrayList<>();
	private SpAdapter adapter;
	private XListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkLogin();
		setContentView(R.layout.activity_pd);
		addBackListener();
		setTitle("盘点");
		player = MediaPlayer.create(getApplicationContext(), R.raw.scan);
		inflater = LayoutInflater.from(mContext);

		initView();
	}

	private void initView() {

		setRightBtnListener("查询", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				rk_get_spinfo();
			}
		});

		ibSm1 = findView(R.id.pd_ib_sm);
		ibSm2 = findView(R.id.pd_ib_sm2);
		edPch = findView(R.id.pd_ed_pch);
		edSpbh = findView(R.id.pd_ed_spbh);
		tvSpmc = findView(R.id.pd_tv_spmc);
		tvDw = findView(R.id.pd_tv_dw);
		tvKcs = findView(R.id.pd_tv_kcs);
		tvPdsl = findView(R.id.pd_tv_pdsl);
		llQr = findView(R.id.pd_ll_qr);
		llQh = findView(R.id.pd_ll_qh);
		tvPdpch = findView(R.id.pd_tv_pdpch);

		ibSm1.setOnClickListener(this);
		ibSm2.setOnClickListener(this);
		llQr.setOnClickListener(this);
		llQh.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v == ibSm1) {
			scanner();
		} else if (v == ibSm2) {
			scanner();
		} else if (v == llQr) {
			pd_pd();
		} else if (v == llQh) {
			edPch.setText("");
			edSpbh.setEnabled(true);
			edSpbh.setText("");

			tvSpmc.setText("商品名称");
			tvDw.setText("单位");
			tvKcs.setText("库存数");
			tvPdsl.setText("盘点数量");
			tvPdpch.setText("");
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		L.e("event" + event.getAction());
		if (keyCode == Constants.KEY_SCAN && event.getAction() == KeyEvent.ACTION_DOWN) {
			scanner();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void scanner() {
		scanner = new ScannerUtils(new ScannerUtils.ScannerListener() {

			@Override
			public void onSuccess(String barCode) {
				player.start();

				if (edSpbh.isEnabled()) {
					edSpbh.setText(barCode);
					rk_get_spinfo();
				} else {
					edPch.setText(barCode);
					pd_pd();
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
	 * 获得商品信息
	 * 库房名
	 * 日期
	 */
	private void pd_get_spinfo() {
		showLoadDialog();
		String kf = mSp.getString(UserInfo.SP_CK_NAME, "");
		String rq = sdf_ymd.format(new Date(System.currentTimeMillis()));
		String spbh = edSpbh.getText().toString();
		HttpJsonRequest request = new HttpJsonRequest(new SpinfoBackListener(spbh),
				Method.SERVICE_NAME_PD, Method.PD_GET_SPINFO
				, kf, rq , spbh);
		ThreadPoolUtils.execute(request);
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

	class SpinfoBackListener implements HttpJsonRequest.CallbackListener {

		private String spbh;

		public SpinfoBackListener(String spbh) {
			this.spbh = spbh;
		}

		@Override
		public void callBack(String result) {
			L.e(result);
			cancleLoadDialog();

			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
			} else {
				PdSpInfoBean returnBean = JsonUtils.getJsonParseObject(result, PdSpInfoBean.class);
				if (returnBean == null) {
					makeShortToase(R.string.loading_error);
				} else {
					if ("100".equals(returnBean.getCode()) && returnBean.getRows() != null && returnBean.getRows() != null) {
						edSpbh.setEnabled(false);
						String sub = returnBean.getRows().getSub();
						String main = returnBean.getRows().getMain();
						if (!TextUtils.isEmpty(sub)) {
							tvPdpch.setText("");
							PdSubBean subBean = JsonUtils.getJsonParseObject(sub, PdSubBean.class);
							if (subBean != null && "100".equals(subBean.getCode())) {
								for (PdSubBean.RowsBean bean : subBean.getRows()) {
//									tvPdpch.append("\n    " + bean.get盘点批次号());
									tvPdpch.setText(bean.get盘点批次号() + "\n" + tvPdpch.getText().toString());
								}
							}
						}
						if (!TextUtils.isEmpty(main)) {
							PdMainBean mainBean = JsonUtils.getJsonParseObject(main, PdMainBean.class);
							if (mainBean != null && "100".equals(mainBean.getCode()) && mainBean.getRows().size() > 0) {
//								tvSpmc.setText("商品名称:" + mainBean.getRows().get(0).get商品名称());
//								tvDw.setText("单位" + mainBean.getRows().get(0).get单位());
								tvKcs.setText("库存数:" + mainBean.getRows().get(0).getZkcsl());
								tvPdsl.setText("盘点数量:" + mainBean.getRows().get(0).getYpdsl());
							}
						}
					} else {
						makeShortToase(R.string.loading_empty);
					}
				}
			}
		}
	}

	/**
	 * 盘点
	 */
	private void pd_pd() {
		if (edSpbh.isEnabled()) {
			makeShortToase("请查询商品信息");
			return;
		}
		String pch = edPch.getText().toString();
		if (TextUtils.isEmpty(pch)) {
			makeShortToase("批次号不能为空");
			return;
		}
		showLoadDialog();
		String kf = mSp.getString(UserInfo.SP_CK_NAME, "");
		String rq = sdf_ymd.format(new Date(System.currentTimeMillis()));
		JSONObject jp_sub = new JSONObject();
		try {
			jp_sub.put("编号",edSpbh.getText().toString());
			jp_sub.put("库房名称",kf);
			jp_sub.put("日期",rq);
			jp_sub.put("批次号", pch);
		} catch (JSONException e) {
			makeShortToase("参数异常");
		}

		HttpJsonRequest request = new HttpJsonRequest(new PdBackListener(pch),
				Method.SERVICE_NAME_PD, Method.PD_PD
				, jp_sub.toString());
		ThreadPoolUtils.execute(request);
	}

	class PdBackListener implements HttpJsonRequest.CallbackListener {

		private String pch;

		public PdBackListener(String pch) {
			this.pch = pch;
		}

		@Override
		public void callBack(String result) {
			L.e(result);
			cancleLoadDialog();

			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
			} else {
				PdCheckBean returnBean = JsonUtils.getJsonParseObject(result, PdCheckBean.class);
				if (returnBean == null) {
					makeShortToase(R.string.loading_error);
				} else {
					if ("true".equals(returnBean.getRes())) {
						makeShortToase(returnBean.getReason());
						edPch.setText("");
//						tvPdpch.append("\n    " + pch);
						tvPdpch.setText(pch + "\n" + tvPdpch.getText().toString());
						int size = 0;
						try {
							size = Integer.valueOf(tvPdsl.getText().toString().substring(5));
						} catch (NumberFormatException e) {
							size = 0;
						}
						size++;
						tvPdsl.setText("盘点数量：" + size);
					} else {
						if ("isAdd".equals(returnBean.getRows())) {
							AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
							builder.setMessage(returnBean.getReason());
							builder.setTitle("提示");
							builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
									pd_pd_add(pch);
								}
							});
							builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();

								}
							});
							builder.show();
						} else {
							makeShortToase(returnBean.getReason());
							edPch.setText("");
						}
					}
				}
			}
		}
	}

	/**
	 * 添加盘点信息
	 */
	private void pd_pd_add(String pch) {
		showLoadDialog();
		String kf = mSp.getString(UserInfo.SP_CK_NAME, "");
		String rq = sdf_ymd.format(new Date(System.currentTimeMillis()));
		JSONObject jp_sub = new JSONObject();
		try {
			jp_sub.put("编号",edSpbh.getText().toString());
			jp_sub.put("库房名称",kf);
			jp_sub.put("商品名称",tvSpmc.getText().toString().substring(5));
			jp_sub.put("批次号", pch);
			jp_sub.put("单位", tvDw.getText().toString().substring(3));
			jp_sub.put("人员", mSp.getString(UserInfo.SP_USER_CODE,""));
		} catch (JSONException e) {
			makeShortToase("参数异常");
		}

		HttpJsonRequest request = new HttpJsonRequest(new AddBackListener(pch),
				Method.SERVICE_NAME_PD, Method.PD_PD_ADD
				, jp_sub.toString());
		ThreadPoolUtils.execute(request);
	}

	class AddBackListener implements HttpJsonRequest.CallbackListener {

		private String pch;

		public AddBackListener(String pch) {
			this.pch = pch;
		}

		@Override
		public void callBack(String result) {
			L.e(result);
			cancleLoadDialog();

			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
			} else {
				PdCheckBean returnBean = JsonUtils.getJsonParseObject(result, PdCheckBean.class);
				if (returnBean == null) {
					makeShortToase(R.string.loading_error);
				} else {
					makeShortToase(returnBean.getReason());
					if ("true".equals(returnBean.getRes())) {
//						tvPdpch.append("\n    " + pch);
						tvPdpch.setText(pch + "\n" + tvPdpch.getText().toString());

						int size = 0;
						try {
							size = Integer.valueOf(tvPdsl.getText().toString().substring(5));
						} catch (NumberFormatException e) {
							size = 0;
						}
						size++;
						tvPdsl.setText("盘点数量：" + size);
						edPch.setText("");
					}
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
		String pageStr = "" + currentPage;
		HttpJsonRequest request = new HttpJsonRequest(new SpInfo2BackListener(),
				Method.SERVICE_NAME_RK, Method.RK_GET_SPINFO, edSpbh.getText().toString() , "", "", pageStr, pageSize);
		ThreadPoolUtils.execute(request);
	}


	class SpInfo2BackListener implements HttpJsonRequest.CallbackListener {

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
				edSpbh.setText(bean.getRows().get(0).get编号());
				tvSpmc.setText("商品名称:"+bean.getRows().get(0).get商品名称());
				tvDw.setText("单位:"+bean.getRows().get(0).get单位());
				pd_get_spinfo();
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
			tvSph.setText("编号:" + bean.get编号());
			tvSpm.setText("商品名称:" + bean.get商品名称());
			tvDw.setText("单位:" + bean.get单位());

			return convertView;
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
				// 选择
				edSpbh.setText(datas.get(p).get编号());
				tvSpmc.setText("商品名称:"+datas.get(p).get商品名称());
				tvDw.setText("单位:" + datas.get(p).get单位());

				pd_get_spinfo();
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


}
