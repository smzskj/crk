package com.smzskj.crk.offline.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseViewAdapter;
import com.smzskj.crk.base.BaseViewHolder;
import com.smzskj.crk.offline.bean.OfflineInUpBean;

import java.util.List;

/**
 * Created by ztt on 2017/3/14.
 */

public class OfflineInUpAdapter extends BaseViewAdapter<OfflineInUpBean> {

	private String rkdd;

	public OfflineInUpAdapter(Context context, List<OfflineInUpBean> list , String rkdd) {
		super(context, list);
		this.rkdd = rkdd;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_offline_in_up,parent,false);
		}

		TextView tvRkdd = BaseViewHolder.get(convertView,R.id.item_offline_in_tv_rkdd);
		TextView tvSph = BaseViewHolder.get(convertView,R.id.item_offline_in_tv_sph);
		TextView tvPch = BaseViewHolder.get(convertView,R.id.item_offline_in_tv_pch);

		tvRkdd.setText("入库地点:" + rkdd);
		tvSph.setText("商品号:" + list.get(position).getBh());
		tvPch.setText("批次号:" + list.get(position).getPch());

		return convertView;
	}
}
