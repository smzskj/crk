package com.smzskj.crk.offline.activity;

import android.app.Activity;
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
import com.smzskj.crk.base.BaseActivity;
import com.smzskj.crk.bean.InSbmBean;
import com.smzskj.crk.offline.adapter.SbhAdapter;
import com.smzskj.crk.offline.adapter.SpAdapter;
import com.smzskj.crk.offline.bean.OfflineSpBean;
import com.smzskj.crk.offline.db.InDbUtils;
import com.smzskj.crk.offline.db.SpDbUtils;
import com.smzskj.crk.utils.Constants;
import com.smzskj.crk.utils.ScannerUtils;
import com.smzskj.crk.utils.UserInfo;
import com.smzskj.crk.view.ListViewInScroll;
import com.smzskj.crk.view.xlistview.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ztt on 2017/3/9.
 * <p>
 * 离线入库
 */

public class OfflineInActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

	private EditText edSph, tvSbm;
	private TextView edSpm;
	private TextView tvCount, tvDh, tvCk;
	private ImageButton ibScanner;
	private ImageButton btnScanner;
	private LinearLayout llIn, llCancle, llQh;
	private EditText edSbm;
	private Button btnSbm;

	private MediaPlayer player;
	private ScannerUtils scanner;
	private LayoutInflater inflater;

	private SpAdapter spAdapter;
	private XListView spListView;
	private AlertDialog dialog;
	private List<OfflineSpBean.RowsBean> spDatas = new ArrayList<>();
	private int spPage;
	private SpDbUtils spDbUtils;

	/**
	 * 商品信息
	 */
	private List<InSbmBean> sbmBeanList = new ArrayList<>();
	/**
	 * 当前商品识别码
	 */
	private List<String> sbmList = new ArrayList<>();
	/**
	 * 商品码
	 */
	private ListViewInScroll lvSbm;
	/**
	 * 商品码适配器
	 */
	private SbhAdapter adapterSbm;

	private InDbUtils dbUtils;

	public static void startInOfflineActivity(Activity context) {
		context.startActivity(new Intent(context, OfflineInActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_offlince_in);
		setContentView(R.layout.activity_in);
		spDbUtils = new SpDbUtils(mContext);
		player = MediaPlayer.create(getApplicationContext(), R.raw.scan);
		inflater = LayoutInflater.from(mContext);
		dbUtils = new InDbUtils(mContext);
		initView();
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

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
		tvDh.setText(simpleDateFormat.format(new Date()));
		tvCk.setText(mSp.getString(UserInfo.SP_DB_NAME, ""));

		tvDh.setOnClickListener(this);
		llIn.setOnClickListener(this);
		llCancle.setOnClickListener(this);
		llQh.setOnClickListener(this);
		ibScanner.setOnClickListener(this);
		btnScanner.setOnClickListener(this);

		edSph.setOnFocusChangeListener(this);

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
					SimpleDateFormat simpleDateFormatRq = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

					dbUtils.insertSp(tvDh.getText().toString(), simpleDateFormatRq.format(new Date()), sbmBeanList);

					clean();
					showToastDialog("离线保存成功");
				}
			});
		} else if (v == btnScanner) {
			scanner();
		} else if (v == ibScanner) {
			scanner();
		} else if (v == tvDh) {
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


	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {
			if (!TextUtils.isEmpty(edSph.getText().toString())) {
				spDialog(edSph.getText().toString());
			}
		}
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
					spDialog(edSph.getText().toString());
				} else {
					if (barCode.length() < 8) {
						makeShortToase(R.string.scanner_error);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == Constants.KEY_SCAN && event.getAction() == KeyEvent.ACTION_DOWN) {
			scanner();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 添加商品码
	 *
	 * @param barCode 商品码
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
	 * 选择商品
	 *
	 * @param index 选择
	 */
	private void selectSp(int index) {

		// 选择
		edSph.setText(spDatas.get(index).get编号().trim());
		edSpm.setText(spDatas.get(index).get商品名称().trim());
		edSph.setEnabled(false);
		setRightBtnListener(getResources().getString(R.string.repertory_query), null);
		ibScanner.setOnClickListener(null);

		// 列表中是否存在此商品
		boolean spFlag = false;
		/*
		 * 选中商品
		 */
		for (InSbmBean bean : sbmBeanList) {
			// 如果商品已经存在
			if (spDatas.get(index).get编号().trim().equals(bean.getSph())) {
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
			bean.setSph(spDatas.get(index).get编号().trim());
			bean.setSpm(spDatas.get(index).get商品名称().trim());
			bean.setDw(spDatas.get(index).get单位().trim());
			sbmBeanList.add(bean);
			sbmList = bean.getSbmList();
		}
		tvCount.setText("" + sbmList.size());
		adapterSbm = new SbhAdapter(mContext, sbmList);
		lvSbm.setAdapter(adapterSbm);

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

	private void clean() {
		switchSp();
		sbmBeanList.clear();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
		tvDh.setText(simpleDateFormat.format(new Date()));
	}

	/**
	 * 商品弹窗
	 */
	private void spDialog(String bh) {
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
//				edSph.setText(list.get(0).get编号());
//				edSpm.setText(list.get(0).get商品名称());
//				edSph.setEnabled(false);
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
}
