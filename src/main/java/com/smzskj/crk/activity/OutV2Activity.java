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
import com.smzskj.crk.adapter.OutPchAdapter;
import com.smzskj.crk.base.BaseActivity;
import com.smzskj.crk.base.BaseViewAdapter;
import com.smzskj.crk.base.BaseViewHolder;
import com.smzskj.crk.bean.OutDhBean;
import com.smzskj.crk.bean.OutDhBeanPch;
import com.smzskj.crk.bean.OutDhBeanSp;
import com.smzskj.crk.bean.OutThBean;
import com.smzskj.crk.bean.OutThTh;
import com.smzskj.crk.bean.OutThZjk;
import com.smzskj.crk.bean.OutThZjkN;
import com.smzskj.crk.bean.OutZcBean;
import com.smzskj.crk.bean.ResultBean;
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

import static com.smzskj.crk.utils.JsonUtils.getJsonParseObject;

/**
 * Created by ztt on 2017/3/17.
 * <p>
 * 出库第二版，修改出库逻辑和退货逻辑，以及所有接口
 */
// 170300042 170300045 170300046 170300052 170300054 170300056 170300057


public class OutV2Activity extends BaseActivity implements View.OnClickListener {

	private XListView listView;
	private EditText edDh, edBzm;
	private TextView tvSpsl, tvQrsl;
	private LinearLayout llQrjx, llDz, llQx;
	private ImageButton ibSm, ibScanner;

	private LayoutInflater inflater;

	private ScannerUtils scanner;
	private MediaPlayer player;

	private OutPchAdapter adapter;


	/**
	 * 是否是退货
	 */
	private boolean isReturn;
	/**
	 * 出库商品数据
	 */
	private List<OutDhBeanPch.RowsBean> datas = new ArrayList<>();
	private int dataSize = 0;
	/**
	 * 本地库房
	 */
	private String lkfmc;

	public static void startOutActivity(Context context) {
		Intent intent = new Intent(context, OutV2Activity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_out);
		player = MediaPlayer.create(getApplicationContext(), R.raw.scan);
		inflater = LayoutInflater.from(mContext);
		lkfmc = mSp.getString(UserInfo.SP_CK_NAME, "").trim();
		initView();
	}

