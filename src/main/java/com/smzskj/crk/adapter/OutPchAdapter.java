package com.smzskj.crk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseViewAdapter;
import com.smzskj.crk.base.BaseViewHolder;
import com.smzskj.crk.bean.OutDhBeanPch;

import java.util.List;

/**
 * Created by ztt on 2017/3/17.
 *
 * 出库商品批次号列表
 */

public class OutPchAdapter extends BaseViewAdapter<OutDhBeanPch.RowsBean> {

	public OutPchAdapter(Context context, List<OutDhBeanPch.RowsBean> list) {
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

		OutDhBeanPch.RowsBean bean = list.get(position);
		tvPch.setText("批次号:" + bean.get批次号());
		tvBh.setText("编号:" + bean.get编号());
		tvSpmc.setText("商品名称:" + bean.get商品名称());

		ImageView imageView = BaseViewHolder.get(convertView, R.id.dh_item_iv);
		if ("D".equals(bean.get登帐())) {
			imageView.setVisibility(View.VISIBLE);
		} else {
			imageView.setVisibility(View.GONE);
		}
		return convertView;
	}
}