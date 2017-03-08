package com.smzskj.crk.offline.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseViewAdapter;
import com.smzskj.crk.base.BaseViewHolder;
import com.smzskj.crk.offline.bean.OfflineCountListBean;

import java.util.List;

/**
 * Created by ztt on 2017/2/27.
 */

public class PdOfflineListAdapter extends BaseViewAdapter<OfflineCountListBean> {

	private String sjk, kf, ry;

	public PdOfflineListAdapter(Context context, List<OfflineCountListBean> list) {
		super(context, list);
	}

	public PdOfflineListAdapter(Context context, List<OfflineCountListBean> list, String sjk,
								String kf, String ry) {
		super(context, list);
		this.sjk = sjk;
		this.kf = kf;
		this.ry = ry;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_pdoffline, parent, false);
		}


		TextView tvSjk = BaseViewHolder.get(convertView, R.id.item_pfoffline_tv_sjk);
		TextView tvKf = BaseViewHolder.get(convertView, R.id.item_pfoffline_tv_kf);
		TextView tvRy = BaseViewHolder.get(convertView, R.id.item_pfoffline_tv_ry);
		TextView tvRq = BaseViewHolder.get(convertView, R.id.item_pfoffline_tv_rq);
		TextView tvPds = BaseViewHolder.get(convertView, R.id.item_pfoffline_tv_pds);
		TextView tvScs = BaseViewHolder.get(convertView, R.id.item_pfoffline_tv_scs);

		tvSjk.setText("数据库:" + sjk);
		tvKf.setText("库房:" + kf);
		tvRy.setText("人员:" + ry);
		tvRq.setText("日期:" + list.get(position).getRq());
		tvPds.setText("盘点数:" + list.get(position).getCount());
		tvScs.setText("上传数:" + list.get(position).getUpCount());
		return convertView;
	}
}
