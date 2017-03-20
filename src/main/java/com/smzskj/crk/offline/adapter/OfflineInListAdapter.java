package com.smzskj.crk.offline.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smzskj.crk.R;
import com.smzskj.crk.base.BaseViewAdapter;
import com.smzskj.crk.base.BaseViewHolder;
import com.smzskj.crk.offline.bean.OfflineInListBean;

import java.util.List;

/**
 * Created by ztt on 2017/3/13.
 */

public class OfflineInListAdapter extends BaseViewAdapter<OfflineInListBean> {

	private String sjk, kf, ry;

	public OfflineInListAdapter(Context context, List<OfflineInListBean> list, String sjk, String kf, String ry) {
		super(context, list);
		this.sjk = sjk;
		this.kf = kf;
		this.ry = ry;
	}

	public OfflineInListAdapter(Context context, List<OfflineInListBean> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_offline_in_list, parent, false);
		}

		TextView tvSjk = BaseViewHolder.get(convertView,R.id.item_pfoffline_in_list_sjk);
		TextView tvRy = BaseViewHolder.get(convertView,R.id.item_pfoffline_in_list_ry);
		TextView tvKf = BaseViewHolder.get(convertView,R.id.item_pfoffline_in_list_kf);
		TextView tvRq = BaseViewHolder.get(convertView,R.id.item_pfoffline_in_list_rq);
		TextView tvDh = BaseViewHolder.get(convertView,R.id.item_pfoffline_in_list_dh);
		TextView tvSl = BaseViewHolder.get(convertView,R.id.item_pfoffline_in_list_sl);
		TextView tvZt = BaseViewHolder.get(convertView,R.id.item_pfoffline_in_list_zt);
		tvSjk.setText("数据库:" + sjk);
		tvKf.setText("库房:" + kf);
		tvRy.setText("人员:" + ry);
		tvRq.setText("日期:" + list.get(position).getRq());
		tvDh.setText("单号:" + list.get(position).getDjhm());
		tvSl.setText("数量:" + list.get(position).getCount());
		tvZt.setText("状态:" + list.get(position).getZt());
		return convertView;
	}
}
