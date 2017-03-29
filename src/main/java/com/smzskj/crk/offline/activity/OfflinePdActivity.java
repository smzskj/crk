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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.smzskj.crk.R;
import com.smzskj.crk.adapter.SingerTvAdapter;
import com.smzskj.crk.base.BaseActivity;
import com.smzskj.crk.offline.adapter.SpAdapter;
import com.smzskj.crk.offline.bean.OfflineSpBean;
import com.smzskj.crk.offline.db.PdDBUtils;
import com.smzskj.crk.offline.db.SpDbUtils;
import com.smzskj.crk.utils.Constants;
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
 * Created by ztt on 2017/2/21.
 * <p>
 * 离线盘点
 */

public class OfflinePdActivity extends BaseActivity implements View.OnClickListener {


	public static void startPdOfflineActivity(Activity context) {
		context.startActivity(new Intent(context, OfflinePdActivity.class));
	}

	private TextView tvRq, tvDb, tvKf, tvRy, tvPds,tvSpm;
	private EditText edSph, edPch;
	private ImageButton ibSm, ibSm2;
	private Button btnAdd;
	private LinearLayout llPd, llQh;
	private ListView listView;
	private LinkedList<String> pchs = new LinkedList<>();

	private BaseAdapter adapter;
	private ScannerUtils scanner;
	private MediaPlayer player;

	private PdDBUtils pdDBUtils;
	private SimpleDateFormat simpleDateFormatRq;
	private SimpleDateFormat simpleDateFormat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offline_pd);
		addBackListener();
		setTitle(R.string.offline_pd);
		spDbUtils = new SpDbUtils(mContext);
		simpleDateFormatRq = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		player = MediaPlayer.create(getApplicationContext(), R.raw.scan);
		pdDBUtils = new PdDBUtils(mContext);
		initView();
	}

	private void initView() {
		tvRq = findView(R.id.offline_tv_rq);
		tvDb = findView(R.id.offline_tv_db);
		tvKf = findView(R.id.offline_tv_kf);
		tvRy = findView(R.id.offline_tv_ry);
		tvPds = findView(R.id.offline_tv_pds);
		tvSpm = findView(R.id.offline_tv_spm);
		edSph = findView(R.id.offline_ed_sph);
		edPch = findView(R.id.offline_ed_pch2);
		ibSm = findView(R.id.offline_ib_sm);
		ibSm2 = findView(R.id.offline_ib_sm2);
		btnAdd = findView(R.id.offline_btn_add);
		listView = findView(R.id.offline_lv);
		llPd = findView(R.id.offline_ll_pd);
		llQh = findView(R.id.offline_ll_qh);

		ibSm.setOnClickListener(this);
		ibSm2.setOnClickListener(this);
		btnAdd.setOnClickListener(this);
		llPd.setOnClickListener(this);
		llQh.setOnClickListener(this);

		tvRq.setText("日期:" + getToday());
		tvDb.setText("数据库:" + mSp.getString(UserInfo.SP_DB_NAME, ""));
		tvKf.setText("库房名称:" + mSp.getString(UserInfo.SP_CK_NAME, ""));
		tvRy.setText("人员:" + mSp.getString(UserInfo.SP_USER_NAME, ""));
		tvPds.setText(String.format(getString(R.string.pds_int), pchs.size()));

		adapter = new SingerTvAdapter(mContext, pchs);
		listView.setAdapter(adapter);
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position,
										   long
					id) {

				showAlertDialog("删除", "是否要删除" + pchs.get(position) + "?", new DialogInterface
						.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						int id = pdDBUtils.deletePchSph(edSph.getText().toString(), pchs.get
								(position));
						if (id > 0) {
							pchs.remove(position);
							adapter.notifyDataSetChanged();
							tvPds.setText(String.format(getString(R.string.pds_int), pchs.size()));

						} else {
							makeShortToase("删除失败，请重试");
						}
						dialog.cancel();
					}
				}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == btnAdd) {
			if (TextUtils.isEmpty(edSph.getText().toString())) {
				makeShortToase("商品号不能为空");
			} else {
				spDialog(edSph.getText().toString());
//				edSph.setEnabled(false);
//				btnAdd.setEnabled(false);
			}
		} else if (v == llPd) {
			String pch = edPch.getText().toString();
			String sph = edSph.getText().toString();
			if (edSph.isEnabled()) {
				makeShortToase("请先查询并选择商品");
			} else if (TextUtils.isEmpty(sph)) {
				makeShortToase("商品号不能为空");
			} else if (TextUtils.isEmpty(pch)) {
				makeShortToase("批次号不能为空");
			} else {
				if (edSph.isEnabled()) {
					onClick(btnAdd);
				}
				addDb(sph, pch);
			}
		} else if (v == ibSm) {
			scanner();
		} else if (v == ibSm2) {
			scanner();
		} else if (v == llQh) {
			edSph.setText("");
			edPch.setText("");
			edSph.setEnabled(true);
			btnAdd.setEnabled(true);
			pchs.clear();
			adapter.notifyDataSetChanged();
			tvPds.setText(String.format(getString(R.string.pds_int), pchs.size()));
		}
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
					edSph.setText(barCode);
					onClick(btnAdd);
				} else {
					edPch.setText(barCode);
					onClick(llPd);
				}
				if (scanner != null) {
					scanner.cloase();
					scanner = null;
				}
			}
		});
		scanner.scanner();
	}

	private void addDb(String sph, String pch)  {
		Date date = new Date();
		long id = pdDBUtils.insert(sph, pch, simpleDateFormatRq.format(date), simpleDateFormat
				.format(date));
		if (-2 == id) {
			makeShortToase("批次号已经存在");
		} else if (-1 == id) {
			makeShortToase("保存失败，请重试");
		} else {
			makeShortToase("保存成功");
			pchs.addFirst(edPch.getText().toString());
//			pchs.add(edPch.getText().toString());
			adapter.notifyDataSetChanged();
			tvPds.setText(String.format(getString(R.string.pds_int), pchs.size()));
			edPch.setText("");
		}
	}




	private SpAdapter spAdapter;
	private XListView spListView;
	private AlertDialog dialog;
	private List<OfflineSpBean.RowsBean> spDatas = new ArrayList<>();
	private int spPage;
	private SpDbUtils spDbUtils;
	/**
	 * 商品弹窗
	 */
	private void spDialog(String bh) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.ProgressDialog);
		View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_sp, null, false);
		spListView = findView(view, R.id.sp_lv);
		spDatas.clear();
		spAdapter = new SpAdapter(mContext,spDatas);
		spListView.setAdapter(spAdapter);

		spListView.setPullLoadEnable(false);
		spListView.setPullRefreshEnable(true);
		DialogListListener listener = new DialogListListener(bh);
		spListView.setXListViewListener(listener);
		spListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int p = (int) id;
				// 选择
				edSph.setText(spDatas.get(p).get编号());
				tvSpm.setText("商品名:" + spDatas.get(p).get商品名称());
				edSph.setEnabled(false);
				btnAdd.setEnabled(false);
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
			List<OfflineSpBean.RowsBean> list = spDbUtils.querySpList(bh,spPage);
			if (list == null || list.size() == 0) {
				spListView.setPullLoadEnable(false);
				dialog.cancel();
				showToastDialog("没有查询到商品信息");
			} else if (list.size() == 1) {
				edSph.setText(list.get(0).get编号());
				tvSpm.setText("商品名:" + list.get(0).get商品名称());
				edSph.setEnabled(false);
				btnAdd.setEnabled(false);
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
			List<OfflineSpBean.RowsBean> list = spDbUtils.querySpList(bh,++spPage);
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