	private void initView() {
		addBackListener();
		setTitle(R.string.repertory_out);

		listView = findView(R.id.out_listview);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		edBzm = findView(R.id.out_ed_bzm);
		llQrjx = findView(R.id.out_ll_qrjx);
		llDz = findView(R.id.out_ll_dz);
		llQx = findView(R.id.out_ll_qx);
		ibScanner = findView(R.id.out_ib_scanner);
		ibScanner.setOnClickListener(this);
		llQrjx.setOnClickListener(this);
		llDz.setOnClickListener(this);
		llQx.setOnClickListener(this);

		adapter = new OutPchAdapter(mContext, datas);
		listView.setAdapter(adapter);
		View view = inflater.inflate(R.layout.item_out_header, null, false);
		edDh = findView(view, R.id.out_ed_dh);
		tvQrsl = findView(view, R.id.out_tv_qrsl_sl);
		tvSpsl = findView(view, R.id.out_tv_spsl);
		ibSm = findView(view, R.id.out_ib_sm);
		ibSm.setOnClickListener(this);
		listView.addHeaderView(view);
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

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.out_ib_scanner:
				scanner();
				break;
			case R.id.out_ib_sm:
				scanner();
				break;
			case R.id.out_ll_qrjx:
				dzPch();
				break;
			case R.id.out_ll_dz:
				showAlertDialog("是否登账？", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						ck_ck_v2();
					}
				});
				break;
			case R.id.out_ll_qx:
				showAlertDialog("是否取消出库？", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						finish();
					}
				});
				break;
			default:
				break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
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
				if (edDh.isEnabled()) {
					edDh.setText(barCode);
//					ck_get_dhinfo();
				} else {
					if (!TextUtils.isEmpty(barCode)) {
						edBzm.setText(barCode);
						dzPch();
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
	 * 确认并继续，登账批次号
	 */
	private void dzPch() {
		String pch = edBzm.getText().toString();
		if (pch.length() < 8) {
			makeShortToase(R.string.scanner_error);
		} else {
			if (isReturn) {
				ck_chk_pch_th_v2(pch);
			} else {
				ck_chk_pch_zc_v2(pch);
			}
		}
	}


	private void setQrsl(int qrsl) {
		int sl = Integer.valueOf(tvQrsl.getText().toString());
		sl += qrsl;
		tvQrsl.setText(String.valueOf(sl + qrsl));
	}

	private void ck_get_dhinfo() {
		String dh = edDh.getText().toString();
		if (TextUtils.isEmpty(dh)) {
			makeShortToase("单号不能为空");
			return;
		}
		showLoadDialog();
		HttpJsonRequest request = new HttpJsonRequest(new DhInfoBackListener(),
				Method.SERVICE_NAME_CK, Method.CK_GET_DHINFO_V2, dh);
		ThreadPoolUtils.execute(request);
	}

	class DhInfoBackListener implements HttpJsonRequest.CallbackListener {

		@Override
		public void callBack(String result) {
			L.e(result);
			cancleLoadDialog();
			datas.clear();
			dataSize = 0;
			adapter.notifyDataSetChanged();
			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
				return;
			}
			OutDhBean bean = getJsonParseObject(result, OutDhBean.class);
			if (bean == null || bean.getRows() == null) {
				makeShortToase(R.string.loading_error);
				return;
			}

			String djsl = bean.getRows().getDjsl();
			String res_sp = bean.getRows().getRes_sp();
			String res_pch = bean.getRows().getRes_pch();
			OutDhBeanSp dhBeanSp = JsonUtils.getJsonParseObject(res_sp, OutDhBeanSp.class);
			OutDhBeanPch dhBeanPch = getJsonParseObject(res_pch, OutDhBeanPch.class);
			if (dhBeanSp == null || dhBeanSp.getRows() == null || dhBeanSp.getRows().size() <= 0) {
				makeShortToase("没有查询到商品信息");
				return;
			}
			if (dhBeanPch == null || dhBeanPch.getRows() == null || dhBeanPch.getRows().size() <= 0) {
				makeShortToase("没有查询到批次号信息");
				return;
			}

			tvQrsl.setText("0");
			// 循环批次号信息，检查仓库名称，服务器包含本地才可以入库
			for (OutDhBeanPch.RowsBean beanPch : dhBeanPch.getRows()) {
				if (beanPch.get库房名称() == null || !beanPch.get库房名称().contains(lkfmc)) {
					makeShortToase(R.string.kf_error);
					tvQrsl.setText("0");
					return;
				}
				if (beanPch.getD登账()) {
					setQrsl(beanPch.get出库数());
				}
			}

			// 数据为double类型的字符串，去掉小数点，小于0是退货
			try {
				double djslDouble = Double.valueOf(djsl);
				if (djslDouble < 0) {
					isReturn = true;
				} else {
					isReturn = false;
				}
				tvSpsl.setText("商品数量" + (long) djslDouble);
			} catch (NumberFormatException e) {
				tvSpsl.setText("商品数量0");
			}

			edDh.setEnabled(false);
			datas.addAll(dhBeanPch.getRows());
			dataSize = datas.size();
			adapter.notifyDataSetChanged();
		}
	}


	/**
	 * 退货批次号查询
	 *
	 * @param pch 批次号
	 */
	private void ck_chk_pch_th_v2(String pch) {
		String dh = edDh.getText().toString();
		if (TextUtils.isEmpty(dh)) {
			makeShortToase("单号不能为空");
			return;
		}
		int spIndex = -1;
		String spBh = "";
		String spPch = "";
		for (int i = 0; i < dataSize; i++) {
			if (!datas.get(i).getD登账()) {
				spIndex = i;
				spBh = datas.get(i).get编号();
				spPch = datas.get(i).get批次号();
				break;
			}
		}

		// 如果扫描批次号和批次号相同，直接登账
		if (pch.equals(spPch)) {
			datas.get(spIndex).setD登帐();
			adapter.notifyDataSetChanged();
			return;
		}

		showLoadDialog();
		HttpJsonRequest request = new HttpJsonRequest(new PchThBackListener(spIndex, pch),
				Method.SERVICE_NAME_CK, Method.CK_CHK_PCH_TH_V2, lkfmc, spBh, dh, pch);
		ThreadPoolUtils.execute(request);
	}

	/**
	 * 退货
	 */
	class PchThBackListener implements HttpJsonRequest.CallbackListener {

		private int index;
		private String pch;

		public PchThBackListener(int index, String pch) {
			this.index = index;
			this.pch = pch;
		}

		@Override
		public void callBack(String result) {
			L.e(result);
			cancleLoadDialog();
			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
				return;
			}
			OutThBean bean = getJsonParseObject(result, OutThBean.class);
			if (bean == null || bean.getRows() == null) {
				makeShortToase(R.string.loading_error);
				return;
			}

			String zjk = bean.getRows().getZjk();
			String zjk_n = bean.getRows().getZjk_n();
			String th_spycz = bean.getRows().getTh_spycz();
			OutThZjk outPchZjk = JsonUtils.getJsonParseObject(zjk, OutThZjk.class);
			OutThZjkN outPchZjk_n = JsonUtils.getJsonParseObject(zjk_n, OutThZjkN.class);
			OutThTh outPchTh = JsonUtils.getJsonParseObject(th_spycz, OutThTh.class);

			if (outPchTh != null && outPchTh.getRows() != null && outPchTh.getRows().size() != 0) {
				makeShortToase("此批次号的商品已经存在，不能退回！");
			} else {
				if (TextUtils.isEmpty(datas.get(index).get批次号())) {
					if (outPchZjk != null && outPchZjk.getRows() != null && outPchZjk.getRows().size() > 0) {
						datas.get(index).setD登帐();
						datas.get(index).set批次号(pch);
						setQrsl(datas.get(index).get出库数());
						adapter.notifyDataSetChanged();
					} else {
						showAlertDialog
								("销售记录里没有此批次号的商品，请确认是否为单品核算前的销售，如果是，可以强行退货，强行退货吗？", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int
											which) {
										dialog.cancel();
										datas.get(index).setD登帐();
										datas.get(index).set批次号(pch);
										setQrsl(datas.get(index).get出库数());
										adapter.notifyDataSetChanged();
									}
								});
					}
				} else {
					if (outPchZjk_n != null) {
						if (outPchZjk_n.getRows() != null && outPchZjk_n.getRows().size() > 0) {
							datas.get(index).setD登帐();
							datas.get(index).set批次号(pch);
							setQrsl(datas.get(index).get出库数());
							adapter.notifyDataSetChanged();
						} else {
							makeShortToase("没有销售过此批次号的商品，不能退货！");
						}
					} else {
						makeShortToase("没有销售过此批次号的商品，不能退货！");
					}
				}
			}
		}
	}

	/**
	 * 正常出库批次号查询，出库批次号都是空
	 *
	 * @param pch 批次号
	 */
	private void ck_chk_pch_zc_v2(String pch) {
		String dh = edDh.getText().toString();
		if (TextUtils.isEmpty(dh)) {
			makeShortToase("单号不能为空");
			return;
		}
		String spBh = "";
		for (int i = 0; i < dataSize; i++) {
			if (!datas.get(i).getD登账()) {
				spBh = datas.get(i).get编号();
				break;
			}
		}
		showLoadDialog();
		HttpJsonRequest request = new HttpJsonRequest(new PchZcBackListener(pch),
				Method.SERVICE_NAME_CK, Method.CK_CHK_PCH_ZC_V2, lkfmc, spBh, dh, pch);
		ThreadPoolUtils.execute(request);
	}


	/**
	 * 正常出库
	 */
	class PchZcBackListener implements HttpJsonRequest.CallbackListener {

		private String pch;

		public PchZcBackListener(String pch) {
			this.pch = pch;
		}

		@Override
		public void callBack(String result) {
			L.e(result);
			cancleLoadDialog();
			if (TextUtils.isEmpty(result)) {
				makeShortToase(R.string.loading_error);
				return;
			}
			OutZcBean bean = getJsonParseObject(result, OutZcBean.class);
			if (bean == null || bean.getRows() == null) {
				makeShortToase(R.string.loading_error);
				return;
			}
			if (bean.getRows().size() <= 0) {
				showAlertDialog
						("没有查询到商品信息，是否强制出库？", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int
									which) {
								dialog.cancel();
								boolean isck = false;
								for (OutDhBeanPch.RowsBean bean : datas) {
									// 批次号为空，并且商品编号相同
									if (TextUtils.isEmpty(bean.get批次号().trim())) {
										bean.set批次号(pch);
										bean.setD登帐();
										adapter.notifyDataSetChanged();
										setQrsl(bean.get出库数());
										isck = true;
										break;
									}
								}

								if (!isck) {
									makeShortToase("已经全部出库");
								} else {
									makeShortToase("确认成功");
								}
							}
						});
				return;
			}

			if (bean.getRows().size() == 1) {
				zcck(pch, bean.getRows().get(0));
			} else {
				spDialog(pch, bean.getRows());
			}
		}
	}

	private void zcck(String pch, OutZcBean.RowsBean rowsBean) {
		boolean isck = false;
		for (OutDhBeanPch.RowsBean bean : datas) {
			// 批次号为空，并且商品编号相同
			if (TextUtils.isEmpty(bean.get批次号().trim()) && bean.get编号().trim().equals(rowsBean.get编号().trim())) {
				bean.set批次号(pch);
				bean.setD登帐();
				adapter.notifyDataSetChanged();
				setQrsl(bean.get出库数());
				isck = true;
				break;
			}
		}
		if (!isck) {
			makeShortToase("此商品已经全部出库");
		} else {
			makeShortToase("确认成功");
		}
	}

	private AlertDialog dialog;

	/**
	 * 商品弹窗
	 */
	private void spDialog(final String pch, final List<OutZcBean.RowsBean> list) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.ProgressDialog);
		View view = inflater.inflate(R.layout.dialog_sp, null, false);
		XListView listView = findView(view, R.id.sp_lv);
		listView.setAdapter(new SpAdapter(mContext, list));

		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int p = (int) id;
				zcck(pch, list.get(p));
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

			}
		});
	}


	class SpAdapter extends BaseViewAdapter<OutZcBean.RowsBean> {

		public SpAdapter(Context context, List<OutZcBean.RowsBean> list) {
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

			OutZcBean.RowsBean bean = list.get(position);
			tvSph.setText("编号:" + bean.get编号());
			tvSpm.setText("商品名称:" + bean.get商品名称());
			tvDw.setVisibility(View.GONE);

			return convertView;
		}
	}

	/**
	 * 出库登账
	 */
	private void ck_ck_v2() {

		String ry = mSp.getString(UserInfo.SP_USER_NAME, "");
		JSONObject jp_sub = new JSONObject();
		JSONArray jp_sub_a = new JSONArray();
		try {
			for (OutDhBeanPch.RowsBean rowsBean : datas) {
				if (TextUtils.isEmpty(rowsBean.get批次号().trim())) {
					continue;
				}
				JSONObject jp_sub_a_obj = new JSONObject();
				jp_sub_a_obj.put("单据号码", edDh.getText().toString());
				jp_sub_a_obj.put("编号", rowsBean.get编号());
				jp_sub_a_obj.put("批次号", rowsBean.get批次号());
				jp_sub_a_obj.put("id", rowsBean.getId());
				jp_sub_a.put(jp_sub_a_obj);
			}
			jp_sub.put("发货人", ry);
			jp_sub.put("出库商品", jp_sub_a);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		String json = jp_sub.toString();
		showLoadDialog();
		HttpJsonRequest request = new HttpJsonRequest(new CkBackListener(),
				Method.SERVICE_NAME_CK, Method.CK_CK_V2, json);
		ThreadPoolUtils.execute(request);
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
			ResultBean bean = getJsonParseObject(result, ResultBean.class);
			if (bean == null) {
				makeShortToase(R.string.loading_error);
				return;
			}

			if ("100".equals(bean.getCode())) {
				edDh.setText("");
				edDh.setEnabled(true);
				datas.clear();
				adapter.notifyDataSetChanged();
				tvSpsl.setText("商品数量");
				tvQrsl.setText("");
				edBzm.setText("");
			}
			makeShortToase(bean.getReason());
		}
	}
}
