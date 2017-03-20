package com.smzskj.crk.offline.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseViewAdapter;
import com.smzskj.crk.base.BaseViewHolder;
import com.smzskj.crk.offline.bean.OfflineSpBean;
import com.smzskj.crk.utils.L;

import java.util.List;

/**
 * Created by ztt on 2017/3/10.
 */

public class SpAdapter extends BaseViewAdapter<OfflineSpBean.RowsBean> {

	public SpAdapter(Context context, List<OfflineSpBean.RowsBean> list) {
		super(context, list);
		L.e("list.size" + list.size());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		L.e("list.size" + list.size());
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_sp, parent, false);
		}
		TextView tvSph = BaseViewHolder.get(convertView, R.id.sp_item_sph);
		TextView tvSpm = BaseViewHolder.get(convertView, R.id.sp_item_spm);
		TextView tvDw = BaseViewHolder.get(convertView, R.id.sp_item_dw);

		OfflineSpBean.RowsBean bean = list.get(position);
		tvSph.setText("编号:" + bean.get编号());
		tvSpm.setText("商品名称:" + bean.get商品名称());
		tvDw.setText("单位:" + bean.get单位());

		return convertView;
	}
}
