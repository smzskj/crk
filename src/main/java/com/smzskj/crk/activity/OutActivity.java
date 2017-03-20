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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseActivity;
import com.smzskj.crk.base.BaseViewAdapter;
import com.smzskj.crk.base.BaseViewHolder;
import com.smzskj.crk.bean.OutCkBean;
import com.smzskj.crk.bean.OutDhInfoBean;
import com.smzskj.crk.bean.OutPchBean;
import com.smzskj.crk.bean.OutPchCkz;
import com.smzskj.crk.bean.OutPchTh;
import com.smzskj.crk.bean.OutPchZjk;
import com.smzskj.crk.net.HttpJsonRequest;
import com.smzskj.crk.net.Method;
import com.smzskj.crk.utils.Constants;
import com.smzskj.crk.utils.JsonUtils;
import com.smzskj.crk.utils.L;
import com.smzskj.crk.utils.ScannerUtils;
import com.smzskj.crk.utils.ThreadPoolUtils;
import com.smzskj.crk.utils.UserInfo;
import com.smzskj.crk.view.xlistview.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ztt on 2017/1/17.
 * <p>
 * 出库
 */

public class OutActivity extends BaseActivity implements View.OnClickListener {

	private String repertoryCode;
	private String repertoryName;

	private XListView listView;
	private EditText edDh, edBzm;
	private TextView tvSpsl, tvQrsl;
	private LinearLayout llQrjx, llDz, llQx;
	private ImageButton ibSm, ibScanner;

	private LayoutInflater inflater;

	/**
	 * 数据
	 */
	private List<OutDhInfoBean.RowsBean> datas = new ArrayList<>();
	/**
	 * 没有批次号的数据id,修改为登账非D数据
	 */
	private List<Integer> pchNull = new ArrayList<>();
	/**
	 * 扫描的批次号
	 */
	private List<String> pchList = new ArrayList<>();
	/**
	 * 扫描的批次号数据
	 */
	private List<OutDhInfoBean.RowsBean> pchDatas = new ArrayList<>();
	private PchAdapter adapter;


	private ScannerUtils scanner;
	private ScannerUtils scannerDh;
	private MediaPlayer player;

	/**
	 * 编号
	 */
	private String bh;
	/**
	 * 单据数量
	 */
	private int djsl = 0;
	/**
	 * 确认数量
	 */
	private int qrsl = 0;

	public static void startOutActivity(Context context, String repertoryCode, String
			repertoryName) {
		Intent intent = new Intent(context, OutActivity.class);
		intent.putExtra("repertoryCode", repertoryCode);
		intent.putExtra("repertoryName", repertoryName);
		context.startActivity(intent);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkLogin();
		setContentView(R.layout.activity_out);
		repertoryCode = getIntent().getStringExtra("repertoryCode");
		repertoryName = getIntent().getStringExtra("repertoryName");
		player = MediaPlayer.create(getApplicationContext(), R.raw.scan);
		inflater = LayoutInflater.from(mContext);
		initView();
	}

