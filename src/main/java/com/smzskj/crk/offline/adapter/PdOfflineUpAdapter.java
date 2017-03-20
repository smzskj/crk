package com.smzskj.crk.offline.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseViewAdapter;
import com.smzskj.crk.offline.bean.OfflineBean;

import java.util.List;

/**
 * Created by ztt on 2017/2/25.
 */

public class PdOfflineUpAdapter extends BaseViewAdapter<OfflineBean> {

	public PdOfflineUpAdapter(Context context, List<OfflineBean> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_offline_pdup, parent, false);
		}

		TextView tvKf = (TextView) convertView.findViewById(R.id.item_pdofflineup_kf);
		TextView tvSph = (TextView) convertView.findViewById(R.id.item_pdofflineup_sph);
		TextView tvPch = (TextView) convertView.findViewById(R.id.item_pdofflineup_pch);
		TextView tvzt = (TextView) convertView.findViewById(R.id.item_pdofflineup_zt);

		tvKf.setText("库房:" + list.get(position).getKf());
		tvSph.setText("商品号:" + list.get(position).getSph());
		tvPch.setText("批次号:" + list.get(position).getPch());
		tvzt.setText("状态:" + list.get(position).getZtStr());
		return convertView;
	}
}
