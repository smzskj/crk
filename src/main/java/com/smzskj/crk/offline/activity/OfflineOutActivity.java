package com.smzskj.crk.offline.activity;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smzskj.crk.R;
import com.smzskj.crk.adapter.SingerTvAdapter;
import com.smzskj.crk.base.BaseActivity;
import com.smzskj.crk.bean.OutSbmBean;
import com.smzskj.crk.offline.adapter.SpAdapter;
import com.smzskj.crk.offline.bean.OfflineSpBean;
import com.smzskj.crk.offline.db.OutDbUtils;
import com.smzskj.crk.offline.db.SpDbUtils;
import com.smzskj.crk.utils.Constants;
import com.smzskj.crk.utils.L;
import com.smzskj.crk.utils.ScannerUtils;
import com.smzskj.crk.utils.UserInfo;
import com.smzskj.crk.view.xlistview.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ztt on 2017/3/20.
 * <p>
 * 离线出库
 */

public class OfflineOutActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

	private XListView listView;
	private LinearLayout llQrjx, llDz, llQx;
	private ImageButton ibScanner;
	private EditText edPch;


	private OutSbmBean outSbmBean = new OutSbmBean();
	private LinkedList<String> pchDatas = new LinkedList<>();
	private SingerTvAdapter adapter;

	private LayoutInflater inflater;
	private ScannerUtils scanner;
	private MediaPlayer player;


	/**
	 * 入库数，仓库名，商品名
	 */
	private TextView tvRks, tvCk, tvSpm;
	private Button btnConfirm;
	/**
	 * 单据号，商品号
	 */
	private EditText edDjh, edSph;
	/**
	 * 商品号扫描
	 */
	private ImageButton ibScannerSph;
	private String djh;


	private OutDbUtils outDbUtils;

	private SpDbUtils spDbUtils;
	private SpAdapter spAdapter;
	private XListView spListView;
	private AlertDialog dialog;
	private List<OfflineSpBean.RowsBean> spDatas = new ArrayList<>();
	private int spPage;

	public static void startOfflineOutActivity(Context context) {
		Intent intent = new Intent(context, OfflineOutActivity.class);
		context.startActivity(intent);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_out);

		spDbUtils = new SpDbUtils(mContext);
		outDbUtils = new OutDbUtils(mContext);

		initView();
	}

	private void initView() {
		addBackListener();
		setTitle(R.string.offline_ck);
		player = MediaPlayer.create(getApplicationContext(), R.raw.scan);
		inflater = LayoutInflater.from(mContext);

		edPch = findView(R.id.out_ed_bzm);
		listView = findView(R.id.out_listview);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		llQrjx = findView(R.id.out_ll_qrjx);
		llDz = findView(R.id.out_ll_dz);
		llQx = findView(R.id.out_ll_qx);
		ibScanner = findView(R.id.out_ib_scanner);
		ibScanner.setOnClickListener(this);
		llQrjx.setOnClickListener(this);
		llDz.setOnClickListener(this);
		llQx.setOnClickListener(this);

		adapter = new SingerTvAdapter(mContext, pchDatas);
		listView.setAdapter(adapter);

		View view = inflater.inflate(R.layout.item_offline_out_header, null, false);
		tvRks = findView(view, R.id.offline_out_tv_count);
		tvCk = findView(view, R.id.offline_out_tv_ck);
		tvSpm = findView(view, R.id.offline_out_tv_spm);
		edDjh = findView(view, R.id.offline_out_ed_djh);
		edSph = findView(view, R.id.offline_out_ed_sph);
		edSph.setOnFocusChangeListener(this);
		btnConfirm = findView(view, R.id.offline_out_btn_confirm);
		btnConfirm.setOnClickListener(this);
		ibScannerSph = findView(view, R.id.offline_out_ib_sm);
		ibScannerSph.setOnClickListener(this);
		tvCk.setText(mSp.getString(UserInfo.SP_CK_NAME, ""));

		listView.addHeaderView(view);

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final int p = (int) id;
				final String pch = pchDatas.get(p);
				showAlertDialog("是否要删除此批次号" + pch + "？", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						pchDatas.remove(p);
						adapter.notifyDataSetChanged();
					}
				});
				return false;
			}
		});
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {
			if (!TextUtils.isEmpty(edSph.getText().toString())) {
				spDialog(edSph.getText().toString());
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == Constants.KEY_SCAN && event.getAction() == KeyEvent.ACTION_DOWN) {
			scanner();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		if (v == llQrjx) {
			addPch();
		} else if (v == llDz) {
			showAlertDialog("是否出库？", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					djh = edDjh.getText().toString().trim();
					if (TextUtils.isEmpty(djh)) {
						makeShortToase("单据号码不能为空");
					} else {
						outSbmBean.setSbmList(pchDatas);
						SimpleDateFormat simpleDateFormatRq = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
						String rq = simpleDateFormatRq.format(new Date());
						outDbUtils.insertSp(djh, rq, outSbmBean);

						edDjh.setEnabled(true);
						edSph.setEnabled(true);
						edSph.setText("");
						edDjh.setText("");
						edPch.setText("");
						tvSpm.setText("");
						tvRks.setText("");
						outSbmBean = new OutSbmBean();
						pchDatas.clear();
						adapter.notifyDataSetChanged();
					}
				}
			});

		} else if (v == llQx) {
			showAlertDialog("是否取消出库？", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					finish();
				}
			});

		} else if (v == ibScanner) {
			scanner();
		} else if (v == btnConfirm) {
			djh = edDjh.getText().toString().trim();
			if (TextUtils.isEmpty(djh)) {
				makeShortToase("单据号码不能为空");
			} else {
				edDjh.setEnabled(false);
				btnConfirm.setEnabled(false);
			}
		} else if (v == ibScannerSph) {
			scanner();
		}
	}

	/**
	 * 商品弹窗
	 */
	private void spDialog(String bh) {
		if (dialog != null && dialog.isShowing()) {
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.ProgressDialog);
		View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_sp, null, false);
		spListView = findView(view, R.id.sp_lv);
		spDatas.clear();
		spAdapter = new SpAdapter(mContext, spDatas);
		spListView.setAdapter(spAdapter);

		spListView.setPullLoadEnable(false);
		spListView.setPullRefreshEnable(true);
		DialogListListener listener = new DialogListListener(bh);
		spListView.setXListViewListener(listener);
		spListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
				spListView = null;
				spDatas.clear();
				spAdapter = null;
				dialog = null;
			}
		});
		listener.onRefresh();
	}


	class DialogListListener implements XListView.IXListViewListener {

		private String bh;

		public DialogListListener(String bh) {
			this.bh = bh;
		}

		@Override
		public void onRefresh() {
			spPage = 0;
			spDatas.clear();
			spAdapter.notifyDataSetChanged();
			List<OfflineSpBean.RowsBean> list = spDbUtils.querySpList(bh, spPage);
			if (list == null || list.size() == 0) {
				spListView.setPullLoadEnable(false);
				dialog.cancel();
				showToastDialog("没有查询到商品信息");
			} else if (list.size() == 1) {
				spDatas.add(list.get(0));
				selectSp(0);
				dialog.cancel();
			} else {
				spDatas.addAll(list);
				spAdapter.notifyDataSetChanged();
				spListView.setPullLoadEnable(true);
			}
			spListView.stopRefresh();
		}

		@Override
		public void onLoadMore() {
			List<OfflineSpBean.RowsBean> list = spDbUtils.querySpList(bh, ++spPage);
			if (list == null || list.size() == 0) {
				spListView.setPullLoadEnable(false);
			} else {
				spListView.setPullLoadEnable(true);
				spDatas.addAll(list);
				spAdapter.notifyDataSetChanged();
			}
			spListView.stopLoadMore();
		}
	}

	/**
	 * 选择商品
	 *
	 * @param index 选择
	 */
	private void selectSp(int index) {
		// 选择
		edSph.setText(spDatas.get(index).get编号().trim());
		tvSpm.setText(spDatas.get(index).get商品名称().trim());
		edSph.setEnabled(false);
		outSbmBean.setSph(spDatas.get(index).get编号().trim());
		outSbmBean.setSpm(spDatas.get(index).get商品名称().trim());
		outSbmBean.setDw(spDatas.get(index).get单位().trim());
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
				if (edDjh.isEnabled()) {
					edDjh.setText(barCode);
					edDjh.setEnabled(false);
				} else if (edSph.isEnabled()) {
					edSph.setText(barCode);
					spDialog(edSph.getText().toString());
				} else {
					edPch.setText(barCode);
					L.e(barCode);
					addPch();
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
	 * 网list中添加批次号
	 */
	private void addPch() {
		String pch = edPch.getText().toString().trim();
		if (TextUtils.isEmpty(pch)) {
			makeShortToase("批次号不能为空");
		} else {
			if (pchDatas.contains(pch)) {
				makeShortToase("批次号已经存在");
			} else {
				pchDatas.addFirst(pch);
				adapter.notifyDataSetChanged();
				tvRks.setText(String.valueOf(pchDatas.size()));
			}
		}
		edPch.setText("");
	}
}