	private void initView() {
		addBackListener();
		setTitle(R.string.repertory_out);
		listView = findView(R.id.out_listview);
		edBzm = findView(R.id.out_ed_bzm);
		llQrjx = findView(R.id.out_ll_qrjx);
		llDz = findView(R.id.out_ll_dz);
		llQx = findView(R.id.out_ll_qx);
		ibScanner = findView(R.id.out_ib_scanner);
		ibScanner.setOnClickListener(this);
		llQrjx.setOnClickListener(this);
		llDz.setOnClickListener(this);
		llQx.setOnClickListener(this);

		View view = inflater.inflate(R.layout.item_out_header, null, false);
		edDh = findView(view, R.id.out_ed_dh);
		edDh.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (edDh.getText().toString().trim().length() == 9) {
					ck_get_dhinfo();
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		tvQrsl = findView(view, R.id.out_tv_qrsl);
		tvSpsl = findView(view, R.id.out_tv_spsl);
		ibSm = findView(view, R.id.out_ib_sm);
		ibSm.setOnClickListener(this);

		listView.addHeaderView(view);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		adapter = new PchAdapter(mContext, datas);
		listView.setAdapter(adapter);

		setRightBtnListener(getResources().getString(R.string.repertory_query), new View
				.OnClickListener() {


			@Override
			public void onClick(View v) {
				djsl = 0;
				qrsl = 0;
				edBzm.setText("");
				datas.clear();
				pchNull.clear();
				pchList.clear();
				pchDatas.clear();
				adapter.notifyDataSetChanged();
				tvSpsl.setText("商品数量");
				tvQrsl.setText("确认数量");
				ck_get_dhinfo();
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (ibSm == v) {
			try {
				scannerDh = new ScannerUtils(new ScannerUtils.ScannerListener() {

					@Override
					public void onSuccess(String barCode) {
						player.start();
						edDh.setText(barCode);
						if (scannerDh != null) {
							scannerDh.cloase();
							scannerDh = null;
						}
						ck_get_dhinfo();
					}
				});
				scannerDh.scanner();
			} catch (Exception e) {
				L.e("扫描异常");
			}

		} else if (llQrjx == v) {
			String pch = edBzm.getText().toString().trim();
			for (OutDhInfoBean.RowsBean bean : datas) {
				if (pch.equals(bean.get批次号().trim()) && "D".equals(bean.get登帐())) {
					makeShortToase("此批次号已经存在，不能重复");
					return;
				}
			}
//			if (pchList.contains(pch)) {
//				makeShortToase("此批次号已经存在，不能重复");
//				return;
//			}
			ck_chk_pch();
		} else if (llDz == v) {
			showAlertDialog("是否登账？", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					ck_ck();
				}
			});
		} else if (llQx == v) {
			showAlertDialog("是否取消出库？", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					finish();
				}
			});
		} else if (ibScanner == v) {
			scanner();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		L.e("keycode" + keyCode);
		if (keyCode == Constants.KEY_SCAN && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (edDh.isEnabled()) {
				onClick(ibSm);
			} else {
				scanner();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	class PchAdapter extends BaseViewAdapter<OutDhInfoBean.RowsBean> {

		public PchAdapter(Context context, List<OutDhInfoBean.RowsBean> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_out_dh, parent, false);
			}
			TextView tvPch = BaseViewHolder.get(convertView, R.id.dh_item_tv_pch);
			TextView tvBh = BaseViewHolder.get(convertView, R.id.dh_item_tv_bh);
			TextView tvSpmc = BaseViewHolder.get(convertView, R.id.dh_item_tv_spm);

			OutDhInfoBean.RowsBean bean = list.get(position);
			tvPch.setText("批次号" + bean.get批次号());
			tvBh.setText("编号" + bean.get编号());
			tvSpmc.setText("商品名称" + bean.get商品名称());

			ImageView imageView = BaseViewHolder.get(convertView, R.id.dh_item_iv);
			if ("D".equals(bean.get登帐())) {
				imageView.setVisibility(View.VISIBLE);
			} else {
				imageView.setVisibility(View.GONE);
			}
			return convertView;
		}
	}

	private void scanner() {
		scanner = new ScannerUtils(new ScannerUtils.ScannerListener() {

			@Override
			public void onSuccess(String barCode) {
				player.start();
				if (edDh.isEnabled()) {
					edDh.setText(barCode);
					if (scannerDh != null) {
						scannerDh.cloase();
						scannerDh = null;
					}
					ck_get_dhinfo();
				} else {
					if (barCode.length() < 8) {
						makeShortToase(R.string.scanner_error);
						return;
					}
					if (!TextUtils.isEmpty(barCode)) {
						edBzm.setText(barCode);
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

	private void ck_get_dhinfo() {
		String dh = edDh.getText().toString();
		if (TextUtils.isEmpty(dh)) {
			makeShortToase("单号不能为空");
			return;
		}
		showLoadDialog();
		HttpJsonRequest request = new HttpJsonRequest(new DhInfoBackListener(),
				Method.SERVICE_NAME_CK, Method.CK_GET_DHINFO, dh);
		ThreadPoolUtils.execute(request);
	}

	class DhInfoBackListener implements HttpJsonRequest.CallbackListener {

		@Override
		public void callBack(String result) {
			L.e(result);
			cancleLoadDialog();
			datas.clear();
			pchNull.clear();
			pchList.clear();
			pchDatas.clear();
			adapter.notifyDataSetChanged();
			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
				return;
			}
			OutDhInfoBean bean = JsonUtils.getJsonParseObject(result, OutDhInfoBean.class);
			if (bean == null || bean.getRows() == null) {
				makeShortToase(R.string.loading_error);
				return;
			}

			if (bean.getRows().size() == 0) {
				makeShortToase("没有此单据或此单据已经确认出库了");
				return;
			}
			String lkfmc = mSp.getString(UserInfo.SP_CK_NAME, "").trim();
			String skfmc = bean.getRows().get(0).get库房名称();
			// 库房
			if (!skfmc.contains(lkfmc)) {
				makeShortToase(R.string.kf_error);
				clean();
				return;
			}

			edDh.setEnabled(false);
			ibSm.setEnabled(false);
			setRightBtnListener(getResources().getString(R.string.repertory_query), null);
			datas.addAll(bean.getRows());
			int size = datas.size();
			for (int i = 0; i < size; i++) {
				if (!"D".equals(datas.get(i).get登帐())) {
					pchNull.add(i);
				} else {
					qrsl += datas.get(i).get出库数();
				}
			}

			// 确认数量 = 所有出库数 （计算）
			adapter.notifyDataSetChanged();
			djsl = bean.getRows().get(0).get单据数量();
			tvSpsl.setText("商品数量:" + djsl);
			tvQrsl.setText("确认数量:" + "0");
			bh = bean.getRows().get(0).get编号();
		}
	}


	/**
	 * 出库检查批次号
	 */
	private void ck_chk_pch() {
		showLoadDialog();
		String dh = edDh.getText().toString();
		String pch = edBzm.getText().toString();
		if (TextUtils.isEmpty(dh)) {
			makeShortToase("单据号不能为空");
		} else if (TextUtils.isEmpty(pch)) {
			makeShortToase("批次号不能为空");
		} else {
			showLoadDialog();
			HttpJsonRequest request = new HttpJsonRequest(new PchBackListener(pch),
					Method.SERVICE_NAME_CK, Method.CK_CHK_PCH, mSp.getString(UserInfo.SP_CK_NAME,
					""), bh, dh, pch);
			ThreadPoolUtils.execute(request);
		}
	}

	class PchBackListener implements HttpJsonRequest.CallbackListener {

		/**
		 * 扫描批次号
		 */
		private String smpch;

		public PchBackListener(String smpch) {
			this.smpch = smpch;
		}

		@Override
		public void callBack(String result) {
			L.e(result);
			cancleLoadDialog();
			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
				return;
			}
			OutPchBean bean = JsonUtils.getJsonParseObject(result, OutPchBean.class);
			if (bean == null || !"100".equals(bean.getCode()) || bean.getRows() == null) {
				makeShortToase(R.string.loading_error);
				return;
			}

			String ckz = bean.getRows().getCkz();
			String zjk = bean.getRows().getZjk();
			String zjk_n = bean.getRows().getZjk_n();
			String exist = bean.getRows().getExist();
			String th_spycz = bean.getRows().getTh_spycz();

			OutPchCkz outPchCkz = JsonUtils.getJsonParseObject(ckz, OutPchCkz.class);
			OutPchZjk outPchZjk = JsonUtils.getJsonParseObject(zjk, OutPchZjk.class);
			OutPchZjk outPchZjk_n = JsonUtils.getJsonParseObject(zjk_n, OutPchZjk.class);
			OutPchTh outPchTh = JsonUtils.getJsonParseObject(th_spycz, OutPchTh.class);
			if (!"100".equals(bean.getCode()) || outPchCkz == null || outPchZjk == null) {
				return;
			}
//			if (outPchCkz.getRows() == null || outPchCkz.getRows().size() == 0 || outPchZjk
//					.getRows() == null || outPchZjk.getRows().size() == 0) {
//				makeShortToase("没有需要确认的单据了");
//				return;
//			}
//			djsl = outPchCkz.getRows().get(0).get单据数量();
//			tvSpsl.setText("商品数量" + djsl);

			/*
			* djsl < 0  : 退货  170200063
			* */
			if (djsl < 0) {
				if (datas != null && datas.size() > 0) {
					if (outPchTh != null && outPchTh.getRows() != null && outPchTh.getRows().size() != 0) {
						makeShortToase("此批次号的商品已经存在，不能退回！");
					} else {
						boolean dd = false;
						for (final OutDhInfoBean.RowsBean rowsBean : datas) {
							L.e("批次号" + rowsBean.get批次号().trim());
							if (!"D".equals(rowsBean.get登帐())) {
								dd = true;
								if (TextUtils.isEmpty(rowsBean.get批次号().trim())) {
									if (outPchZjk.getRows() != null && outPchZjk.getRows().size()
											> 0) {
										rowsBean.set批次号(smpch);
										rowsBean.set登帐("D");
										qrsl += rowsBean.get出库数();
										tvQrsl.setText("确认数量:" + qrsl);
										pchList.add(smpch);
										pchDatas.add(rowsBean);
										adapter.notifyDataSetChanged();
										break;
									} else {
										// 没有销售过此批次号的商品，不能退货！
//									makeShortToase("没有销售过此批次号的商品，不能退货！");

										showAlertDialog
												("销售记录里没有此批次号的商品，请确认是否为单品核算前的销售，如果是，可以强行退货，强行退货吗？", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int
													which) {
												dialog.cancel();
												rowsBean.set批次号(smpch);
												rowsBean.set登帐("D");
												qrsl += rowsBean.get出库数();
												tvQrsl.setText("确认数量:" + qrsl);
												pchList.add(smpch);
												pchDatas.add(rowsBean);
												adapter.notifyDataSetChanged();
											}
										});
										break;
									}
								} else if (!rowsBean.get批次号().equals(smpch)) {

//								makeShortToase("原发货的批次号为：'" + rowsBean.get批次号() + "'退回的货批次号为：'" +
//										smpch + "'不是原来的商品，不能退货！");

									if (outPchZjk_n.getRows() != null && outPchZjk_n.getRows()
											.size() > 0) {
										rowsBean.set批次号(smpch);
										rowsBean.set登帐("D");
										qrsl += rowsBean.get出库数();
										tvQrsl.setText("确认数量:" + qrsl);
										pchList.add(smpch);
										pchDatas.add(rowsBean);
										adapter.notifyDataSetChanged();
									} else {
										makeShortToase("没有销售过此批次号的商品，不能退货！");
									}
									break;
								} else {
									rowsBean.set登帐("D");
									pchList.add(rowsBean.get批次号());
									pchDatas.add(rowsBean);
									adapter.notifyDataSetChanged();
									break;
								}
							}
						}

						// 如果登账标识都为D
						if (!dd) {
							makeShortToase("没有需要确认的单据了");
						}

					}
				} else {
					makeShortToase("没有需要确认的单据了");
				}
			} else { // 出货
				if ("true".equals(exist)) {
					// 没有查到此批次号信息，弹出dialog
					if (outPchCkz.getRows() == null || outPchCkz.getRows().size() == 0) {
						showPicDialog();
						return;
					}
					String bh = outPchCkz.getRows().get(0).get编号().trim();
					// 已经录入的size
					int size = pchList.size();
					// 录入的数量小于空白的数量
					if (size < pchNull.size()) {
						if (bh.equals(datas.get(pchNull.get(size)).get编号().trim())) {
							datas.get(pchNull.get(size)).set批次号(smpch);
							datas.get(pchNull.get(size)).set登帐("D");
							adapter.notifyDataSetChanged();
							pchList.add(smpch);
							pchDatas.add(datas.get(pchNull.get(size)));
							qrsl += datas.get(pchNull.get(size)).get出库数();
							tvQrsl.setText("确认数量:" + qrsl);
						} else {
							makeShortToase("批次号为" + smpch + "的商品,在库存里是" + bh + ",不能出库!");
						}
					} else {
						makeShortToase("已全部确认完毕，请登账");
					}
				} else {
					makeShortToase(exist);
				}
			}
		}
	}

	private void showPicDialog() {
		final String pch = edBzm.getText().toString();
		final String bh = "";
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("提示");
		builder.setMessage("此批次号的商品不存在或已经没有库存，强行出库吗？");
		builder.setNegativeButton("继续", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				int size = pchList.size();
				if (size < pchNull.size()) {
					if (bh.equals(datas.get(pchNull.get(size)).get编号().trim()) || "".equals(bh)) {
						datas.get(pchNull.get(size)).set批次号(pch);
						adapter.notifyDataSetChanged();
						pchList.add(pch);
						pchDatas.add(datas.get(pchNull.get(size)));
						tvQrsl.setText("确认数量:" + pchList.size());
					} else {
						makeShortToase("批次号为" + pch + "的商品,在库存里是" + bh + ",不能出库!");
					}
				} else {
					makeShortToase("已全部确认完毕，请登账");
				}
				dialog.cancel();
			}
		});

		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	private void ck_ck() {
		try {
			String dh = edDh.getText().toString();
			if (TextUtils.isEmpty(dh)) {
				makeShortToase("单据号不能为空");
				return;
			}

			if (datas.isEmpty()) {
				makeShortToase("商品信息不能为空");
				return;
			}

			if (pchDatas.isEmpty()) {
				makeShortToase("没有通过确认的数据，不能登帐");
				return;
			}

//			boolean isNull = true;

			JSONArray jp_sub_a = new JSONArray();
			for (OutDhInfoBean.RowsBean bean : pchDatas) {
				JSONObject jp_sub_a_obj = new JSONObject();
				String pch = bean.get批次号();
//				if (!TextUtils.isEmpty(pch)) {
//					isNull = false;
//				}
				jp_sub_a_obj.put("批次号", pch);
				jp_sub_a_obj.put("id", bean.getId());
				jp_sub_a.put(jp_sub_a_obj);
			}

//			if (isNull) {
//				makeShortToase("没有通过确认的数据，不能登帐");
//				return;
//			}

			JSONObject jp_sub = new JSONObject();
			jp_sub.put("单据号码", dh);
			jp_sub.put("编号", pchDatas.get(0).get编号());
			jp_sub.put("发货人", UserInfo.RY_NAME);
			jp_sub.put("出库商品", jp_sub_a);
			String json = jp_sub.toString();
			showLoadDialog();
			HttpJsonRequest request = new HttpJsonRequest(new CkBackListener(),
					Method.SERVICE_NAME_CK, Method.CK_CK, json);
			ThreadPoolUtils.execute(request);
		} catch (JSONException e) {
			e.printStackTrace();
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
			OutCkBean bean = JsonUtils.getJsonParseObject(result, OutCkBean.class);
			if (bean == null) {
				makeShortToase(R.string.loading_error);
				return;
			}
			makeShortToase(bean.getReason());
			if ("true".equals(bean.getRes())) {
				clean();
			}
		}
	}


	/**
	 * 清空信息
	 */
	private void clean() {

		djsl = 0;
		qrsl = 0;
		edBzm.setText("");
		edBzm.setEnabled(true);
		datas.clear();
		edDh.setText("");
		edDh.setEnabled(true);
		ibSm.setEnabled(true);
		setRightBtnListener(getResources().getString(R.string.repertory_query), null);

		pchNull.clear();
		pchList.clear();
		pchDatas.clear();
		adapter.notifyDataSetChanged();
		tvSpsl.setText("商品数量");
		tvQrsl.setText("确认数量");

		setRightBtnListener(getResources().getString(R.string.repertory_query), new View
				.OnClickListener() {


			@Override
			public void onClick(View v) {
				djsl = 0;
				qrsl = 0;
				edBzm.setText("");
				datas.clear();
				pchNull.clear();
				pchList.clear();
				pchDatas.clear();
				adapter.notifyDataSetChanged();
				tvSpsl.setText("商品数量");
				tvQrsl.setText("确认数量");
				ck_get_dhinfo();
			}
		});
	}
}
